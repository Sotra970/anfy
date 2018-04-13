package anfy.com.anfy.Model;

import com.google.gson.annotations.SerializedName;

public class TopicSegment{

	@SerializedName("title")
	private String title;

	@SerializedName("body")
	private String body;

	public TopicSegment(String title, String body) {
		this.title = title;
		this.body = body;
	}

	public String getTitle(){
		return title;
	}

	public String getContent(){
		return body;
	}

	@Override
 	public String toString(){
		return 
			"TopicSegment{" + 
			"title = '" + title + '\'' + 
			",body = '" + body + '\'' + 
			"}";
		}
}