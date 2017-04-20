package sport.user.register.domain;

import java.util.Date;

public class UserPaymentBean {
	
	private String id;  //记录ID
	
	private String app; //应用 ID
	private String cbi;  //用户ID
	private String ct;  //支付完成时间
	private int fee;//付费金额
	private String pt;//付费时间
	private String sdk;//梁道在易接服务器上的ID
	private String ssid;//订单在梁道平台上的流水号
	private int st ;//是否成功，1表示陈宫，其他表示失败
	private String tcd;//订单在易接服务器上的订单号
	private String uid;//付费用户 在梁道上的唯一标记
	private String ver;//协议版本号，目前为1
	private String sign ;//上述内容的签名
	
	//private Date created_at;
	//private Date updated_at;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getApp() {
		return app;
	}
	public void setApp(String app) {
		this.app = app;
	}
	public String getCbi() {
		return cbi;
	}
	public void setCbi(String cbi) {
		this.cbi = cbi;
	}
	public String getCt() {
		return ct;
	}
	public void setCt(String ct) {
		this.ct = ct;
	}
	public int getFee() {
		return fee;
	}
	public void setFee(int fee) {
		this.fee = fee;
	}
	public String getPt() {
		return pt;
	}
	public void setPt(String pt) {
		this.pt = pt;
	}
	public String getSdk() {
		return sdk;
	}
	public void setSdk(String sdk) {
		this.sdk = sdk;
	}
	public String getSsid() {
		return ssid;
	}
	public void setSsid(String ssid) {
		this.ssid = ssid;
	}
	public int getSt() {
		return st;
	}
	public void setSt(int st) {
		this.st = st;
	}
	public String getTcd() {
		return tcd;
	}
	public void setTcd(String tcd) {
		this.tcd = tcd;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getVer() {
		return ver;
	}
	public void setVer(String ver) {
		this.ver = ver;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	/*
	public Date getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}
	public Date getUpdated_at() {
		return updated_at;
	}
	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}
	*/
	@Override
	public String toString() {
		return "UserPaymentBean [id=" + id + ", app=" + app + ", cbi=" + cbi + ", ct=" + ct + ", fee=" + fee + ", pt="
				+ pt + ", sdk=" + sdk + ", ssid=" + ssid + ", st=" + st + ", tcd=" + tcd + ", uid=" + uid + ", ver="
				+ ver + ", sign=" + sign + "]";
	}
	

}
