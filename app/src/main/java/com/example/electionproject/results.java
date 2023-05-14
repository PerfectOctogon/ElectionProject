package com.example.electionproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

public class results extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        //Database
        DBHandler dbHandler = new DBHandler(this);

        //Variables
        int numSpaces = 51;

        //UI components
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.resultsinsert);
        Button returnbutton = (Button) findViewById(R.id.returnbutton);

        //Density control for padding
        int value = (int) (30 * getResources().getDisplayMetrics().density);
        int value1 = (int) (16 * getResources().getDisplayMetrics().density);

        //Creating the results page
        for (Position position:
             PositionsAndCandidates.getInstance().GetAllPosition()) {
            TextView positionText = new TextView(this);
            positionText.setText(position.positionName.toUpperCase(Locale.ROOT));
            positionText.setGravity(17);
            positionText.setTextColor(Color.rgb(225,225,225));
            positionText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
            positionText.setTypeface(Typeface.create("sans-serif-black", Typeface.NORMAL));
            positionText.setPadding(0,value,0,value);
            linearLayout.addView(positionText);

            for (Candidate candidate:
                 PositionsAndCandidates.getInstance().GetAllCandidates()) {
                if (candidate.GetPosition().positionName.equals(position.positionName)) {
                    TextView candidateText = new TextView(this);
                    candidateText.setText(candidate.GetName() + ": " + dbHandler.GetCandidateVotes(candidate.GetName()) + " votes");
                    candidateText.setTextColor(Color.rgb(225, 225, 225));
                    candidateText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                    candidateText.setTypeface(Typeface.create("sans-serif-black", Typeface.NORMAL));
                    positionText.setPadding(0, value1, 0, value1);
                    linearLayout.addView(candidateText);
                }
            }
        }

        //If the return button is pressed...
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