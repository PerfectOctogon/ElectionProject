package com.example.electionproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Password extends AppCompatActivity {

    //UI components
    Button submitbutton;
    Button returnbutton;

    //Variables
    EditText password;
    String actualPassword;
    String temporaryPassword = "admin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        //Initializing UI elements
        password = (EditText) findViewById(R.id.pintext);
        submitbutton = (Button) findViewById(R.id.submitPINbutton);
        returnbutton = (Button) findViewById(R.id.returnbutton1);

        //Checking Shared Preferences to get whatever password was set for the election
        SharedPreferences preferences = getSharedPreferences("key", 0);
        actualPassword = preferences.getString("electionpassword", "");

        //Checking to see if an election is active and a password is needed to access the lobby
        if(!preferences.getBoolean("bool", false)){
            Intent intent = new Intent(getApplicationContext(), TeacherLobby.class);
            startActivity(intent);
            finish();
        }

        //Checking if the entered password matches the correct password when the Submit button is pressed
        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!actualPassword.equals(password.getText().toString())){
                    Toast toast=Toast. makeText(getApplicationContext(),"Wrong password!",Toast. LENGTH_SHORT);
                    toast.show();
                    return;
                }

                Intent intent = new Intent(getApplicationContext(), TeacherLobby.class);
                startActivity(intent);
                finish();
            }
        });

        //Returning to Main when Return button is pressed
        returnbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity((intent));
                finish();
            }
        });

    }
}