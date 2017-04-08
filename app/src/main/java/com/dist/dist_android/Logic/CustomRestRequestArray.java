package com.dist.dist_android.Logic;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dist.dist_android.POJOS.Event;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lbirk on 08-04-2017.
 */

public class CustomRestRequestArray {
    Authorizer authorizer;
    String response;

    public ArrayList<Event> getEvents(Context context, String url) throws JSONException {
        final ArrayList<Event> events = new ArrayList<Event>();
        authorizer = new Authorizer(context);
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest getRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonEvents = response.getJSONArray("data");

                            for (int i=0; i< jsonEvents.length(); i++){
                                JSONObject obj = jsonEvents.getJSONObject(i);
                                Event event = new Event(
                                        obj.getInt("id"),
                                        obj.getString("name"),
                                        obj.getString("description"),
                                        obj.getString("start"),
                                        obj.getString("end"),
                                        obj.getBoolean("isPublic"),
                                        obj.getString("address")
                                );
                                events.add(event);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", "error => " + error.toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + authorizer.getToken());
                return params;
            }
        };
        queue.add(getRequest);
        return events;
    }
}
