package anfy.com.anfy.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NotificationItem implements Serializable{

	@SerializedName("read")
	private int read;

	@SerializedName("time_stamp")
	private int timeStamp;

	@SerializedName("user_id")
	private int userId;

	@SerializedName("id")
	private int id;

	@SerializedName("title")
	private String title;

	@SerializedName("body")
	private String body;

	@SerializedName("tybe")
	private int tybe;

	@SerializedName("relation_id")
	private int relationId;

	public void setRead(int read){
		this.read = read;
	}

	public int getRead(){
		return read;
	}

	public void setTimeStamp(int timeStamp){
		this.timeStamp = timeStamp;
	}

	public int getTimeStamp(){
		return timeStamp;
	}

	public void setUserId(int userId){
		this.userId = userId;
	}

	public int getUserId(){
		return userId;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setBody(String body){
		this.body = body;
	}

	public String getBody(){
		return body;
	}

	public void setTybe(int tybe){
		this.tybe = tybe;
	}

	public int getTybe(){
		return tybe;
	}

	public void setRelationId(int relationId){
		this.relationId = relationId;
	}

	public int getRelationId(){
		return relationId;
	}

	@Override
 	public String toString(){
		return 
			"NotificationItem{" + 
			"read = '" + read + '\'' + 
			",time_stamp = '" + timeStamp + '\'' + 
			",user_id = '" + userId + '\'' + 
			",id = '" + id + '\'' + 
			",title = '" + title + '\'' + 
			",body = '" + body + '\'' + 
			",tybe = '" + tybe + '\'' + 
			",relation_id = '" + relationId + '\'' + 
			"}";
		}

	public boolean isRead() {
		return  read == 0 ? false : true ;
	}
}