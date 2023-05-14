package com.example.electionproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.cast.framework.media.ImagePicker;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;

public class CandidatesCuztomizer extends AppCompatActivity {

    //UI components
    private LinearLayout parentcontainer;
    private ArrayList<Candidate> candidates;
    Button donebutton;
    ImageView selectedImageView;

    //Variables
    private Candidate selectedCandidate;
    String selectedImage;
    Boolean canLoadImages = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidates_cuztomizer);

        //Initializing UI elements
        donebutton = (Button) findViewById(R.id.donebutton);
        parentcontainer = (LinearLayout) findViewById(R.id.candidateholder);
        candidates  = PositionsAndCandidates.getInstance().GetAllEditableCandidate();

        //Checking if permissions are met...
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            canLoadImages = false;
        }

        //Go to Election Creator End if Done button is pressed
        donebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PositionsAndCandidates.getInstance().EditCandidates(candidates, getApplicationContext());
                Intent intent = new Intent(getApplicationContext(), ElectionCreatorEnd.class);
                startActivity(intent);
                finish();
            }
        });

        //For each existing position, add every candidate in that position
        for(Position position : PositionsAndCandidates.getInstance().GetAllEditablePosition()){

            LinearLayout candidateparentholder = new LinearLayout(this);
            candidateparentholder.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams parentLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            candidateparentholder.setLayoutParams(parentLayoutParams);

            LinearLayout positionholder = new LinearLayout(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0,dpToPx(16),0,0);
            positionholder.setOrientation(LinearLayout.VERTICAL);
            positionholder.setBackgroundColor(Color.rgb(0,0,0));
            positionholder.getBackground().setTint(Color.rgb(25, 40, 96));
            positionholder.setLayoutParams(layoutParams);

            parentcontainer.addView(positionholder);

            TextView positionname = new TextView(this);
            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams1.gravity = Gravity.CENTER_HORIZONTAL;
            layoutParams1.setMargins(0, dpToPx(8), 0, 0);
            positionname.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
            positionname.setText(position.positionName.toUpperCase(Locale.ROOT));
            positionname.setTextColor(Color.rgb(255,255,255));
            positionname.setTextSize(20);
            positionname.setLayoutParams(layoutParams1);

            positionholder.addView(positionname);

            positionholder.addView(candidateparentholder);

            Button addCandButton = new Button(getApplicationContext());

            for(Candidate existingCans : candidates){
                if(existingCans.GetPosition().positionName.equals(position.positionName)){
                    CardView ecandidateinfo = new CardView(getApplicationContext());
                    LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(150));
                    layoutParams2.gravity = Gravity.CENTER_HORIZONTAL;
                    layoutParams2.setMargins(0, dpToPx(16), 0, 0);
                    ecandidateinfo.getBackground().setTint(Color.rgb(42, 64, 145));
                    ecandidateinfo.setRadius(dpToPx(10));
                    ecandidateinfo.setLayoutParams(layoutParams2);

                    candidateparentholder.addView(ecandidateinfo);

                    ImageView ecandidatephoto = new ImageView(getApplicationContext());
                    LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(dpToPx(125), dpToPx(125));
                    layoutParams3.setMargins(dpToPx(10), dpToPx(10), 0, 0);
                    ecandidatephoto.setImageResource(getResources().getIdentifier("profilepicture", "drawable", getPackageName()));
                    if(existingCans.GetURIString() != null && canLoadImages){ecandidatephoto.setImageURI(Uri.parse(existingCans.GetURIString()));}
                    ecandidatephoto.setLayoutParams(layoutParams3);

                    Button eimageChangeButton = new Button(getApplicationContext());
                    eimageChangeButton.setLayoutParams(layoutParams3);
                    eimageChangeButton.setVisibility(View.INVISIBLE);

                    LinearLayout.LayoutParams layoutParams5 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams5.setMargins(dpToPx(150), dpToPx(90), 0, 0);
                    Button removeCandidate = new Button(getApplicationContext());
                    removeCandidate.setLayoutParams(layoutParams5);
                    removeCandidate.setText("REMOVE CANDIDATE");
                    removeCandidate.setTextColor(Color.rgb(255, 0, 0));
                    removeCandidate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            candidates.remove(existingCans);
                            candidateparentholder.removeView(ecandidateinfo);
                        }
                    });
                    ecandidateinfo.addView(removeCandidate);
                    ecandidateinfo.addView(ecandidatephoto);
                    ecandidateinfo.addView(eimageChangeButton);

                    TextView ecandidatename = new TextView(getApplicationContext());
                    LinearLayout.LayoutParams layoutParams4 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams4.setMargins(dpToPx(150), dpToPx(35), 0, 0);
                    ecandidatename.setEms(10);
                    ecandidatename.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
                    ecandidatename.setText(existingCans.GetName());
                    ecandidatename.setTextColor(Color.rgb(225, 225, 225));
                    ecandidatename.setLayoutParams(layoutParams4);

                    ecandidateinfo.addView(ecandidatename);
                }
            }

            addCandButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final Candidate[] newCan = new Candidate[1];

                    newCan[0] = new Candidate(" ", position);

                    CardView candidateinfo = new CardView(getApplicationContext());
                    LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(150));
                    layoutParams2.gravity = Gravity.CENTER_HORIZONTAL;
                    layoutParams2.setMargins(0, dpToPx(16), 0, 0);
                    candidateinfo.getBackground().setTint(Color.rgb(42, 64, 145));
                    candidateinfo.setRadius(dpToPx(10));
                    candidateinfo.setLayoutParams(layoutParams2);

                    candidateparentholder.addView(candidateinfo);

                    ImageView candidatephoto = new ImageView(getApplicationContext());
                    LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(dpToPx(125), dpToPx(125));
                    layoutParams3.setMargins(dpToPx(10), dpToPx(10), 0, 0);
                    candidatephoto.setImageResource(getResources().getIdentifier("profilepicture", "drawable", getPackageName()));

                    candidatephoto.setLayoutParams(layoutParams3);

                    Button imageChangeButton = new Button(getApplicationContext());
                    imageChangeButton.setLayoutParams(layoutParams3);
                    imageChangeButton.setBackgroundColor(Color.TRANSPARENT);
                    imageChangeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SetImageFromGallery(candidatephoto);
                            selectedCandidate = newCan[0];
                        }
                    });

                    Button finalizeCandidate = new Button(getApplicationContext());
                    LinearLayout.LayoutParams layoutParams5 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams5.setMargins(dpToPx(150), dpToPx(90), 0, 0);
                    finalizeCandidate.setLayoutParams(layoutParams5);
                    finalizeCandidate.setText("FINALIZE CANDIDATE");
                    candidateinfo.addView(finalizeCandidate);

                    Button removeCandidate = new Button(getApplicationContext());
                    removeCandidate.setLayoutParams(layoutParams5);
                    removeCandidate.setText("REMOVE CANDIDATE");
                    removeCandidate.setTextColor(Color.rgb(255, 0, 0));
                    removeCandidate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            candidates.remove(newCan[0]);
                            candidateparentholder.removeView(candidateinfo);
                        }
                    });

                    EditText candidatename = new EditText(getApplicationContext());
                    LinearLayout.LayoutParams layoutParams4 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams4.setMargins(dpToPx(150), dpToPx(35), 0, 0);
                    candidatename.setEms(10);
                    candidatename.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
                    candidatename.setHint("Enter name...");
                    candidatename.setTextColor(Color.rgb(225, 225, 225));
                    candidatename.setInputType(16);
                    candidatename.setLayoutParams(layoutParams4);

                    candidateinfo.addView(candidatename);

                    finalizeCandidate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //System.out.println(candidatename.getText().toString());
                            newCan[0].SetName(candidatename.getText().toString());
                            candidates.add(newCan[0]);
                            for(Candidate candidate : candidates){
                                System.out.println(candidate.GetName() + " in " + candidate.GetPosition().positionName);
                            }
                            candidatename.setEnabled(false);
                            finalizeCandidate.setVisibility(View.GONE);
                            candidateinfo.addView(removeCandidate);
                        }
                    });

                    candidateinfo.addView(candidatephoto);
                    candidateinfo.addView(imageChangeButton);
                }
            });

            LinearLayout.LayoutParams layoutParams5 = new LinearLayout.LayoutParams(dpToPx(75), dpToPx(75));
            layoutParams5.gravity = Gravity.CENTER_HORIZONTAL;
            addCandButton.setLayoutParams(layoutParams5);
            addCandButton.setBackgroundResource(getResources().getIdentifier("plussign", "drawable", getPackageName()));
            positionholder.addView(addCandButton);
        }

    }

    //Get image from gallery
    private void SetImageFromGallery(ImageView imageview){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        selectedImageView = imageview;
        startActivityForResult(intent, 3);
    }

    //If a picture is selected, set the active variables to the appropriate candidate
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data != null)
        {

            /*InputStream imageStream = null;
            try {
                imageStream = getContentResolver().openInputStream(
                        data.getData());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap bmp = BitmapFactory.decodeStream(imageStream);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 50, stream);
            selectedImageView.setImageBitmap(bmp);
            byte[] byteArray = stream.toByteArray();
            try {
                stream.close();
                stream = null;
            } catch (IOException e) {

                e.printStackTrace();
            }*/
            selectedImageView.setImageURI(data.getData());
            System.out.println(data.getData().toString());
            selectedImage = data.getData().toString();
            selectedCandidate.SetURIString(data.getData().toString());
            selectedCandidate = null;
        }
        else{
            selectedImage = null;
        }
    }

    //Method to convert density pixels to normal pixels
    public int dpToPx(float dp){
        float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
        return (int)pixels;
    }

}