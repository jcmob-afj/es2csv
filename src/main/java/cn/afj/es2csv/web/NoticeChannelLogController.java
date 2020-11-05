package cn.afj.es2csv.web;


import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.ClearScrollRequest;
import org.elasticsearch.action.search.ClearScrollResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Sets;

import cn.afj.es2csv.log.NoticeChannelLog;
import cn.afj.es2csv.util.DateTimeUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 通知媒体日志
 *
 * @author fengjian
 */
@RestController
@RequestMapping("notice")
@Api(tags = "通知媒体日志")
public class NoticeChannelLogController {

    private static final Long MAX_SIZE = 1048576L;
    @Autowired
    RestHighLevelClient restHighLevelClient;

    /**
     * 移除非必须的key
     */
    private HashSet<String> set = Sets.newHashSet("@version", "thread_name", "tags", "logger_name", "level_value", "level", "_type", "_score", "_index", "_id", "@timestamp", "message", "port");

    /**
     * 导出历史数据
     *
     * @return
     */
    @GetMapping("download")
    @ApiOperation(value = "导出历史数据")
    public void historyDownload(@RequestParam(name = "startTime") String startTime, @RequestParam(name = "endTime") String endTime,
                                @RequestParam(name = "keyword") String keyword, @RequestParam(name = "keywordValue") String keywordValue, HttpServletResponse response) throws IOException {

        try {
            List<NoticeChannelLog> scroll = scroll(startTime, endTime, keyword, keywordValue);
            if (scroll != null && scroll.size() > 0) {
                String fileName = "notice-" + System.currentTimeMillis() + "";
                response.setContentType("application/vnd.ms-excel");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".csv");
                EasyExcel.write(response.getOutputStream(), NoticeChannelLog.class)
                        .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).sheet("模板").doWrite(scroll);
            }

        } catch (Exception e) {
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = new HashMap<String, String>();
            map.put("status", "failure");
            map.put("message", "下载文件失败" + e.getMessage());
            response.getWriter().println(JSON.toJSONString(map));
        }
    }

    /**
     * 滚动查询
     *
     * @throws Exception
     */
    public List<NoticeChannelLog> scroll(String startTime, String endTime, String keyword, String keywordValue) throws Exception {
        List<NoticeChannelLog> noticeChannelLogs = new ArrayList<>();

        BoolQueryBuilder boolQuery = boolQuery()
                // 定位日志类型
                .filter(termQuery("log_type.keyword", "ChannelClick"));
        long begin = DateTimeUtils.parseDateTime(startTime);
        long end = DateTimeUtils.parseDateTime(endTime);
        boolQuery.filter(rangeQuery("channelTime").from(begin).to(end));
        if (StringUtils.isNotBlank(keyword)) {
            boolQuery.filter(termQuery(keyword, keywordValue));
        }
        final Scroll scroll = new Scroll(TimeValue.timeValueMinutes(10L));
        SearchRequest searchRequest = new SearchRequest("jc-c-channelclick-*", "jc-a-channelclick-*");
        searchRequest.scroll(scroll);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(boolQuery);
        searchSourceBuilder.size(10000);
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = null;
        try {
            searchResponse = restHighLevelClient.search(searchRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String scrollId = searchResponse.getScrollId();
        SearchHit[] searchHits = searchResponse.getHits().getHits();
        long totalHits = searchResponse.getHits().totalHits;
        System.out.println("查询到： " + totalHits + "条数据");
        for (SearchHit hit : searchHits) {
            Map<String, Object> source = hit.getSource();
            source.keySet().removeAll(set);
            addLog(noticeChannelLogs, source);
        }
        while (searchHits != null && searchHits.length > 0) {
            SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
            scrollRequest.scroll(scroll);
            searchResponse = restHighLevelClient.searchScroll(scrollRequest);
            scrollId = searchResponse.getScrollId();
            searchHits = searchResponse.getHits().getHits();
            for (SearchHit hit : searchHits) {
                Map<String, Object> source = hit.getSource();
                source.keySet().removeAll(set);
                addLog(noticeChannelLogs, source);
            }
        }
        ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
        clearScrollRequest.addScrollId(scrollId);
        ClearScrollResponse clearScrollResponse = restHighLevelClient.clearScroll(clearScrollRequest);
        clearScrollResponse.isSucceeded();
        return noticeChannelLogs;
    }


    public void addLog(List<NoticeChannelLog> noticeChannelLogs, Map<String, Object> source) {
        NoticeChannelLog noticChannelLog = new NoticeChannelLog();
        if (source != null) {
            source.entrySet().forEach(entry -> {
                if ("advId".equals(entry.getKey())) { // 广告主
                    noticChannelLog.setAdvertiserName(entry.getValue() != null ? entry.getValue().toString() : null);
                }
                if ("proId".equals(entry.getKey())) { // 产品
                    noticChannelLog.setProductName(entry.getValue() != null ? entry.getValue().toString() : null);
                }
                if ("orderId".equals(entry.getKey())) { // 订单
                    noticChannelLog.setOrderName(entry.getValue() != null ? entry.getValue().toString() : null);
                }
                if ("delPlat".equals(entry.getKey())) { // 订单投放平台
                    noticChannelLog.setOrderDeliveryPlatform(entry.getValue() != null ? entry.getValue().toString() : null);
                }
                if ("delMode".equals(entry.getKey())) { // 订单投放方式
                    noticChannelLog.setOrderDeliveryMethod(entry.getValue() != null ? entry.getValue().toString() : null);
                }
                if ("sourceId".equals(entry.getKey())) { // 渠道
                    noticChannelLog.setChannelName(entry.getValue() != null ? entry.getValue().toString() : null);
                }
                if ("delMode".equals(entry.getKey())) { // 渠道投放方式
                    noticChannelLog.setChannelDeliveryMode(entry.getValue() != null ? entry.getValue().toString() : null);
                }
                if ("scid".equals(entry.getKey())) { // 子渠道
                    noticChannelLog.setSubChannel(entry.getValue() != null ? entry.getValue().toString() : null);
                }
                if ("cid".equals(entry.getKey())) { // 创意ID
                    noticChannelLog.setIdeaId(entry.getValue() != null ? entry.getValue().toString() : null);
                }
                if ("status".equals(entry.getKey())) { // 请求渠道状态
                    noticChannelLog.setStatusDesc(entry.getValue() != null ? entry.getValue().toString() : null);
                }
                if ("uuid".equals(entry.getKey())) { // IDFA
                    noticChannelLog.setIdfa(entry.getValue() != null ? entry.getValue().toString() : null);
                }
                if ("client_ip".equals(entry.getKey())) { // 用户ip
                    noticChannelLog.setUserIp(entry.getValue() != null ? entry.getValue().toString() : null);
                }
                if ("responseMsg".equals(entry.getKey())) { // 渠道返回内容
                    noticChannelLog.setLastReplyContent(entry.getValue() != null ? entry.getValue().toString() : null);
                }
                if ("noticeChannelTime".equals(entry.getKey())) { // 通知渠道时间
                    noticChannelLog.setNoticeChannelTime(entry.getValue() != null ? DateTimeUtils.formatDateTime(entry.getValue().toString()) : null);
                }
                if ("notifyChannelUrl".equals(entry.getKey())) { // 通知渠道URL
                    noticChannelLog.setNoticeChannelUrl(entry.getValue() != null ? entry.getValue().toString() : null);
                }
                if ("sc_name".equals(entry.getKey())) { // sc_name
                    noticChannelLog.setScName(entry.getValue() != null ? entry.getValue().toString() : null);
                }
                if ("appid".equals(entry.getKey())) { // appid
                    noticChannelLog.setAppid(entry.getValue() != null ? entry.getValue().toString() : null);
                }
            });
        }
        noticeChannelLogs.add(noticChannelLog);
    }

}
