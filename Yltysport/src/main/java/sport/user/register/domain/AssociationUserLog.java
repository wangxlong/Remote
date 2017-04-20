package sport.user.register.domain;

public class AssociationUserLog {
	
	//private String user_id;//用户ID
	private String user_name;//用户名称
	private String  current_sign_in_ip;//登录IP地址
	
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getCurrent_sign_in_ip() {
		return current_sign_in_ip;
	}
	public void setCurrent_sign_in_ip(String current_sign_in_ip) {
		this.current_sign_in_ip = current_sign_in_ip;
	}
	@Override
	public String toString() {
		return "AssociationUserLog [user_name=" + user_name + ", current_sign_in_ip=" + current_sign_in_ip + "]";
	}
	
	
	
}
