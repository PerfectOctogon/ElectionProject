package com.example.electionproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Random;
import java.util.ArrayList;

public class ConfirmReset extends AppCompatActivity {

    //UI components
    Button submitbutton;
    Button returnbutton;
    EditText pintext;
    TextView pindisplayer;

    //Database
    DBHandler dbHandler = new DBHandler(this);

    //Randomized variable
    private int pin = (int)(Math.random()*(9999-1000+1)+1000);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_reset);

        //Initializing UI elements
        submitbutton = (Button) findViewById(R.id.submitPINbutton);
        returnbutton = (Button) findViewById(R.id.returnbutton1);
        pintext = (EditText) findViewById(R.id.pintext);
        pindisplayer = (TextView) findViewById(R.id.pindisplay);

        pindisplayer.setText("Enter " + "'" + pin + "'");

        //Check if the PIN was entered correctly and if it was, end the election
        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(pintext.getText());
                if(pin == (Integer.parseInt(pintext.getText().toString()))) {
                    dbHandler.ResetVotes();
                    Toast toast = Toast.makeText(getApplicationContext(), "Election has ended", Toast.LENGTH_SHORT);
                    toast.show();
                    SharedPreferences preferences = getApplicationContext().getSharedPreferences("key",0);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("bool", false);
                    editor.putString("electionname", "NO ELECTION IN PROGRESS");
                    editor.commit();
                    PositionsAndCandidates.getInstance().EditPositions(new ArrayList<Position>(), getApplicationContext());
                    PositionsAndCandidates.getInstance().EditCandidates(new ArrayList<Candidate>(), getApplicationContext());
                    dbHandler.EditEditableCandidates();
                    dbHandler.EditEditablePositions();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
                Toast toast = Toast.makeText(getApplicationContext(), "Wrong PIN!", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        //Return to Main
        returnbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }

        });
    }
}