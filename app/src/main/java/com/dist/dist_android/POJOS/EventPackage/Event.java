package com.dist.dist_android.POJOS.EventPackage;

import com.dist.dist_android.POJOS.Organizer;
import com.dist.dist_android.POJOS.User;
import com.dist.dist_android.R;

import java.util.ArrayList;
import java.util.Random;

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
}
