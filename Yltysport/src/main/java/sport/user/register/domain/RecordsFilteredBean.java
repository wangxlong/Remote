package sport.user.register.domain;

public class RecordsFilteredBean {
	
	private String searchSQL;
	private String orderColumn;
	private String orderDir;
	public String getSearchSQL() {
		return searchSQL;
	}
	public void setSearchSQL(String searchSQL) {
		this.searchSQL = searchSQL;
	}
	public String getOrderColumn() {
		return orderColumn;
	}
	public void setOrderColumn(String orderColumn) {
		this.orderColumn = orderColumn;
	}
	public String getOrderDir() {
		return orderDir;
	}
	public void setOrderDir(String orderDir) {
		this.orderDir = orderDir;
	}
	
	
}
