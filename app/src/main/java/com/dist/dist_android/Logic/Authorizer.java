package com.dist.dist_android.Logic;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lbirk on 04-04-2017.
 */

public class Authorizer {

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

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

    public String getToken()
    {
        return prefs.getString("token","empty");
    }
}
