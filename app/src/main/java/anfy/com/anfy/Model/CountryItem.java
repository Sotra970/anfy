package anfy.com.anfy.Model;

import com.google.gson.annotations.SerializedName;

public class CountryItem{

	@SerializedName("name")
	private String name;

	@SerializedName("icon")
	private String icon;

	@SerializedName("id")
	private int id;

	@SerializedName("phone_code")
	private String phoneCode;

	public String getName(){
		return name;
	}

	public String getIcon(){
		return icon;
	}

	public int getId(){
		return id;
	}

	public String getPhoneCode(){
		return phoneCode;
	}

	@Override
 	public String toString(){
		return 
			"CountryItem{" + 
			"name = '" + name + '\'' + 
			",icon = '" + icon + '\'' + 
			",id = '" + id + '\'' + 
			",phone_code = '" + phoneCode + '\'' + 
			"}";
		}
}