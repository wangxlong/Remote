package sport.user.register.domain;

public class UserVipBean {
	
	private String user_name;
	private String vip_to;
	private String is_vip;
	
	public String getIs_vip() {
		return is_vip;
	}
	public void setIs_vip(String is_vip) {
		this.is_vip = is_vip;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getVip_to() {
		return vip_to;
	}
	public void setVip_to(String vip_to) {
		this.vip_to = vip_to;
	}
	@Override
	public String toString() {
		return "UserVipBean [user_name=" + user_name + ", vip_to=" + vip_to + ", is_vip=" + is_vip + "]";
	}

	
	

}
