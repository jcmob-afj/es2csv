package cn.afj.es2csv.log;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.excel.annotation.ExcelProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 通知渠道日志
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeChannelLog implements Serializable {

    private static final long serialVersionUID = 1L;

	/**
	 * 广告主
	 */
    @ExcelProperty(value = "advertiserId")
	private Long advertiserId;
	/**
	 * 广告主名称
	 */
    @ExcelProperty(value = "advertiserName")
    private String advertiserName;
	/**
	 * 产品
	 */
    @ExcelProperty(value = "productId")
    private Long productId;
	/**
	 * 产品名称
	 */
    @ExcelProperty(value = "productName")
	private String productName;
	/**
	 * 订单
	 */
    @ExcelProperty(value = "orderId")
	private Long orderId;
	/**
	 * 订单名称
	 */
    @ExcelProperty(value = "orderName")
	private String orderName;
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
	 * 渠道
	 */
    @ExcelProperty(value = "channelId")
	private Long channelId;
	/**
	 * 渠道名称
	 */
    @ExcelProperty(value = "channelName")
	private String channelName;
	/**
	 * 渠道投放方式
	 */
    @ExcelProperty(value = "channelDeliveryMode")
	private String channelDeliveryMode;
	/**
	 * 子渠道
	 */
    @ExcelProperty(value = "subChannel")
	private String subChannel;
	/**
	 * 创意ID
	 */
    @ExcelProperty(value = "ideaId")
	private String ideaId;
	/**
	 * IDFA
	 */
    @ExcelProperty(value = "idfa")
	private String idfa;
	/**
	 * 用户IP
	 */
    @ExcelProperty(value = "userIp")
	private String userIp;
	/**
	 * 尝试次数
	 */
    @ExcelProperty(value = "tryCount")
	private Integer tryCount;
	/**
	 * 渠道返回内容
	 */
    @ExcelProperty(value = "lastReplyContent")
	private String lastReplyContent;
	/**
	 * 通知渠道时间
	 */
    @ExcelProperty(value = "noticeChannelTime")
	private String noticeChannelTime;
	/**
	 * 最后尝试时间
	 */
    @ExcelProperty(value = "lastTryTime")
	private Date lastTryTime;
	/**
	 * 通知渠道URL
	 */
    @ExcelProperty(value = "noticeChannelUrl")
	private String noticeChannelUrl;

	/**
	 * 状态
	 */
    @ExcelProperty(value = "status")
	private Integer status;
    @ExcelProperty(value = "statusDesc")
	private String statusDesc;
    @ExcelProperty(value = "scName")
	private String scName;
    @ExcelProperty(value = "appid")
	private String appid;

}
