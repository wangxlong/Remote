package sport.user.register.dao;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import sport.user.register.domain.RecordsFilteredBean;
import sport.user.register.domain.ShowUserInfoBean;
import sport.user.register.domain.UserVipBean;
import sport.user.register.util.BootstrapPar;

public interface ForcePaymentsDao {

	//查询某个用户离当前时间的已注册天数
	public String hasRegisterDate(String id);
	
	//查询某个用户的vip_to(vip截止日期)字段的值	
	public String selectVip_to(String id);
	
	//用户付款后，更新vip_to字段	
	public void updateVip_to(String id,String vip_to);	
	//查出所有的web上显示的user的vip情况
	public List<UserVipBean> selectAllWebUserVip(BootstrapPar bootstrapPar);
	//recordsFilteredSql
	public String selectAllWebUserVipFiltered(RecordsFilteredBean recordsFilteredBean);
	//计算users总人数
	public String vipCountUsers();
	//管理员在web上设置有效期时，点击确认后更新vip_to字段
	public void webUpdateVip_to(String user_name,String vip_to);
	//客户端获得vip_to字段
	public String toGetVip_to(String id);
	
	//（支付宝）查询支付订单是否已经存在，如果存在不做处理
	public String queryOut_Trade_No(String out_Trade_No);
	//（支付宝）付费记录插入数据库
	public void aliPayinsert(Map params);
	
	//(微信)查询支付订单 是否已经存在，如果存在不做处理
	public String wxqueryOut_Trade_No(String out_Trade_No);
	//（微信）付费记录插入数据库
	public void wxPayinsert(Map params);
}
