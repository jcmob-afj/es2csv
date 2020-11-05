package cn.afj.es2csv;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;

import com.google.common.collect.Lists;

import cn.afj.es2csv.log.NoticeChannelLog;
import cn.afj.es2csv.util.DateTimeUtils;


public class Test {

    public static void main(String[] args) throws Exception {

        List<NoticeChannelLog> scroll = scroll();
        System.out.println(scroll.toString());

    }


    /**
     * 滚动查询
     *
     * @throws Exception
     */
    public static List<NoticeChannelLog> scroll() throws Exception {
        List<NoticeChannelLog> noticeChannelLogs = new ArrayList<>();

        List<HttpHost> httpHosts = Lists.newArrayList();
        httpHosts.add(new HttpHost("106.14.240.250", 19200, "http"));

        RestClient lowLevelRestClient = RestClient.builder(httpHosts.toArray(new HttpHost[]{})).build();
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(lowLevelRestClient);

        BoolQueryBuilder boolQuery = boolQuery()
                // 定位日志类型
                .filter(termQuery("log_type.keyword", "ChannelClick"));
//        long begin = DateTimeUtils.parseDateTime(startTime);
//        long end = DateTimeUtils.parseDateTime(endTime);
//        boolQuery.filter(rangeQuery("channelTime").from(begin).to(end));
//        boolQuery.filter(termQuery("advId", 42));
//        boolQuery.filter(termQuery("proId", 109));

        final Scroll scroll = new Scroll(TimeValue.timeValueMinutes(10L));
        SearchRequest searchRequest = new SearchRequest("jc-a-channelclick-*", "jc-c-channelclick-*");
        searchRequest.scroll(scroll);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(boolQuery);
        searchSourceBuilder.size(1000).sort("callAdvTime", SortOrder.DESC);
        ;

        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = null;
        try {
            searchResponse = restHighLevelClient.search(searchRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SearchHit[] searchHits = searchResponse.getHits().getHits();

        for (SearchHit hit : searchHits) {
            Map<String, Object> source = hit.getSource();
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
        return noticeChannelLogs;
    }


    public static void loop(List<Map<String, Object>> rows, SearchHit[] searchHits) {
        for (SearchHit searchHit : searchHits) {
            Map<String, Object> map = new LinkedHashMap<>();
            Map<String, Object> source = searchHit.getSource();
            if (source != null) {
                source.entrySet().forEach(entry -> {
                    if ("advId".equals(entry.getKey())) { // 广告主
                        map.put("广告主", entry.getValue());
                    }
                    if ("proId".equals(entry.getKey())) { // 产品
                        map.put("产品", entry.getValue());
                    }
                    if ("orderId".equals(entry.getKey())) { // 订单
                        map.put("订单", entry.getValue());
                    }
                    if ("delPlat".equals(entry.getKey())) { // 订单投放平台
                        map.put("订单投放平台", entry.getValue());
                    }
                    if ("delMode".equals(entry.getKey())) { // 订单投放方式
                        map.put("订单投放方式", entry.getValue());
                    }
                    if ("sourceId".equals(entry.getKey())) { // 渠道
                        map.put("渠道", entry.getValue());
                    }
                    if ("delMode".equals(entry.getKey())) { // 渠道投放方式
                        map.put("渠道投放方式", entry.getValue());
                    }
                    if ("scid".equals(entry.getKey())) { // 子渠道
                        map.put("子渠道", entry.getValue());
                    }
                    if ("cid".equals(entry.getKey())) { // 创意ID
                        map.put("创意ID", entry.getValue());
                    }
                    if ("status".equals(entry.getKey())) { // 状态
                        map.put("状态", entry.getValue());
                    }
                    if ("uuid".equals(entry.getKey())) { // IDFA
                        map.put("IDFA", entry.getValue());
                    }
                    if ("from_ip".equals(entry.getKey())) { // 渠道IP
                        map.put("渠道IP", entry.getValue());
                    }
                    if ("client_ip".equals(entry.getKey())) { // 用户IP
                        map.put("用户IP", entry.getValue());
                    }
                    if ("channelTime".equals(entry.getKey())) { // 渠道发送点击时间
                        map.put("渠道发送点击时间", entry.getValue());
                    }
                    if ("callChannelUrl".equals(entry.getKey())) { // 渠道回调地址
                        map.put("渠道回调地址", entry.getValue());
                    }
                    if ("repeatTime".equals(entry.getKey())) { // 是否重复点击
                        map.put("是否重复点击", entry.getValue());
                    }
                    if ("notifyAdvertiser".equals(entry.getKey())) { // 通知广告主
                        map.put("通知广告主", entry.getValue());
                    }
                    if ("advertiserConfirmation".equals(entry.getKey())) { // 广告主确认
                        map.put("广告主确认", entry.getValue());
                    }
                    if ("notificationChannel".equals(entry.getKey())) { // 通知渠道
                        map.put("通知渠道", entry.getValue());
                    }
                    if ("sc_name".equals(entry.getKey())) { // sc_name
                        map.put("sc_name", entry.getValue());
                    }
                    rows.add(map);
                });
            }
        }
    }


}
