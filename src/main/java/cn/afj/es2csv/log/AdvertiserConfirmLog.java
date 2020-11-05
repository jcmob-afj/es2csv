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
 * 广告主确认日志
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdvertiserConfirmLog implements Serializable {

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
	 * 订单投放单价
	 */
    @ExcelProperty(value = "orderDeliveryPrice")
	private Integer orderDeliveryPrice;
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
	 * 渠道投放单价
	 */
    @ExcelProperty(value = "channelDeliveryPrice")
	private Integer channelDeliveryPrice;
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
	 * 来源IP  广告主服务器IP
	 */
    @ExcelProperty(value = "sourceIp")
	private String sourceIp;
	/**
	 * 用户IP
	 */
    @ExcelProperty(value = "userIp")
	private String userIp;
	/**
	 * 确认时间
	 */
    @ExcelProperty(value = "confirmTime")
	private String confirmTime;
	/**
	 * 数据确认点击时间
	 */
    @ExcelProperty(value = "clickTime")
	private String clickTime;
	/**
	 * 是否重复确认
	 */
    @ExcelProperty(value = "isRepeatTime")
	private Integer isRepeatTime;


	/**
	 * 状态
	 */
    @ExcelProperty(value = "status")
	private Integer status;

	/**
	 * 状态描述
	 */
    @ExcelProperty(value = "statusDesc")
	private String statusDesc;



    @ExcelProperty(value = "scName")
	private String scName;
    @ExcelProperty(value = "appid")
	private String appid;
    @ExcelProperty(value = "orderInputId")
	private String orderInputId;

}
