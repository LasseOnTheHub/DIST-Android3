package com.dist.dist_android.POJOS;

import com.dist.dist_android.R;

import java.util.Date;
import java.util.Random;

/**
 * POJO to bind REST-feed to objects
 *
 * Created by lbirk on 08-04-2017.
 */

public class Event {
    private     int     id;
    private     String  name;
    private     String  description;
    private     String    start;
    private     String    end;
    private     boolean isPublic;
    private     String  address;
    private     int     thumbnail;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public String getAddress() {
        return address;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Event(int id, String name, String description, String start, String end, boolean isPublic, String address) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.start = start;
        this.end = end;
        this.isPublic = isPublic;
        this.address = address;

        Random rand = new Random();

        int[] covers = new int[]{
                R.drawable.event1,
                R.drawable.event2,
                R.drawable.event3,
                R.drawable.event4,
                R.drawable.event5,
                R.drawable.event6,
                R.drawable.event7,
                R.drawable.event8,};

        this.thumbnail = covers[rand.nextInt(7)+1];

    }
}
