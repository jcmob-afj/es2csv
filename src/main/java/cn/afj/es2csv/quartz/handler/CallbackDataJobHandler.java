package cn.afj.es2csv.quartz.handler;

import static cn.afj.es2csv.util.DateUtils.DATETIME_PATTERN;
import static cn.afj.es2csv.util.DateUtils.DATETIME_PATTERN_DATE;
import static cn.afj.es2csv.util.DateUtils.getEndTime;
import static cn.afj.es2csv.util.DateUtils.getStartTime;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

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

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;

import cn.afj.es2csv.log.AdvertiserConfirmLog;
import cn.afj.es2csv.util.DateTimeUtils;
import cn.afj.es2csv.util.DateUtils;
import cn.afj.es2csv.util.TestFileUtil;

/**
 * @Author afj
 * @Date 2020/11/3 11:06
 * @Version 1.0
 * @description:
 */
public class CallbackDataJobHandler {


    public static void simpleWrite(String startTime,String endTime,RestHighLevelClient restHighLevelClient, HashSet<String> set) throws Exception {

        String fileName = TestFileUtil.getPath() + "advconfirm-" + DateUtils.formatCstTime(endTime, DATETIME_PATTERN_DATE) + ".csv";
        System.out.println("<————开始查询数据————>");
        //获取开始时间
        long start = System.currentTimeMillis();
        List<AdvertiserConfirmLog> scroll = scroll(startTime, endTime, restHighLevelClient, set);
        //获取结束时间
        long end = System.currentTimeMillis();
        //输出程序运行时间
        System.out.println("查询数据耗时：" + (end - start) + "ms");
        System.out.println("<————数据查询结束————>");

        if (scroll != null && scroll.size() > 0) {
            System.out.println("<————导出开始————>");
            long start1 = System.currentTimeMillis();
            EasyExcel.write(fileName, AdvertiserConfirmLog.class).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    .sheet("模板").doWrite(scroll);
            long end1 = System.currentTimeMillis();
            System.out.println("导出数据耗时：" + (end1 - start1) + "ms");
            System.out.println("<————导出完毕————>");
        }

    }

    /**
     * 滚动查询
     *
     * @throws Exception
     */
    public static List<AdvertiserConfirmLog> scroll(String startTime, String endTime, RestHighLevelClient restHighLevelClient, HashSet<String> set) throws Exception {
        List<AdvertiserConfirmLog> logList = new ArrayList<>();

        BoolQueryBuilder boolQuery = boolQuery()
                // 定位日志类型
                .filter(termQuery("log_type.keyword", "AdvConfirm"));
        long begin = DateTimeUtils.parseDateTime(startTime);
        long end = DateTimeUtils.parseDateTime(endTime);
        boolQuery.filter(rangeQuery("confirmTime").from(begin).to(end));
        final Scroll scroll = new Scroll(TimeValue.timeValueMinutes(10L));
        SearchRequest searchRequest = new SearchRequest("jc-c-advconfirm-*", "jc-a-advconfirm-*");
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
            addLog(logList, source);
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
                addLog(logList, source);
            }
        }
        ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
        clearScrollRequest.addScrollId(scrollId);
        ClearScrollResponse clearScrollResponse = restHighLevelClient.clearScroll(clearScrollRequest);
        clearScrollResponse.isSucceeded();
        return logList;
    }


    public static void addLog(List<AdvertiserConfirmLog> logList, Map<String, Object> source) {
        AdvertiserConfirmLog advConfirmLog = new AdvertiserConfirmLog();
        if (source != null) {
            source.entrySet().forEach(entry -> {
                if ("advId".equals(entry.getKey())) { // 广告主
                    advConfirmLog.setAdvertiserName(entry.getValue() != null ? entry.getValue().toString() : null);
                }
                if ("proId".equals(entry.getKey())) { // 产品
                    advConfirmLog.setProductName(entry.getValue() != null ? entry.getValue().toString() : null);
                }
                if ("orderId".equals(entry.getKey())) { // 订单
                    advConfirmLog.setOrderName(entry.getValue() != null ? entry.getValue().toString() : null);
                }
                if ("delPlat".equals(entry.getKey())) { // 订单投放平台
                    advConfirmLog.setOrderDeliveryPlatform(entry.getValue() != null ? entry.getValue().toString() : null);
                }
                if ("delMode".equals(entry.getKey())) { // 订单投放方式
                    advConfirmLog.setOrderDeliveryMethod(entry.getValue() != null ? entry.getValue().toString() : null);
                }
                if ("inputMoney".equals(entry.getKey())) { // 订单投放单价
                    advConfirmLog.setOrderDeliveryPrice(entry.getValue() != null ? (Integer) entry.getValue() : null);
                }
                if ("sourceId".equals(entry.getKey())) { // 渠道
                    advConfirmLog.setChannelName(entry.getValue() != null ? entry.getValue().toString() : null);
                }
                if ("delMode".equals(entry.getKey())) { // 渠道投放方式
                    advConfirmLog.setChannelDeliveryMode(entry.getValue() != null ? entry.getValue().toString() : null);
                }
                if ("channelMoney".equals(entry.getKey())) { // 渠道投放单价
                    advConfirmLog.setChannelDeliveryPrice(entry.getValue() != null ? (Integer) entry.getValue() : null);
                }
                if ("scid".equals(entry.getKey())) { // 子渠道
                    advConfirmLog.setSubChannel(entry.getValue() != null ? entry.getValue().toString() : null);
                }
                if ("cid".equals(entry.getKey())) { // 创意ID
                    advConfirmLog.setIdeaId(entry.getValue() != null ? entry.getValue().toString() : null);
                }
                if ("status".equals(entry.getKey())) { // 平台接收状态
                    advConfirmLog.setStatusDesc(entry.getValue() != null ? entry.getValue().toString() : null);
                }
                if ("uuid".equals(entry.getKey())) { // IDFA
                    advConfirmLog.setIdfa(entry.getValue() != null ? entry.getValue().toString() : null);
                }
                if ("from_ip".equals(entry.getKey())) { // 广告主服务器IP
                    advConfirmLog.setSourceIp(entry.getValue() != null ? entry.getValue().toString() : null);
                }
                if ("client_ip".equals(entry.getKey())) { // 用户ip
                    advConfirmLog.setUserIp(entry.getValue() != null ? entry.getValue().toString() : null);
                }
                if ("confirmTime".equals(entry.getKey())) { // 确认时间
                    advConfirmLog.setConfirmTime(entry.getValue() != null ? DateTimeUtils.formatDateTime(entry.getValue().toString()) : null);
                }
                if ("channelTime".equals(entry.getKey())) { // 数据确认点击时间
                    advConfirmLog.setClickTime(entry.getValue() != null ? DateTimeUtils.formatDateTime(entry.getValue().toString()) : null);
                }
                if ("repeatTime".equals(entry.getKey())) { // 是否重复确认
                    advConfirmLog.setIsRepeatTime(entry.getValue() != null ? (Integer) entry.getValue() : null);
                }
                if ("sc_name".equals(entry.getKey())) { // sc_name
                    advConfirmLog.setScName(entry.getValue() != null ? entry.getValue().toString() : null);
                }
                if ("appid".equals(entry.getKey())) { // appid
                    advConfirmLog.setAppid(entry.getValue() != null ? entry.getValue().toString() : null);
                }
                if ("orderInputId".equals(entry.getKey())) { // orderInputId
                    advConfirmLog.setOrderInputId(entry.getValue() != null ? entry.getValue().toString() : null);
                }

            });
        }
        logList.add(advConfirmLog);
    }
}
