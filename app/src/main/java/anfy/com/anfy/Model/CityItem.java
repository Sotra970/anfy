package anfy.com.anfy.Model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CityItem implements Serializable{

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private int id;

	@SerializedName("country_id")
	private int countryId;

	public String getName(){
		return name;
	}

	public int getId(){
		return id;
	}

	public int getCountryId(){
		return countryId;
	}

	@Override
 	public String toString(){
		return 
			"CityItem{" + 
			"name = '" + name + '\'' + 
			",id = '" + id + '\'' + 
			",country_id = '" + countryId + '\'' + 
			"}";
		}
}