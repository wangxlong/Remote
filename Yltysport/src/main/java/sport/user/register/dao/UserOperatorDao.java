package sport.user.register.dao;

import java.util.List;

import sport.user.register.domain.AssociationUserLog;
import sport.user.register.domain.RecordsFilteredBean;
import sport.user.register.domain.ReturnUserLoginBean;
import sport.user.register.domain.ShowUserInfoBean;
import sport.user.register.domain.ShowWebUserAliPaymentsBean;
import sport.user.register.domain.ShowWebUserLogBean;
import sport.user.register.domain.ShowWebUserPaymentsBean;
import sport.user.register.domain.ShowWebUserWxPaymentsBean;
import sport.user.register.domain.UserLog;
import sport.user.register.domain.UserPaymentBean;
import sport.user.register.domain.UserRegisterBean;
import sport.user.register.domain.UserResetPersonalInfo;
import sport.user.register.util.BootstrapPar;
import sport.user.register.util.Page;

public interface UserOperatorDao {
	
      //注册时候使用
	  public void registerUserdao(UserRegisterBean user);
	  //登陆将 “登陆次数”加1
	  //public void loginAndUpdateSign_in_count(String user_nameOrPhone);
	  //查询是否有该用户
	  public String loginSelectUsername(String user_nameOrPhone);
	  //查询是否有该手机号对于的用户
	  //public String loginSelectPhone(String phone);
	  //返回登陆用户的基本信息
	  public ReturnUserLoginBean returnUserLoginBean(String user_nameOrphone);
	  //交换current_sign_in_at和last_sign_in_at操作
	  //public void exchangeTime(String user_nameOrphone);
	  //交换IP,时间，sign_count+1
	  public void updateAfterLogin(String user_nameOrphone,String currentIp);
	  //查询登录后的用户ID
	  public String selectUserId(String user_nameOrphone);
	  //登录成功插入登录日志中
	  public void loginSucessInsertLog(String id,String user_id);
	  //退出登录成功插入日志中
	  public void logoutSucessInsertLog(String id,String user_id);
	  //忘记密码，插入新的密码
	  public void forgetPassword(String phone,String encrypted_password);
	  //重置密码接口
	  public void resetPassword(String phone,String new_encrypted_password);
	  //重置用户名
	  public void resetUser_name(String phone,String new_user_name);
	  //重置密码时，验证输入的旧密码是否一样
	  public String compareTwoPassword(String phone);
	  //分页查询
	  public List<UserLog> paginationSelect(Page page);
	  //修改用户基本信息
	  public void resetPersonalInfo(UserResetPersonalInfo userResetPersonalInfo);
	  //付费记录插入（易接）
	  public void userPayments(UserPaymentBean userPaymentBean);
	  //付费记录查重
	  public String userPaymentsRetry(String tcd);
	  //获取users表的总记录数
	  public int countUsers();
	  //查出所有的web上显示的userinfo
	  public List<ShowUserInfoBean> selectAllWebUserInfo(BootstrapPar bootstrapPar);
	  //recordsFilteredSql
	  public String selectAllWebUserInfoFiltered(RecordsFilteredBean recordsFilteredBean);
	  //下载user表
	  public List<ShowUserInfoBean> downloadUsers();
	  //web上显示的某个人的登录日志
	  public List<ShowWebUserLogBean> webShowUserLog(String id);
	  //易接得到某个人的付费记录
	  public List<ShowWebUserPaymentsBean> webShowUserPayments(String id);
	  //支付宝得到某人的付费记录
	  public List<ShowWebUserAliPaymentsBean> webShowUserAliPayments(String id);
	//微信得到某人的付费记录
	  public List<ShowWebUserWxPaymentsBean> webShowUserWxPayments(String id);
	  //web管理员登录判断
	  public String webAdminLogin(String user_name_phone,String encrypted_password);
	  //删除用户
	  public void deleteUser(String id);
	  
	  //统计推荐战绩总数
	  public int countAllRecMatch();
	  //统计推荐战绩全胜场次
	  public int countWinRecMatch();
	  //统计推荐战绩半胜场次
	  public int countHalfWinRecMatch();
	  //统计推荐战绩走盘
	  public int countDrawRecMatch();
	  //统计推荐战绩全输场次
	  public int countLostRecMatch();
	  //统计推荐战绩半输场次
	  public int countHalfLostRecMatch();
	  
}
