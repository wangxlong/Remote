package sport.user.register.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import sport.user.register.dao.ForcePaymentsDao;
import sport.user.register.dao.webOperatorDao;
import sport.user.register.domain.AnnouncementBean;
import sport.user.register.service.ForcePaymentsService;
import sport.user.register.util.TimeConverter;

@Controller
public class ForcePayments {
	
	@Autowired(required=true)  
	   private ForcePaymentsDao paymentsDao;
	@Autowired(required=true)  
	   private ForcePaymentsService forcePaymentsService;
	@Autowired(required=true)  
	   private webOperatorDao weboperatordao;
	//返回某个用户距离当前的注册天数
	@RequestMapping(value="/user/registerdate/{id}")
	public void returnRegisterDate(@PathVariable String id,HttpServletResponse response) throws IOException, ParseException{
		String hasRegisterDate=paymentsDao.hasRegisterDate(id);
		System.out.println("已经注册天数…………"+hasRegisterDate);
		Long fromRegisterDate=TimeConverter.getNowDaySub(hasRegisterDate);
		System.out.println("当前时间距离注册 的天数间隔是："+fromRegisterDate);
		String vip_to=forcePaymentsService.returnVip_to(id);
		int is_vip=0;
		//判断是否是VIP
		if(vip_to!=null){
			Date vip_date=(Date) TimeConverter.stringToDate(vip_to);
			java.util.Date nowdate=new java.util.Date();
			if(vip_date.after(nowdate)){
				is_vip=1;
			}
		}		
		
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("fromRegisterDate",fromRegisterDate);
		jsonObject.put("is_vip",is_vip);
		response.setHeader("Content-type", "text/json;charset=UTF-8");		 
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(jsonObject.toString());
	}
	
	//返回计算某用户如果充值n个月的有效期时间
	@RequestMapping(value="/user/vip_to/{id}")
	public void returnVipLastDate(@PathVariable String id,HttpServletRequest request,HttpServletResponse response) throws IOException, ParseException{
		int numberday=Integer.parseInt(request.getParameter("numberOfMonth"));
		Date vip_to=forcePaymentsService.calculateEfficientDate(numberday, id);	//计算有效期，numberday表示包n个月的会员，
		JSONObject jsonObject=new JSONObject();                                 //一个月按照30天计算,id表示用户的id
		jsonObject.put("vip_to",TimeConverter.dateToString(vip_to));
		response.setHeader("Content-type", "text/json;charset=UTF-8");		 
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(jsonObject.toString());
		
	}
	
	//web上更新VIP_TO字段
	@RequestMapping(value="/user/updateVip_to/{user_name}")
	public String updateVip_to(@PathVariable String user_name,HttpServletRequest request,RedirectAttributes redirectAttributes){
		System.out.println("进行更新VIP_TO……");	
		String vip_to=request.getParameter("vip_to");
		System.out.println("user_name为："+user_name+" vip_to为："+vip_to);
		forcePaymentsService.updateVip_toService(user_name, vip_to);
		return "redirect:/wuser/toSetVip";
	}
	
	//获取VIP_TO字段
	
	@RequestMapping(value="/user/getVip_to/{id}")
	public void getVip_to(@PathVariable String id,HttpServletResponse response) throws IOException, ParseException{
		JSONObject jsonObject=new JSONObject();
		String vip_to=forcePaymentsService.returnVip_to(id);
		java.util.Date nowdate=new java.util.Date();
		int is_vip=0;
		//判断是否是VIP
		if(vip_to!=null){
			Date vip_date=(Date) TimeConverter.stringToDate(vip_to);		
			if(vip_date.after(nowdate)){
				is_vip=1;
			}
		}
		int is_intThreeDays=-1;//vip的有效期是否在3天内
		if(is_vip==1){			
			Date vip_date=(Date) TimeConverter.stringToDate(vip_to);
			Date isafternow_vip_to=(Date) TimeConverter.afterDate1(nowdate,3);
			System.out.println("isafternow_vip:"+isafternow_vip_to);
			if(isafternow_vip_to.before(vip_date)){
				is_intThreeDays=0;
			}else{
				is_intThreeDays=1;
			}
		}
		jsonObject.put("vip_to",vip_to);
		jsonObject.put("is_vip",is_vip);
		jsonObject.put("is_intThreeDays",is_intThreeDays);
		response.setHeader("Content-type", "text/json;charset=UTF-8");		 
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(jsonObject.toString());
	}
	
	//今日推送完毕 和 今日无推送
	@RequestMapping(value="/user/retRecommendState")
	public void retRecommendState(HttpServletRequest request,HttpServletResponse response) throws IOException{		
		ServletContext application = request.getServletContext();
		if(application.getAttribute("todayOvelRecomend")==null){
			 application.setAttribute("todayOvelRecomend", "0");
			 application.setAttribute("todayNoRecomend", "0");
		}
		int todayOvelRecomend =Integer.parseInt((String)application.getAttribute("todayOvelRecomend"));
		int todayNoRecomend=Integer.parseInt((String) application.getAttribute("todayNoRecomend"));
		
		JSONObject jsonObject=new JSONObject();		
		jsonObject.put("todayOvelRecomend",todayOvelRecomend);
		jsonObject.put("todayNoRecomend",todayNoRecomend);
		response.setHeader("Content-type", "text/json;charset=UTF-8");		 
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(jsonObject.toString());
	}
	
	//公告栏内容接口
	@RequestMapping(value="/user/retCommonContent")
	public void retCommonContent(HttpServletResponse response) throws IOException{
		List<AnnouncementBean> announcementList=weboperatordao.selectThisMonthAnnouncement();
		JSONArray jsonArray=JSONArray.fromObject(announcementList);
		//jsonArray.put("commentContent",jsonArray);
		response.setHeader("Content-type", "text/json;charset=UTF-8");		 
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(jsonArray.toString());
	}
	//阿里支付隐藏标记
	@RequestMapping(value="/user/retAlipayFlag.json")
	public void retAliPaymentFlag(HttpServletResponse response) throws IOException{
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("aliPayFlag",1);
		response.setHeader("Content-type", "text/json;charset=UTF-8");		 
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(jsonObject.toString());
	}
	
	
	
}
