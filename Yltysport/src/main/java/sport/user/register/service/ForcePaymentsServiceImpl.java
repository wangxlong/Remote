package sport.user.register.service;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sport.user.alipay.util.UtilDate;
import sport.user.register.dao.ForcePaymentsDao;
import sport.user.register.dao.UserOperatorDao;
import sport.user.register.domain.UserPaymentBean;
import sport.user.register.util.TimeConverter;

@Service
public  class ForcePaymentsServiceImpl implements ForcePaymentsService{

	@Autowired(required=true)  
	   private ForcePaymentsDao paymentsDao;
	@Autowired(required=true)  
	private UserOperatorDao userDao;
	
	@Override	
	public Date calculateEfficientDate(int numberDay,String id) {
		// TODO Auto-generated method stub		
		java.util.Date nowdate=new java.util.Date();//获取当前时间
		String vip_to_string=paymentsDao.selectVip_to(id);	
		System.out.println("vip_to的值为…………"+vip_to_string);
		Date vip_to=null;
		try {
			if(vip_to_string!=null){			
			vip_to = (Date) TimeConverter.stringToDate(vip_to_string);		
			if(vip_to.before(nowdate)){
				System.out.println("vip_to早于当前时间，所以不是vip");
				vip_to=(Date) TimeConverter.afterDate(nowdate, numberDay);			
			}else{
				System.out.println("vip_to晚于当前时间，所以是vip");
				vip_to=(Date) TimeConverter.afterDate(vip_to, numberDay);
		   }	
		}else{
				vip_to=(Date) TimeConverter.afterDate(nowdate, numberDay);
		}
		System.out.println("vip的截止时间是："+vip_to);
		System.out.println("vip的截止时间是："+TimeConverter.dateToString(vip_to));			
		}catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return vip_to;
	}
	@Override
	@Transactional(rollbackFor = {RuntimeException.class, Exception.class})
	public void updateVip_toService(String user_name,String vip_to) {
		// TODO Auto-generated method stub
		System.out.println("进行事务测试");
		paymentsDao.webUpdateVip_to(user_name, vip_to);
		System.out.println("出去事务测试");
	}
	@Override
	@Transactional
	public String returnVip_to(String id) {
		// TODO Auto-generated method stub
		String vip_to=paymentsDao.toGetVip_to(id);
		return vip_to;
	}
	@Override
	@Transactional(rollbackFor={RuntimeException.class, Exception.class})
	public void userPayment(UserPaymentBean userPaymentBean) {
		// TODO Auto-generated method stub
		  userDao.userPayments(userPaymentBean);
		  //充值之后，计算有效期，并且更新vip_to字段
		  //System.out.println("插入payments");
		  /*
		  Date vip_to=calculateEfficientDate(userPaymentBean.getFee()/10000, userPaymentBean.getCbi());
		  //java.sql.Date sqlVipDate = new java.sql.Date(vip_to.getTime());
		  //java.sql.Date sqlVipDate=java.sql.Date.valueOf(TimeConverter.dateToString(vip_to));
		  String sqlVipDate=TimeConverter.dateToString(vip_to);
		  System.out.println(userPaymentBean.getCbi()+" 更新有效期至"+sqlVipDate);
		  paymentsDao.updateVip_to(userPaymentBean.getCbi(), sqlVipDate);
		  */
	}
	@Override
	@Transactional(rollbackFor={RuntimeException.class, Exception.class})
	public void userAliPayment(Map params) {
		// TODO Auto-generated method stub
		
		//获取付费的用户ID
		String userid=UtilDate.getAlipayUserId((String)params.get("out_trade_no"));
		//插入付费记录
		params.put("userid", userid);
		paymentsDao.aliPayinsert(params);	
		//String userId=UtilDate.getAlipayUserId((String)params.get("out_trade_no"));
		System.out.println("付费异步调用的用户ID为："+userid);
		double total_fee=Double.valueOf((String)params.get("total_fee"));
		int total_fee_int=(int)total_fee;
		System.out.println(userid+"付费的金额为："+total_fee);
		Date vip_to=calculateEfficientDate(total_fee_int/100, userid);
		  //java.sql.Date sqlVipDate = new java.sql.Date(vip_to.getTime());
		  //java.sql.Date sqlVipDate=java.sql.Date.valueOf(TimeConverter.dateToString(vip_to));
		String sqlVipDate=TimeConverter.dateToString(vip_to);
		System.out.println(userid+" 更新有效期至----"+sqlVipDate);
		paymentsDao.updateVip_to(userid, sqlVipDate);
		
	}
	@Override
	@Transactional(rollbackFor={RuntimeException.class, Exception.class})
	public void userWxPayment(Map params) {
		// TODO Auto-generated method stub
		//获取付费的用户ID
		String userid=UtilDate.getAlipayUserId((String)params.get("out_trade_no"));
		//插入付费记录
		params.put("userid", userid);
		paymentsDao.wxPayinsert(params);	
		//String userId=UtilDate.getAlipayUserId((String)params.get("out_trade_no"));
		System.out.println("微信付费异步调用的用户ID为："+userid);
		double total_fee=Double.valueOf((String)params.get("total_fee"));
		int total_fee_int=(int)total_fee;
		System.out.println(userid+"微信付费的金额为："+total_fee);
		Date vip_to=calculateEfficientDate(total_fee_int/10000, userid);
		//java.sql.Date sqlVipDate = new java.sql.Date(vip_to.getTime());
		//java.sql.Date sqlVipDate=java.sql.Date.valueOf(TimeConverter.dateToString(vip_to));
		String sqlVipDate=TimeConverter.dateToString(vip_to);
		System.out.println(userid+" 更新有效期至----"+sqlVipDate);
		paymentsDao.updateVip_to(userid, sqlVipDate);
	}

}
