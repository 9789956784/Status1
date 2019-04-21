package com.virmana.status_app_all.api;

import com.virmana.status_app_all.config.Global;
import com.virmana.status_app_all.model.ApiResponse;
import com.virmana.status_app_all.model.Category;
import com.virmana.status_app_all.model.Comment;
import com.virmana.status_app_all.model.Payout;
import com.virmana.status_app_all.model.Slide;
import com.virmana.status_app_all.model.Status;
import com.virmana.status_app_all.model.Language;
import com.virmana.status_app_all.model.Transaction;
import com.virmana.status_app_all.model.User;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by Tamim on 28/09/2017.
 */

public interface apiRest {

    @GET("version/check/{code}/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<ApiResponse> check(@Path("code") Integer code);

    @FormUrlEncoded
    @POST("video/add/like/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<Integer> imageAddLike(@Field("id")  Integer id);


    @FormUrlEncoded
    @POST("video/add/love/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<Integer> imageAddLove(@Field("id")  Integer id);


    @FormUrlEncoded
    @POST("video/add/angry/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<Integer> imageAddAngry(@Field("id")  Integer id);


    @FormUrlEncoded
    @POST("video/add/haha/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<Integer> imageAddHaha(@Field("id")  Integer id);


    @FormUrlEncoded
    @POST("video/add/sad/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<Integer> imageAddSad(@Field("id")  Integer id);

    @FormUrlEncoded
    @POST("video/add/woow/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<Integer> imageAddWoow(@Field("id")  Integer id);

    @FormUrlEncoded
    @POST("video/delete/like/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<Integer> imageDeleteLike(@Field("id")  Integer id);

    @FormUrlEncoded
    @POST("video/delete/love/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<Integer> imageDeleteLove(@Field("id")  Integer id);

    @FormUrlEncoded
    @POST("video/delete/angry/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<Integer> imageDeleteAngry(@Field("id")  Integer id);

    @FormUrlEncoded
    @POST("video/delete/haha/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<Integer> imageDeleteHaha(@Field("id")  Integer id);

    @FormUrlEncoded
    @POST("video/delete/sad/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<Integer> imageDeleteSad(@Field("id")  Integer id);


    @FormUrlEncoded
    @POST("user/register/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<ApiResponse> register(@Field("name") String name, @Field("username") String username, @Field("password") String password, @Field("type") String type, @Field("image") String image);

    @GET("device/{tkn}/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<ApiResponse> addDevice(@Path("tkn")  String tkn);
    
    @GET("status/by/query/{order}/{language}/{page}/{query}/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<List<Status>> searchImage(@Path("order")  String order, @Path("language")  String language, @Path("page")  Integer page, @Path("query") String query);
    
    @GET("status/by/user/{page}/{order}/{language}/{user}/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<List<Status>> userImage(@Path("order")  String order, @Path("language")  String language, @Path("user") Integer user, @Path("page") Integer page);

    @GET("status/by/follow/{page}/{language}/{user}/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<List<Status>> followImage(@Path("page") Integer page, @Path("language")  String language, @Path("user") Integer user);

    @GET("status/by/me/{page}/{user}/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<List<Status>> meImage(@Path("page") Integer page, @Path("user") Integer user);

    @GET("comment/list/{id}/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<List<Comment>> getComments(@Path("id") Integer id);
    
    @FormUrlEncoded
    @POST("comment/add/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<ApiResponse> addCommentImage(@Field("user")  String user,@Field("id") Integer id,@Field("comment") String comment);

    @GET("user/follow/{user}/{follower}/{key}/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<ApiResponse> follow(@Path("user") Integer user,@Path("follower") Integer follower,@Path("key") String key);


    @GET("user/followers/{user}/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<List<User>> getFollowers(@Path("user") Integer user);

    @GET("user/followings/{user}/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<List<User>> getFollowing(@Path("user") Integer user);


    @GET("user/followingstop/{user}/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<List<User>> getFollowingTop(@Path("user") Integer user);

    @GET("user/get/{user}/{me}/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<ApiResponse> getUser(@Path("user") Integer user,@Path("me") Integer me);

    @FormUrlEncoded
    @POST("user/edit/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<ApiResponse> editUser(@Field("user") Integer user,@Field("key") String key,@Field("name") String name,@Field("email") String email,@Field("facebook") String facebook,@Field("twitter") String twitter,@Field("instagram") String instagram);


    @GET("install/add/{id}/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<ApiResponse> addInstall(@Path("id") String id);



    @FormUrlEncoded
    @POST("support/add/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<ApiResponse> addSupport(@Field("email") String email, @Field("name") String name , @Field("message") String message);



    @GET("category/all/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<List<Category>> categoriesImageAll();

    @GET("category/popular/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<List<Category>> categoriesPopular();



    @GET("language/all/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<List<Language>> languageAll();

    @GET("status/all/{page}/{order}/{language}/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<List<Status>> imageAll(@Path("page") Integer page, @Path("order") String order, @Path("language") String language);

    @GET("status/by/category/{page}/{order}/{language}/{category}/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<List<Status>> imageByCategory(@Path("page") Integer page, @Path("order") String order, @Path("language")  String language, @Path("category") Integer category);

    @GET("status/by/random/{language}/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<List<Status>> ImageByRandom(@Path("language")  String language);



    @FormUrlEncoded
    @POST("status/add/share/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<Integer> addShare(@Field("id")  Integer id,@Field("user")  Integer user,@Field("key")  String key);

    @FormUrlEncoded
    @POST("status/add/view/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<Integer> addView(@Field("id")  Integer id,@Field("user")  Integer user,@Field("key")  String key);

    @FormUrlEncoded
    @POST("video/delete/woow/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<Integer> imageDeleteWoow(@Field("id")  Integer id);



    @Multipart
    @POST("video/upload/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<ApiResponse> uploadVideo(@Part MultipartBody.Part file,@Part MultipartBody.Part file_thum,@Part("id") String id, @Part("key") String key, @Part("title") String title, @Part("description") String description,@Part("language") String language,@Part("categories") String categories);

    @Multipart
    @POST("image/upload/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<ApiResponse> uploadImage(@Part MultipartBody.Part file,@Part("id") String id, @Part("key") String key, @Part("title") String title, @Part("description") String description,@Part("language") String language,@Part("categories") String categories);

    @Multipart
    @POST("gif/upload/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<ApiResponse> uploadGif(@Part MultipartBody.Part file,@Part("id") String id, @Part("key") String key, @Part("title") String title, @Part("description") String description,@Part("language") String language,@Part("categories") String categories);


    @FormUrlEncoded
    @POST("user/token/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<ApiResponse> editToken(@Field("user") Integer user,@Field("key") String key,@Field("token_f") String token_f);

    @FormUrlEncoded
    @POST("quote/upload/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<ApiResponse> uploadQuote(@Field("id") String id, @Field("key")  String key,@Field("quote") String quote, @Field("color") String color,@Field("font") Integer font,@Field("language") String language,@Field("categories") String categories);


    @FormUrlEncoded
    @POST("user/code/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<ApiResponse> sendRefereceCode(@Field("user") Integer user,@Field("key") String key,@Field("code") String code);

    @GET("transaction/by/user/{user}/{key}/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<List<Transaction>> userTransaction(@Path("user") Integer user, @Path("key") String key);

    @GET("earning/by/user/{user}/{key}/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<ApiResponse> userEarning( @Path("user") Integer user, @Path("key") String key);

    @FormUrlEncoded
    @POST("request/withdrawal/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<ApiResponse> requestWithdrawal(@Field("user")  Integer user,@Field("key")  String key,@Field("method")  String method,@Field("account")  String account);


    @GET("withdrawals/by/user/{user}/{key}/"+ Global.SECURE_KEY+"/"+ Global.ITEM_PURCHASE_CODE+"/")
    Call<List<Payout>> userWithdrawals(@Path("user") Integer user, @Path("key") String key);


    @GET("slide/all/"+ Global.SECURE_KEY+"/"+Global.ITEM_PURCHASE_CODE+"/")
    Call<List<Slide>> slideAll();

}
