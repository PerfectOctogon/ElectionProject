package com.example.electionproject;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.ViewDebug;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {

    //Database name
    private static final String DB_NAME = "electionappdatabase";

    //Context
    private Context context;

    //Database version
    private static final int DB_VERSION = 7;


    //Names of all the tables in the database
    private static final String TABLE_NAME = "candidatevotes";
    private static final String ETABLE_NAME = "editablecandidates";
    private static final String EPTABLE_NAME = "editablepositions";

    //ID column for each table
    private static final String ID_COL = "id";
    private static final String EID_COL = "id";
    private static final String EPID_COL = "id";

    //Candidate name column for all appropriate tables
    private static final String NAME_COL = "candidatename";
    private static final String ENAME_COL = "candidatename";

    //Total votes column for the candidate info table
    private static final String TOTALVOTES_COL = "totalcandidatevotes";

    //Candidate position column for the appropriate tables
    private static final String POSITION_COL = "candidateposition";
    private static final String EPOSITION_COL = "candidateposition";
    //private static final String EPPOSITION_COL = "positionname";

    //Candidate image URI column for the appropriate tables
    private static final String CANDURI_COL = "candidateURI";
    private static final String ECANDURI_COL = "candidateURI";

    // Constructor for the Database handler
    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
        System.out.println("This was created");
    }

    //Creating the database
    @Override
    public void onCreate(SQLiteDatabase db) {

        //Creating a SqLite query and setting column names along with their data types

        String equery = "CREATE TABLE " + ETABLE_NAME + " ("
                + EID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ENAME_COL + " TEXT, "
                + ECANDURI_COL + " TEXT, "
                + EPOSITION_COL + " TEXT);";

        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + NAME_COL + " TEXT,"
                + TOTALVOTES_COL + " INTEGER,"
                + CANDURI_COL + " TEXT,"
                + POSITION_COL + " TEXT);";

        String epquery = "CREATE TABLE " + EPTABLE_NAME + " ("
                + EPID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ECANDURI_COL + " TEXT,"
                + EPOSITION_COL + " TEXT);";


        db.execSQL(query);
        db.execSQL(equery);
        db.execSQL(epquery);

    }

    //Method to edit Editable Positions in the Database
    public void EditEditablePositions(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(EPTABLE_NAME, null, null);
        ContentValues epvalues = new ContentValues();

        for(Position pos : PositionsAndCandidates.getInstance().GetAllEditablePosition()){
            epvalues.put(EPOSITION_COL, pos.positionName);
            db.insert(EPTABLE_NAME, null, epvalues);
        }
    }

    //Method to edit Editable Candidates in the Database
    public void EditEditableCandidates(){
        System.out.println("This was called");
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ETABLE_NAME, null, null);
        ContentValues evalues = new ContentValues();

        for(Candidate can : PositionsAndCandidates.getInstance().GetAllEditableCandidate()){
            System.out.println(can.GetName());
            evalues.put(ENAME_COL, can.GetName());
            evalues.put(EPOSITION_COL, can.GetPosition().positionName);
            evalues.put(ECANDURI_COL, can.GetURIString());
            db.insert(ETABLE_NAME, null, evalues);
        }
    }

    //Method to get Editable Positions from the Database
    public ArrayList<Position> GetEditablePositions(){
        SQLiteDatabase db = this.getWritableDatabase();

        //db = this.getWritableDatabase();
        ArrayList<Position> pos = new ArrayList<>();
        long count = DatabaseUtils.queryNumEntries(db, EPTABLE_NAME);
        if(count > 0) {
            String query = "SELECT * " + "FROM " + EPTABLE_NAME + ';';
            Cursor cursor = db.rawQuery(query, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    System.out.println("These is the position name : " + cursor.getString(2));
                    pos.add(new Position(cursor.getString(2), 10));
                    cursor.moveToNext();
                }
            }
        }
        return pos;
    }

    //Method to get Editable Candidates from the Database
    public ArrayList<Candidate> GetEditableCandidates(){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Candidate> cans = new ArrayList<>();
        long count = DatabaseUtils.queryNumEntries(db, ETABLE_NAME);
        System.out.println(count);
        if(count > 0) {
            String query = "SELECT * " + "FROM " + ETABLE_NAME + ';';
            Position pos;
            Cursor cursor = db.rawQuery(query, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    for (Position p : PositionsAndCandidates.getInstance().GetAllEditablePosition()) {
                        if (p.positionName.equals(cursor.getString(3))) {
                            pos = p;
                            Candidate can = new Candidate(cursor.getString(1), pos);
                            System.out.println("This is the URI : ");
                            //System.out.println(cursor.getString(2));
                            can.SetURIString(cursor.getString(2));
                            cans.add(can);
                        }
                    }
                    cursor.moveToNext();
                }
            }
        }
        return cans;
    }

    //Method to set the Finalized Candidates to the Main arraylist
    public void SetActiveCandidates(ArrayList<Candidate> cans){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        ContentValues values = new ContentValues();
        for(Candidate can : cans){
            System.out.println(can.GetName() + " is added to database");
            values.put(NAME_COL, can.GetName());
            values.put(TOTALVOTES_COL, 0);
            values.put(ECANDURI_COL, can.GetURIString());
            values.put(POSITION_COL, can.GetPosition().positionName);
            db.insert(TABLE_NAME, null, values);
        }
    }

    //Method to add a vote to a Candidate
    public void AddCandidateVotes(String candidateName, String positionName){
        SQLiteDatabase db = this.getWritableDatabase();
        int currVotes = 0;
        String query = "SELECT * " + "FROM " + TABLE_NAME + ';';

        Cursor cursor = db.rawQuery(query,null);

        if (cursor != null) {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                if(cursor.getString(1).equals(candidateName) && cursor.getString(4).equals(positionName)){
                    System.out.println(candidateName + " has " + cursor.getInt(2) + " votes.");
                    int newVoteNumber = cursor.getInt(2) + 1;
                    String addQuery = "UPDATE " + TABLE_NAME + " SET " + TOTALVOTES_COL + " = " + newVoteNumber + " WHERE " + NAME_COL + " = '" + candidateName + "';";
                    db.execSQL(addQuery);
                }

                cursor.moveToNext();
            }
        }
        /*System.out.println(cursor.getString(0));
        System.out.println("Total votes for " + candidateName + ": " + currVotes );*/
    }

    //Method to reset every candidate votes to zero
    public void ResetVotes(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + TOTALVOTES_COL + " = " + "0";
        db.execSQL(query);
    }

    //Method to get the number of votes a Candidate has
    public int GetCandidateVotes(String candidateName){
        SQLiteDatabase db = this.getWritableDatabase();
        //String query = "SELECT " + TOTALVOTES_COL + " FROM " + TABLE_NAME + " WHERE " + NAME_COL + " = " + "'Rakshith';";
        String query = "SELECT * " + "FROM " + TABLE_NAME + ';';

        Cursor cursor = db.rawQuery(query,null);

        if (cursor != null) {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                if(cursor.getString(1).equals(candidateName)){
                    return cursor.getInt(2);
                }
                cursor.moveToNext();
            }
        }
        return 0;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ETABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + EPTABLE_NAME);
        onCreate(db);

    }
}
