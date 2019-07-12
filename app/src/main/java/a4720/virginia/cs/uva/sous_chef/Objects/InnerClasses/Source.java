package a4720.virginia.cs.uva.sous_chef.Objects.InnerClasses;

import org.parceler.Parcel;

@Parcel
public class Source {
    public String sourceDisplayName;
    public String sourceSiteUrl;
    public String sourceRecipeUrl;

    public String getSourceDisplayName() {
        return sourceDisplayName;
    }

    public void setSourceDisplayName(String sourceDisplayName) {
        this.sourceDisplayName = sourceDisplayName;
    }

    public String getSourceSiteUrl() {
        return sourceSiteUrl;
    }

    public void setSourceSiteUrl(String sourceSiteUrl) {
        this.sourceSiteUrl = sourceSiteUrl;
    }

    public String getSourceRecipeUrl() {
        return sourceRecipeUrl;
    }

    public void setSourceRecipeUrl(String sourceRecipeUrl) {
        this.sourceRecipeUrl = sourceRecipeUrl;
    }
}
