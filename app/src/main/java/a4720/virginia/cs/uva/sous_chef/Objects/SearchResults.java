package a4720.virginia.cs.uva.sous_chef.Objects;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;
import a4720.virginia.cs.uva.sous_chef.Objects.InnerClasses.*;

@Parcel
public class SearchResults {
    public ArrayList<Match> matches;
    public int totalMatchCount;
    public Attribution attribution;
    public ArrayList<String> smallImageUrls;

    public ArrayList<Match> getMatches() {
        return matches;
    }

    public void setMatches(ArrayList<Match> matches) {
        this.matches = matches;
    }

    public int getTotalMatchCount() {
        return totalMatchCount;
    }

    public void setTotalMatchCount(int totalMatchCount) {
        this.totalMatchCount = totalMatchCount;
    }

    public Attribution getAttribution() {
        return attribution;
    }

    public ArrayList<String> getSmallImageUrls() {
        return smallImageUrls;
    }

    public void setSmallImageUrls(ArrayList<String> smallImageUrls) {
        this.smallImageUrls = smallImageUrls;
    }
}
