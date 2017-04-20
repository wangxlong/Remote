package sport.user.register.util;

public class BootstrapPar {
	
	private String draw;//获取请求次数
	private String start;//数据的起始位置
	private String length;//数据的长度
	private String orderDir;//某一列的排序方式（升或降）
	private String orderColumn;//需要排序的那一列
	private String individualSearch;//搜索过滤条件
	//private 
	public String getDraw() {
		return draw;
	}
	public void setDraw(String draw) {
		this.draw = draw;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getLength() {
		return length;
	}
	public void setLength(String length) {
		this.length = length;
	}
	public String getOrderDir() {
		return orderDir;
	}
	public void setOrderDir(String orderDir) {
		this.orderDir = orderDir;
	}
	public String getOrderColumn() {
		return orderColumn;
	}
	public void setOrderColumn(String orderColumn) {
		this.orderColumn = orderColumn;
	}
	public String getIndividualSearch() {
		return individualSearch;
	}
	public void setIndividualSearch(String individualSearch) {
		this.individualSearch = individualSearch;
	}
	
}
