package com.dist.dist_android.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dist.dist_android.Logic.Authorizer;
import com.dist.dist_android.Logic.CustomEventListeners.LoginListener;
import com.dist.dist_android.Logic.EventProvider;
import com.dist.dist_android.R;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private TextView statusText;
    private Button loginButton;
    private Authorizer authorizer;
    private Context     context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;

        authorizer = new Authorizer(context);
        //Calls the EventProvider and adds the context only once
        EventProvider.getInstance(context);

        usernameEditText = (EditText) findViewById(R.id.usernameeditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        loginButton = (Button) findViewById(R.id.loginbutton);
        statusText = (TextView) findViewById(R.id.status_text);

        //Button event click listener
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    EventProvider.getInstance().Login(
                            usernameEditText.getText().toString(),
                            passwordEditText.getText().toString(),
                            new LoginListener() {
                                @Override
                                public void result(boolean result) {
                                    if (result){
                                        Toast.makeText(context,"Success!",
                                                Toast.LENGTH_LONG).show();
                                        openMainActivity();
                                    }
                                    else{
                                        Toast.makeText(context,"Oh no! Try again",
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
