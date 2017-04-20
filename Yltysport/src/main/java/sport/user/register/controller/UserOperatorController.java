package sport.user.register.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import sport.user.register.dao.UserOperatorDao;
import sport.user.register.domain.UserLog;
import sport.user.register.domain.UserRegisterBean;
import sport.user.register.domain.UserResetPersonalInfo;
import sport.user.register.service.UserOperatorService;
import sport.user.register.service.UserOperatorServiceImpl;
import sport.user.register.util.UserRegisterUtil;
import sport.user.register.validator.UserRegisterValidator;

@Controller
public class UserOperatorController {
	
	@Autowired
	private UserOperatorService userOperatorService;
	
	@Autowired(required=true)  
	   private UserOperatorDao userDao;
	
	@ResponseBody
	@RequestMapping(value="/user/register.json")
	public void registerUser(HttpServletRequest request,HttpServletResponse response) throws IOException{
		request.setCharacterEncoding("UTF-8");
		UserRegisterBean userRegisterBean=new UserRegisterBean();
		//创建userregister的验证对象
		//UserRegisterValidator  userRegisterValidator=new UserRegisterValidator();
		System.out.println("输出的城市是："+request.getParameter("address"));
		//userRegisterBean.setId(13112);
		userRegisterBean.setPhone(request.getParameter("phone"));
		userRegisterBean.setUser_name(request.getParameter("user_name"));
		userRegisterBean.setEncrypted_password(request.getParameter("encrypted_password"));
		userRegisterBean.setAge(Integer.parseInt(request.getParameter("age")));
		userRegisterBean.setGender(request.getParameter("gender"));
		//String address=new String(request.getParameter("address").getBytes("ISO8859-1"),"UTF-8");//转码，防止中文乱码
		//System.out.println("再次输出为："+address);
		userRegisterBean.setAddress(request.getParameter("address"));
		userRegisterBean.setEmail(request.getParameter("email"));
		userRegisterBean.setCurrent_sign_in_ip(UserRegisterUtil.getUserIp(request));
		userRegisterBean.setIs_iphone(Integer.parseInt(request.getParameter("is_iphone")));
		userRegisterBean.setRecommener(request.getParameter("recommener"));
		//userRegisterBean.setCurrent_sign_in_at();
		//bindingResult.
		//BindingResult bindingResult=new BindException();
		//Hashtable errortable=userRegisterValidator.validate(userRegisterBean);
		/*System.out.println("userRegisterBeanΪ为+"+userRegisterBean.getUser_id()+
				userRegisterBean.getUser_name()+userRegisterBean.getPassword()+
				userRegisterBean.getPhone()+userRegisterBean.getStatus()+
				userRegisterBean.getEmail()+userRegisterBean.getProvice()
				+userRegisterBean.getAge()+userRegisterBean.getSex());
		*/
		//if(errortable.isEmpty()){
		//创建jsonObject对象
		
		//params.put("username", username);
		//params.put("user_json", user);
		 //JSONObject jsonObject = new JSONObject();
			System.out.println("开始执行插入操作！");
			System.out.println("userRegisterBean为+"+userRegisterBean.getId());
		Map params =  new HashMap();
		try{			
		    userOperatorService.registerUser(userRegisterBean);
		    //jsonObject.put("retCode", 0);
		    //jsonObject.put("retMsg", "注册成功");
		    params.put("retCode", 0);
		    params.put("retMsg","注册成功");
		}catch(Exception e){
			params.put("retCode", 1);
			System.out.println("捕获异常:"+e.getMessage());
			if(e.getMessage().toString().contains("(MATCH.SYS_C0015223)")){
				System.out.println("该手机号码已经注册过");
				//jsonObject.put("retMsg", "该手机号码已被注册过");
				params.put("retMsg","该手机号码已被注册过");
			}
			else {
				if(e.getMessage().toString().contains("(MATCH.INDEX_USERS_ON_EMAIL)")){
				System.out.println("该邮箱已被使用");
				//jsonObject.put("retMsg", "该邮箱已被使用");
				params.put("retMsg","该邮箱已被使用");
				}
				if(e.getMessage().toString().contains("MATCH.INDEX_USERS_ON_PHONE")){
				System.out.println("该手机号码已被使用");
				//jsonObject.put("retMsg", "该手机号码已被注册过");
				params.put("retMsg","该手机号码已被注册过");
				}
				if(e.getMessage().toString().contains("MATCH.INDEX_USERS_ON_NAME)")){
				System.out.println("该用户名已被使用");
				//jsonObject.put("retMsg", "该用户名已被使用");	
				params.put("retMsg","该用户名已被使用");
				}
			}
		}
		PrintWriter out=response.getWriter();
		//response.setContentLength("");
		//JSONObject jsonObject = JSONObject.fromObject(params);
		String json = new Gson().toJson(params);
		response.setHeader("Content-type", "text/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");	
		response.setContentType("text/json");
		//out.write(jsonObject.toString());
		out.write(json);
		out.flush();
		//int id=userDao.registerUserdao(userRegisterBean);
		//System.out.println("id为"+id);
		//}
	}
	
