package com.example.electionproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import java.lang.*;
import android.view.ViewDebug;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class ElectionCreatorEnd extends AppCompatActivity {

    //UI components
    Button startelectionbutton;
    Button dontstartelectionbutton;
    Button viewdetailsbutton;
    TextView numcands;
    TextView numpos;
    EditText electionName;
    EditText electionPassword;
    private RewardedAd rewardedAd;
    boolean isLoading;
    boolean adsfailedload;
    String AD_UNIT_ID = "ca-app-pub-7712421072657725~3212238035";

    //Intent
    private Intent i;

    //Database
    DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_election_creator_end);

        MobileAds.initialize(getApplicationContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {

            }
        });

        loadRewardedAd();

        //Initializing UI elements
        startelectionbutton = (Button) findViewById(R.id.startelection);
        dontstartelectionbutton = (Button) findViewById(R.id.dontstartelection);
        viewdetailsbutton = (Button) findViewById(R.id.viewdetail);
        numcands = (TextView) findViewById(R.id.numcands);
        numpos = (TextView) findViewById(R.id.numpos);
        electionName = (EditText) findViewById(R.id.electionName);
        electionPassword = (EditText) findViewById(R.id.password);

        //Getting instance of Database
        dbHandler = new DBHandler(this);

        //Start the election if all election criteria is met
        startelectionbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(electionName.getText().toString().equals("") && electionPassword.getText().toString().equals(""))){
                    if(!PositionsAndCandidates.getInstance().CheckEmptyPosition()) {

                        if(!adsfailedload) {
                            showAds();
                        }
                        else{
                            SharedPreferences preferences = getApplicationContext().getSharedPreferences("key", 0);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("electionname", electionName.getText().toString());
                            editor.putString("electionpassword", electionPassword.getText().toString());
                            editor.commit();
                            PositionsAndCandidates.getInstance().StartElectionWithSelectedCandidates(getApplicationContext());
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }
                    else{
                        Toast toast=Toast. makeText(getApplicationContext(),"One or more of your positions do not have at least 1 candidate!",Toast. LENGTH_SHORT);
                        toast.show();
                    }
                }
                else{
                    Toast toast=Toast. makeText(getApplicationContext(),"Name your election to start!",Toast. LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        //Don't start the election if indicated by the user
        dontstartelectionbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = new Intent(getApplicationContext(), TeacherLobby.class);
                startActivity(i);
                finish();
            }
        });

        //Check the details of the election
        viewdetailsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = new Intent(getApplicationContext(), CandidatesCuztomizer.class);
                startActivity(i);
                finish();
            }
        });

        //Setting the number of candidates and positions in the textview
        numpos.setText(""+PositionsAndCandidates.getInstance().GetAllEditablePosition().size()+"");
        numcands.setText(""+PositionsAndCandidates.getInstance().GetAllEditableCandidate().size()+"");
    }

    private void loadRewardedAd() {
        if (rewardedAd == null) {
            isLoading = true;
            AdRequest adRequest = new AdRequest.Builder().build();
            RewardedAd.load(
                    this,
                    AD_UNIT_ID,
                    adRequest,
                    new RewardedAdLoadCallback() {
                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            // Handle the error.
                            rewardedAd = null;
                            ElectionCreatorEnd.this.isLoading = false;
                            //Toast.makeText(ElectionCreatorEnd.this, "Ads failed to load...", Toast.LENGTH_SHORT).show();
                            adsfailedload = true;
                        }

                        @Override
                        public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                            ElectionCreatorEnd.this.rewardedAd = rewardedAd;
                            ElectionCreatorEnd.this.isLoading = false;
                            //Toast.makeText(ElectionCreatorEnd.this, "onAdLoaded", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void showAds(){
        if (rewardedAd == null) {
            Toast.makeText(ElectionCreatorEnd.this, "Rewarded ad was not ready!", Toast.LENGTH_SHORT);
            return;
        }

        rewardedAd.setFullScreenContentCallback(
                new FullScreenContentCallback() {
                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Called when ad is shown.
                        //Toast.makeText(ElectionCreatorEnd.this, "onAdShowedFullScreenContent", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        // Called when ad fails to show.
                        // Don't forget to set the ad reference to null so you
                        // don't show the ad a second time.
                        rewardedAd = null;
                        //Toast.makeText(ElectionCreatorEnd.this, "onAdFailedToShowFullScreenContent", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when ad is dismissed.
                        // Don't forget to set the ad reference to null so you
                        // don't show the ad a second time.
                        rewardedAd = null;

                        //Toast.makeText(ElectionCreatorEnd.this, "", Toast.LENGTH_SHORT).show();
                        // Preload the next rewarded ad.

                    }
                });
        Activity activityContext = ElectionCreatorEnd.this;
        rewardedAd.show(
                activityContext,
                new OnUserEarnedRewardListener() {
                    @Override
                    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                        // Handle the reward.
                        SharedPreferences preferences = getApplicationContext().getSharedPreferences("key", 0);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("electionname", electionName.getText().toString());
                        editor.putString("electionpassword", electionPassword.getText().toString());
                        editor.commit();
                        PositionsAndCandidates.getInstance().StartElectionWithSelectedCandidates(getApplicationContext());
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                });
    }
}