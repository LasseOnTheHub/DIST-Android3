package com.dist.dist_android.POJOS.EventPackage;

import com.dist.dist_android.POJOS.User;

/**
 * Created by lbirk on 20-04-2017.
 */

public class Invitation {
    private Integer id;
    private User user;
    private Integer event;
    private boolean accepted;

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getEvent() {
        return event;
    }

    public void setEvent(Integer event) {
        this.event = event;
    }
}
