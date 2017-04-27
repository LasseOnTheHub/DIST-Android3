package com.dist.dist_android.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import java.util.ArrayList;
import java.util.List;

import com.dist.dist_android.Logic.Authorizer;
import com.dist.dist_android.Logic.CustomEventListeners.EventRecievedListener;
import com.dist.dist_android.Logic.EventProvider;
import com.dist.dist_android.POJOS.EventPackage.Event;
import com.dist.dist_android.POJOS.EventPackage.Invitation;
import com.dist.dist_android.R;
import com.dist.dist_android.Logic.EventsAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyInvitedEventsFragment extends Fragment {

    private RecyclerView recyclerView;
    private EventsAdapter adapter;

    private Button testButton;
    Authorizer authorizer;
    ArrayList<Event> events;

    public MyInvitedEventsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_my_invited_events, container, false);

        authorizer = new Authorizer(rootView.getContext());
        events = new ArrayList<>();
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(rootView.getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //Gets events from EventProvider and subscribes to the custom event listener (EventRecievedListener)
        EventProvider.getInstance().catchEvents(new EventRecievedListener() {
            @Override
            public void getResult(List<Event> events) {

                ArrayList<Event> subsetEvents = new ArrayList<>();
                for (Event e: events)
                {
                    for(Invitation i: e.getInvitations())
                    {
                            if (i.getUser().getID() ==authorizer.getId())
                            {
                                subsetEvents.add(e);
                                break;
                            }
                    }
                }
                //adapter = new EventsAdapter(rootView.getContext(), subsetEvents);
                adapter = EventsAdapter.getInstance();
                adapter.setEventList(subsetEvents);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
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
