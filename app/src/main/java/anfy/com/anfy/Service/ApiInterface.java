package anfy.com.anfy.Service;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import anfy.com.anfy.Model.ArticleItem;
import anfy.com.anfy.Model.CityItem;
import anfy.com.anfy.Model.ConsultationItem;
import anfy.com.anfy.Model.CountryItem;
import anfy.com.anfy.Model.DepartmentItem;
import anfy.com.anfy.Model.DoctorItem;
import anfy.com.anfy.Model.UserModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Ahmed on 8/29/2017.
 */

public interface ApiInterface {


    @POST("phone_regitser")
    @FormUrlEncoded
    Call<UserModel> phoneRegister(
            @Field("phone") String phone,
            @Field("country_id") int countryId
    );

    @GET("countries")
    Call<ArrayList<CountryItem>> countries();

    @GET("country/{id}/cities")
    Call<ArrayList<CityItem>> cities(@Path("id") int countryId);

    @POST("send_verfication")
    @FormUrlEncoded
    Call<ResponseBody> sendVerfication(
            @Field("phone") String phone,
            @Field("user_id") int userID
    );

    @POST("user/verfy")
    @FormUrlEncoded
    Call<UserModel> verifyCode(
            @Field("id") int id,
            @Field("verfication_code") String code
    );

    @GET("departments")
    Call<ArrayList<DepartmentItem>> getDepartments();

    @POST("department/articles")
    @FormUrlEncoded
    Call<ArrayList<ArticleItem>> getDepartmentArticles(
            @Field("user_id") int user_id,
            @Field("department_id") int department_id
    );

    @POST("article")
    @FormUrlEncoded
    Call<ArticleItem> getArticle(
            @Field("article_id")  int article_id,
            @Field("user_id") int user_id
    );

    @GET("user/{id}/articles/favorites")
    Call<ArrayList<ArticleItem>> getUserFavourites(
            @Path("id") int user_id
    );

    @POST("user/articles/favorites/add")
    @FormUrlEncoded
    Call<ResponseBody> addFav(
            @Field("user_id") int user_id,
            @Field("article_id") int article_id
    );

    @POST("user/articles/favorites/remove")
    @FormUrlEncoded
    Call<ResponseBody> removeFav(
            @Field("user_id") int user_id,
            @Field("article_id") int article_id
    );


    @GET("about_us")
    Call<ArrayList<DoctorItem>> getAboutUs();

    @POST("doctors")
    Call<ArrayList<DoctorItem>> getDoctors();

    @POST("doctors")
    @FormUrlEncoded
    Call<ArrayList<DoctorItem>> getDoctors(@Field("country_id") int countryId);

    @POST("doctors")
    @FormUrlEncoded
    Call<ArrayList<DoctorItem>> getDoctors(@Field("country_id")int countryId, @Field("city_id")int cityId);

    @POST("consultation/add")
    Call<ResponseBody> sendCosult(@Body ConsultationItem consultationItem);

    @GET("user/{id}/consultation")
    Call<ArrayList<ConsultationItem>> getConsults(@Path("id") int userID);

    @POST("suggestions/add")
    @FormUrlEncoded
    Call<ResponseBody> addSuggestion(@Field("user_id") int user_id, @Field("details") String details);

    @POST("user/email/update")
    @FormUrlEncoded
    Call<ResponseBody> changeEmail(@Field("id") int userId, @Field("email") String email, @Field("verfication_code") String verify);

    @POST("user/phone/update")
    @FormUrlEncoded
    Call<ResponseBody> changePhone(@Field("id") int userId, @Field("phone ") String phone , @Field("verfication_code") String verify);

    @POST("user/name/update")
    @FormUrlEncoded
    Call<ResponseBody> changeName(@Field("id") int userId, @Field("name") String name, @Field("verfication_code") String verify);


}
