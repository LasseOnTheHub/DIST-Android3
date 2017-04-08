package com.dist.dist_android;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dist.dist_android.Logic.CustomRestRequest;
import com.dist.dist_android.Logic.CustomRestRequestArray;

import org.json.JSONException;


/**
 * A simple {@link Fragment} subclass.
 */
public class StartFragment extends Fragment {

    private Button testButton;
    CustomRestRequest customRestRequest;
    CustomRestRequestArray customRestRequestArray;

    public StartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_start, container, false);

        customRestRequest = new CustomRestRequest();
        customRestRequestArray = new CustomRestRequestArray();

        testButton = (Button) rootView.findViewById(R.id.testButton);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    customRestRequestArray.getEvents(rootView.getContext(),"http://ubuntu4.javabog.dk:3028/rest/api/events");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });



        return rootView;
    }

}
