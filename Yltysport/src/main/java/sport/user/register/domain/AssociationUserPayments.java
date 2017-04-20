package sport.user.register.domain;

public class AssociationUserPayments {

	//private String id;
	private String user_name;

	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	@Override
	public String toString() {
		return "AssociationUserPayments [user_name=" + user_name + "]";
	}
	
}
