package com.dist.dist_android.Activities;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.dist.dist_android.Fragments.CreateEventFragment;
import com.dist.dist_android.Fragments.MyEventsFragment;
import com.dist.dist_android.Fragments.MyInvitedEventsFragment;
import com.dist.dist_android.Logic.CustomEventListeners.RecyclerItemsClickedListener;
import com.dist.dist_android.Logic.EventProvider;
import com.dist.dist_android.Logic.EventsAdapter;
import com.dist.dist_android.R;
import com.dist.dist_android.Fragments.PublicEventsFragment;



public class MainActivity extends AppCompatActivity {

    private BottomNavigationView navigation;
    private EventsAdapter eventsAdapter;
    private EventProvider eventProvider;
    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_public:
                    if(getSupportFragmentManager().findFragmentByTag("PUBLIC_FRAGMENT")==null ||
                            !getSupportFragmentManager().findFragmentByTag("PUBLIC_FRAGMENT").isVisible()){
                        Fragment startFragment = new PublicEventsFragment();
                        getSupportFragmentManager().beginTransaction()
                                .addToBackStack("tag")
                                .replace(R.id.content, startFragment)
                                .commit();
                    }
                    return true;
                case R.id.action_myEvents:
                    if(getSupportFragmentManager().findFragmentByTag("MY_EVENTS_FRAGMENT")==null ||
                            !getSupportFragmentManager().findFragmentByTag("MY_EVENTS_FRAGMENT").isVisible()) {
                        Fragment myEventsFragment = new MyEventsFragment();
                        getSupportFragmentManager().beginTransaction()
                                .addToBackStack("tag")
                                .replace(R.id.content, myEventsFragment,"MY_EVENTS_FRAGMENT")
                                .commit();
                    }
                    return true;
                case R.id.action_invited:
                    if(getSupportFragmentManager().findFragmentByTag("INVITED_FRAGMENT")==null ||
                            !getSupportFragmentManager().findFragmentByTag("INVITED_FRAGMENT").isVisible()) {
                        if(navigation.getSelectedItemId()!=item.getItemId()) {
                            Fragment invitedEventsFragment = new MyInvitedEventsFragment();
                            getSupportFragmentManager().beginTransaction()
                                    .addToBackStack("tag")
                                    .replace(R.id.content, invitedEventsFragment,"INVITED_FRAGMENT")
                                    .commit();
                        }
                    }
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        eventProvider = EventProvider.getInstance(this);
        eventsAdapter = EventsAdapter.getInstance(this);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        eventsAdapter.setRecyclerItemsClickedListener(new RecyclerItemsClickedListener() {
            @Override
            public void onImageClick(int eventID) {
                Toast.makeText(getApplicationContext(),
                        "Du har trykket på: " + eventID,
                        Toast.LENGTH_LONG).show();

                Fragment createFragment = new CreateEventFragment();
                Bundle args = new Bundle();
                args.putInt("EVENTID",eventID);
                createFragment.setArguments(args);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content, createFragment)
                        .addToBackStack("tag")
                        .commit();
            }
        });

        if (savedInstanceState == null) {
            Fragment fragment = new PublicEventsFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack("tag")
                    .add(R.id.content, fragment,"PUBLIC_FRAGMENTS")  // tom container i layout
                    .commit();
        }
    }

}
