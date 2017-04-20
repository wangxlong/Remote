package sport.user.register.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import net.sf.json.JSONObject;
import sport.user.register.dao.UserOperatorDao;
import sport.user.register.domain.ReturnUserLoginBean;
import sport.user.register.domain.UserLog;
import sport.user.register.domain.UserRegisterBean;
import sport.user.register.util.Page;
import sport.user.register.util.TimeConverter;

@Service
public class UserOperatorServiceImpl implements UserOperatorService{

	@Autowired(required=true)  
	   private UserOperatorDao userDao; 
	
	@Override
	public void registerUser(UserRegisterBean user) {
		// TODO Auto-generated method stub
		System.out.println("开始调用registerUser中的registerUserDao函数");
		//ApplicationContext ctx=null;
		//ctx=new ClassPathXmlApplicationContext("classpath:application.xml");
		//UserOperatorDao userOperatorDao=(UserOperatorDao) ctx.getBean("userOperatorDao");
		userDao.registerUserdao(user);
		
	}
	@Override
	public JSONObject loginUser(String nameOrPhone, String password,String userIp,HttpServletRequest request) {
		
		//检查是否存在该user_name对于的用户,如果有返回用户密码，没有则返回null
		JSONObject jsonObject=new JSONObject();
		String encrypted_password=userDao.loginSelectUsername(nameOrPhone);
		System.out.println(nameOrPhone+" 用户对应的密码为："+encrypted_password);
		//String phone_encrypted_password=userDao.loginSelectPhone(nameOrPhone);
		//System.out.println(nameOrPhone+" 手机对应的密码为："+phone_encrypted_password);
		JSONObject Nulljson=new JSONObject();
		if(encrypted_password==null)
		{				
			System.out.println("该用户不存在");
			jsonObject.put("retCode", 1);
			jsonObject.put("retMsg", "该用户不存在");
			jsonObject.put("userinfo", Nulljson.toString());
			
		}else{
			System.out.println("该用户存在");
			if(password.equals(encrypted_password)){
				System.out.println("登录成功");
				jsonObject.put("retCode", 0);
				jsonObject.put("retMsg", "登录成功");
				ReturnUserLoginBean returnUserLoginBean=userDao.returnUserLoginBean(nameOrPhone);
				returnUserLoginBean.setFromRegisterDate(String.valueOf(TimeConverter.getNowDaySub(returnUserLoginBean.getFromRegisterDate())));
				JSONObject jsonReturnUserLoginBean = JSONObject.fromObject(returnUserLoginBean);
				jsonObject.put("userinfo",jsonReturnUserLoginBean);
				/**
				 * 在这里插入一系列的登录后的操作
				 */
				//sign_in_count+1操作
				//userDao.loginAndUpdateSign_in_count(nameOrPhone);
				//交换current_sign_in_at和last_sign_in_at操作(交换time)
				//userDao.exchangeTime(nameOrPhone);
				//交换IP
				//userDao.exchangeIp(nameOrPhone, userIp);
				//查询出登录后的User的ID
				//updateAfterLogin
				userDao.updateAfterLogin(nameOrPhone, userIp);
				String userID=userDao.selectUserId(nameOrPhone);
				//记录在Session中
				HttpSession loginSession=request.getSession();
				loginSession.setAttribute("userId",userID);
				loginSession.setMaxInactiveInterval(-1);
				System.out.println("登录后的userID为："+userID);
				//登录成功后，插入日志表中
				userDao.loginSucessInsertLog("0",userID);
			}else{
				System.out.println("密码错误");
				jsonObject.put("retCode", 1);
				jsonObject.put("retMsg", "密码错误");
				jsonObject.put("userinfo",Nulljson);
			}
		}
		
		
		//ReturnUserLoginBean returnUserLoginBean=userDao.returnUserLoginBean(nameOrPhone);
		//System.out.println("查询出来的对象为：");
		//System.out.println(returnUserLoginBean.toString());
		//检查是否存在该phone对于的用户
		//userDao.loginSelectPhone(nameOrPhone);
		
		
		return jsonObject;
		// TODO Auto-generated method stub
		
	}
	@Override
	public List<UserLog> pageSelectUserLogs(int nowNumber,int pageSize) {
		// TODO Auto-generated method stub
		System.out.println("处理service方法中……");
		System.out.println("当前页："+nowNumber+" 每页行数："+pageSize);
		Page<UserLog> page = new Page<UserLog>();
        page.setPageNo(nowNumber);
        page.setPageSize(pageSize);
		List<UserLog> userLogs=(List<UserLog>) userDao.paginationSelect(page);
		System.out.println("sevice方法处理结束");
		return userLogs;
	}
}
