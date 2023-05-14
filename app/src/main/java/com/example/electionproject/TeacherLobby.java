package com.example.electionproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class TeacherLobby extends AppCompatActivity {

    //UI components
    Button viewresultsbutton;
    Button resetvotesbutton;
    Button editelectionbutton;

    //Variables
    private boolean electionStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_lobby);

        //Initializing UI elements
        viewresultsbutton = (Button) findViewById(R.id.submitPINbutton);
        resetvotesbutton = (Button) findViewById(R.id.returnbutton1);
        editelectionbutton = (Button) findViewById(R.id.editbutton);

        //Checking SharedPreferences to see if election has started...
        SharedPreferences preferences = getSharedPreferences("key", 0);
        electionStarted = preferences.getBoolean("bool", false);

        //When View Result button is clicked
        viewresultsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), results.class);
                startActivity(intent);
                finish();
            }
        });

        //Reset the votes when Reset Votes button is clicked
        resetvotesbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ConfirmReset.class);
                startActivity(intent);
                finish();
            }
        });

        //Open Election Editor when edit election button is clicked based upon the electionStarted boolean
        editelectionbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!electionStarted) {
                    Intent intent = new Intent(getApplicationContext(), PositionsCuztomizer.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast toast=Toast. makeText(getApplicationContext(),"An election is in progress. Reset votes to end the election.",Toast. LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }
}