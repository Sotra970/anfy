package anfy.com.anfy.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ConsultationItem implements Serializable{

    public final static String GENDER_MALE = "male";
    public final static String GENDER_FEMALE = "female";

    @SerializedName("id")
    private int id;

	@SerializedName("gender")
	private String gender;

	@SerializedName("user_id")
	private int userId;

	@SerializedName("details")
	private String details;

	@SerializedName("age")
	private Integer age;

	@SerializedName("time_stamp")
    private long timeStamp;

	public String getGender(){
		return gender;
	}

	public int getUserId(){
		return userId;
	}

	public String getDetails(){
		return details;
	}

	public int getAge(){
		return age;
	}

    public ConsultationItem(String gender, int userId, String details, int age) {
        this.gender = gender;
        this.userId = userId;
        this.details = details;
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    @Override
 	public String toString(){
		return 
			"ConsultationItem{" + 
			"gender = '" + gender + '\'' + 
			",user_id = '" + userId + '\'' + 
			",details = '" + details + '\'' + 
			",age = '" + age + '\'' + 
			"}";
		}
}