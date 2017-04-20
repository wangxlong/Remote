package sport.user.register.dao;

import java.util.List;

import sport.user.register.domain.AnnouncementBean;

public interface webOperatorDao {
	
	//插入公告内容
	public void writeAnnouncement(String title,String content);
	
	//获取当月的所有公告	
	public List<AnnouncementBean> selectThisMonthAnnouncement();
	
	//获取指定的公告
	public AnnouncementBean getIdAnnouncementBean(String id);
	
	//删除公告
	
	public void deleteAnnouncement(String id);

}
