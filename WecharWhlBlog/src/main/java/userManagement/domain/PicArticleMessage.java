package userManagement.domain;

import java.util.List;

public class PicArticleMessage extends BaseMessage{

	private String ArticleCount;
	private List<Article> Articles;
	public String getArticleCount() {
		return ArticleCount;
	}
	public void setArticleCount(String articleCount) {
		ArticleCount = articleCount;
	}
	public List<Article> getArticles() {
		return Articles;
	}
	public void setArticles(List<Article> articles) {
		Articles = articles;
	}
	@Override
	public String toString() {
		return "PicArticleMessage [ArticleCount=" + ArticleCount + ", Articles=" + Articles + "]";
	}
	
	
}
