package resources.Database.entities;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Posts
{
	private String id;
	private String title;
	private Communities community;
	private Users creator;
	private String date;
	private String msg;
	private String image;
	private String parentId;
	private List<Users> likes;
	

	public String getTitle() {
		return title;
	}


	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public List<Users> getLikes() {
		return likes;
	}

	public String getMessage() {
		return msg;
	}


	public void setMessage(String message) {
		this.msg = message;
	}

	public void setLikes(List<Users> likes) {
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
