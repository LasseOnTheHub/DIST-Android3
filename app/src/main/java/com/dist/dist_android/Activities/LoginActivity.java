package com.dist.dist_android.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.dist.dist_android.Logic.EventProvider;
import com.dist.dist_android.R;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    EditText usernameEditText;
    EditText passwordEditText;
    TextView statusText;
    Button loginButton;
    Authorizer authorizer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        authorizer = new Authorizer(this);
        //Calls the EventProvider and adds the context only once
        EventProvider.getInstance(this);

        usernameEditText = (EditText) findViewById(R.id.usernameeditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        loginButton = (Button) findViewById(R.id.loginbutton);
        statusText = (TextView) findViewById(R.id.status_text);

        //Button event click listener
        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                try {
                    login();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void login() throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://ubuntu4.javabog.dk:3028/rest/api/authentication";
        final JSONObject jsonBody = new JSONObject("{" +
                "username: "+ usernameEditText.getText().toString().toLowerCase().trim() +"," +
                "password: "+ passwordEditText.getText().toString().trim() +
                "}");

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonBody,
                new Response.Listener<JSONObject>() {
                    String status;
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response.toString() !=""){
                            try {
                                authorizer.setToken(response.getString("token"));
                                //JSONObject user = response.getJSONObject("user");
                                //authorizer.setId(user.getInt("id"));
                                authorizer.setId(120);

                                Toast.makeText(getApplicationContext(),
                                        "Sucess: " + authorizer.getToken(),
                                        Toast.LENGTH_LONG).show();
                                openMainActivity();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(),
                                        "Error: " + e.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonObjectRequest);
    }

    public void openMainActivity()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
