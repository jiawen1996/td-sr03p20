package message;

import java.io.Serializable;

public class TextMessage implements Serializable{
	private String msg = "";

	public TextMessage(String msg) {
		this.msg = msg;
	}
	
	public String getMsg() {
		return this.msg;
	}
}
