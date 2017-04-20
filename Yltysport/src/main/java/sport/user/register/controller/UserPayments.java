package sport.user.register.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


import net.sf.json.JSONObject;
import sport.user.alipay.util.*;
import sport.user.register.dao.ForcePaymentsDao;
import sport.user.register.dao.UserOperatorDao;
import sport.user.register.domain.UserPaymentBean;
import sport.user.register.service.ForcePaymentsService;
import sport.user.register.util.TimeConverter;



@Controller
public class UserPayments {
	
	@Autowired(required=true)  
	private UserOperatorDao userDao;
	@Autowired(required=true)  
	private ForcePaymentsDao forcePaymentsDao;
	
	@Autowired(required=true)
	ForcePaymentsService forcePaymentsService;
	
	@RequestMapping(value="/user/payment.json",method=RequestMethod.GET)
	public void userPayments(HttpServletRequest request,HttpServletResponse response){
		//Logger log = Logger.getLogger(Log4jInit.class);
		//log.info("Logg4j日志已经初始化。");
		
		UserPaymentBean userPaymentBean=new UserPaymentBean();
		userPaymentBean.setApp(request.getParameter("app"));
		userPaymentBean.setCbi(request.getParameter("cbi")); //用户ID
		userPaymentBean.setCt(request.getParameter("ct"));
		userPaymentBean.setFee(Integer.parseInt(request.getParameter("fee")));
		userPaymentBean.setPt(request.getParameter("pt"));
		userPaymentBean.setSdk(request.getParameter("sdk"));
		userPaymentBean.setSign(request.getParameter("sign"));
		userPaymentBean.setSsid(request.getParameter("ssid"));
		userPaymentBean.setSt(Integer.parseInt(request.getParameter("st")));
		userPaymentBean.setTcd(request.getParameter("tcd"));
		userPaymentBean.setUid(request.getParameter("uid"));
		userPaymentBean.setVer(request.getParameter("ver"));
		
		try{
			String examTcd=userDao.userPaymentsRetry(userPaymentBean.getTcd());
			System.out.println("examTcd对于的记录ID为："+examTcd);
		  if(examTcd==null){
			  System.out.println("没有重复");
			  System.out.println(userPaymentBean.toString());
			  forcePaymentsService.userPayment(userPaymentBean);//进行易接付款记录更新和有效期字段更新
			  response.getWriter().write("SUCCESS");
			}else{
			  System.out.println("重复记录");
		      response.getWriter().write("SUCCESS");	
			}
		}catch(Exception e){
			try {
				response.getWriter().write("FAIL");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println(e.getMessage());
		}		
	}
	
	@RequestMapping(value="/user/alipayReturnOrder/{id}.json")
	public void aliPayReturnOrder(@PathVariable String id,HttpServletRequest request,HttpServletResponse response) throws IOException{		
		//生成订单号
		String orderNumber=UtilDate.getOrderNum()+id;
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("orderNumber", orderNumber);
		response.setHeader("Content-type", "text/json;charset=UTF-8");		 
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(jsonObject.toString());
	}
	
	@RequestMapping(value="/user/alipayReturnSign.json")
	public void alipayReturnOrder(HttpServletRequest request,HttpServletResponse response) throws IOException{
		//获取支付宝POST过来反馈信息
		Map<String,String> params = new HashMap<String,String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
			params.put(name, valueStr);
		}
		//把接收到的map除去空格和sign,signtype参数，然后再按照字典序拼接
		String content=AlipayCore.createLinkString(AlipayCore.paraFilter(params));
		System.out.println("content的值为："+content);
		String signResult=RSA.sign(content, AlipayConfig.private_key, "utf-8");
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("orderSign", signResult);
		response.setHeader("Content-type", "text/json;charset=UTF-8");		 
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(jsonObject.toString());
	}
	
