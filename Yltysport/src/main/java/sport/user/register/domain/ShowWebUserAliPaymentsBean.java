package sport.user.register.domain;

import java.util.Date;

public class ShowWebUserAliPaymentsBean {
	private String id;
	private String fee;
	private String created_at;
	private AssociationUserPayments associationUserPayments;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	public AssociationUserPayments getAssociationUserPayments() {
		return associationUserPayments;
	}
	public void setAssociationUserPayments(AssociationUserPayments associationUserPayments) {
		this.associationUserPayments = associationUserPayments;
	}
	@Override
	public String toString() {
		return "ShowWebUserAliPaymentsBean [id=" + id + ", fee=" + fee + ", created_at=" + created_at
				+ ", associationUserPayments=" + associationUserPayments + "]";
	}
	
	

}
