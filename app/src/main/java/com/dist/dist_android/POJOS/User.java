package com.dist.dist_android.POJOS;

/**
 * Created by lbirk on 14-04-2017.
 */

public class User {
    private Integer ID;
    private String Name;

    public User(Integer ID, String name) {
        this.ID = ID;
        Name = name;
    }

    public User() {
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
