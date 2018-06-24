package anfy.com.anfy.Model;

import com.google.gson.annotations.SerializedName;

public class StaticDataItem{

	@SerializedName("reserved_word")
	private String reservedWord;

	@SerializedName("rel")
	private int rel;

	@SerializedName("details")
	private String details;

	@SerializedName("id")
	private int id;

	@SerializedName("title")
	private String title;

	@SerializedName("details_en")
	private String detailsEn;

	public void setReservedWord(String reservedWord){
		this.reservedWord = reservedWord;
	}

	public String getReservedWord(){
		return reservedWord;
	}

	public void setRel(int rel){
		this.rel = rel;
	}

	public int getRel(){
		return rel;
	}

	public void setDetails(String details){
		this.details = details;
	}

	public String getDetails(){
		return details;
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

	public void setDetailsEn(String detailsEn){
		this.detailsEn = detailsEn;
	}

	public String getDetailsEn(){
		return detailsEn;
	}

	@Override
 	public String toString(){
		return 
			"StaticDataItem{" + 
			"reserved_word = '" + reservedWord + '\'' + 
			",rel = '" + rel + '\'' + 
			",details = '" + details + '\'' + 
			",id = '" + id + '\'' + 
			",title = '" + title + '\'' + 
			",details_en = '" + detailsEn + '\'' + 
			"}";
		}
}