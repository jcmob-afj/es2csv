package cn.afj.es2csv.quartz.job;

import static cn.afj.es2csv.util.DateUtils.DATETIME_PATTERN;
import static cn.afj.es2csv.util.DateUtils.getYesterdayEndTime;
import static cn.afj.es2csv.util.DateUtils.getYesterdayStartTime;
import static com.alibaba.fastjson.JSON.parseObject;
import static com.xxl.job.core.biz.model.ReturnT.SUCCESS_CODE;

import java.util.HashSet;

import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Sets;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;

import cn.afj.es2csv.quartz.handler.ToCSVJobHandler;
import cn.afj.es2csv.util.DateUtils;

/**
 * XxlJob开发示例（Bean模式）
 * <p>
 * 开发步骤：
 * 1、在Spring Bean实例中，开发Job方法，方式格式要求为 "public ReturnT<String> execute(String param)"
 * 2、为Job方法添加注解 "@XxlJob(value="自定义jobhandler名称", init = "JobHandler初始化方法", destroy = "JobHandler销毁方法")"，注解value值对应的是调度中心新建任务的JobHandler属性的值。
 * 3、执行日志：需要通过 "XxlJobLogger.log" 打印执行日志；
 *
 * @Author afj
 * @Date 2020/9/4 19:20
 */
@Component
public class Es2csvXxlJob {
    private static Logger logger = LoggerFactory.getLogger(Es2csvXxlJob.class);

    @Autowired
    RestHighLevelClient restHighLevelClient;
    /**
     * 移除非必须的key
     */
    private HashSet<String> set = Sets.newHashSet("@version", "thread_name", "tags", "logger_name", "level_value", "level", "_type", "_score", "_index", "_id", "@timestamp", "message", "port");

    /**
     * 1、获取点击日志
     */
    @XxlJob("clickJobHandler")
    public ReturnT<String> clickJobHandler(String param) throws Exception {
        String startTime = DateUtils.getDateFormat(getYesterdayStartTime(), DATETIME_PATTERN);
        String endTime = DateUtils.getDateFormat(getYesterdayEndTime(), DATETIME_PATTERN);
        ToCSVJobHandler.simpleWrite(startTime,endTime,restHighLevelClient,"channelclick","channelTime","jc-c-channelclick-*", "jc-a-channelclick-*");
        return new ReturnT(SUCCESS_CODE, "数据下载成功");
    }

    /**
     * 2、获取回调日志
     */
    @XxlJob("callbackJobHandler")
    public ReturnT<String> callbackJobHandler(String param) throws Exception {
        String startTime = DateUtils.getDateFormat(getYesterdayStartTime(), DATETIME_PATTERN);
        String endTime = DateUtils.getDateFormat(getYesterdayEndTime(), DATETIME_PATTERN);
        ToCSVJobHandler.simpleWrite(startTime,endTime,restHighLevelClient,"advconfirm","confirmTime","jc-c-advconfirm-*", "jc-a-advconfirm-*");
        return new ReturnT(SUCCESS_CODE, "数据下载成功");
    }

    /**
     * 3、获取历史点击日志
     */
    @XxlJob("clickHistoryJobHandler")
    public ReturnT<String> clickHistoryJobHandler(String param) throws Exception {
        JSONObject object = parseObject(param);
        String startTime = object.get("startTime").toString();
        String endTime = object.get("endTime").toString();
        ToCSVJobHandler.simpleWrite(startTime,endTime,restHighLevelClient,"channelclick","channelTime","jc-c-channelclick-*", "jc-a-channelclick-*");
        return new ReturnT(SUCCESS_CODE, "数据下载成功");
    }

    /**
     * 4、获取历史回调日志
     */
    @XxlJob("callbackHistoryJobHandler")
    public ReturnT<String> callbackHistoryJobHandler(String param) throws Exception {
        JSONObject object = parseObject(param);
        String startTime = object.get("startTime").toString();
        String endTime = object.get("endTime").toString();
        ToCSVJobHandler.simpleWrite(startTime,endTime,restHighLevelClient,"advconfirm","confirmTime","jc-c-advconfirm-*", "jc-a-advconfirm-*");
        return new ReturnT(SUCCESS_CODE, "数据下载成功");
    }

    public static void main(String[] args) {
        String startTime = DateUtils.getDateFormat(getYesterdayStartTime(), DATETIME_PATTERN);
        String endTime = DateUtils.getDateFormat(getYesterdayEndTime(), DATETIME_PATTERN);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("startTime", startTime);
        jsonObject.put("endTime", endTime);
        System.out.println(jsonObject.toJSONString());
    }
}
