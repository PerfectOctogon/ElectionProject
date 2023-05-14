package com.example.electionproject;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Iterator;

public class PositionsAndCandidates {
    private Context context;

    //Database
    DBHandler dbHandler;

    //Singleton
    private static PositionsAndCandidates instance = null;

    //Arraylists
    private ArrayList<Position> allPositions = new ArrayList<>();
    private ArrayList<Candidate> allCandidates = new ArrayList<>();
    private ArrayList<Position> editablePositions = new ArrayList<>();
    private ArrayList<Candidate> editableCandidates = new ArrayList<>();

    //Singleton
    private PositionsAndCandidates(){
        this.context = context;
        dbHandler = new DBHandler(context);
    }

    //Method to get an instance of this Singleton
    public static PositionsAndCandidates getInstance(){
        if(instance == null){
            instance = new PositionsAndCandidates();
        }
        return instance;
    }

    //Method to get existing candidates from the database
    public void FetchEditableCandidatesFromDatabase(Context context){
        dbHandler = new DBHandler(context);
        editableCandidates = dbHandler.GetEditableCandidates();
    }

    //Method to get existing positions from the database
    public void FetchEditablePositionsFromDatabase(Context context){
        dbHandler = new DBHandler(context);
        editablePositions = dbHandler.GetEditablePositions();
    }

    //Method to edit the Editable Positions array
    public void EditPositions(ArrayList<Position> newPositions, Context context){
        editablePositions = newPositions;
        dbHandler = new DBHandler(context);
        dbHandler.EditEditablePositions();
    }

    //Method to edit the Editable Candidates array
    public void EditCandidates(ArrayList<Candidate> newCandidates, Context context){
        editableCandidates = newCandidates;
        dbHandler = new DBHandler(context);
        dbHandler.EditEditableCandidates();
    }

    //Method to check to see if any position contains no candidates
    public Boolean CheckEmptyPosition(){
        int canCount = 0;
        for(Position pos : editablePositions){
            canCount = 0;
            for(Candidate can : editableCandidates){
                if(can.GetPosition().positionName == pos.positionName){
                    canCount++;
                }
            }
            if(canCount <= 0){return true;}
        }
        return false;
    }

    //Method to start the election with the selected candidates and positions
    public void StartElectionWithSelectedCandidates(Context context){
        dbHandler = new DBHandler(context);
        boolean electionStarted = true;
        SharedPreferences preferences = context.getSharedPreferences("key",0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("bool", electionStarted);
        editor.commit();

        allCandidates.clear();
        allPositions.clear();

        for(Candidate can : editableCandidates){
            allCandidates.add(can);
        }
        for(Position pos : editablePositions){
            allPositions.add(pos);
        }

        dbHandler.SetActiveCandidates(allCandidates);
    }

    //Method to get all election candidates
    public ArrayList<Candidate> GetAllCandidates(){
        return allCandidates;
    }

    //Method to finalize all editable candidates
    public void FinalizePositionsAndCandidates()
    {
        allPositions = editablePositions;
        allCandidates = editableCandidates;
    }

    //Method to get all election positions
    public ArrayList<Position> GetAllPosition(){return allPositions;}

    //Method to get all editable positions
    public ArrayList<Position> GetAllEditablePosition(){return editablePositions;}

    //Method to get all editable candidates
    public ArrayList<Candidate> GetAllEditableCandidate(){return editableCandidates;}
}
