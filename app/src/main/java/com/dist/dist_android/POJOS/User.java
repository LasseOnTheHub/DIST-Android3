package com.dist.dist_android.POJOS;

/**
 * Created by lbirk on 14-04-2017.
 */

public class User {
    private Integer id;
    private String username;
    private String firstname;
    private String lastname;
    private String email;

    public User(Integer id, String username) {
        this.id = id;
        this.username = username;
    }

    public User() {
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getID() {
        return id;
    }

    public void setID(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
