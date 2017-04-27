package com.dist.dist_android.Logic;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dist.dist_android.Fragments.PublicEventsFragment;
import com.dist.dist_android.Logic.CustomEventListeners.RecyclerItemsClickedListener;
import com.dist.dist_android.POJOS.EventPackage.Event;
import com.dist.dist_android.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by lbirk on 09-04-2017.
 */

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.MyViewHolder> {
    private Context mContext;
    private List<Event> eventList;

    private RecyclerItemsClickedListener recyclerItemsClickedListener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, description;
        public ImageView thumbnail, overflow;
        public int          eventId;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            description = (TextView) view.findViewById(R.id.count);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);
            eventId = 0;
        }
    }

    public EventsAdapter(Context mContext, List<Event> eventList) {
        this.mContext = mContext;
        this.eventList = eventList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.title.setText(event.getDetails().getTitle());
        holder.description.setText(event.getDetails().getDescription());
        holder.thumbnail.setTag(event.getId());

        /**
         * Loads the provided thumbnailimage with the image URL
         * If no imageURL is provided, a placeholder image is used.
         */
        if(event.getDetails().getImageURL().endsWith(".jpg")||
                event.getDetails().getImageURL().endsWith(".png")){
            Picasso.with(mContext)
                    .load(event.getDetails().getImageURL())
                    .into(holder.thumbnail);
        }
        else{
            Picasso.with(mContext)
                    .load("http://3.bp.blogspot.com/-xFIp1U2vz8w/UrBl-ZsVzyI/AAAAAAAACUE/qscQploleMg/s400/NoPhotoIcon.jpg")
                    .into(holder.thumbnail);
        }

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow);
            }
        });

/*        holder.thumbnail.setOnClickListener(new RecyclerItemsClickedListener(){
            @Override
            public void onClick(int eventID) {

            }
        } );*/

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = (int) view.getTag();
                Toast.makeText(mContext,
                        "Du klikkede på event med ID: " + id,
                        Toast.LENGTH_LONG).show();
            }
        });
    }


    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_event, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    Toast.makeText(mContext, "Add to favourite", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_play_next:
                    Toast.makeText(mContext, "Play next", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }
}
