package com.example.electionproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class Voting extends AppCompatActivity {

    //UI components
    LinearLayout containerLayout;
    TextView positionName;
    ProgressBar progressBar;
    Button nextButton;
    TextView scrollText;

    //Variables
    int maxProgressNumber = PositionsAndCandidates.getInstance().GetAllPosition().size() + 1;
    private int progressNumber = 1;

    //Candidate variables
    private Candidate selectedCandidate;
    ArrayList<RelativeLayout> candidatelayouts = new ArrayList<RelativeLayout>();
    ArrayList<Candidate> candidateSelected = new ArrayList<Candidate>();

    //Database
    DBHandler dbHandler = new DBHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting);

        //Initializing all UI elements and some variables
        positionName = (TextView) findViewById(R.id.positiontext);
        nextButton = (Button) findViewById(R.id.nextbutton);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBar.setMax(maxProgressNumber);
        containerLayout = (LinearLayout) findViewById(R.id.container);
        scrollText = (TextView) findViewById(R.id.ScrollForMore);

        //Calling the Update UI method to update the current position and candidates
        UpdateUIv2();

        //If the next button is clicked...
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Checking if a candidate is selected
                if(selectedCandidate == null){ Toast toast=Toast. makeText(getApplicationContext(),"You must choose a candidate!",Toast. LENGTH_SHORT);
                    toast. show(); return;}

                //Checking if there are more positions left
                if(progressNumber < maxProgressNumber){
                    candidateSelected.add(selectedCandidate);
                    ResetSelections();
                    //Updating UI
                    UpdateUIv2();
                }
                //No more positions, so end the voting section...
                else{
                    candidateSelected.add(selectedCandidate);
                    for(Candidate can : candidateSelected){
                        can.AddVote();
                        dbHandler.AddCandidateVotes(can.GetName(), can.GetPosition().positionName);
                    }
                    candidateSelected.clear();
                    progressNumber = 1;
                    Intent intent = new Intent(getApplicationContext(), FinishedPage.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    //Deselecting a selected candidate
    private void ResetSelections(){
        selectedCandidate = null;
        for (RelativeLayout layout:
                candidatelayouts) {
            layout.getBackground().setTint(Color.rgb(42, 83, 188));
        }
    }

    //Method to update the candidates for each position
    private void UpdateUIv2(){
        containerLayout.removeAllViews();
        candidatelayouts.clear();
        int canCount = 0;
        scrollText.setVisibility(View.INVISIBLE);
        Position currentPosition = PositionsAndCandidates.getInstance().GetAllPosition().get(progressNumber - 1);
        System.out.println("Current position name: " + currentPosition.positionName);
        positionName.setText(currentPosition.positionName);
        progressBar.setProgress(progressNumber);
            for (Candidate candidate : PositionsAndCandidates.getInstance().GetAllCandidates()) {

                System.out.println("List candidate position name: " + candidate.GetPosition().positionName);
                if (candidate.GetPosition().positionName.equals(currentPosition.positionName)) {

                    Candidate createdCandidate = candidate;
                    canCount++;
                    if(canCount > 3 && scrollText.getVisibility() == View.INVISIBLE){scrollText.setVisibility(View.VISIBLE);}

                    int id = getResources().getIdentifier(createdCandidate.GetName().toLowerCase(Locale.ROOT).replaceAll(" ", ""), "drawable", getPackageName());

                    ImageView candidateImage = new ImageView(this);
                    candidateImage.setBackgroundColor(Color.rgb(0, 0, 0));
                    candidateImage.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    candidateImage.getLayoutParams().height = (int) (150 * getResources().getDisplayMetrics().density);
                    candidateImage.getLayoutParams().width = (int) (150 * getResources().getDisplayMetrics().density);
                    candidateImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    candidateImage.setImageResource(id);
                    if(createdCandidate.GetURIString() != null){candidateImage.setImageURI(Uri.parse(createdCandidate.GetURIString()));}

                    TextView candidateName = new TextView(this);
                    candidateName.setText(createdCandidate.GetName());
                    RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                    textParams.setMargins((int) (178 * getResources().getDisplayMetrics().density), (int) (38 * getResources().getDisplayMetrics().density), (int) (33 * getResources().getDisplayMetrics().density), (int) (41 * getResources().getDisplayMetrics().density));
                    textParams.height = (int) (67 * getResources().getDisplayMetrics().density);
                    textParams.width = (int) (178 * getResources().getDisplayMetrics().density);
                    candidateName.setLayoutParams(textParams);
                    candidateName.setGravity(Gravity.CENTER);
                    candidateName.setTextColor(Color.rgb(225, 225, 225));
                    candidateName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    candidateName.setTypeface(Typeface.create("sans-serif-black", Typeface.NORMAL));

                    RelativeLayout relativeLayout = new RelativeLayout(this);
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
                    params.height = (int) (150 * getResources().getDisplayMetrics().density);
                    params.setMargins(0, (int) (16 * getResources().getDisplayMetrics().density), 0, 0);
                    relativeLayout.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.default_option_border));
                    relativeLayout.setLayoutParams(params);
                    candidatelayouts.add(relativeLayout);
                    relativeLayout.setClickable(true);
                    relativeLayout.addView(candidateImage);
                    relativeLayout.addView(candidateName);


                    relativeLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ResetSelections();
                            selectedCandidate = createdCandidate;
                            relativeLayout.getBackground().setTint(Color.rgb(163, 193, 239));
                            System.out.println(selectedCandidate.GetName());
                        }
                    });
                    containerLayout.addView(relativeLayout);
                }
            }
        progressNumber++;
    }

}