	@RequestMapping(value="/user/login.json")
	public void loginUser(HttpServletRequest request,HttpServletResponse response){
		try{
          String user_name_phone=request.getParameter("user_name_phone");
          String encrypted_password=request.getParameter("encrypted_password");
		  String userIp=UserRegisterUtil.getUserIp(request);
		  JSONObject jsonObject=userOperatorService.loginUser(user_name_phone,encrypted_password,userIp, request);//账号和密码
		  response.setHeader("Content-type", "text/json;charset=UTF-8");		 
		  response.setCharacterEncoding("UTF-8");
		  response.getWriter().write(jsonObject.toString());
		  System.out.println("写出后……");
		}catch(Exception e){
			System.out.println(e.getMessage());
		}		
	}
	
	@RequestMapping(value="/user/logout.json")
	public void logoutUser(HttpServletResponse response,HttpSession session){
		JSONObject jsonObject=new JSONObject();
		try{			
			if(session.getAttribute("userId")!=null){
			//if(user_id)
				String userid=(String) session.getAttribute("userId");
				System.out.println("将要推出登陆的userId为："+userid);
				//插入退出登录日志
				userDao.logoutSucessInsertLog("0", userid);
				session.removeAttribute("userId");
				jsonObject.put("retCode",0);
				jsonObject.put("retMsg","退出成功");				
			}else{
				jsonObject.put("retCode",1);
				jsonObject.put("retMsg","退出失败");
			}
		response.setHeader("Content-type", "text/json;charset=UTF-8");		 
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(jsonObject.toString());
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
	
	@RequestMapping(value="/user/forgetPassword.json")
	public void userForgetPassword(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String phone=request.getParameter("phone");
		String encrypted_password=request.getParameter("encrypted_password");
		String hasUser=userDao.loginSelectUsername(phone);
		JSONObject jsonObject=new JSONObject();
		try{
			if(hasUser!=null){
				userDao.forgetPassword(phone, encrypted_password);
				jsonObject.put("retCode",0);
				jsonObject.put("retMsg","重置密码成功");	
			}else{
				jsonObject.put("retCode",1);
				jsonObject.put("retMsg","该用户不存在");
			}
		}catch(Exception e){			
			System.out.println(e.getMessage());
		}
		response.setHeader("Content-type", "text/json;charset=UTF-8");		 
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(jsonObject.toString());
	}
	
	@RequestMapping(value="/user/{phone}/resetUser_name.json")
	public void userResetUser_name(@PathVariable String phone,HttpServletRequest request,HttpServletResponse response) throws IOException{
		String new_user_name=request.getParameter("new_user_name");
		JSONObject jsonObject=new JSONObject();
		try{		
			userDao.resetUser_name(phone, new_user_name);
			jsonObject.put("retCode",0);
			jsonObject.put("retMsg","修改用户名成功");						
		}catch(Exception e){
			System.out.println(e.getMessage());
			if(e.getMessage().toString().contains("MATCH.INDEX_USERS_ON_NAME)")){
				jsonObject.put("retCode",1);
				jsonObject.put("retMsg","该用户名已被使用");
			}
		}
		response.setHeader("Content-type", "text/json;charset=UTF-8");		 
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(jsonObject.toString());
		
	}
		
	@RequestMapping(value="/user/{phone}/resetPassword.json")
	public void userResetPassword(@PathVariable String phone,HttpServletRequest request,HttpServletResponse response) throws IOException{		
		String old_encrypted_password=request.getParameter("old_encrypted_password");
		String new_encrypted_password=request.getParameter("new_encrypted_password");
		//String new_user_name=request.getParameter("new_user_name");
		JSONObject jsonObject=new JSONObject();
		String selectold_encrypted_password=userDao.compareTwoPassword(phone);
		if(selectold_encrypted_password.equals(old_encrypted_password)){
			userDao.resetPassword(phone, new_encrypted_password);
			jsonObject.put("retCode",0);
			jsonObject.put("retMsg","重置密码成功");	
		}else{
			jsonObject.put("retCode",1);
			jsonObject.put("retMsg","旧密码输入错误");	
		}				
		response.setHeader("Content-type", "text/json;charset=UTF-8");		 
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(jsonObject.toString());
	}
	
	@RequestMapping(value="user/{id}/resetPersonalInfo.json")
	public void userResetPersonalInfo(@PathVariable String id,HttpServletRequest request,HttpServletResponse response) throws IOException{
		request.setCharacterEncoding("UTF-8");
		UserResetPersonalInfo userResetPersonalInfo=new UserResetPersonalInfo();
		userResetPersonalInfo.setAddress(request.getParameter("address"));
		userResetPersonalInfo.setAge(Integer.parseInt(request.getParameter("age")));
		userResetPersonalInfo.setEmail(request.getParameter("email"));
		userResetPersonalInfo.setGender(request.getParameter("gender"));
		userResetPersonalInfo.setPhone(request.getParameter("phone"));
		userResetPersonalInfo.setUser_name(request.getParameter("user_name"));
		userResetPersonalInfo.setId(id);
		Map params =  new HashMap();
		try{			
		    userDao.resetPersonalInfo(userResetPersonalInfo);
		    //jsonObject.put("retCode", 0);
		    //jsonObject.put("retMsg", "注册成功");
		    params.put("retCode", 0);
		    params.put("retMsg","修改成功");
		}catch(Exception e){
			params.put("retCode", 1);
			System.out.println("捕获异常:"+e.getMessage());
			if(e.getMessage().toString().contains("(MATCH.SYS_C0015223)")){
				System.out.println("该手机号码已经已被使用");
				//jsonObject.put("retMsg", "该手机号码已被注册过");
				params.put("retMsg","该手机号码已经已被使用");
			}
			else {
				if(e.getMessage().toString().contains("(MATCH.INDEX_USERS_ON_EMAIL)")){
				System.out.println("该邮箱已被使用");
				//jsonObject.put("retMsg", "该邮箱已被使用");
				params.put("retMsg","该邮箱已被使用");
				}
				if(e.getMessage().toString().contains("MATCH.INDEX_USERS_ON_PHONE")){
				System.out.println("该手机号码已被使用");
				//jsonObject.put("retMsg", "该手机号码已被注册过");
				params.put("retMsg","该手机号码已被使用");
				}
				if(e.getMessage().toString().contains("MATCH.INDEX_USERS_ON_NAME)")){
				System.out.println("该用户名已被使用");
				//jsonObject.put("retMsg", "该用户名已被使用");	
				params.put("retMsg","该用户名已被使用");
				}
			}
		}
		PrintWriter out=response.getWriter();
		//response.setContentLength("");
		JSONObject jsonObject = JSONObject.fromObject(params);
		response.setHeader("Content-type", "text/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");	
		response.setContentType("text/json");
		out.write(jsonObject.toString());
		out.flush();	
	}
	
	@RequestMapping(value="/user/{nowNumber}/{pageSize}/userLog.json")
	public void userLogPaganationSelect(@PathVariable int nowNumber,@PathVariable int pageSize,HttpServletRequest request,HttpServletResponse response) {
		try{
			List<UserLog> userlogs=userOperatorService.pageSelectUserLogs(nowNumber,pageSize);
			for(int i=0;i<userlogs.size();i++){
				System.out.println(userlogs.get(i).getCreated_at());
			}
			JSONArray jsonArray=JSONArray.fromObject(userlogs);		
			response.setHeader("Content-type", "text/json;charset=UTF-8");		 
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonArray.toString());
			}catch(Exception e){
				e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
	
	@RequestMapping(value="/user/recommendMatchCount.json")
	public void recommendMatchCount(HttpServletResponse response) throws IOException{
		
		int AllRecMatch=userDao.countAllRecMatch();//推荐战绩总场次
		int WinRecMatch=userDao.countWinRecMatch();//全赢场次
		int HalfWinRecMatch=userDao.countHalfWinRecMatch(); //半赢场次
		int DrawRecMatch=userDao.countDrawRecMatch(); //走盘场次
		int LostRecMatch=userDao.countLostRecMatch(); //全输场次
		int HalfLostRecMatch=userDao.countHalfLostRecMatch(); //半输场次
		
		JSONObject jsonObject=new JSONObject();
		//--------------input------------------
		jsonObject.put("AllRecMatch", AllRecMatch);
		jsonObject.put("WinRecMatch", WinRecMatch);
		jsonObject.put("HalfWinRecMatch", HalfWinRecMatch);
		jsonObject.put("DrawRecMatch", DrawRecMatch);
		jsonObject.put("LostRecMatch", LostRecMatch);
		jsonObject.put("HalfLostRecMatch", HalfLostRecMatch);
		response.setHeader("Content-type", "text/json;charset=UTF-8");		 
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(jsonObject.toString());		
	}
	
}
