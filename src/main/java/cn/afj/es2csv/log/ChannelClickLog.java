package cn.afj.es2csv.log;

import java.io.Serializable;

import com.alibaba.excel.annotation.ExcelProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 渠道点击日志
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChannelClickLog implements Serializable {

    /**
     * appid
     */
    @ExcelProperty(value = "appid")
    private String appid;
    /**
     * 子渠道 对应scid
     */
    @ExcelProperty(value = "subChannel")
    private String subChannel;
    /**
     * idfa 对应uuid
     */
    @ExcelProperty(value = "idfa")
    private String idfa;

    /**
     * 创意ID 对应cid
     */
    @ExcelProperty(value = "creativeId")
    private String creativeId;
    /**
     * 用户ip
     */
    @ExcelProperty(value = "userIp")
    private String userIp;
    /**
     * 渠道ip
     */
    @ExcelProperty(value = "sourceIp")
    private String sourceIp;
    /**
     * 渠道发送点击时间(渠道点击日志)
     */
    @ExcelProperty(value = "clickTime")
    private String clickTime;
    /**
     * 订单投放平台
     */
    @ExcelProperty(value = "orderDeliveryPlatform")
    private String orderDeliveryPlatform;
    /**
     * 订单投放方式
     */
    @ExcelProperty(value = "orderDeliveryMethod")
    private String orderDeliveryMethod;
    /**
     * 渠道投放方式
     */
    @ExcelProperty(value = "channelDeliveryMode")
    private String channelDeliveryMode;

    /**
     * 渠道
     */
    @ExcelProperty(value = "channelName")
    private String channelName;

    /**
     * 订单
     */
    @ExcelProperty(value = "orderName")
    private String orderName;

    /**
     * 广告主
     */
    @ExcelProperty(value = "advertiserName")
    private String advertiserName;
    /**
     * 产品
     */
    @ExcelProperty(value = "productName")
    private String productName;


    /**
     * 是否重复点击
     */
    @ExcelProperty(value = "isRepeatClick")
    private Integer isRepeatClick;

    /**
     * 回调地址
     */
    @ExcelProperty(value = "callBackUrl")
    private String callBackUrl;
    /**
     * 通知广告主
     */
    @ExcelProperty(value = "")
    private String notifyAdvertiser;
    /**
     * 广告主确认
     */
    @ExcelProperty(value = "advertiserConfirmation")
    private String advertiserConfirmation;
    /**
     * 通知渠道
     */
    @ExcelProperty(value = "notificationChannel")
    private String notificationChannel;
    /**
     * 状态描述
     */
    @ExcelProperty(value = "statusDesc")
    private String statusDesc;
    @ExcelProperty(value = "scName")
    private String scName;
    @ExcelProperty(value = "orderInputId")
    private String orderInputId;


}
