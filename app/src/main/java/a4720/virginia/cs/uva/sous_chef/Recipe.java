package a4720.virginia.cs.uva.sous_chef;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;

import org.parceler.Parcel;

import java.io.File;
import java.util.ArrayList;
import a4720.virginia.cs.uva.sous_chef.Objects.InnerClasses.*;

@Entity
@Parcel
public class Recipe {

    @PrimaryKey(autoGenerate = true)
    public int uid=0;

    @ColumnInfo(name="name")
    public String name;

//    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
//    public byte[] image;
    @TypeConverters(ArrayListTypeConverter.class)
    @ColumnInfo(name="ingredientLines")
    public ArrayList<String> ingredientLines;

    @ColumnInfo(name="id")
    public String id;

    @ColumnInfo(name="totalTime")
    public String totalTime;

    @ColumnInfo(name="notes")
    public String notes;

    @ColumnInfo(name="numberOfServings")
    public int numberOfServings;

    @ColumnInfo(name="rating")
    public int rating;
    public String cardImageUrl;
    @Embedded
    public ArrayList<Images> images;

    @ColumnInfo(name="saved")
    public boolean saved;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    public byte[] localImageBytes;


//    public Images images;
    @Embedded
    public Source source;
    @Embedded
    public Attribution attribution;

    // outer class methods
    @Ignore
    public Recipe(){}

    public Recipe(String name) {
        this.name = name;
    }


    @Ignore
    public Recipe(String name, String id, int rating, String cardImageUrl) {
        this.name = name;
        this.id = id;
        this.rating = rating;
        this.cardImageUrl = cardImageUrl;
    }
    @Ignore
    public Recipe(String name, ArrayList<String> ingredientLines, String totalTime, int numberOfServings, String notes, Boolean saved) {
        this.name = name;
        this.ingredientLines = ingredientLines;
        this.totalTime = totalTime;
        this.numberOfServings = numberOfServings;
        this.notes = notes;
        this.saved = saved;
    }

    public static ArrayList<Recipe> createInitialRecipeList() {
        ArrayList<Recipe> recipes = new ArrayList<>();
        return recipes;
    }
    public static ArrayList<Recipe> loadRecipeList() {
        AppDatabase db= MainActivity.getDb();
        ArrayList<Recipe> recipes = new ArrayList<Recipe>(db.recipeDao().getAll());
        if(recipes.size()==0) {
            recipes=createInitialRecipeList();
        }
        Log.d("Yeet", "loadRecipeList: loaded");
        return recipes;
    }

    @Delete
    static void delete(Recipe recipe) {

    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getIngredientLines() {
        return ingredientLines;
    }

    public void setIngredientLines(ArrayList<String> ingredientLines) {
        this.ingredientLines = ingredientLines;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public int getNumberOfServings() {
        return numberOfServings;
    }

    public void setNumberOfServings(int numberOfServings) {
        this.numberOfServings = numberOfServings;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getCardImageUrl() {
        return cardImageUrl;
    }

    public void setCardImageUrl(String cardImageUrl) {
        this.cardImageUrl = cardImageUrl;
    }

    public ArrayList<Images> getImages() {
        return images;
    }

    public void setImages(ArrayList<Images> images) {
        this.images = images;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public Attribution getAttribution() {
        return attribution;
    }

    public void setAttribution(Attribution attribution) {
        this.attribution = attribution;
    }

    public String getNotes(){return notes;}

    public byte[] getLocalImageBytes() {
        return localImageBytes;
    }

    public void setLocalImageBytes(byte[] localImageBytes) {
        this.localImageBytes = localImageBytes;
    }

    public void setNotes(String notes){this.notes=notes;}

    public Boolean getSaved(){return saved;}

    public void setSaved(Boolean saved){this.saved = saved;}
    //    public Bitmap getBitmap() {
//        return ImageConverter.getImage(image);
//    }
//
//    public void setBitmap(Bitmap bitmap) {
//        this.image = ImageConverter.getBytes(bitmap);
//    }

    public String shareToEmail() {
        String displayNotes = (notes != null && !notes.isEmpty()) ? notes : "none";
        String sourceRecipeUrl = (source.getSourceRecipeUrl() != null && !source.getSourceRecipeUrl().isEmpty()) ? "\nCheck out " + source.getSourceRecipeUrl() + "for more details!" : "";
        return name + "\n" +
                "Ingredients: " + "\n" +
                ingredientLines + "\n" + "\n" +
                "Notes: " + "\n" +
                displayNotes + "\n" +
                sourceRecipeUrl;
    }
}