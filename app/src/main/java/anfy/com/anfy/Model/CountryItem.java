package anfy.com.anfy.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CountryItem implements Serializable{

	@SerializedName("name")
	public String name;

	@SerializedName("icon")
	public String icon;

	@SerializedName("id")
	public int id;

	@SerializedName("phone_code")
	public String phoneCode;

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