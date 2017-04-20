package com.dist.dist_android.POJOS.EventPackage;

/**
 * Created by lbirk on 20-04-2017.
 */

public class Details {
    private     String  title;
    private     String  description;
    private     String  address;
    private     String  imageURL;
    private     Long    start;
    private     Long    end;
    private     boolean isPublic;

    public Details() {
    }

    public Details(String title,
                   String description,
                   String address,
                   String imageURL,
                   Long start,
                   Long end,
                   boolean isPublic) {
        this.title = title;
        this.description = description;
        this.address = address;
        this.imageURL = imageURL;
        this.start = start;
        this.end = end;
        this.isPublic = isPublic;


        if (imageURL==""){
            imageURL="http://yoga-india.net/wp-content/uploads/2014/03/No_available_image.gif";
        }

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public Long getEnd() {
        return end;
    }

    public void setEnd(Long end) {
        this.end = end;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }
}
