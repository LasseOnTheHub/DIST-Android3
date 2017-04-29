package com.dist.dist_android.POJOS;

/**
 * Created by lbirk on 23-04-2017.
 */

public class Organizer {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private User user;
    private int eventID;

    public Organizer(User user, int eventID) {
        this.user = user;
        this.eventID = eventID;
    }

    public Organizer() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }
}
