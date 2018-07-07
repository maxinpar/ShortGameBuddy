package boucoiran.fr.shortgamebuddy.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import boucoiran.fr.shortgamebuddy.models.GenericPuttingDrill;
import boucoiran.fr.shortgamebuddy.models.GenericShortGameDrill;
import boucoiran.fr.shortgamebuddy.models.GolfPracticeContract.PuttingCardEntry;
import boucoiran.fr.shortgamebuddy.models.GolfPracticeContract.PuttingDrillEntry;
import boucoiran.fr.shortgamebuddy.models.GolfPracticeContract.ShortGameCardEntry;
import boucoiran.fr.shortgamebuddy.models.GolfPracticeContract.ShortGameDrillEntry;
import boucoiran.fr.shortgamebuddy.models.PuttingCard;
import boucoiran.fr.shortgamebuddy.models.ShortGameCard;

public class GolfPracticeDBHelper extends SQLiteOpenHelper {

    private final static int DATABASE_VERSION = 8;
    private final static String DATABASE_NAME = "GolfPractice.db";
    private final static String TEXT_TYPE = " Text";
    private final static String TAG = "GolfPracticeDBHelper";

    /*
     * Below are constants for Short Game Drill ID's to be used when linking Short Game Cards to
     * Short Game Drills
     */
    public final static int P_CARD_ID = 0;
    public final static int P_3FT_PUTT_DRILL_ID = 1;
    public final static int P_6FT_PUTT_DRILL_ID = 2;
    public final static int P_MAKEABLE_DRILL_ID = 3;
    public final static int P_MEDIUM_DRILL_ID = 4;
    public final static int P_LAG_DRILL_ID = 5;
    public final static int P_BIG_BREAK_DRILL_ID = 6;

    /*
     * Below are constants for Short Game Drill ID's to be used when linking Short Game Cards to
     * Short Game Drills
     */
    private final static int SC_CARD_ID = 0;
    public final static int SC_SHORT_CHIP_DRILL_ID = 1;
    public final static int SC_LONG_CHIP_DRILL_ID = 2;
    public final static int SC_SHORT_SAND_DRILL_ID = 3;
    public final static int SC_LONG_SAND_DRILL_ID = 4;
    public final static int SC_SHORT_PITCH_DRILL_ID = 5;
    public final static int SC_MEDIUM_PITCH_DRILL_ID = 6;
    public final static int SC_LONG_PITCH_DRILL_ID = 7;
    public final static int SC_ROUGH_CHIP_DRILL_ID = 8;
    public final static int SC_LOB_SHOT_DRILL_ID = 9;



    private String[] drillNames = {"", "Short Chip Drill", "Long Chip Drill", "Short Sand Drill", "Long Sand Drill", "Short Pitch Drill", "Medium Pitch Drill", "Long Pitch Drill", "Chip from Rough", "Lob Shot Drill"};

    /**********************************************************************************************
     * CREATE TABLE queries are below.                                                            *
     * They use constants from table and column names from the GolfPracticeContract class          *
     **********************************************************************************************/


    private final static String SQL_CREATE_SHORT_GAME_DRILL_TABLE = "CREATE TABLE " + ShortGameDrillEntry.TABLE_NAME + " ("
            + ShortGameDrillEntry._ID + " INTEGER PRIMARY KEY, "
            + ShortGameDrillEntry.COLUMN_DATE + TEXT_TYPE + ", "
            + ShortGameDrillEntry.COLUMN_TOTAL_SCORE + " INTEGER, "
            + ShortGameDrillEntry.COLUMN_CARD_ID + "  INTEGER, "
            + ShortGameDrillEntry.COLUMN_NOTES + TEXT_TYPE + ", "
            + ShortGameDrillEntry.COLUMN_NUM_IN_HOLE + "  INTEGER, "
            + ShortGameDrillEntry.COLUMN_NUM_IN_3FT + "  INTEGER, "
            + ShortGameDrillEntry.COLUMN_NUM_IN_6FT + "  INTEGER, "
            + ShortGameDrillEntry.COLUMN_NUM_OUTSIDE + "  INTEGER, "
            + ShortGameDrillEntry.COLUMN_DRILL_TYPE + "  INTEGER, "
            + ShortGameDrillEntry.COLUMN_GRADE + TEXT_TYPE + ", "
            + ShortGameDrillEntry.COLUMN_HANDICAP + "  INTEGER" + ")";

