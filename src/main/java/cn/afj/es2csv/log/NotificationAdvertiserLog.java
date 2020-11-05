package cn.afj.es2csv.log;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通知广告主日志
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationAdvertiserLog implements Serializable{

    private Long id;

    private String logType; // 日志区分

    private String source;  // 渠道标识

    private String appid;   // appid

    private String orderInputId; //子订单id

    private String subChannel;    // 子渠道 对应scid

    private String idfa;    //idfa 对应uuid

    private Integer status;   //状态

    private String creativeId;        //创意ID 对应cid

    private String userIp;   // 用户ip

    private Date clickTime;     //点击时间(渠道点击日志)

    private String orderDeliveryPlatform;     //订单投放平台

    private String orderDeliveryMethod;     //订单投放方式
    private String channelDeliveryMode;     //渠道投放方式

    private Long sourceId;      //渠道ID
    private String channelName;      //渠道

    private Long orderId;       //订单ID
    private String orderName;       //订单

    private Long advId;         //广告主ID
    private String advertiserName;         //广告主

    private Long proId;         //产品ID
    private String productName;         //产品
    private String advertiserTime;         // 请求广告主时间(通知广告主日志)

    private String returnContent;           // 广告主返回内容
    private String reqUrl;                  // 上报广告主URL
    private Integer isRepeateReport;         // 是否重复上报

    private String statusDesc;                  // 状态描述
    private String scName;
}
