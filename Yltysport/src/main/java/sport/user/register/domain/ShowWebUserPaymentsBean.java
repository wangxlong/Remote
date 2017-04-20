package sport.user.register.domain;

import java.util.Date;

public class ShowWebUserPaymentsBean {

	private String id;
	private double fee;
	private Date created_at;
	private AssociationUserPayments associationUserPayments;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public double getFee() {
		return fee;
	}
	public void setFee(double fee) {
		this.fee = fee;
	}
	public Date getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Date created_at) {
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
		return "ShowWebUserPaymentsBean [fee=" + fee + ", created_at=" + created_at + ", associationUserPayments="
				+ associationUserPayments.toString() + "]";
	}	
}
