package sport.user.register.domain;

import java.util.Date;

public class ShowUserInfoBean {
	
	private String id; 
	private String user_name;
	private String phone;
	private String email;
	private int age;
	private String gender;
	private String address;
	private String created_at;
	private String  is_iphone;
	private String recommener;
	
	
	public String getIs_iphone() {
		return is_iphone;
	}
	public void setIs_iphone(String is_iphone) {
		this.is_iphone = is_iphone;
	}
	public String getRecommener() {
		return recommener;
	}
	public void setRecommener(String recommener) {
		this.recommener = recommener;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	@Override
	public String toString() {
		return "ShowUserInfoBean [id=" + id + ", user_name=" + user_name + ", phone=" + phone + ", email=" + email
				+ ", age=" + age + ", gender=" + gender + ", address=" + address + ", created_at=" + created_at + "]";
	}
	
	

}
