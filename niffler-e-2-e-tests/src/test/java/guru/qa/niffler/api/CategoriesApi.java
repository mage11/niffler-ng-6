package guru.qa.niffler.api;

import guru.qa.niffler.model.CategoryJson;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Query;

import java.util.List;

public interface CategoriesApi {

    @POST("internal/categories/add")
    Call<CategoryJson> addCategory(@Body CategoryJson category);

    @PATCH("internal/categories/update")
    Call<CategoryJson> updateCategory(@Body CategoryJson category);

    @GET("internal/categories/all")
    Call<List<CategoryJson>> getCategories(@Query("username") String username,
                                           @Query("excludeArchived") boolean excludeArchived);
}
