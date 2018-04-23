package anfy.com.anfy.Model;

import java.io.Serializable;

/**
 * Created by sotra on 10/14/2017.
 */

public class SocialUser implements Serializable{
    public static final String FACEBOOK_SOCIAL_TYPE = "facebook" ;
    public static final String TWITTER_SOCIAL_TYPE = "twitter" ;
    public static final String GOOGLE_SOCIAL_TYPE = "google" ;
    String name  , photo , email , uid  ;
    String type ;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
