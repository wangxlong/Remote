package sport.user.register.domain;

import java.util.Date;

public class UserRegisterBean {
	private String id;
	private String email;
	private String encrypted_password;
	private String phone;
	//private int status;
	//private String email;
	private String user_name;
	private int  age;
	private String gender;
	private String address;
	//private Date current_sign_in_at;
	private String current_sign_in_ip;
	//private Date created_at;
	
	private int is_iphone;
	private String recommener;
	
	public int getIs_iphone() {
		return is_iphone;
	}
	public void setIs_iphone(int is_iphone) {
		this.is_iphone = is_iphone;
	}
	public String getRecommener() {
		return recommener;
	}
	public void setRecommener(String recommener) {
		this.recommener = recommener;
	}
	public String getCurrent_sign_in_ip() {
		return current_sign_in_ip;
	}
	public void setCurrent_sign_in_ip(String current_sign_in_ip) {
		this.current_sign_in_ip = current_sign_in_ip;
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
	public String getEncrypted_password() {
		return encrypted_password;
	}
	public void setEncrypted_password(String encrypted_password) {
		this.encrypted_password = encrypted_password;
	}
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
	
}
