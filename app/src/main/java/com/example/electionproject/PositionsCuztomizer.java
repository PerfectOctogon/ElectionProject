package com.example.electionproject;
import static android.widget.LinearLayout.*;

import java.util.ArrayList;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;


public class PositionsCuztomizer extends AppCompatActivity {

    //UI components
    ArrayList<Position> positions = new ArrayList<Position>();
    ArrayList<CardView> cardViews = new ArrayList<CardView>();
    Button addPositionButton;
    Button removePositionButton;
    Button donebutton;
    LinearLayout positionholder;

    //Variables
    int positionNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_positions_cuztomizer);

        //Checking to see if Media access is granted by the user
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        //Initializing UI elements
        positionholder = (LinearLayout) findViewById(R.id.positionholder);
        addPositionButton = (Button) findViewById(R.id.addPositionButton);
        removePositionButton = (Button) findViewById(R.id.removePositionButton);
        donebutton = (Button) findViewById(R.id.donebutton);

        //Add position when Add Position button is clicked
        addPositionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Clicked addPositionButton");
                AddEmptyPositionCard("");
            }
        });

        //Remove position when Remove Position button is clicked
        removePositionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RemovePositionCard();
            }
        });

        //Save positions to Singleton array and enter next activity when Done button is clicked
        donebutton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                DeclarePositions();
                Intent intent = new Intent(getApplicationContext(), CandidatesCuztomizer.class);
                startActivity(intent);
                finish();
            }
        });

        InitializeExistingPositions();

    }

    //Method for initializing any existing positions
    private void InitializeExistingPositions(){
        for(Position position : PositionsAndCandidates.getInstance().GetAllEditablePosition()){
            AddEmptyPositionCard(position.positionName);
        }
    }

    //Method for adding an empty position card
    private void AddEmptyPositionCard(String nameOfExistingPosition){

        positionNumber += 1;
        System.out.println("Added a position card... ");
        CardView positionCard = new CardView(getApplicationContext());
        positionholder.addView(positionCard);
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setColor(Color.rgb(25, 40, 96));
        shape.setCornerRadius(dpToPx(10));

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        layoutParams.height = dpToPx(150);
        layoutParams.setMargins(0, dpToPx(16), 0,0);
        positionCard.setBackground(shape);
        positionCard.setLayoutParams(layoutParams);

        TextView positionText = new TextView(getApplicationContext());
        positionText.setText("POSITION " + positionNumber);
        positionText.setTextColor(Color.rgb(255,255,255));
        positionText.setTextSize(20);
        positionText.setGravity(Gravity.CENTER);
        positionText.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));

        FrameLayout.LayoutParams layoutParams1 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams1.gravity = Gravity.CENTER;


        FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);

        layoutParams2.width = dpToPx(150);
        layoutParams2.setMargins(0, dpToPx(30), 0, 0);
        layoutParams2.gravity = Gravity.CENTER;

        EditText positionName = new EditText(getApplicationContext());
        positionName.setText(nameOfExistingPosition);
        positionName.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        positionName.setTextColor(Color.rgb(255,255,255));
        positionName.setSingleLine(true);
        positionName.setTextSize(20);


        positionCard.addView(positionText);
        positionCard.addView(positionName);

        positionText.setLayoutParams(layoutParams1);
        positionName.setLayoutParams(layoutParams2);

        cardViews.add(positionCard);


        LinearLayout.LayoutParams resizer = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        resizer.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        positionholder.setLayoutParams(resizer);

    }

    //Method to remove a position card
    private void RemovePositionCard(){
        if(cardViews.size() <= 0) return;
        positionNumber -= 1;
        positionholder.removeView(cardViews.get(cardViews.size() - 1));
        cardViews.remove(cardViews.get(cardViews.size() - 1));
    }

    //Method to convert Density pixels to normal pixels
    public int dpToPx(float dp){
        float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
        return (int)pixels;
    }

    //Save created and existing positions to a Singleton array
    public void DeclarePositions(){
        for(CardView positionCard : cardViews){
            EditText et = (EditText) positionCard.getChildAt(1);
            if(!et.getText().toString().equals("")){
                positions.add(new Position(et.getText().toString(), 0));
            };
        }
        PositionsAndCandidates.getInstance().EditPositions(positions, getApplicationContext());
    }

}