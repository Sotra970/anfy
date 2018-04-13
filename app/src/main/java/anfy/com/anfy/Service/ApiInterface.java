package anfy.com.anfy.Service;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import anfy.com.anfy.Model.ArticleItem;
import anfy.com.anfy.Model.CountryItem;
import anfy.com.anfy.Model.DepartmentItem;
import anfy.com.anfy.Model.DoctorItem;
import okhttp3.ResponseBody;
import retrofit2.Call;
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
    Call<ResponseBody> phoneRegister(
            @Field("phone") String phone,
            @Field("country_id") int countryId
    );

    @GET("countries")
    Call<ArrayList<CountryItem>> countries();

    @POST("send_verfication")
    @FormUrlEncoded
    Call<ResponseBody> sendVerfication(
            @Field("phone") String phone,
            @Field("country_id") int countryId
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
}
