package com.dist.dist_android.Activities;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.dist.dist_android.Fragments.CreateEventFragment;
import com.dist.dist_android.Fragments.MyEventsFragment;
import com.dist.dist_android.Fragments.MyInvitedEventsFragment;
import com.dist.dist_android.Logic.CustomEventListeners.RecyclerItemsClickedListener;
import com.dist.dist_android.R;
import com.dist.dist_android.Fragments.PublicEventsFragment;

public class MainActivity extends AppCompatActivity implements RecyclerItemsClickedListener {

    private DrawerLayout mDrawerLayout;
    BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        if (savedInstanceState == null) {
            Fragment fragment = new PublicEventsFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content, fragment,"PUBLIC_FRAGMENTS")  // tom container i layout
                    .commit();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_public:
                    if(getSupportFragmentManager().findFragmentByTag("PUBLIC_FRAGMENT")==null ||
                            !getSupportFragmentManager().findFragmentByTag("PUBLIC_FRAGMENT").isVisible()){
                        Fragment startFragment = new PublicEventsFragment();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content, startFragment)
                                .commit();
                    }
                    return true;
                case R.id.action_myEvents:
                    if(getSupportFragmentManager().findFragmentByTag("MY_EVENTS_FRAGMENT")==null ||
                            !getSupportFragmentManager().findFragmentByTag("MY_EVENTS_FRAGMENT").isVisible()) {
                        Fragment myEventsFragment = new MyEventsFragment();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content, myEventsFragment,"MY_EVENTS_FRAGMENT")
                                .commit();
                    }
                    //mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.action_invited:
                    if(getSupportFragmentManager().findFragmentByTag("INVITED_FRAGMENT")==null ||
                            !getSupportFragmentManager().findFragmentByTag("INVITED_FRAGMENT").isVisible()) {
                        if(navigation.getSelectedItemId()!=item.getItemId()) {
                            Fragment invitedEventsFragment = new MyInvitedEventsFragment();
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.content, invitedEventsFragment,"INVITED_FRAGMENT")
                                    .commit();
                        }
                    }
                    //mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }

    };

    @Override
    public void onClick(int eventID) {
        Fragment createEventsFragment = new CreateEventFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, createEventsFragment,"CREATE_EVENT_FRAGMENT")
                .commit();
    }
}
