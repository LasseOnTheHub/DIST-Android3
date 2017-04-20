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
import com.dist.dist_android.Logic.CustomEventListeners.EventRecievedListener;
import com.dist.dist_android.POJOS.EventPackage.Details;
import com.dist.dist_android.POJOS.EventPackage.Event;
import com.dist.dist_android.POJOS.EventPackage.Invitation;
import com.dist.dist_android.POJOS.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lbirk on 14-04-2017.
 */

public class EventProvider {

    private static final String TAG = "EventProvider";
    private static EventProvider instance = null;
    String baseUrl = "http://ubuntu4.javabog.dk:3028/rest/api/";

    //Volley Request queue
    public RequestQueue requestQueue;

    ArrayList<Event> events;
    ArrayList<User> user;
    Event eventHolder;

    Authorizer authorizer;

    private EventProvider(Context context){
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        authorizer = new Authorizer(context);
    }
    //Constructor for first time declaration with Context argument
    public static synchronized EventProvider getInstance(Context context){
        if(instance == null)
            instance = new EventProvider(context);
        return instance;
    }

    //Constructor withouth arguments to avoid needing to pass Context every time
    public static synchronized EventProvider getInstance(){
        if(instance == null){
            throw new IllegalStateException(EventProvider.class.getSimpleName() +
                    " is not initialized, call getInstance(...) first");
        }
        return instance;
    }

    //Creates an event. If the event is created it will return the created event
    //wich can be accessed through the listener.
    /*public void createEvent(int id,
                            String name,
                            String description,
                            long start,
                            long end,
                            boolean isPublic,
                            String address,
                            final EventCreatedListener<Event> listener) throws JSONException {

        String url = baseUrl + "events";
        final JSONObject jsonBody = new JSONObject("{" +
                "id: "+ 1 +","+
                "name: \""+name +"\","+
                "description: \""+ description +"\","+
                "start: "+start+","+
                "end: "+end+","+
                "isPublic: "+isPublic+","+
                "address: \""+address+"\""+
                "}");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonBody,
                new Response.Listener<JSONObject>() {
                    String status;
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            eventHolder = new Event(
                                    response.getInt("id"),
                                    response.getString("name"),
                                    response.getString("description"),
                                    response.getString("start"),
                                    response.getString("end"),
                                    response.getBoolean("isPublic"),
                                    response.getString("address")
                            );
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (eventHolder!=null){
                            Log.d(TAG,response.toString());
                            listener.getResult(eventHolder);

                        }
                    }
                }, new Response.ErrorListener() {
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
        requestQueue.add(jsonObjectRequest);
    }*/

    //Gets all events, and notifies a custom eventlistener (eventRevieverListener.java)
    public void catchEvents(final EventRecievedListener<ArrayList> listener){
        events = new ArrayList<>();
        String url = baseUrl+"events";
        JsonObjectRequest getRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonEvents = response.getJSONArray("results");

                            for (int i=0; i< jsonEvents.length(); i++){
                                JSONObject obj = jsonEvents.getJSONObject(i);
                                Event event = new Event();

                                event.setId(obj.getInt("id"));

                                JSONObject jsonDetails = obj.getJSONObject("details");
                                for (int j=0; j<jsonDetails.length();j++){
                                    Details details = new Details();
                                    details.setTitle(jsonDetails.getString("title"));
                                    details.setDescription(jsonDetails.getString("description"));
                                    details.setAddress(jsonDetails.getString("address"));
                                    details.setImageURL(jsonDetails.getString("imageURL"));
                                    details.setStart(jsonDetails.getLong("start"));
                                    details.setEnd(jsonDetails.getLong("end"));
                                    details.setPublic(jsonDetails.getBoolean("isPublic"));
                                    event.setDetails(details);
                                }
                                JSONArray jsonInvitations = obj.getJSONArray("invitations");
                                ArrayList<Invitation> invitationArrayList = new ArrayList<>();
                                for(int k=0; k<jsonInvitations.length();k++){
                                    Invitation invitation = new Invitation();
                                    invitation.setInvitationID(jsonInvitations.getJSONObject(k).getInt("id"));
                                    invitation.setAssociatedEventID(jsonInvitations.getJSONObject(k).getInt("event"));
                                    User user = new User();
                                    JSONObject jsonUser = jsonInvitations.getJSONObject(k).getJSONObject("user");
                                    user.setID(jsonUser.getInt("id"));
                                    user.setName(jsonUser.getString("name"));
                                    invitation.setInvitedUser(user);
                                    invitationArrayList.add(invitation);
                                }
                                event.setInvitations(invitationArrayList);

                                JSONArray jsonOrganizers = obj.getJSONArray("organizers");
                                ArrayList<User> organizers = new ArrayList<>();
                                for(int l=0;l<jsonOrganizers.length();l++){
                                    User organizer = new User();
                                    organizer.setID(jsonOrganizers.getJSONObject(l).getInt("id"));
                                    organizer.setName(jsonOrganizers.getJSONObject(l).getString("name"));
                                    organizers.add(organizer);
                                }
                                event.setOrganizers(organizers);
                                events.add(event);
                            }
                            listener.getResult(events);
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
        requestQueue.add(getRequest);
    }


}
