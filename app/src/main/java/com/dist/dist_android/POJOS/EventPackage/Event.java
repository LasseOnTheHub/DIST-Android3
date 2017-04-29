package com.dist.dist_android.POJOS.EventPackage;

import com.dist.dist_android.POJOS.Organizer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

/**
 * POJO to bind REST-feed to objects
 *
 * Created by lbirk on 08-04-2017.
 */

public class Event {
    private     int                     id;
    private     Details                 details;
    private     ArrayList<Invitation>   invitations;
    private     ArrayList<Organizer>    organizers;
    private     Integer                 thumbnail;

    public Event() {
    }

    public Integer getThumbnail() {
        return thumbnail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Details getDetails() {
        return details;
    }

    public void setDetails(Details details) {
        this.details = details;
    }

    public ArrayList<Invitation> getInvitations() {
        return invitations;
    }

    public void setInvitations(ArrayList<Invitation> invitations) {
        this.invitations = invitations;
    }

    public ArrayList<Organizer> getOrganizers() {
        return organizers;
    }

    public void setOrganizers(ArrayList<Organizer> organizers) {
        this.organizers = organizers;
    }

    public static  Event parseJSON(String response){
        Gson gson = new GsonBuilder().create();
        Event event = gson.fromJson(response,Event.class);
        return event;
    }

    public String parseJSON(){
        Gson gson = new GsonBuilder().create();
        String jsonString = gson.toJson(this, Event.class);
        return jsonString;
    }

}
