package com.dist.dist_android.POJOS.EventPackage;

import com.dist.dist_android.POJOS.User;

/**
 * Created by lbirk on 20-04-2017.
 */

public class Invitation {
    private Integer     invitationID;
    private User        invitedUser;
    private Integer     associatedEventID;

    public Integer getInvitationID() {
        return invitationID;
    }

    public void setInvitationID(Integer invitationID) {
        this.invitationID = invitationID;
    }

    public User getInvitedUser() {
        return invitedUser;
    }

    public void setInvitedUser(User invitedUser) {
        this.invitedUser = invitedUser;
    }

    public Integer getAssociatedEventID() {
        return associatedEventID;
    }

    public void setAssociatedEventID(Integer associatedEventID) {
        this.associatedEventID = associatedEventID;
    }
}
