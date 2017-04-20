package sport.user.register.domain;

import java.util.Date;

public class ShowWebUserLogBean {
	
	private String id;//日志id
	private String user_id;//用户id
	private AssociationUserLog associationUserLog; //日志所属的用户基本信息
	private String op_type;//登录或者退出，1表示登录，2表示退出
	private Date created_at;//登录或者退出的时间
	//private String [] color={"class=\"success\"","class=\"error\"","","class=\"warning\"","class=\"info\""};
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public AssociationUserLog getAssociationUserLog() {
		return associationUserLog;
	}
	public void setAssociationUserLog(AssociationUserLog associationUserLog) {
		this.associationUserLog = associationUserLog;
	}
	public String getOp_type() {
		return op_type;
	}
	public void setOp_type(String op_type) {
		this.op_type = op_type;
	}
	public Date getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	@Override
	public String toString() {
		return "ShowWebUserLogBean [id=" + id + ", user_id=" + user_id + ", associationUserLog=" + associationUserLog.toString()
				+ ", op_type=" + op_type + ", created_at=" + created_at + "]";
	}
	

	
	
}
