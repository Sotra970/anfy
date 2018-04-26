package anfy.com.anfy.Model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class DoctorItem{

	@SerializedName("image")
	private String image;

	@SerializedName("country")
	private String country;

	@SerializedName("certificates")
	private List<String> certificates;

	@SerializedName("phone")
	private String phone;

	@SerializedName("city")
	private String city;

	@SerializedName("name")
	private String name;

	@SerializedName("specification")
	private String specification;

	@SerializedName("facebook")
	private String facebook;
	@SerializedName("twitter")
	private String twitter;
	@SerializedName("google")
	private String google;
	@SerializedName("linkedin")
	private String linkedin;
	@SerializedName("youtube")
	private String youtube;

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


	public String getFacebook() {
		return facebook;
	}

	public String getTwitter() {
		return twitter;
	}

	public String getGoogle() {
		return google;
	}

	public String getLinkedin() {
		return linkedin;
	}

	public String getYoutube() {
		return youtube;
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