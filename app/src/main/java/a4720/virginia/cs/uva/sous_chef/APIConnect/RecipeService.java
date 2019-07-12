package a4720.virginia.cs.uva.sous_chef.APIConnect;

import a4720.virginia.cs.uva.sous_chef.Recipe;
import a4720.virginia.cs.uva.sous_chef.Objects.SearchResults;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RecipeService {
    @GET("https://api.yummly.com/v1/api/recipe/{RECIPE_ID}")
    Call<Recipe> getRecipe(
            @Path("RECIPE_ID") String recipeID,
            @Query("_app_id") String id,
            @Query("_app_key") String key);

    @GET("recipes?_app_id={APP_ID}&_app_key={APP_KEY}&q={query}")
    Call<SearchResults> searchQuery(@Path("APP_ID") String id, @Path("APP_KEY") String key, @Path("query") String query);

    @GET("https://api.yummly.com/v1/api/recipes")
    Call<SearchResults> searchQueryWithPagination(
            @Query("_app_id") String id,
            @Query("_app_key") String key,
            @Query("q") String query,
            @Query("maxResult") String max,
            @Query("start") String start);
}
