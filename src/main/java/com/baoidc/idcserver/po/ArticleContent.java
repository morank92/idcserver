package com.baoidc.idcserver.po;

import java.io.Serializable;

public class ArticleContent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6859709816308179021L;
	
	private int id;
	private int articleTypeId;
	private String articleTitle;
	private String publishTime;
	private String aContent;
	private String authorUserName;
	private ArticleType articleType; //文章分类
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getArticleTypeId() {
		return articleTypeId;
	}
	public void setArticleTypeId(int articleTypeId) {
		this.articleTypeId = articleTypeId;
	}
	public String getArticleTitle() {
		return articleTitle;
	}
	public void setArticleTitle(String articleTitle) {
		this.articleTitle = articleTitle;
	}
	public String getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}
	public String getaContent() {
		return aContent;
	}
	public void setaContent(String aContent) {
		this.aContent = aContent;
	}
	public ArticleType getArticleType() {
		return articleType;
	}
	public void setArticleType(ArticleType articleType) {
		this.articleType = articleType;
	}
	public String getAuthorUserName() {
		return authorUserName;
	}
	public void setAuthorUserName(String authorUserName) {
		this.authorUserName = authorUserName;
	}

}
