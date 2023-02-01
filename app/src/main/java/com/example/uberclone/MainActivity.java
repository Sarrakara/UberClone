package com.example.uberclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.sql.Driver;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /** onClick method of "btnOneTimeLogin" */
    @Override
    public void onClick(View view) {

        if (edtDriverOrPassenger.getText().toString().equals("Driver") ||
                edtDriverOrPassenger.getText().toString().equals("Passenger")) {
            // if there is no current user
            if(ParseUser.getCurrentUser() == null){
                // login anonymously (without creating an account with username and password
                ParseAnonymousUtils.logIn(new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if(user != null && e == null){
                            Toast.makeText(MainActivity.this, "we have an anonymous user!", Toast.LENGTH_SHORT).show();

                            user.put("as", edtDriverOrPassenger.getText().toString());
                            // to save the value of "as" column in the parseServer
                            user.saveInBackground();
                        }
                    }
                });
            }
        }else{
            Toast.makeText(this, "Are you a Driver or a Passenger?", Toast.LENGTH_SHORT).show();
        }
    }

    enum State { SIGNUP, LOGIN };

    private State state;
    private Button btnSignupLogin, btnOneTimeLogin;
    private RadioButton driverRadioButton, passengerRadioButton;
    private EditText edtUsername, edtPassword, edtDriverOrPassenger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseInstallation.getCurrentInstallation().saveInBackground();
        if(ParseUser.getCurrentUser() != null){
            // or transition to another activity
            ParseUser.logOut();
        }

        state = State.SIGNUP;

        btnSignupLogin = findViewById(R.id.btnSignUpLogin);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        passengerRadioButton = findViewById(R.id.rbPassenger);
        driverRadioButton = findViewById(R.id.rbDriver);
        btnOneTimeLogin = findViewById(R.id.btnOneTimeLogin);
        edtDriverOrPassenger = findViewById(R.id.edtDorP);
        btnOneTimeLogin.setOnClickListener(MainActivity.this);

        btnSignupLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /** Sign up the user*/
                if( state == State.SIGNUP){
                    // if we don't check any radioButton
                    if(passengerRadioButton.isChecked() == false && driverRadioButton.isChecked() == false){
                        Toast.makeText(MainActivity.this, "Are you a driver or a passenger?", Toast.LENGTH_SHORT).show();
                       //if the condition is evaluated to true then the code after the if statement won't be executed
                        return;
                    }
                    ParseUser appUser = new ParseUser();
                    appUser.setUsername(edtUsername.getText().toString());
                    appUser.setPassword(edtPassword.getText().toString());

                    if(driverRadioButton.isChecked()){
                        appUser.put("as", "Driver");
                    }else if(passengerRadioButton.isChecked()){
                        appUser.put("as", "Passenger");
                    }

                    // Sign up the user in background
                    appUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null){
                                Toast.makeText(MainActivity.this, "signed up!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                /** login the user */
                else if (state == State.LOGIN){

                    ParseUser.logInInBackground(edtUsername.getText().toString(),
                            edtPassword.getText().toString(), new LogInCallback() {
                                @Override
                                public void done(ParseUser user, ParseException e) {
                                    if( user != null && e == null){
                                        Toast.makeText(MainActivity.this, "user is logged in!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_signup_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.loginItem:

                if(state == State.SIGNUP){
                    state = State.LOGIN;
                    item.setTitle("Sign up");
                    btnSignupLogin.setText("Login");
                }else if(state == State.LOGIN){
                    state = State.SIGNUP;
                    item.setTitle("Login");
                    btnSignupLogin.setText("Sign up");
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}