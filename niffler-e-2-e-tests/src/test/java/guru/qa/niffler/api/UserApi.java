package guru.qa.niffler.api;

import guru.qa.niffler.model.UserJson;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

import java.util.List;

public interface UserApi {

    @GET("/internal/users/current")
    Call<UserJson> currentUser(@Query("username") String username);

    @POST("/internal/users/update")
    Call<UserJson> update(@Body UserJson user);

    @GET("internal/users/all")
    Call<List<UserJson>> allUsers(@Query("username") String username, @Query("searchQuery") String searchQuery);

    @GET("internal/friends/all")
    Call<List<UserJson>> allFriends(@Query("username") String username, @Query("searchQuery") String searchQuery);

    @POST("internal/invitations/send")
    Call<UserJson> sendInvitation(@Query("username") String username, @Query("targetUsername") String targetUsername);

    @POST("internal/invitations/accept")
    Call<UserJson> acceptInvitation(@Query("username") String username, @Query("targetUsername") String targetUsername);

    @DELETE("internal/friends/remove")
    Call<UserJson> declineInvitation(@Query("username") String username, @Query("targetUsername") String targetUsername);


}
