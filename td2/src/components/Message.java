package components;

import java.util.Date;

public class Message {
	private String content;
	private Date date;
	private Date time;
	private Forum forum;
	private User sender;
	
	public Message() {
		// TODO Auto-generated constructor stub
	}
	
	//Getter
	public String getContent() {
		return content;
	}
	
	public Date getDate() {
		return date;
	}
	
	public Date getTime() {
		return time;
	}
	
	public Forum getForum() {
		return forum;
	}
	
	public User getSender() {
		return sender;
	}
	
	//Setter
	public void setContent(String content) {
		this.content = content;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public void setTime(Date time) {
		this.time = time;
	}
	
	public void setForum(Forum forum) {
		this.forum = forum;
	}
	
	public void setSender(User sender) {
		this.sender = sender;
	}
	
}
