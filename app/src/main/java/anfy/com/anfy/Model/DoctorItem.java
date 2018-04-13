package anfy.com.anfy.Model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class DoctorItem{

	@SerializedName("image")
	private String image;

	@SerializedName("country")
	private String country;

	@SerializedName("certificates ")
	private List<String> certificates;

	@SerializedName("phone")
	private String phone;

	@SerializedName("city")
	private String city;

	@SerializedName("name")
	private String name;

	@SerializedName("specification")
	private String specification;

	@SerializedName("id")
	private int id;

	public String getImage(){
		return image;
	}

	public String getCountry(){
		return country;
	}

	public List<String> getCertificates(){
		return certificates;
	}

	public String getPhone(){
		return phone;
	}

	public String getCity(){
		return city;
	}

	public String getName(){
		return name;
	}

	public String getSpecification(){
		return specification;
	}

	public int getId(){
		return id;
	}

	@Override
 	public String toString(){
		return 
			"DoctorItem{" + 
			"image = '" + image + '\'' + 
			",country = '" + country + '\'' + 
			",certificates  = '" + certificates + '\'' + 
			",phone = '" + phone + '\'' + 
			",city = '" + city + '\'' + 
			",name = '" + name + '\'' + 
			",specification = '" + specification + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}
}