package com.dist.dist_android;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.dist.dist_android.Logic.Authorizer;
import com.dist.dist_android.Logic.CustomRestRequest;
import com.dist.dist_android.Logic.CustomRestRequestArray;
import com.dist.dist_android.POJOS.Event;
import com.dist.dist_android.TempForRecycler.EventsAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class StartFragment extends Fragment {

    private RecyclerView recyclerView;
    private EventsAdapter adapter;

    private Button testButton;
    CustomRestRequest customRestRequest;
    CustomRestRequestArray customRestRequestArray;
    Authorizer authorizer;
    ArrayList<Event> events;

    public StartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_start, container, false);

        customRestRequest = new CustomRestRequest();
        customRestRequestArray = new CustomRestRequestArray();
        authorizer = new Authorizer(rootView.getContext());
        events = new ArrayList<>();
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(rootView.getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //Gets events and sets the adapter
        RequestQueue queue = Volley.newRequestQueue(rootView.getContext());
        JsonObjectRequest getRequest = new JsonObjectRequest
                (Request.Method.GET, "http://ubuntu4.javabog.dk:3028/rest/api/events", null, new Response.Listener<JSONObject>() {
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
                                adapter = new EventsAdapter(rootView.getContext(), events);
                                recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
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




/*        try {
            Glide.with(this).load(R.drawable.cover).into((ImageView) rootView.findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }*/


        return rootView;
    }
    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }
    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

}
