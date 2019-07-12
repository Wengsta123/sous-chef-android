package a4720.virginia.cs.uva.sous_chef;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

@Database(entities= {Recipe.class}, version='1')
@TypeConverters({ArrayListTypeConverter.class})
public abstract class AppDatabase extends RoomDatabase{
    public abstract RecipeDao recipeDao();
}
