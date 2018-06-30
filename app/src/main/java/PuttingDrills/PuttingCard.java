package PuttingDrills;

/*
 * This is the Java object that is going to represent our full Putting card.
 * It should contain 6 individual drills
 */

import android.database.Cursor;

import data.GolfPracticeContract;

public class PuttingCard {

    private int id;
    private int isComplete;
    private String lastUpdated;
    private int totalScore;
    private String Grade;
    private int handicap;

    public PuttingCard(int isComplete, String lastUpdated, int totalScore, String grade, int handicap) {
        this.isComplete = isComplete;
        this.lastUpdated = lastUpdated;
        this.totalScore = totalScore;
        Grade = grade;
        this.handicap = handicap;
    }

    public PuttingCard(Cursor c) throws Exception {
        if (c != null) {
            c.moveToFirst();

            this.setId(c.getInt(c.getColumnIndex(GolfPracticeContract.PuttingCardEntry._ID)));
            this.setIsComplete(c.getInt(c.getColumnIndex(GolfPracticeContract.PuttingCardEntry.COLUMN_IS_COMPLETE)));
            this.setTotalScore(c.getInt(c.getColumnIndex(GolfPracticeContract.PuttingCardEntry.COLUMN_TOTAL_SCORE)));
            this.setGrade(c.getString(c.getColumnIndex(GolfPracticeContract.PuttingCardEntry.COLUMN_GRADE)));
            this.setLastUpdated(c.getString(c.getColumnIndex(GolfPracticeContract.PuttingCardEntry.COLUMN_LAST_UPDATE)));
            this.setHandicap(c.getInt(c.getColumnIndex(GolfPracticeContract.PuttingCardEntry.COLUMN_HANDICAP)));
        } else {
            throw new Exception("Could not instantiate a GENERIC Putting Card from cursor");
        }
    }

    public PuttingCard() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(int isComplete) {
        this.isComplete = isComplete;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public String getGrade() {
        return Grade;
    }

    public void setGrade(String grade) {
        Grade = grade;
    }

    public int getHandicap() {
        return handicap;
    }

    public void setHandicap(int handicap) {
        this.handicap = handicap;
    }
}
