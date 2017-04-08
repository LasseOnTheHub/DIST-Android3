package com.dist.dist_android.POJOS;

import java.util.Date;

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

    public Event(int id, String name, String description, String start, String end, boolean isPublic, String address) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.start = start;
        this.end = end;
        this.isPublic = isPublic;
        this.address = address;
    }
}
