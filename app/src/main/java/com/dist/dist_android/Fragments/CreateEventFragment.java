package com.dist.dist_android.Fragments;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.dist.dist_android.Logic.Authorizer;
import com.dist.dist_android.Logic.CustomEventListeners.EventCreatedListener;
import com.dist.dist_android.Logic.CustomEventListeners.EventUpdatedListener;
import com.dist.dist_android.Logic.CustomEventListeners.InvitationAcceptListener;
import com.dist.dist_android.Logic.CustomEventListeners.InvitationSentListener;
import com.dist.dist_android.Logic.CustomEventListeners.SingleEventRecievedListener;
import com.dist.dist_android.Logic.EventProvider;
import com.dist.dist_android.POJOS.EventPackage.Details;
import com.dist.dist_android.POJOS.EventPackage.Event;
import com.dist.dist_android.POJOS.EventPackage.Invitation;
import com.dist.dist_android.POJOS.Organizer;
import com.dist.dist_android.R;

import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


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
    private EditText imageEditText;
    private CheckBox privateEventCheckbox;
    private Button createEventButton;
    private TextView titleTextView;
    private Authorizer authorizer;
    private Calendar myCalendar;
    private DatePickerDialog.OnDateSetListener startDate;
    private DatePickerDialog.OnDateSetListener endDate;
    private TimePickerDialog.OnTimeSetListener startTime;
    private TimePickerDialog.OnTimeSetListener endTime;
    private final String myFormat = "dd/MM/yy";
    private final SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
    private final String myTimeFormat = "HH:mm";
    private final SimpleDateFormat stf = new SimpleDateFormat(myTimeFormat);
    private String formState = "CREATEEVENT";
    private int eventID;
    private Event tmpevent;
    private Toolbar toolbar;

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
        privateEventCheckbox = (CheckBox) rootView.findViewById(R.id.privateCheckBox);
        createEventButton = (Button) rootView.findViewById(R.id.createEventButton);
        titleTextView = (TextView) rootView.findViewById(R.id.textView3);

        //Get arguments if any.
        Bundle bundle = getArguments();
        //If the arguments contains an event ID it means that this fragment was called
        //from a onClick event on a existing event, and the values needs to be loaded for the correct state.
        eventID = 0;
        eventID = bundle.getInt("EVENTID");
        if (eventID != 0) {
            EventProvider.getInstance().catchEvent(eventID, new SingleEventRecievedListener() {
                @Override
                public void getResult(Event event) {
                    CreateEventFragment.this.tmpevent = event;
                    decideEventType(event);
                    switch (formState) {
                        case "INVITATIONEVENT":
                            setInvitationEventFormState(event);
                            return;
                        case "MYEVENT":
                            setMyEventFormState(event);
                            return;
                        case "PUBLICEVENT":
                            setPublicEventFormState(event);
                            return;
                    }
                }
            });
        }


        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Opret Event");


        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (formState) {
                    case "INVITATIONEVENT":
                        try {
                            sendAcceptToInvitation(rootView.getContext(), eventID);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return;
                    case "MYEVENT":
                        try {
                            updateEvent(rootView.getContext());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return;
                    case "PUBLICEVENT":

                        return;
                    case "CREATEEVENT":
                        try {
                            createEvent(rootView.getContext());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        return;
                }
                try {
                    createEvent(rootView.getContext());
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });


        /**
         * Initializing date and time components, including handling events for date and time pickers
         */
        dateStartEditText = (EditText) rootView.findViewById(R.id.dateStartEditText);
        dateEndEditText = (EditText) rootView.findViewById(R.id.dateEndEditText);
        timeStartEditText = (EditText) rootView.findViewById(R.id.timeStartEditText);
        timeEndEditText = (EditText) rootView.findViewById(R.id.timeEndEditText);
        myCalendar = Calendar.getInstance();
        timeStartEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(rootView.getContext(), startTime, 12, 00, true).show();
            }
        });
        startTime = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                timeStartEditText.setText(String.format("%02d:%02d", i, i1));
            }
        };

        timeEndEditText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                new TimePickerDialog(rootView.getContext(), endTime, 12, 00, true).show();
            }
        });
        endTime = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                timeEndEditText.setText(String.format("%02d:%02d", i, i1));
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

    private void createEvent(final Context context) throws JSONException, ParseException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy HH:mm");
        String startDateTime = dateStartEditText.getText().toString() + " " + timeStartEditText.getText().toString();
        String endDateTime = dateEndEditText.getText().toString() + " " + timeEndEditText.getText().toString();
        Date startDate = simpleDateFormat.parse(startDateTime);
        Date endDate = simpleDateFormat.parse(endDateTime);

        Log.d("time", String.valueOf(startDate.getTime()));

        if (startDate.getTime() < endDate.getTime()) {
            EventProvider.getInstance().createEvent(
                    nameEditText.getText().toString(),
                    descriptionEditText.getText().toString(),
                    startDate.getTime(),
                    endDate.getTime()+1,
                    !privateEventCheckbox.isChecked(),
                    addressEditText.getText().toString(),
                    new EventCreatedListener<Event>() {
                        @Override
                        public void getResult(Integer result) {
                            Toast.makeText(context,
                                    "Sucess: " + result.toString() + " got created",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        }
        else{
            Toast.makeText(context,
                    "The start date is after the end date, please correct",
                    Toast.LENGTH_LONG).show();
    }


    }

/*    private void sendInvites(final Context context, int eventID) throws JSONException {
        EventProvider.getInstance().sendInvite(eventID, authorizer.getId(), new InvitationSentListener() {
            @Override
            public void getResult(Integer result) {
                Toast.makeText(context,
                        "Sucess: " + result.toString() + " got created",
                        Toast.LENGTH_LONG).show();
            }
        });
    }*/

    private void decideEventType(Event event) {
        if (event.getDetails().isPublic()) {
            formState = "PUBLICEVENT";
        }
        for (Invitation i : event.getInvitations()) {
            if (i.getUser().getID() == authorizer.getId()) {
                formState = "INVITATIONEVENT";
            }
        }
        for (Organizer o : event.getOrganizers()) {
            if (o.getUser().getID() == authorizer.getId()) {
                formState = "MYEVENT";
            }
        }
    }

    private void updateEvent(final Context context) throws ParseException, JSONException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy HH:mm");
        String startDateTime = dateStartEditText.getText().toString() + " " + timeStartEditText.getText().toString();
        String endDateTime = dateEndEditText.getText().toString() + " " + timeEndEditText.getText().toString();
        Date startDate = simpleDateFormat.parse(startDateTime);
        Date endDate = simpleDateFormat.parse(endDateTime);

        Details eventDetails = new Details();

        eventDetails.setTitle(nameEditText.getText().toString());
        eventDetails.setDescription(descriptionEditText.getText().toString());
        eventDetails.setImageURL(tmpevent.getDetails().getImageURL());
        eventDetails.setStart(startDate.getTime());
        eventDetails.setEnd(endDate.getTime()+1);
        eventDetails.setPublic(!privateEventCheckbox.isChecked());
        eventDetails.setAddress(addressEditText.getText().toString());


        if(eventDetails.getStart()<eventDetails.getEnd()) {
            EventProvider.getInstance().updateEvent(eventDetails, eventID, new EventUpdatedListener() {

                @Override
                public void getResult(boolean result) {
                    Toast.makeText(context,
                            "Event " + eventID + " er blevet opdateret",
                            Toast.LENGTH_LONG).show();
                }
            });
        }
        else{
            Toast.makeText(context,
                    "The start date is after the end date, please correct",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void sendAcceptToInvitation(final Context context, int eventID) throws JSONException {
        int invitationId = 0;
        for (Invitation i : tmpevent.getInvitations()) {
            if (i.getUser().getID() == authorizer.getId()) {
                invitationId = i.getId();
            }
        }
        EventProvider.getInstance().acceptInvite(eventID, invitationId, new InvitationAcceptListener() {
            @Override
            public void getResult(Integer result) {

            }
        });
        Toast.makeText(context,
                "You are now participating in the event!",
                Toast.LENGTH_LONG).show();
    }

    private void setMyEventFormState(Event event) {
        nameEditText.setText(event.getDetails().getTitle());
        descriptionEditText.setText(event.getDetails().getDescription());
        addressEditText.setText(event.getDetails().getAddress());
        if (!event.getDetails().isPublic()) {
            privateEventCheckbox.setChecked(false);
        }

        //Get Date and time
        Date startDate = new Date(event.getDetails().getStart());
        dateStartEditText.setText(sdf.format(startDate));
        timeStartEditText.setText(stf.format(startDate));
        Date endDate = new Date(event.getDetails().getEnd());
        dateEndEditText.setText(sdf.format(endDate));
        timeEndEditText.setText(stf.format(endDate));
        createEventButton.setText("Update Event");
        titleTextView.setText("Edit your event!");
        toolbar.setTitle("My Event");

    }

    private void setPublicEventFormState(Event event) {

        nameEditText.setText(event.getDetails().getTitle());
        descriptionEditText.setText(event.getDetails().getDescription());
        addressEditText.setText(event.getDetails().getAddress());
        if (!event.getDetails().isPublic()) {
            privateEventCheckbox.setChecked(false);
        }

        //Get Date and time
        Date startDate = new Date(event.getDetails().getStart());
        dateStartEditText.setText(sdf.format(startDate));
        timeStartEditText.setText(stf.format(startDate));
        Date endDate = new Date(event.getDetails().getEnd());
        dateEndEditText.setText(sdf.format(endDate));
        timeEndEditText.setText(stf.format(endDate));

        disableEditText(nameEditText);
        disableEditText(descriptionEditText);
        disableEditText(addressEditText);
        disableEditText(timeStartEditText);
        disableEditText(dateStartEditText);
        disableEditText(timeEndEditText);
        disableEditText(dateEndEditText);
        privateEventCheckbox.setEnabled(false);
        titleTextView.setText("Public Event!");
        createEventButton.setEnabled(false);
        createEventButton.setVisibility(View.GONE);
        toolbar.setTitle("Public event");
    }

    private void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
        editText.setBackgroundColor(Color.TRANSPARENT);
        editText.setTextColor(Color.BLACK);
        editText.setTextSize(18);
    }

    private void setInvitationEventFormState(Event event) {
        nameEditText.setText(event.getDetails().getTitle());
        descriptionEditText.setText(event.getDetails().getDescription());
        addressEditText.setText(event.getDetails().getAddress());
        if (!event.getDetails().isPublic()) {
            privateEventCheckbox.setChecked(false);
        }

        //Get Date and time
        Date startDate = new Date(event.getDetails().getStart());
        dateStartEditText.setText(sdf.format(startDate));
        timeStartEditText.setText(stf.format(startDate));
        Date endDate = new Date(event.getDetails().getEnd());
        dateEndEditText.setText(sdf.format(endDate));
        timeEndEditText.setText(stf.format(endDate));
        toolbar.setTitle("My Invitation");

        disableEditText(nameEditText);
        disableEditText(descriptionEditText);
        disableEditText(addressEditText);
        disableEditText(timeStartEditText);
        disableEditText(dateStartEditText);
        disableEditText(timeEndEditText);
        disableEditText(dateEndEditText);
        privateEventCheckbox.setEnabled(false);
        titleTextView.setText("You are invited!");

        for (Invitation i : event.getInvitations()) {
            if (i.getUser().getID() == authorizer.getId() && i.isAccepted()) {
                createEventButton.setText("You are participating");
                createEventButton.setEnabled(false);
                createEventButton.setBackgroundColor(Color.CYAN);
            } else {
                createEventButton.setText("Accept Event!");
                createEventButton.setEnabled(true);
                createEventButton.setBackgroundColor(Color.GREEN);
            }
        }
    }
}
