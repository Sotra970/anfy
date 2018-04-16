package anfy.com.anfy.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ArticleItem{

	@SerializedName("cover")
	private String cover;

	@SerializedName("dep_id")
	private String depId;

	@SerializedName("time_stamp")
	private String timeStamp;

	@SerializedName("is_fav")
	private int isFav;

	@SerializedName("releated_articles")
	private ArrayList<ArticleItem> releatedArticles;

	@SerializedName("id")
	private int id;

	@SerializedName("title")
	private String title;

	@SerializedName("content")
	private ArrayList<TopicSegment> contents;

	public String getCover(){
		return cover;
	}

	public String getDepId(){
		return depId;
	}

	public String getTimeStamp(){
		return timeStamp;
	}

	public void setIsFav(boolean isFav) {
		this.isFav = isFav ? 1 : 0;
	}

	public boolean isFav(){
		return isFav == 1;
	}

    public ArrayList<ArticleItem> getReleatedArticles() {
        return releatedArticles;
    }

    public int getId(){
		return id;
	}

	public String getTitle(){
		return title;
	}

	public ArrayList<TopicSegment> getContents() {
		return contents;
	}

	@Override
 	public String toString(){
		return 
			"ArticleItem{" + 
			"cover = '" + cover + '\'' + 
			",dep_id = '" + depId + '\'' + 
			",time_stamp = '" + timeStamp + '\'' + 
			",is_fav = '" + isFav + '\'' + 
			",releated_articles = '" + releatedArticles + '\'' + 
			",id = '" + id + '\'' + 
			",title = '" + title + '\'' + 
			"}";
		}
}