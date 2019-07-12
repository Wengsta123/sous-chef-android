package a4720.virginia.cs.uva.sous_chef.Objects.InnerClasses;

import org.parceler.Parcel;

@Parcel
public class Attribution {
    public String url;
    public String text;
    public String logo;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
