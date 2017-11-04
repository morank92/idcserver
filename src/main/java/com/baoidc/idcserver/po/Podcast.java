package com.baoidc.idcserver.po;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Podcast implements Serializable {

	private static final long serialVersionUID = -1558873807231901788L;
	
	private int id;
	
	private String title;
	
	private String linkOnPodcastPedia;
	
	private String feed;
	
	private String description;
	
	private String insertionDate;
	
	public Podcast(){}
	
	public Podcast(String title,String linkOnPodcastPedia,String feed,String description){
		this.title = title;
		this.linkOnPodcastPedia = linkOnPodcastPedia;
		this.feed = feed;
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLinkOnPodcastPedia() {
		return linkOnPodcastPedia;
	}

	public void setLinkOnPodcastPedia(String linkOnPodcastPedia) {
		this.linkOnPodcastPedia = linkOnPodcastPedia;
	}

	public String getFeed() {
		return feed;
	}

	public void setFeed(String feed) {
		this.feed = feed;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getInsertionDate() {
		return insertionDate;
	}

	public void setInsertionDate(String insertionDate) {
		this.insertionDate = insertionDate;
	}

}
