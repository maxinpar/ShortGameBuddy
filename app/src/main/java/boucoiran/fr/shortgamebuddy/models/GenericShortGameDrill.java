package boucoiran.fr.shortgamebuddy.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import boucoiran.fr.shortgamebuddy.models.GolfPracticeContract.ShortGameDrillEntry;


/*
 * Object representing a generic Short Game Drill
 */


public class GenericShortGameDrill {

    private int id;
    private String date_played;
    private int totalScore;
    private int cardId;
    private String notes;
    private int numberInHole;
    private int numberInside3Ft;
    private int numberInside6Ft;
    private int numberOutside;
    private String grade;
    private int drillHandicap;
    private int drillType;

    public GenericShortGameDrill() {
    }

    public GenericShortGameDrill (int id, String dplayed, int score, int card, String notes, int inH, int in3, int in6, int out, String grade, int cap, int dtype) {
        setId(id);
        setDate_played(dplayed);
        setTotalScore(score);
        setCardId(card);
        setNotes(notes);
        setNumberInHole(inH);
        setNumberInside3Ft(in3);
        setNumberInside6Ft(in6);
        setNumberOutside(out);
        setGrade(grade);
        setDrillHandicap(cap);
        setDrillType(dtype);
    }

    /*
     * This constructor will build the object straight from a database cursor.
     * Presumably that cursor is the result of a SELECT Query with an ID WHERE clause
     */

    public GenericShortGameDrill(Cursor c) throws Exception {
        if (c != null) {
            c.moveToFirst();

            this.setId(c.getInt(c.getColumnIndex(ShortGameDrillEntry._ID)));
            this.setDate_played(c.getString(c.getColumnIndex(ShortGameDrillEntry.COLUMN_DATE)));
            this.setTotalScore(c.getInt(c.getColumnIndex(ShortGameDrillEntry.COLUMN_TOTAL_SCORE)));
            this.setCardId(c.getInt(c.getColumnIndex(ShortGameDrillEntry.COLUMN_CARD_ID)));
            this.setNotes(c.getString(c.getColumnIndex(ShortGameDrillEntry.COLUMN_NOTES)));
            this.setNumberInHole(c.getInt(c.getColumnIndex(ShortGameDrillEntry.COLUMN_NUM_IN_HOLE)));
            this.setNumberInside3Ft(c.getInt(c.getColumnIndex(ShortGameDrillEntry.COLUMN_NUM_IN_3FT)));
            this.setNumberInside6Ft(c.getInt(c.getColumnIndex(ShortGameDrillEntry.COLUMN_NUM_IN_6FT)));
            this.setNumberOutside(c.getInt(c.getColumnIndex(ShortGameDrillEntry.COLUMN_NUM_OUTSIDE)));
            this.setGrade(c.getString(c.getColumnIndex(ShortGameDrillEntry.COLUMN_GRADE)));
            this.setDrillType(c.getInt(c.getColumnIndex(ShortGameDrillEntry.COLUMN_DRILL_TYPE)));
            this.setDrillHandicap(c.getInt(c.getColumnIndex(ShortGameDrillEntry.COLUMN_HANDICAP)));
        } else {
            throw new Exception("Could not instantiate a GENERIC Short Game Drill from cursor");
        }
    }

    /*
     * Attempts to use the DB passed as parameter and save the object to that database.
     */

    public long saveToDB(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(ShortGameDrillEntry.COLUMN_DATE, this.getDate_played());
        values.put(ShortGameDrillEntry.COLUMN_TOTAL_SCORE, this.getTotalScore());
        values.put(ShortGameDrillEntry.COLUMN_CARD_ID, this.getCardId());
        values.put(ShortGameDrillEntry.COLUMN_NOTES, this.getNotes());
        values.put(ShortGameDrillEntry.COLUMN_NUM_IN_3FT, this.getNumberInside3Ft());
        values.put(ShortGameDrillEntry.COLUMN_NUM_IN_6FT, this.getNumberInside6Ft());
        values.put(ShortGameDrillEntry.COLUMN_NUM_IN_HOLE, this.getNumberInHole());
        values.put(ShortGameDrillEntry.COLUMN_NUM_OUTSIDE, this.getNumberOutside());
        values.put(ShortGameDrillEntry.COLUMN_GRADE, this.getGrade());
        values.put(ShortGameDrillEntry.COLUMN_HANDICAP, this.getDrillHandicap());
        values.put(ShortGameDrillEntry.COLUMN_DRILL_TYPE, this.getDrillType());

        return db.insert(ShortGameDrillEntry.TABLE_NAME, null, values);
    }

    /*
     * Updates a this object to the DB passed in param
     */

    public int updateToDB(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(ShortGameDrillEntry.COLUMN_DATE, this.getDate_played());
        values.put(ShortGameDrillEntry.COLUMN_TOTAL_SCORE, this.getTotalScore());
        values.put(ShortGameDrillEntry.COLUMN_CARD_ID, this.getCardId());
        values.put(ShortGameDrillEntry.COLUMN_NOTES, this.getNotes());
        values.put(ShortGameDrillEntry.COLUMN_NUM_IN_3FT, this.getNumberInside3Ft());
        values.put(ShortGameDrillEntry.COLUMN_NUM_IN_6FT, this.getNumberInside6Ft());
        values.put(ShortGameDrillEntry.COLUMN_NUM_IN_HOLE, this.getNumberInHole());
        values.put(ShortGameDrillEntry.COLUMN_NUM_OUTSIDE, this.getNumberOutside());
        values.put(ShortGameDrillEntry.COLUMN_GRADE, this.getGrade());
        values.put(ShortGameDrillEntry.COLUMN_HANDICAP, this.getDrillHandicap());
        values.put(ShortGameDrillEntry.COLUMN_DRILL_TYPE, this.getDrillType());

        // updating row
        return db.update(ShortGameDrillEntry.TABLE_NAME, values, ShortGameDrillEntry._ID + " = ?",
                new String[] { String.valueOf(this.getId()) });
    }

    public int getNumberInHole() {
        return numberInHole;
    }

    public void setNumberInHole(int numberInHole) {
        this.numberInHole = numberInHole;
    }

    public int getNumberInside3Ft() {
        return numberInside3Ft;
    }

    public void setNumberInside3Ft(int numberInside3Ft) {
        this.numberInside3Ft = numberInside3Ft;
    }

    public int getNumberInside6Ft() {
        return numberInside6Ft;
    }

    public void setNumberInside6Ft(int numberInside6Ft) {
        this.numberInside6Ft = numberInside6Ft;
    }

    public int getNumberOutside() {
        return numberOutside;
    }

    public void setNumberOutside(int numberOutside) {
        this.numberOutside = numberOutside;
    }

    public int getDrillType() {
        return drillType;
    }

    public void setDrillType(int drillType) {
        this.drillType = drillType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate_played() {
        return date_played;
    }

    public void setDate_played(String date_played) {
        this.date_played = date_played;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public int getDrillHandicap() {
        return drillHandicap;
    }

    public void setDrillHandicap(int drillHandicap) {
        this.drillHandicap = drillHandicap;
    }

}
