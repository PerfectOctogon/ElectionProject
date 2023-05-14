package com.example.electionproject;

import java.util.ArrayList;

public class Position {

    //Position attributes
    public String positionName;
    public int numCandidates;

    //Candidates in this position
    public ArrayList<Candidate> candidates = new ArrayList<Candidate>();

    //Position constructor
    public Position(String pName, int nCands){
        positionName = pName;
        numCandidates = nCands;
        candidates.clear();
    }

}
