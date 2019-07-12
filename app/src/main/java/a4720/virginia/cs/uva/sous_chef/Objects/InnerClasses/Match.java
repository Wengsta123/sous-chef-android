package a4720.virginia.cs.uva.sous_chef.Objects.InnerClasses;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class Match {
    public String id;
    public String recipeName;
    public int rating;
    public List<String> smallImageUrls;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public List<String> getSmallImageUrls() {
        return smallImageUrls;
    }

    public void setSmallImageUrls(List<String> smallImageUrls) {
        this.smallImageUrls = smallImageUrls;
    }
}
