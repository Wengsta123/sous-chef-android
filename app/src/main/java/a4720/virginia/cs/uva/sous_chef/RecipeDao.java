package a4720.virginia.cs.uva.sous_chef;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface RecipeDao {
    @Query("SELECT * FROM recipe")
    List<Recipe> getAll();

    @Query("SELECT COUNT(*) FROM recipe")
    int countRecipes();

    @Query("SELECT * FROM recipe WHERE uid =:recipeId")
    Recipe getRecipe(int recipeId);

    @Query("UPDATE Recipe SET name = :name, ingredientLines = :ingredientLines, totalTime= :totalTime, numberOfServings = :numberOfServings, notes = :notes WHERE uid = :uid")
    int updateRecipe(int uid, String name, ArrayList<String> ingredientLines, String totalTime, Integer numberOfServings, String notes);

    @Query("UPDATE Recipe SET localImageBytes = :localImageBytes where uid=:uid")
    int updateLocalImage(int uid, byte[] localImageBytes);

    @Insert
    void insertAll(Recipe... recipes);

    @Delete
    void delete(Recipe recipe);
}