	@RequestMapping(value="/user/alipayPrivateKey.json")
	public void returnAlipayPrivateKey(HttpServletResponse response) throws IOException{
		String private_key ="MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMaijbswtT7BCcy5"
				+ "5qdmfc5Fl+zBUNwb5b/VLkMlft711Uv4iMB+ZI9J5Zk+kJzqYT/KgAyLo/GU5kcT"
				+ "eYOdR9v33B7WjOoxQdiw97GT7u4dBJyegcYXxQOnN2XEJ7HiGXzBwLw4gfdCQdiq"
				+ "gCoSKga6dfD+YYi8y3kdYct8B4P3AgMBAAECgYAF29OMWuJtqGnulOkdtOMvC1Rw"
				+ "0j7dTyFWqCYGKvvz/ZI9/GWkL9Ytd8OC4pqHVk5qAEIInLHINVkZbZSe8iulXvel"
				+ "c3ECP6OuBf2ZlFvIqSqP6KEq28L+KWwQ5ahpjUc+0Vg+51TsIx0DLPvd5cFTrSjT"
				+ "e1pUyx2UZjaZ2beEgQJBAOzZW0zLWikCmY3xX6dEhnuYUtKFzwoF4RDpPJmaqoG3"
				+ "6qzdrjVRGFowjShpFyMQa2BRDSG53UIKw4fQ20IR97cCQQDWsi/cEiAJlLaBgHvH"
				+ "qu6XEDwhZVHqJA/bzet8wMikevxZnPZ+pRaGcSQK5DvoLwEhqNgvDigLDy0Ryqwc"
				+ "pFXBAkEA3nGZZPuLtv6BFyW+L4uVPpkmGCCh/YjeA5L6Xk07nE8OccJVri92zqXQ"
				+ "vUZN+5mdLz2QNr+oRTF0yKbodhoH6wJAEKUl8sDw/O0rrSSh9Bv7fXjDwK8TR7ze"
				+ "AeOYI0brB12FnQj/T1hLQ8cXzURepKSoSMzHXfaSAjF+BAgbiejvwQJAHZ09czEA"
				+ "DhUBRZhM3L6XmDHrwcFmedGW3lOwR6wf/Of4Ha2duVknDldYNQb/cHlsdNIzY/5p"
				+ "CrWvTABwjpVmmA==";
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("privateKey",private_key);
		response.setHeader("Content-type", "text/json;charset=UTF-8");		 
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(jsonObject.toString());
	}
	
	@RequestMapping(value="/user/alipayCallBack.json")
	public void aliPaymentCallBack(HttpServletRequest request,HttpServletResponse response) throws IOException{
		//
		//获取支付宝POST过来反馈信息
		Map<String,String> params = new HashMap<String,String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
		}
		
		//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
		//商户订单号	
		String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");

		//支付宝交易号	String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");

		//交易状态
		String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");

		//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//

		//if(AlipayNotify.verify(params)){//验证成功
			//////////////////////////////////////////////////////////////////////////////////////////
			//请在这里加上商户的业务逻辑程序代码

			//——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
			
			if(trade_status.equals("TRADE_FINISHED")){
				//判断该笔订单是否在商户网站中已经做过处理
					//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
					//如果有做过处理，不执行商户的业务程序
					
				//注意：
				//退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
				//请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
			} else if (trade_status.equals("TRADE_SUCCESS")){
				//判断该笔订单是否在商户网站中已经做过处理
					//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
					//如果有做过处理，不执行商户的业务程序
				//判断订单号是否已经处理过了
				String query_out_trade_num=forcePaymentsDao.queryOut_Trade_No(out_trade_no);
				//如果没有处理过
				if(query_out_trade_num==null){
					 System.out.println("没有处理过");
					 System.out.println("异步回调的接口参数值为："+params.toString());
					 try{
					 
						 forcePaymentsService.userAliPayment(params);//进行支付宝付款记录更新和有效期字段更新
					 }catch(Exception e){
						 System.out.println("付款回调错误！"+e.getMessage());
					 }
				 }
				//注意：
				//付款完成后，支付宝系统发送该交易状态通知
				//请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
			}

			//——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
			
			response.getWriter().write("success");
			//out.println("success");	//请不要修改或删除

			//////////////////////////////////////////////////////////////////////////////////////////
		//}
		//else{//验证失败
			//response.getWriter().write("fail");
			//out.println("fail");
		//}
		//
	}
	
	//当付款成功后，接受用户ID，用户付款金额（元）
	@RequestMapping(value="/user/paySuccessCall.json")
	public void paySuccessCallCalVip_to(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String userid=request.getParameter("userid");//用户ID
		//String paydate=request.getParameter("paydate");//付款日期
		String payamount=request.getParameter("payamount");//付款金额（元）
		//根据付款日期和付款金额，计算用户的vip有效期
		Date vip_to=forcePaymentsService.calculateEfficientDate(Integer.parseInt(payamount)/100, userid);
		  //java.sql.Date sqlVipDate = new java.sql.Date(vip_to.getTime());
		  //java.sql.Date sqlVipDate=java.sql.Date.valueOf(TimeConverter.dateToString(vip_to));
		String sqlVipDate=TimeConverter.dateToString(vip_to);
		System.out.println(userid+" 更新有效期至----"+sqlVipDate);
		//更新数据库中vip_to字段
		forcePaymentsDao.updateVip_to(userid, sqlVipDate);
		
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("vip_to",sqlVipDate);
		response.setHeader("Content-type", "text/json;charset=UTF-8");		 
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(jsonObject.toString());
	}
	
}
