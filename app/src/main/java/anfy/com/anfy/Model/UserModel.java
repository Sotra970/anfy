package anfy.com.anfy.Model;

import com.google.gson.annotations.SerializedName;

public class UserModel{

	@SerializedName("image")
	private String image;

	@SerializedName("social_media_id")
	private String socialMediaId;

	@SerializedName("verfication_codde")
	private String verficationCodde;

	@SerializedName("phone")
	private String phone;

	@SerializedName("name")
	private String name;

	@SerializedName("social_media_name")
	private String socialMediaName;

	@SerializedName("id")
	private int id;

	@SerializedName("email")
	private String email;

	@SerializedName("country_id")
	private String countryId;

	@SerializedName("activated")
	private int activated;

	public String getImage(){
		return image;
	}

	public String getSocialMediaId(){
		return socialMediaId;
	}

	public String getVerficationCodde(){
		return verficationCodde;
	}

	public String getPhone(){
		return phone;
	}

	public String getName(){
		return name;
	}

	public String getSocialMediaName(){
		return socialMediaName;
	}

	public int getId(){
		return id;
	}

	public String getEmail(){
		return email;
	}

	public String getCountryId(){
		return countryId;
	}

	public int getActivated(){
		return activated;
	}

	@Override
 	public String toString(){
		return 
			"UserModel{" + 
			"image = '" + image + '\'' + 
			",social_media_id = '" + socialMediaId + '\'' + 
			",verfication_codde = '" + verficationCodde + '\'' + 
			",phone = '" + phone + '\'' + 
			",name = '" + name + '\'' + 
			",social_media_name = '" + socialMediaName + '\'' + 
			",id = '" + id + '\'' + 
			",email = '" + email + '\'' + 
			",country_id = '" + countryId + '\'' + 
			",activated = '" + activated + '\'' + 
			"}";
		}
}