package guru.qa.niffler.api;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AuthApi {

    @GET("/register")
    Call<Void> registerPage();

    @FormUrlEncoded
    @POST("/register")
    Call<Void> registerUser(@Field("username") String username,
                            @Field("password") String password,
                            @Field("passwordSubmit") String passwordSubmit,
                            @Field("_csrf") String csrf);

    @FormUrlEncoded
    @POST("/login")
    Call<Void> login(@Field("username") String username,
                     @Field("password") String password,
                     @Field("_csrf") String csrf);
}