    private final static String SQL_CREATE_SHORT_GAME_CARD_TABLE = "CREATE TABLE " + ShortGameCardEntry.TABLE_NAME + " ("
            + ShortGameCardEntry._ID + " INTEGER PRIMARY KEY, "
            + ShortGameCardEntry.COLUMN_IS_COMPLETE + " INTEGER, "
            + ShortGameCardEntry.COLUMN_LAST_UPDATE + TEXT_TYPE + ", "
            + ShortGameCardEntry.COLUMN_TOTAL_SCORE + "  INTEGER, "
            + ShortGameCardEntry.COLUMN_GRADE + TEXT_TYPE + ", "
            + ShortGameCardEntry.COLUMN_HANDICAP + "  INTEGER" + ")";

    private final static String SQL_CREATE_PUTTING_DRILL_TABLE = "CREATE TABLE " + PuttingDrillEntry.TABLE_NAME + " ("
            + PuttingDrillEntry._ID + " INTEGER PRIMARY KEY, "
            + PuttingDrillEntry.COLUMN_DATE + TEXT_TYPE + ", "
            + PuttingDrillEntry.COLUMN_TOTAL_SCORE + " INTEGER, "
            + PuttingDrillEntry.COLUMN_CARD_ID + "  INTEGER, "
            + PuttingDrillEntry.COLUMN_NOTES + TEXT_TYPE + ", "
            + PuttingDrillEntry.COLUMN_NUM_IN_HOLE + "  INTEGER, "
            + PuttingDrillEntry.COLUMN_NUM_IN_3FT + "  INTEGER, "
            + PuttingDrillEntry.COLUMN_NUM_IN_6FT + "  INTEGER, "
            + PuttingDrillEntry.COLUMN_NUM_IN_ZONE + "  INTEGER, "
            + PuttingDrillEntry.COLUMN_NUM_OUTSIDE + "  INTEGER, "
            + PuttingDrillEntry.COLUMN_DRILL_TYPE + "  INTEGER, "
            + PuttingDrillEntry.COLUMN_GRADE + TEXT_TYPE + ", "
            + PuttingDrillEntry.COLUMN_HANDICAP + "  INTEGER" + ")";

    private final static String SQL_CREATE_PUTTING_CARD_TABLE = "CREATE TABLE " + PuttingCardEntry.TABLE_NAME + " ("
            + PuttingCardEntry._ID + " INTEGER PRIMARY KEY, "
            + PuttingCardEntry.COLUMN_IS_COMPLETE + " INTEGER, "
            + PuttingCardEntry.COLUMN_LAST_UPDATE + TEXT_TYPE + ", "
            + PuttingCardEntry.COLUMN_TOTAL_SCORE + "  INTEGER, "
            + PuttingCardEntry.COLUMN_GRADE + TEXT_TYPE + ", "
            + PuttingCardEntry.COLUMN_HANDICAP + "  INTEGER" + ")";

    /**********************************************************************************************
     * DROP TABLE queries are below.                                                              *
     * They use constants fro table and column names from the GolfPracticeContract class          *
     **********************************************************************************************/

    private static final String SQL_DELETE_SHORT_GAME_DRILL_ENTRY= "DROP TABLE IF EXISTS " + ShortGameDrillEntry.TABLE_NAME;
    private static final String SQL_DELETE_SHORT_GAME_CARD_ENTRY= "DROP TABLE IF EXISTS " + ShortGameCardEntry.TABLE_NAME;
    private static final String SQL_DELETE_PUTTING_DRILL_ENTRY= "DROP TABLE IF EXISTS " + PuttingDrillEntry.TABLE_NAME;
    private static final String SQL_DELETE_PUTTING_CARD_ENTRY= "DROP TABLE IF EXISTS " + PuttingCardEntry.TABLE_NAME;


    public GolfPracticeDBHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        /* #TODO update this to actually update and not replace */

