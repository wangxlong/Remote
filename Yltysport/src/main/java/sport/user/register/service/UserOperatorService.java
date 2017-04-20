package sport.user.register.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;
import sport.user.register.domain.UserLog;
import sport.user.register.domain.UserRegisterBean;

public interface UserOperatorService {
	
	public void registerUser(UserRegisterBean user);//注册用户
	public JSONObject loginUser(String nameOrPassword,String password,String userIp,HttpServletRequest request);//用户登录
	public List<UserLog> pageSelectUserLogs(int nowNumber,int pageSize);
}
