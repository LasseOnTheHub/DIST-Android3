package com.dist.dist_android.Logic;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dist.dist_android.Logic.CustomEventListeners.EventCreatedListener;
import com.dist.dist_android.Logic.CustomEventListeners.EventRecievedListener;
import com.dist.dist_android.Logic.CustomEventListeners.EventUpdatedListener;
import com.dist.dist_android.Logic.CustomEventListeners.InvitationAcceptListener;
import com.dist.dist_android.Logic.CustomEventListeners.InvitationSentListener;
import com.dist.dist_android.Logic.CustomEventListeners.LoginListener;
import com.dist.dist_android.Logic.CustomEventListeners.SingleEventRecievedListener;
import com.dist.dist_android.POJOS.EventPackage.Details;
import com.dist.dist_android.POJOS.EventPackage.Event;
import com.dist.dist_android.POJOS.EventPackage.Invitation;
import com.dist.dist_android.POJOS.Organizer;
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
    private final String baseUrl = "http://ubuntu4.javabog.dk:3028/rest/api/";

    //Volley Request queue
    private final RequestQueue requestQueue;

    private ArrayList<Event> events;
    private Event event;
    ArrayList<User> user;
    Event eventHolder;

    private final Authorizer authorizer;

    private EventProvider(Context context) {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        authorizer = new Authorizer(context);
    }

    //Constructor for first time declaration with Context argument
    public static synchronized EventProvider getInstance(Context context) {
        if (instance == null)
            instance = new EventProvider(context);
        return instance;
    }

    //Constructor withouth arguments to avoid needing to pass Context every time
    public static synchronized EventProvider getInstance() {
        if (instance == null) {
            throw new IllegalStateException(EventProvider.class.getSimpleName() +
                    " is not initialized, call getInstance(...) first");
        }
        return instance;
    }

    public void catchEvent(int eventID, final SingleEventRecievedListener listener) {
        event = new Event();

        String url = baseUrl + "events/" + eventID;
        JsonObjectRequest getRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        event = Event.parseJSON(response.toString());
                        listener.getResult(event);
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

    public void updateEvent(Details details, int eventId, final EventUpdatedListener listener) throws JSONException {
        String url = baseUrl + "events/" + eventId;

        final JSONObject jsonBody = new JSONObject(details.parseJSON());

        Log.d("JSON", url);
        Log.d("JSON", jsonBody.toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listener.getResult(true);
                        Log.d("JSON", "error => " + response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                listener.getResult(false);
                Log.d("JSON", "error => " + error.toString());
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
    }

    //Creates an event. If the event is created it will return the created event
    //wich can be accessed through the listener.
    public void createEvent(String name,
                            String description,
                            long start,
                            long end,
                            boolean isPublic,
                            String address,
                            final EventCreatedListener<Event> listener) throws JSONException {

        String url = baseUrl + "events";
        final JSONObject jsonBody = new JSONObject("{" +
                "title: \"" + name + "\"," +
                "description: \"" + description + "\"," +
                "address: \"" + address + "\"," +
                "imageURL: \"" + "www.etbillede.dk" + "\"," +
                "start: " + start + "," +
                "end: " + end + "," +
                "isPublic: " + isPublic + "" +
                "}");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            Log.d(TAG, response.toString());
                            try {
                                listener.getResult(response.getInt("id"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

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
    }

    //Gets all events, and notifies a custom eventlistener (eventRevieverListener.java)
    public void catchEvents(final EventRecievedListener listener) {
        events = new ArrayList<>();
        String url = baseUrl + "events";
        JsonObjectRequest getRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonEvents = response.getJSONArray("results");

                            for (int i = 0; i < jsonEvents.length(); i++) {
                                JSONObject obj = jsonEvents.getJSONObject(i);
                                Event event = new Event();

                                event.setId(obj.getInt("id"));

                                JSONObject jsonDetails = obj.getJSONObject("details");
                                for (int j = 0; j < jsonDetails.length(); j++) {
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
                                for (int k = 0; k < jsonInvitations.length(); k++) {
                                    Invitation invitation = new Invitation();
                                    invitation.setId(jsonInvitations.getJSONObject(k).getInt("id"));
                                    invitation.setEvent(jsonInvitations.getJSONObject(k).getInt("event"));


                                    User user = new User();
                                    JSONObject jsonUser = jsonInvitations.getJSONObject(k).getJSONObject("user");
                                    user.setID(jsonUser.getInt("id"));
                                    user.setUsername(jsonUser.getString("username"));
                                    invitation.setUser(user);
                                    invitationArrayList.add(invitation);
                                }
                                event.setInvitations(invitationArrayList);

                                JSONArray jsonOrganizers = obj.getJSONArray("organizers");
                                ArrayList<Organizer> organizers = new ArrayList<>();

                                for (int l = 0; l < jsonOrganizers.length(); l++) {
                                    Organizer organizer = new Organizer();

                                    organizer.setId(jsonOrganizers.getJSONObject(l).getInt("id"));
                                    JSONObject organizerUser = jsonOrganizers.getJSONObject(l).getJSONObject("user");

                                    organizer.setUser(new User(
                                            organizerUser.getInt("id"),
                                            organizerUser.getString("username")));
                                    organizer.setEventID(jsonOrganizers.getJSONObject(l).getInt("event_id"));

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

    public void acceptInvite(int eventID, int invitationID, final InvitationAcceptListener listener) throws JSONException {
        String url = baseUrl + "events/" + eventID + "/invitations/" + invitationID;

        final JSONObject jsonBody = new JSONObject(
                "{\"accepted\": true}");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            Log.d(TAG, response.toString());
                            try {
                                listener.getResult(response.getInt("id"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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
    }

    public void sendInvite(int eventID, int invitedUser, final InvitationSentListener listener) throws JSONException {
        String url = baseUrl + "events/" + eventID + "/invitations";
        final JSONObject jsonBody = new JSONObject(
                "{" +
                        "user_id:" + invitedUser + "" +
                        "}");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            Log.d(TAG, response.toString());
                            try {
                                listener.getResult(response.getInt("id"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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
    }


    public void Login(String userid, String password, final LoginListener listener) throws JSONException {
        String url = baseUrl + "authentication";
        final JSONObject jsonBody = new JSONObject("{" +
                "username: " + userid.toLowerCase().trim() + "," +
                "password: " + password.trim() +
                "}");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            try {
                                authorizer.setToken(response.getString("token"));
                                JSONObject user = response.getJSONObject("user");
                                authorizer.setId(user.getInt("id"));
                                listener.result(true);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR", "error => " + error.toString());
                listener.result(false);
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
    }
}