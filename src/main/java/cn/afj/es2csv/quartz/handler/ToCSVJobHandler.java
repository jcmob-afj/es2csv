package cn.afj.es2csv.quartz.handler;

import static cn.afj.es2csv.util.DateUtils.DATETIME_PATTERN_DATE;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;

import java.io.File;
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

import com.google.common.collect.Sets;

import cn.afj.es2csv.util.DateTimeUtils;
import cn.afj.es2csv.util.DateUtils;
import cn.afj.es2csv.util.TestFileUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;

/**
 * @Author afj
 * @Date 2020/11/3 11:05
 * @Version 1.0
 * @description:
 */
public class ToCSVJobHandler {

    /**
     * 移除非必须的key
     */
    private static final HashSet<String> set = Sets.newHashSet("@version", "thread_name", "tags", "logger_name", "level_value", "level", "_type", "_score", "_index", "_id", "@timestamp", "message", "port");


    public static void simpleWrite(String startTime, String endTime, RestHighLevelClient restHighLevelClient, String name, String rangeQuery, String indexA, String indexB) throws Exception {
        String s1 = DateUtils.formatCstTime(startTime, DATETIME_PATTERN_DATE);
        String s2 = DateUtils.formatCstTime(endTime, DATETIME_PATTERN_DATE);
        String fileName = "";
        if (s1.equals(s2)) {
            fileName = TestFileUtil.getPath() + name + "-[" + s1 + "].csv";
        } else {
            fileName = TestFileUtil.getPath() + name + "-[" + s1 + "~" + s2 + "].csv";
        }
        System.out.println("<————开始查询查询数据————>");
        //获取开始时间
        long start = System.currentTimeMillis();
        List<Map<String, Object>> rows = scroll(startTime, endTime, restHighLevelClient, rangeQuery, indexA, indexB);
        //获取结束时间
        long end = System.currentTimeMillis();
        //输出程序运行时间
        System.out.println("查询数据耗时：" + (end - start) + "ms");
        System.out.println("<————数据查询结束————>");
        if (rows != null && rows.size() > 0) {
            ExcelWriter writer = null;
            try {
                System.out.println("<————导出开始————>");
                long start1 = System.currentTimeMillis();
                File file = new File(fileName);
                // 通过工具类创建writer
                writer = ExcelUtil.getWriter(file);
                // 一次性写出
                writer.write(rows);
                writer.flush();
                long end1 = System.currentTimeMillis();
                System.out.println("导出数据耗时：" + (end1 - start1) + "ms");
                System.out.println("<————导出完毕————>");
            } catch (Exception e) {
                throw e;
            } finally {
                writer.close();
            }
        }
    }


    /**
     * 滚动查询
     *
     * @throws Exception
     */
    public static List<Map<String, Object>> scroll(String startTime, String endTime, RestHighLevelClient restHighLevelClient, String rangeQuery, String indexA, String indexB) throws Exception {
        List<Map<String, Object>> rows = new ArrayList<>();

        BoolQueryBuilder boolQuery = boolQuery();
        long begin = DateTimeUtils.parseDateTime(startTime);
        long end = DateTimeUtils.parseDateTime(endTime);
        boolQuery.filter(rangeQuery(rangeQuery).from(begin).to(end));
        final Scroll scroll = new Scroll(TimeValue.timeValueMinutes(10L));
        SearchRequest searchRequest = new SearchRequest(indexA, indexB);
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
            rows.add(source);
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
                rows.add(source);
            }
        }
        ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
        clearScrollRequest.addScrollId(scrollId);
        ClearScrollResponse clearScrollResponse = restHighLevelClient.clearScroll(clearScrollRequest);
        clearScrollResponse.isSucceeded();
        return rows;
    }

}
