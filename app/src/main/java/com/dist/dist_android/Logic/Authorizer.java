package com.dist.dist_android.Logic;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by lbirk on 04-04-2017.
 */

public class Authorizer {

    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;

    public Authorizer(Context context)
    {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        editor = prefs.edit();
    }

    public void setToken(String token)
    {
        editor.putString("token",token);
        editor.commit();
    }
    public void setId(){
        editor.putInt("id", 120);
        editor.commit();
    }

    public String getToken()
    {
        return prefs.getString("token","empty");
    }
    public Integer getId()
    {
        return prefs.getInt("id",0000);
    }
}
