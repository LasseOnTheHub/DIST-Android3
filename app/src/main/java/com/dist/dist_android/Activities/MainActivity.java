package com.dist.dist_android.Activities;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
    Toolbar toolbar;
    Fragment publicEventFragment = new PublicEventsFragment();
    Fragment myEventsFragment = new MyEventsFragment();
    Fragment invitedEventsFragment = new MyInvitedEventsFragment();

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_public:
                    replaceFragment(publicEventFragment);
                    toolbar.setTitle("Public Event");
                    return true;
                case R.id.action_myEvents:
                    replaceFragment(myEventsFragment);
                    toolbar.setTitle("My Events");
                    return true;
                case R.id.action_invited:
                    replaceFragment(invitedEventsFragment);
                    toolbar.setTitle("Invitations");
                    return true;
            }
            return false;
        }
    };

    private void replaceFragment(Fragment fragment) {
        String backStateName = fragment.getClass().getName();

        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped) { //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.content, fragment);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                args.putInt("EVENTID", eventID);
                createFragment.setArguments(args);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content, createFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    //.addToBackStack(null)
                    .add(R.id.content, publicEventFragment, publicEventFragment.getClass().getName())  // tom container i layout
                    .commit();
        }
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackEntryCount == 0) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Click BACK again to log out", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else {
            super.onBackPressed();
        }
    }

}
