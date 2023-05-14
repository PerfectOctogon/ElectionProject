package com.example.electionproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    //UI components
    Button studentbutton;
    Button teacherbutton;
    TextView electionName;

    //Variables
    Boolean electionStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initializing UI elements
        studentbutton = (Button)(findViewById(R.id.studentbutton));
        teacherbutton = (Button) findViewById(R.id.teacherbutton);
        electionName = (TextView) findViewById(R.id.electionNamemain);

        //Fetching Editable Candidates and Positions from Singleton
        PositionsAndCandidates.getInstance().FetchEditablePositionsFromDatabase(getApplicationContext());
        PositionsAndCandidates.getInstance().FetchEditableCandidatesFromDatabase(getApplicationContext());

        //Using Shared Preferences to check is an election is already in progress
        SharedPreferences preferences = getSharedPreferences("key", 0);
        electionStarted = preferences.getBoolean("bool", false);
        String electionNamemain = preferences.getString("electionname", "NO ELECTION IN PROGRESS");
        electionName.setText(electionNamemain.toUpperCase(Locale.ROOT));
        if(electionStarted){
            System.out.println("Election was started, setting main position array to editable position array");
            PositionsAndCandidates.getInstance().FinalizePositionsAndCandidates();
        }

        //Taking the user to the Voting activity if they indicate they are a student
        studentbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(electionStarted) {
                    Intent intent = new Intent(getApplicationContext(), Voting.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast toast=Toast. makeText(getApplicationContext(),"Election has to be started for voting to begin!",Toast. LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        //Taking the user to the password enter screen if they indicate they are a teacher
        teacherbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Password.class);
                startActivity(intent);
                finish();
            }
        });
    }
}