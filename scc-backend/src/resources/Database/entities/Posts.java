package resources.Database.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Posts
{
	private String id;
	private String title;
	private Communities community;
	private Users creator;
	private String msg;
	private String image;
	private String parentId;
	private int likes;
	

	public String getTitle() {
		return title;
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

	public int getLikes() {
		return likes;
	}

	public String getMessage() {
		return msg;
	}


	public void setMessage(String message) {
		this.msg = message;
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