        db.execSQL(SQL_DELETE_SHORT_GAME_DRILL_ENTRY);
        db.execSQL(SQL_DELETE_SHORT_GAME_CARD_ENTRY);
        db.execSQL(SQL_DELETE_PUTTING_DRILL_ENTRY);
        db.execSQL(SQL_DELETE_PUTTING_CARD_ENTRY);

        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_SHORT_GAME_DRILL_TABLE);
        db.execSQL(SQL_CREATE_SHORT_GAME_CARD_TABLE);
        db.execSQL(SQL_CREATE_PUTTING_CARD_TABLE);
        db.execSQL(SQL_CREATE_PUTTING_DRILL_TABLE);
    }

    /***********************************************************************************************
     * Below are the 4 main DB helper methods for the Short Game Drill Object:                     *
     *  - Read from DB and instantiate java object                                                 *
     *  - Create DB entry from object                                                              *
     *  - Update DB entry from object                                                              *
     *  - Delete Object from DB                                                                    *
     **********************************************************************************************/

    /*
     * Get single Short Game Drill
     * This will attempt to retrieve data from table SHORT GAME DRILL TABLE and create and
     * instantiate the corresponding object.
     *
     * @parameter: short_game_drill_id
     *      The id of the Drill object we want to retrieve
     */
    public GenericShortGameDrill getShortGameDrill(long short_game_drill_id) throws Exception {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + ShortGameDrillEntry.TABLE_NAME + " WHERE "
                + ShortGameDrillEntry._ID + " = " + short_game_drill_id;
        Cursor c = db.rawQuery(selectQuery, null);
        return new GenericShortGameDrill(c);
    }

    /*
     * Attempts to create a DB entry into SHORT GAME DRILL TABLE from the corresponding object
     * passed as param.
     *
     * Actually just built a save method into this generic object..
     *
     * Note: this method also checks to see if by updating this drill we need to update the card's
     * total score and handicap (if the card already had 9 drills attached)
     */

    public long createShortGameDrill(GenericShortGameDrill gd) {
        long sgdID;
        SQLiteDatabase db = this.getWritableDatabase();
        sgdID = gd.saveToDB(db);
        updateCardCompleteness(gd.getCardId());
        return sgdID;
    }

    /*
     * Update a Generic Short Game Drill.
     * I ended up just building the update method in the java object.
     *
     * Note: this method also checks to see if by updating this drill we need to update the card's
     * total score and handicap (if the card already had 9 drills attached)
     */

    public int updateShortGameDrill(GenericShortGameDrill gd) {
        int toRet;
        SQLiteDatabase db = this.getWritableDatabase();
        toRet = gd.updateToDB(db);
        updateCardCompleteness(gd.getCardId());
        return toRet;
    }

    public int deleteShortGameDrill(long shortGameDrillId) throws Exception {
        SQLiteDatabase db = this.getWritableDatabase();
        int cId = getShortGameDrill(shortGameDrillId).getCardId();
        int rowsDeleted = db.delete(ShortGameDrillEntry.TABLE_NAME, ShortGameDrillEntry._ID + " = ?",new String[] {String.valueOf(shortGameDrillId)});
        updateCardCompleteness(cId);
        return rowsDeleted;
    }

    /*
     * This will delete all drills from DRILL table where the associated card id is sgcId
     * This method is called when deleting a SG card
     *
     * @parameter: sgcId
     *      The id of the SG Card object associated to the drills we want to delte
     */

    private void deleteDrillsFromCard(long sgcId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(ShortGameDrillEntry.TABLE_NAME, ShortGameDrillEntry.COLUMN_CARD_ID + " = ?",new String[] {String.valueOf(sgcId)});
        updateCardCompleteness((int)sgcId);
        Log.i(TAG, "We have just deleted " + rowsDeleted + " drills associated to card (" + sgcId+") from the Drills table");
    }


    /***********************************************************************************************
     * Below are the 4 main DB helper methods for the Putting Drill Object:                     *
     *  - Read from DB and instantiate java object                                                 *
     *  - Create DB entry from object                                                              *
     *  - Update DB entry from object                                                              *
     *  - Delete Object from DB
     *
     *  also useful:
     *  - get a putting drill based on drill ID and card id.
     *
     **********************************************************************************************/

    public GenericPuttingDrill getPuttingDrill(long drillId) {
        return PDHelper.getDrill(drillId, this.getWritableDatabase());
    }

    public long createPuttingDrill(GenericPuttingDrill pd) {
        return PDHelper.createDrill(pd, this.getWritableDatabase());
    }

    public int updatePuttingDrill(GenericPuttingDrill pd) {
        return PDHelper.updateDrill(pd, this.getWritableDatabase());
    }

    public int deletePuttingDrill(long drillId) {
        return PDHelper.deleteDrill(drillId, this.getWritableDatabase());
    }

    public GenericPuttingDrill getPuttingDrill(int cardId, int drillId) throws Exception {
        return PDHelper.getDrill(cardId, drillId, this.getWritableDatabase());
    }

    /***********************************************************************************************
     * Below are the 4 main DB helper methods for the Putting Card Object:                     *
     *  - Read from DB and instantiate java object                                                 *
     *  - Create DB entry from object                                                              *
     *  - Update DB entry from object                                                              *
     *  - Delete Object from DB                                                                    *
     **********************************************************************************************/

    public PuttingCard getPuttingCard(long cId) throws Exception {
        return PDHelper.getCard(cId, this.getWritableDatabase());
    }

    public long createPuttingCard(PuttingCard pc) {
        return PDHelper.createCard(pc, this.getWritableDatabase());
    }

    public int updatePuttingDrill(PuttingCard pc) {
        return PDHelper.updateCard(pc, this.getWritableDatabase());
    }

    public int deletePuttingCard(long cId) {
        return PDHelper.deleteCard(cId, this.getWritableDatabase());
    }

    /***********************************************************************************************
     * Below are the 3 main DB helper methods for the <b>Short Game Card Object</b>:                      *
     *  - Read from DB and instantiate java object                                                 *
     *  - Create DB entry from object                                                              *
     *  - Update DB entry from object                                                              *
     **********************************************************************************************/

    /*
     * Get single Short Game Card from DB and instantiate object
     * This will attempt to retrieve data from table SHORT GAME CARD TABLE and create and
     * instantiate the corresponding object.
     *
     * @parameter: short_game_card_id
     *      The id of the SG Card object we want to retrieve
     */
    public ShortGameCard getShortGameCard(long short_game_card_id) throws Exception {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + ShortGameCardEntry.TABLE_NAME + " WHERE "
                + ShortGameCardEntry._ID + " = " + short_game_card_id;

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null) {
            c.moveToFirst();

            ShortGameCard sgc = new ShortGameCard();
            sgc.setId(c.getInt(c.getColumnIndex(ShortGameCardEntry._ID)));
            sgc.setIsComplete(c.getInt(c.getColumnIndex(ShortGameCardEntry.COLUMN_IS_COMPLETE)));
            sgc.setTotalScore(c.getInt(c.getColumnIndex(ShortGameCardEntry.COLUMN_TOTAL_SCORE)));
            sgc.setLastUpdated(c.getString(c.getColumnIndex(ShortGameCardEntry.COLUMN_LAST_UPDATE)));
            sgc.setGrade(c.getString(c.getColumnIndex(ShortGameCardEntry.COLUMN_GRADE)));
            sgc.setHandicap(c.getInt(c.getColumnIndex(ShortGameCardEntry.COLUMN_HANDICAP)));

            c.close();
            return sgc;
        } else {
            throw new Exception("Could not retrieve Short Game Card from database of SGC ID "+ short_game_card_id);
        }
    }

    /*
     * Attempts to create a DB entry into SHORT GAME CARD from the corresponding object
     * passed as param
     *
     */
    public long createShortGameCard(ShortGameCard sgc) {
        long newRowId = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ShortGameCardEntry.COLUMN_IS_COMPLETE, sgc.getIsComplete());
        values.put(ShortGameCardEntry.COLUMN_TOTAL_SCORE, sgc.getTotalScore());
        values.put(ShortGameCardEntry.COLUMN_LAST_UPDATE, sgc.getLastUpdated());
        values.put(ShortGameCardEntry.COLUMN_GRADE, sgc.getGrade());
        values.put(ShortGameCardEntry.COLUMN_HANDICAP, sgc.getHandicap());

        newRowId = db.insert(ShortGameCardEntry.TABLE_NAME, null, values);

        Log.v("DB Helper Class", "just created a new Short Game Card!!");
        return newRowId;
    }

    /*
     * Attempts to Update a Short Game Card object
     */

    private int updateShortGameCard(ShortGameCard sgc) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ShortGameCardEntry.COLUMN_IS_COMPLETE, sgc.getIsComplete());
        values.put(ShortGameCardEntry.COLUMN_TOTAL_SCORE, sgc.getTotalScore());
        values.put(ShortGameCardEntry.COLUMN_LAST_UPDATE, sgc.getLastUpdated());
        values.put(ShortGameCardEntry.COLUMN_GRADE, sgc.getGrade());
        values.put(ShortGameCardEntry.COLUMN_HANDICAP, sgc.getHandicap());

        // updating row
        return db.update(ShortGameCardEntry.TABLE_NAME, values, ShortGameCardEntry._ID + " = ?",
                new String[] { String.valueOf(sgc.getId()) });
    }


    /*
     * Delete a Short Game Card
     * Note that this will delete all drills associated with it too.
     *
     * @parameter: card_id
     *      The id of the SG Card we want to count the drills for
     */
    public int deleteShortGameCard(long shortGameCardId) {
        SQLiteDatabase db = this.getWritableDatabase();
        //First remove any drills attached to this card
        deleteDrillsFromCard(shortGameCardId);

        //Next delete the card itself
        Log.i(TAG, "Attempting to delete Short Game Card");
        int rowsDeleted = db.delete(ShortGameCardEntry.TABLE_NAME, ShortGameCardEntry._ID + " = ?",new String[] {String.valueOf(shortGameCardId)});
        Log.i(TAG, rowsDeleted + "rows deleted from SG Card table");
        Log.i(TAG, "Short Game  Card of ID " + shortGameCardId + " has been deleted from database");
        return rowsDeleted;
    }

    /*
     * Count the number of drills a Card has
     *
     * @parameter: card_id
     *      The id of the SG Card we want to count the drills for
     */
    public int countDrills(long card_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;

        String selectQuery = "SELECT COUNT(*) FROM " + ShortGameDrillEntry.TABLE_NAME + " WHERE "
                + ShortGameDrillEntry.COLUMN_CARD_ID + " = " + card_id;

        try{
            c = db.rawQuery(selectQuery, null);

            if (c == null) return 0;
            c.moveToFirst();
            return c.getInt(0);
        } catch (Exception e) {
            Log.e(TAG, "Tried getting countDrills for drill ID " + card_id+ " and failed...");
            Log.e(TAG, e.getMessage());
            return 0;
        }
    }

    /*
     * This will update a card's TotalScore, Handicap and isComplete attributes if it has been
     * completed
     */

    private void updateCardCompleteness(int cardID) {
        try {
            ShortGameCard sgc = getShortGameCard(cardID);
            sgc.setTotalScore(getTotalCardScore(cardID));

            if (countDrills(cardID) == 9) {
                Log.d(TAG, "Hey we got 9 drills!!");
                //Card is now complete
                //let's mark is as complete and update the score
                sgc.setIsComplete(1);
                sgc.setHandicap(getCardHCap(sgc.getTotalScore()));
            } else {
                //if not then mark card as not complete (could have just deleted a drill)
                sgc.setIsComplete(0);
                sgc.setHandicap(99);
                updateShortGameCard(sgc);
            }
            //update the card
            updateShortGameCard(sgc);

        } catch (Exception e) {
            Log.d(TAG, "Tried to see if card was completed. Failed.");
        }

    }

    public String[] getPuttingDrillScoresFromCard(long cardId) {
        return PDHelper.getDrillScoresFromCard(cardId, this.getReadableDatabase());
    }

    /*
     * This method will return an array of Strings representing the scores from all 9 drills
     * given a Short Game card. We use this for badges.
     *
     * If a Short Game drill has not been initiated, we will return '-1'
     */

    public String[] getSGDrillScoresFromCard(long cardId) {
        String[] toRet = new String[10];
        for(int i=0; i<10; i++) {
            try {
                toRet[i] = String.valueOf(getSGDrillScoreFromSGCard(cardId, i));

            } catch (Exception e) {
                toRet[i]="-1";
            }
            Log.i(TAG, "badge["+i+"] = "+toRet[i]);
        }
        return toRet;
    }

    /*
     * Will return a SG Drill score based on cardID and Drill Type. Will return -1 if drill not
     * done
     */

    public long getSGDrillScoreFromSGCard(long cardId, int drillType) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT  * FROM " + ShortGameDrillEntry.TABLE_NAME + " WHERE ("
                + ShortGameDrillEntry.COLUMN_DRILL_TYPE + " = " + drillType
                + ") AND (" + ShortGameDrillEntry.COLUMN_CARD_ID + " = " + cardId +")";
        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null) {
            if (c.getCount() > 0) {
                c.moveToFirst();
                return c.getLong(c.getColumnIndex(ShortGameDrillEntry.COLUMN_TOTAL_SCORE));
            }
        }
        return -1;
    }



    /*
     * 14/6/2018: new generic way of doing things!
     *
     * here we are gong to try and get a Drill of type TYPE associated with card CARD_ID and send
     * back
     */


    public GenericShortGameDrill getShortGameDrill(int card_id, int drill_type) throws Exception{
        GenericShortGameDrill gd;
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT  * FROM " + ShortGameDrillEntry.TABLE_NAME + " WHERE ("
                + ShortGameDrillEntry.COLUMN_DRILL_TYPE + " = " + drill_type
                + ") AND (" + ShortGameDrillEntry.COLUMN_CARD_ID + " = " + card_id +")";

        Log.i(TAG, "in getShortGameDrill here is the query we are going to run: "+selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        gd = new GenericShortGameDrill(c);
        return gd;
    }



    /*
     * This method gives back a % score for a drill score. It is used to determine what gradient
     * image to display in the SCore Card Activity
     *
     * It works by dividing the score by the top score, multiplying by 100 and then using modulo
     * to round to the nearest (upper) 5.
     */

    private String getClosestPC(int score, int drillId) {
        int ret = 0;
        int maxScore = 1;
        switch (drillId) {
            case SC_CARD_ID: maxScore = 155; break;
            case SC_SHORT_CHIP_DRILL_ID: maxScore = 24; break;
            case SC_LONG_CHIP_DRILL_ID: maxScore = 20; break;
            case SC_SHORT_SAND_DRILL_ID: maxScore = 12; break;
            case SC_LONG_SAND_DRILL_ID: maxScore = 16; break;
            case SC_SHORT_PITCH_DRILL_ID: maxScore = 19; break;
            case SC_MEDIUM_PITCH_DRILL_ID: maxScore = 16; break;
            case SC_LONG_PITCH_DRILL_ID: maxScore = 17; break;
            case SC_LOB_SHOT_DRILL_ID: maxScore = 14; break;
            case SC_ROUGH_CHIP_DRILL_ID: maxScore = 17; break;
        }
        Log.i(TAG, "getting maxscore for drill " + drillId + " = "+maxScore);

        ret = Math.round(100* score / maxScore);
        ret += (5-(ret%5));
        if (ret > 100) return "100";
        if (ret == 5) return "05";
        return String.valueOf(ret);
    }

    /*
     * Method to get all Putting card data into a String[][] format for displaying in Scorecard
     */

    public String[][] getPuttScoreCardData(long card_id) {
        return PDHelper.getScoreCardData(card_id, this.getWritableDatabase());
    }
    /*
     * Method to get all SG card data into a String[][] format for displaying in Scorecard
     */

    public String [][] getSGScoreCardData(long card_id) {
        int drillsCompleted = 0;
        int totalScore = 0;
        String[][] sgCardScorecardData = new String [10][3];

        for (int i = SC_SHORT_CHIP_DRILL_ID; i <= SC_LOB_SHOT_DRILL_ID; i++) {

            String[] sgSCScoreCardData = new String[3];
            try {
                GenericShortGameDrill scd = getShortGameDrill((int)card_id, i);
                sgSCScoreCardData[0] = drillNames[i] + " (" + String.valueOf(scd.getTotalScore()) + ")";
                sgSCScoreCardData[1] = String.valueOf(scd.getDrillHandicap());
                sgSCScoreCardData[2] = "sb" + getClosestPC(scd.getTotalScore(), i);
                drillsCompleted++;
                totalScore += scd.getTotalScore();
            } catch (Exception e) {
                sgSCScoreCardData = new String[]{drillNames[i]+": No - Data", "-", "sb00"};
            }

            //set at previous index as drills ID start at 1 and string array starts at 0
            sgCardScorecardData[i-1] = sgSCScoreCardData;
        }

        //finally,lets try and load the data for the entire card

        String[] sgSCScoreCardData = new String[3];
        if (drillsCompleted == 9) {
            //If all drills have been completed then the card is completed and we can also get a
            // full card score.
            try {
                ShortGameCard c = getShortGameCard(card_id);
                c.setTotalScore(totalScore);
                c.setIsComplete(1);
                //TODO: Should we save the card here? Or should the onus of saving the card be
                //TODO: after each drill complete event?
                sgSCScoreCardData[0] = "Overall Score: " + totalScore;
                sgSCScoreCardData[1] = String.valueOf(getCardHCap(totalScore));
                sgSCScoreCardData[2] = "sb" + getClosestPC(totalScore, SC_CARD_ID);
            } catch (Exception e) {
                Log.e(TAG, "failed to load card when trying to display Short Game score card");
                sgSCScoreCardData = new String[]{"Overall Short Game", "-", "sb00"};
            }
        } else {
            sgSCScoreCardData = new String[]{"Overall Short Game", "-", "sb00"};
        }
        sgCardScorecardData[9] = sgSCScoreCardData;
        return sgCardScorecardData;
    }

    /*
     * if card has 9 drills: return sum score of all 9 drills tied to card
     */

    private int getTotalCardScore(int cardId) {
        int totalScore = 0;

        String testQuery = "SELECT "+ShortGameDrillEntry.COLUMN_TOTAL_SCORE+", " +ShortGameDrillEntry.COLUMN_DRILL_TYPE + " FROM "
                +ShortGameDrillEntry.TABLE_NAME + " WHERE "
                +ShortGameDrillEntry.COLUMN_CARD_ID + " = " + cardId;

        SQLiteDatabase db1 = getReadableDatabase();
        Cursor c1 = db1.rawQuery(testQuery, null);
        Log.d(TAG, "DEBUG QUERY: " + testQuery);

        while (c1.moveToNext()) {
            try {
                Log.i(TAG, "SCORE: " + c1.getInt(0)+ " | Drill ID: "+c1.getInt(1));
            }
            catch (Exception e) {
                Log.e(TAG, "Error trying to get totalScore for a card:" + e.toString());
            }
        }

            String totScoreQuery = "SELECT SUM("+ShortGameDrillEntry.COLUMN_TOTAL_SCORE+") FROM "
                +ShortGameDrillEntry.TABLE_NAME + " WHERE "
                +ShortGameDrillEntry.COLUMN_CARD_ID + " = " + cardId;

        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(totScoreQuery, null);
        while (c.moveToNext()) {
            try {
                Log.i(TAG, "just ran Query:" + totScoreQuery);
                totalScore = c.getInt(0);
                Log.i(TAG, "result is" + totalScore);
            }
            catch (Exception e) {
                Log.e(TAG, "Error trying to get totalScore for a card:" + e.toString());
            }
        }
        return totalScore;
    }

    //This method needs to be somewhere else.
    // Maybe in SGC?
    //dunno
    public ArrayList<ShortGameCard> getCardsFromDB() {
        ArrayList<ShortGameCard> toRet = new ArrayList<ShortGameCard>();

        SQLiteDatabase db = getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + ShortGameCardEntry.TABLE_NAME;
        Cursor c = db.rawQuery(selectQuery, null);
        while (c.moveToNext()) {
            try {
                ShortGameCard sgc = getShortGameCard(c.getInt(c.getColumnIndex(ShortGameCardEntry._ID)));
                toRet.add(sgc);
            }
            catch (Exception e) {
                Log.e(TAG, "Error " + e.toString());
            }
        }
        return toRet;
    }

    /*
     * This method will create am ArrayList of Putting Cards from the database.
     */

    public ArrayList<PuttingCard> getPuttingCardsFromDB() {
        return PDHelper.getCardsFromDB(this.getWritableDatabase());
    }

    /*
     * Lookup card h'cap based on score
     */

    private int getCardHCap (int score) {
        if (score > 155) return -8;

        switch (score) {
            case 0: return 39;
            case 1: return 39;
            case 2: return 38;
            case 3: return 37;
            case 4: return 37;
            case 5: return 37;
            case 6: return 37;
            case 7: return 37;
            case 8: return 36;
            case 9: return 35;
            case 10: return 35;
            case 11: return 35;
            case 12: return 34;
            case 13: return 34;
            case 14: return 33;
            case 15: return 33;
            case 16: return 32;
            case 17: return 32;
            case 18: return 32;
            case 19: return 31;
            case 20: return 31;
            case 21: return 31;
            case 22: return 30;
            case 23: return 29;
            case 24: return 29;
            case 25: return 29;
            case 26: return 29;
            case 27: return 28;
            case 28: return 28;
            case 29: return 27;
            case 30: return 27;
            case 31: return 27;
            case 32: return 27;
            case 33: return 26;
            case 34: return 25;
            case 35: return 25;
            case 36: return 25;
            case 37: return 24;
            case 38: return 24;
            case 39: return 24;
            case 40: return 24;
            case 41: return 23;
            case 42: return 23;
            case 43: return 22;
            case 44: return 22;
            case 45: return 22;
            case 46: return 21;
            case 47: return 21;
            case 48: return 21;
            case 49: return 20;
            case 50: return 20;
            case 51: return 20;
            case 52: return 19;
            case 53: return 19;
            case 54: return 19;
            case 55: return 19;
            case 56: return 18;
            case 57: return 18;
            case 58: return 18;
            case 59: return 17;
            case 60: return 17;
            case 61: return 16;
            case 62: return 16;
            case 63: return 16;
            case 64: return 16;
            case 65: return 16;
            case 66: return 15;
            case 67: return 15;
            case 68: return 14;
            case 69: return 14;
            case 70: return 14;
            case 71: return 13;
            case 72: return 13;
            case 73: return 13;
            case 74: return 12;
            case 75: return 12;
            case 76: return 12;
            case 77: return 12;
            case 78: return 11;
            case 79: return 11;
            case 80: return 10;
            case 81: return 10;
            case 82: return 10;
            case 83: return 10;
            case 84: return 9;
            case 85: return 9;
            case 86: return 9;
            case 87: return 9;
            case 88: return 8;
            case 89: return 8;
            case 90: return 7;
            case 91: return 7;
            case 92: return 7;
            case 93: return 7;
            case 94: return 6;
            case 95: return 6;
            case 96: return 6;
            case 97: return 6;
            case 98: return 6;
            case 99: return 5;
            case 100: return 5;
            case 101: return 4;
            case 102: return 4;
            case 103: return 4;
            case 104: return 4;
            case 105: return 4;
            case 106: return 3;
            case 107: return 3;
            case 108: return 3;
            case 109: return 3;
            case 110: return 2;
            case 111: return 2;
            case 112: return 2;
            case 113: return 1;
            case 114: return 1;
            case 115: return 1;
            case 116: return 1;
            case 117: return 0;
            case 118: return 0;
            case 119: return 0;
            case 120: return 0;
            case 121: return -1;
            case 122: return -1;
            case 123: return -1;
            case 124: return -1;
            case 125: return -2;
            case 126: return -2;
            case 127: return -3;
            case 128: return -3;
            case 129: return -3;
            case 130: return -3;
            case 131: return -3;
            case 132: return -3;
            case 133: return -3;
            case 134: return -4;
            case 135: return -4;
            case 136: return -4;
            case 137: return -4;
            case 138: return -5;
            case 139: return -5;
            case 140: return -5;
            case 141: return -5;
            case 142: return -5;
            case 143: return -6;
            case 144: return -6;
            case 145: return -6;
            case 146: return -6;
            case 147: return -6;
            case 148: return -7;
            case 149: return -7;
            case 150: return -7;
            case 151: return -7;
            case 152: return -7;
            case 153: return -7;
            case 154: return -7;
            case 155: return -8;
        }
        Log.e("SG Card Hcap Lookup", "Can't find hcap for value: "+score);
        return 40;
    }
}
