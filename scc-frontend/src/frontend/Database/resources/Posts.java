package frontend.Database.resources;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Posts
{
	private String id;
	private String title;
	private Communities community;
	private Users creator;
	private String message;
	private Date date;
	private String linkToMultimedia;
	private String linkToParent;
	private int likes;
	

	public String getTitle() {
		return title;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getDate() {
		return date;
	}

	public String getLinkToMultimedia() {
		return linkToMultimedia;
	}

	public void setLinkToMultimedia(String linkToMultimedia) {
		this.linkToMultimedia = linkToMultimedia;
	}

	public String getLinkToParent() {
		return linkToParent;
	}

	public void setLinkToParent(String linkToParent) {
		this.linkToParent = linkToParent;
	}

	public int getLikes() {
		return likes;
	}

	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	public Communities getCommunity() {
		return community;
	}

	public void setCommunity(Communities community) {
		this.community = community;
	}

	public Users getCreator() {
		return creator;
	}

	public void setCreator(Users user) {
		this.creator = user;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
