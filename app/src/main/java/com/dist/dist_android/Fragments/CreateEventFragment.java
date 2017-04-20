package com.dist.dist_android.Fragments;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.dist.dist_android.Logic.Authorizer;
import com.dist.dist_android.Logic.CustomEventListeners.EventCreatedListener;
import com.dist.dist_android.Logic.EventProvider;
import com.dist.dist_android.POJOS.EventPackage.Event;
import com.dist.dist_android.R;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateEventFragment extends Fragment {

    private EditText dateStartEditText;
    private EditText dateEndEditText;
    private EditText timeStartEditText;
    private EditText timeEndEditText;
    private EditText addressEditText;
    private EditText nameEditText;
    private EditText descriptionEditText;
    private CheckBox publicEventCheckBox;
    private CheckBox privateEventCheckBox;
    private Button createEventButton;
    private TextInputLayout nameInputLayout;
    private Authorizer authorizer;
    private Calendar myCalendar;
    DatePickerDialog.OnDateSetListener startDate;
    DatePickerDialog.OnDateSetListener endDate;
    TimePickerDialog.OnTimeSetListener startTime;
    TimePickerDialog.OnTimeSetListener endTime;
    String myFormat = "dd/MM/yy";
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.GERMAN);

    public CreateEventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_create_event, container, false);
        authorizer = new Authorizer(rootView.getContext());
        nameEditText = (EditText) rootView.findViewById(R.id.nameEditText);
        descriptionEditText = (EditText) rootView.findViewById(R.id.descriptionEditText);
        addressEditText = (EditText) rootView.findViewById(R.id.addressEditText);
        publicEventCheckBox = (CheckBox) rootView.findViewById(R.id.publicCheckBox);
        privateEventCheckBox = (CheckBox) rootView.findViewById(R.id.privateCheckBox);
        createEventButton = (Button) rootView.findViewById(R.id.createEventButton);
        nameInputLayout = (TextInputLayout) rootView.findViewById(R.id.nameTextInputLayout);

        nameInputLayout.setError("Eksempel på en fejlbesked");

/*        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    createEvent(rootView.getContext());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });*/


        /**
         * Initializing date and time components, including handling events for date and time pickers
         */
        dateStartEditText = (EditText) rootView.findViewById(R.id.dateStartEditText);
        dateEndEditText = (EditText) rootView.findViewById(R.id.dateEndEditText);
        timeStartEditText = (EditText) rootView.findViewById(R.id.timeStartEditText);
        timeEndEditText = (EditText) rootView.findViewById(R.id.timeEndEditText);
        myCalendar = Calendar.getInstance();
        timeStartEditText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                new TimePickerDialog(rootView.getContext(),startTime,12,00,true).show();
            }
        });
        startTime = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                timeStartEditText.setText(i+":"+i1);
            }
        };

        timeEndEditText.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                new TimePickerDialog(rootView.getContext(),endTime,12,00,true).show();
            }
        });
        endTime = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                timeEndEditText.setText(i+":"+i1);
            }
        };

        dateStartEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(rootView.getContext(), startDate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        startDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                dateStartEditText.setText(sdf.format(myCalendar.getTime()));
            }
        };
        dateEndEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(rootView.getContext(), endDate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        endDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                dateEndEditText.setText(sdf.format(myCalendar.getTime()));
            }
        };


        return rootView;
    }

/*    private void createEvent(final Context context) throws JSONException {

        EventProvider.getInstance().createEvent(1,
                nameEditText.getText().toString(),
                descriptionEditText.getText().toString(),
                1491789098,
                1491789099,
                true,
                addressEditText.getText().toString(),
                new EventCreatedListener<Event>() {
            @Override
            public void getResult(Event event) {
                Toast.makeText(context,
                        "Sucess: " + event.getName()+" got created",
                        Toast.LENGTH_LONG).show();
            }
        });*/

    }
 /*       RequestQueue queue = Volley.newRequestQueue(context);
        String url = "http://ubuntu4.javabog.dk:3028/rest/api/events";
        final JSONObject jsonBody = new JSONObject("{" +
                "id: "+ 1 +","+
                "name: \""+nameEditText.getText() +"\","+
                "description: \""+ descriptionEditText.getText() +"\","+
                "start: "+1491789098+","+
                "end: "+1491799098+","+
                "isPublic: "+true+","+
                "address: \""+addressEditText.getText()+"\""+
                "}");

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonBody,
                new Response.Listener<JSONObject>() {
                    String status;
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response.toString() !=""){
                                Toast.makeText(context,
                                        "Sucess: " + authorizer.getToken(),
                                        Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,
                        error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + authorizer.getToken());
                return params;
            }
        };
        queue.add(jsonObjectRequest);
    }
}*/
