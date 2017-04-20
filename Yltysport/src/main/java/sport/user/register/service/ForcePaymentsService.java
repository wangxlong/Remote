package sport.user.register.service;

import java.util.Date;
import java.util.Map;

import sport.user.register.domain.UserPaymentBean;

public interface ForcePaymentsService {

	public Date calculateEfficientDate(int numberDay,String  id);
	
	public void updateVip_toService(String user_name,String vip_to);
	
	//返回vip_to字段
	public String returnVip_to(String id); 
	
	//用户付款(易接)
	public void userPayment(UserPaymentBean userPaymentBean);
	
	//用户付款(支付宝)
	public void userAliPayment(Map params);
	
	//用户付款(微信)
	public void userWxPayment(Map params);
		
}
