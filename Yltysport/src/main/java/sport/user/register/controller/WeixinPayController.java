package sport.user.register.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom2.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import net.sf.json.JSONObject;
import sport.user.weixinpayment.service.WXPay;
import sport.user.weixinpayment.service.WXPrepay;
import sport.user.weixinpayment.util.OrderUtil;
import sport.user.weixinpayment.util.XMLUtil;
import sport.user.weixinpayment.util.wxpNoiceUtil;
import sport.user.alipay.util.UtilDate;
import sport.user.register.dao.ForcePaymentsDao;
import sport.user.register.service.ForcePaymentsService;


@Controller
public class WeixinPayController {
	
	@Autowired(required=true)  
	private ForcePaymentsDao forcePaymentsDao;
	
	@Autowired(required=true)
	ForcePaymentsService forcePaymentsService;
	
	private static final Logger logger = LoggerFactory.getLogger(WeixinPayController.class);
	private static String appId="wxc5a8c6c68bb0fab1";//应用号
	private static String mch_id="1366640002";//商户号
	private static String notify_url="http://www.datahcong.cn:8080/Yltysport/user/wxpRecallUrl.json";//回调地址
	private static String partnerKey="Ewyv1mI36F6s8PZIJazaaP3cpPtimVbK";//商户秘钥
	
	//接受客户的付款金额（分）、用户ID参数，生成预支付订单
	//返回appId，partnerId，prepayId，nonceStr，timeStamp，package以及sign给客户端
	@RequestMapping(value="/user/wxpReturnPayid.json")
	public void wxpReturnPayid(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String spbill_create_ip = request.getRemoteAddr();//获得spbill_create_ip参数
		String total_fee=request.getParameter("total_fee");//得到付款金额参数
		String userid=request.getParameter("userid");//获得用户ID，用来生成订单号
		String out_trade_no=UtilDate.getOrderNum()+userid; //获得订单号
		WXPrepay prePay = new WXPrepay();
		prePay.setAppid(appId);
		prePay.setBody("盈郎体育微信支付");
		prePay.setPartnerKey(partnerKey);
		prePay.setMch_id(mch_id);
		prePay.setNotify_url(notify_url);
		prePay.setOut_trade_no(out_trade_no);
		prePay.setSpbill_create_ip(spbill_create_ip);
		prePay.setTotal_fee(total_fee);
		prePay.setTrade_type("APP");
        //此处添加获取openid的方法，获取预支付订单需要此参数！！！！！！！！！！！ 
		// 获取预支付订单号
		String prepayid = prePay.submitXmlGetPrepayId();
		logger.info("微信支付获取的预支付订单是：" + prepayid);
		
		if (prepayid != null && prepayid.length() > 10) {
			// 生成微信支付参数，此处拼接为完整的JSON格式，符合支付调起传入格式
			String AppParam = WXPay.createPackageValue(appId,mch_id,partnerKey, prepayid);
			System.out.println("返回给前端的微信Param=" + AppParam);
			// 此处可以添加订单的处理逻辑
			//JSONObject jsonObject=new JSONObject();
			//jsonObject.put("AppParam",AppParam);
			response.setHeader("Content-type", "text/json;charset=UTF-8");		 
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(AppParam.toString());
			
		}
	}
	
	//微信支付回调地址
	@RequestMapping(value="/user/wxpRecallUrl.json")
	public void wxpRecallUrl(HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		System.out.println("进入微信支付的回调接口");
		InputStream inStream = request.getInputStream();
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outSteam.write(buffer, 0, len);
		}
		outSteam.close();
		inStream.close();
		String result = new String(outSteam.toByteArray(), "utf-8");
		Map<String, String> map = null;
		try {
			map = XMLUtil.doXMLParse(result);
		} catch (JDOMException e) {
			e.printStackTrace();
		}
		
		// 此处获取accessToken
		//String accessToken = "3oWof69djPQPm2SWHrLmpDpmpMWMFYgxE4UKCc0QwEIt7MhdITnCOCdKxo6Df2LxAXkPRIsoDtyAqAGaQEDWlIKkqseoy5dA8VjOlul6UMs";
		// 此处调用订单查询接口验证是否交易成功
		boolean isSucc = wxpNoiceUtil.reqOrderquery(map,partnerKey);
		// 支付成功，商户处理后同步返回给微信参数
		if (!isSucc) {
			// 支付失败
			System.out.println("支付失败");
		} else {
			System.out.println("===============微信付款成功==============");
			// ------------------------------
			// 处理业务开始
			// ------------------------------
			// 此处处理订单状态，结合自己的订单数据完成订单状态的更新
			System.out.println("微信支付异步回调收到的参数有："+map);
			String query_out_trade_num=forcePaymentsDao.wxqueryOut_Trade_No(map.get("out_trade_no"));
			if(query_out_trade_num==null){
				 System.out.println("该微信订单没有处理过");
				 System.out.println("微信异步回调的接口参数值为："+map.toString());
				 try{				 
					 forcePaymentsService.userWxPayment(map);//进行支付宝付款记录更新和有效期字段更新
				 }catch(Exception e){
					 System.out.println("付款回调错误！"+e.getMessage());
				 }
			 }
			// ------------------------------
			// 处理业务完毕
			// ------------------------------
			String noticeStr = wxpNoiceUtil.setXML("SUCCESS", "");
			PrintWriter out = response.getWriter();
			out.print(new ByteArrayInputStream(noticeStr.getBytes(Charset.forName("UTF-8"))));
		}
	}
}
