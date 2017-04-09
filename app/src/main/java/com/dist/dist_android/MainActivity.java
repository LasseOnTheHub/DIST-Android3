package com.dist.dist_android;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private ImageView profilePicture;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        profilePicture = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.profileImageView);
        Picasso.with(this)
                .load("http://www.beaconsinn.com/images/unique-information.com/wp-content/uploads/2016/04/funny-pictures-of-monkeys-smiling-490x419.png")
                .transform(new CircleTransform())
                .resize(800, 800)
                .centerCrop()
                .into(profilePicture);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close);

        mDrawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();


        if (savedInstanceState == null) {
            Fragment fragment = new StartFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_frame, fragment)  // tom container i layout
                    .commit();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        item.setCheckable(true);
        item.setChecked(true);

        switch (item.getItemId())
        {
            case R.id.item_startFragment:
                //Sætter et nyt fragment
                Fragment startFragment = new StartFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, startFragment)
                        .commit();
                break;
            case R.id.item_CreateEventFragment:
                Fragment createEventFragment = new CreateEventFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, createEventFragment)
                        .commit();
                break;
        }
        hideDrawer();
        return true;
    }

    private void showDrawer(){mDrawerLayout.openDrawer(GravityCompat.START);}
    private void hideDrawer(){mDrawerLayout.closeDrawer(GravityCompat.START);}

    public void onBackPressed()
    {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START))
            mDrawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }
}
