package anfy.com.anfy.Model;


import com.google.gson.annotations.SerializedName;

public class DepartmentItem{

	@SerializedName("icon")
	private String icon;

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private int id;

	public String getIcon(){
		return icon;
	}

	public String getName(){
		return name;
	}

	public int getId(){
		return id;
	}

	@Override
 	public String toString(){
		return 
			"DepartmentItem{" + 
			"icon = '" + icon + '\'' + 
			",name = '" + name + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}
}