package sport.user.register.domain;

public class ReturnUserLoginBean {
	
	private String id;
	private String email;
	//private String encrypted_password;
	private String phone;
	private String user_name;
	private int  age;
	private String gender;
	private String address;
	
	private String fromRegisterDate; //已注册天数
	private String vip_to; //vip有效时间
	
	
	
	public String getFromRegisterDate() {
		return fromRegisterDate;
	}
	public void setFromRegisterDate(String fromRegisterDate) {
		this.fromRegisterDate = fromRegisterDate;
	}
	public String getVip_to() {
		return vip_to;
	}
	public void setVip_to(String vip_to) {
		this.vip_to = vip_to;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	/*public String getEncrypted_password() {
		return encrypted_password;
	}
	public void setEncrypted_password(String encrypted_password) {
		this.encrypted_password = encrypted_password;
	}
	*/
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return ("姓名为："+this.getUser_name()+"  手机号码为："+this.getPhone()+"  性别为："+this.getGender()+"  年龄为："+this.getAge()
		+"  地址为："+this.getAddress()+"  邮箱为："+this.getEmail());
	}
	
	
}
