package sport.user.weixinpayment.util;

import java.util.Map;

import sport.user.weixinpayment.service.WXOrderQuery;



public class wxpNoiceUtil {
	
	public static String setXML(String return_code, String return_msg) {
		return "<xml><return_code><![CDATA[" + return_code + "]]></return_code><return_msg><![CDATA[" + return_msg + "]]></return_msg></xml>";
	}

	/**
	 * 请求订单查询接口
	 * @param map
	 * @param accessToken
	 * @return
	 */
	public static boolean reqOrderquery(Map<String, String> map, String accessToken) {
		WXOrderQuery orderQuery = new WXOrderQuery();
		orderQuery.setAppid(map.get("appid"));
		orderQuery.setMch_id(map.get("mch_id"));
		orderQuery.setTransaction_id(map.get("transaction_id"));
		orderQuery.setOut_trade_no(map.get("out_trade_no"));
		orderQuery.setNonce_str(map.get("nonce_str"));
		
		//此处需要密钥PartnerKey，此处直接写死，自己的业务需要从持久化中获取此密钥，否则会报签名错误
		orderQuery.setPartnerKey(accessToken);
		
		Map<String, String> orderMap = orderQuery.reqOrderquery();
		//此处添加支付成功后，支付金额和实际订单金额是否等价，防止钓鱼
		if (orderMap.get("return_code") != null && orderMap.get("return_code").equalsIgnoreCase("SUCCESS")) {
			if (orderMap.get("trade_state") != null && orderMap.get("trade_state").equalsIgnoreCase("SUCCESS")) {
				String total_fee = map.get("total_fee");
				String order_total_fee = map.get("total_fee");
				if (Integer.parseInt(order_total_fee) >= Integer.parseInt(total_fee)) {
					return true;
				}
			}
		}
		return false;
	}
}
