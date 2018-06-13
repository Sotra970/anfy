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

	@SerializedName("disease")
	private String illness;

	@SerializedName("age")
	private String age;

	@SerializedName("gender")
	private String gender;


	public void setIllness(String illness) {
		this.illness = illness;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

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

	public UserModel() {
	}

	public void setImage(String image) {
		this.image = image;
	}

	public void setSocialMediaId(String socialMediaId) {
		this.socialMediaId = socialMediaId;
	}

	public void setVerficationCodde(String verficationCodde) {
		this.verficationCodde = verficationCodde;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSocialMediaName(String socialMediaName) {
		this.socialMediaName = socialMediaName;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}

	public void setActivated(int activated) {
		this.activated = activated;
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

    public String getAge() {
        return age;
    }

	public String getGender() {
		return gender;
	}

	public String getIllness() {
		return illness;
	}
}