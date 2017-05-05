package com.dist.dist_android.Logic;

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

import com.dist.dist_android.Logic.CustomEventListeners.RecyclerItemsClickedListener;
import com.dist.dist_android.POJOS.EventPackage.Event;
import com.dist.dist_android.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by lbirk on 09-04-2017.
 */

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.MyViewHolder> {
    private final Context mContext;
    private List<Event> eventList;



    private static EventsAdapter instance = null;
    private RecyclerItemsClickedListener recyclerItemsClickedListener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public final TextView title;
        public final TextView description;
        public final ImageView thumbnail;
        public final int          eventId;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            description = (TextView) view.findViewById(R.id.count);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            eventId = 0;
        }
    }


    private EventsAdapter(Context mContext) {
        this.mContext = mContext;
    }

    //Constructor for first time declaration with Context argument
    public static synchronized EventsAdapter getInstance(Context mContext){
        if(instance == null)
            instance = new EventsAdapter(mContext);
        return instance;
    }

    //Constructor withouth arguments to avoid needing to pass Context every time
    public static synchronized EventsAdapter getInstance(){
        if(instance == null){
            throw new IllegalStateException(EventProvider.class.getSimpleName() +
                    " is not initialized, call getInstance(...) first");
        }
        return instance;
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



        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = (int) view.getTag();
                recyclerItemsClickedListener.onImageClick(id);
            }
        });
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

    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
    }

    public void setRecyclerItemsClickedListener(RecyclerItemsClickedListener recyclerItemsClickedListener) {
        this.recyclerItemsClickedListener = recyclerItemsClickedListener;
    }
}
