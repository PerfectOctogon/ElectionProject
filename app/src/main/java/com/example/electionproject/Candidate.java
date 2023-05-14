package com.example.electionproject;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.widget.ImageView;

public class Candidate {

    //Candidate attributes
    private String candidateName;
    private Position candidatePosition;
    private int candidateTotalVotes;
    private String imageURIString = null;

    //Context
    private Context context;

    //Candidate constructor
    public Candidate(String name, Position position){

        this.context = context;
        candidateName = name;
        candidatePosition = position;
        candidateTotalVotes = 0;

    }

    //Add a vote to this candidate
    public void AddVote(){
        candidateTotalVotes++;
    }

    //Set an image URI for this candidate
    public void SetURIString(String string){
        imageURIString = string;
    }

    //Get the image URI for this candidate
    public String GetURIString(){
        return imageURIString;
    }

    //Set the name for this candidate
    public void SetName(String n)
    {
        this.candidateName = n;
    }

    //Get the name for this candidate
    public String GetName(){return this.candidateName;}

    //Get the position for this candidate
    public Position GetPosition(){return this.candidatePosition;}

}
