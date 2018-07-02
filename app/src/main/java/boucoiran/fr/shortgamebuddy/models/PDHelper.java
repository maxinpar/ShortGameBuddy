package boucoiran.fr.shortgamebuddy.models;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import boucoiran.fr.shortgamebuddy.models.GolfPracticeContract.PuttingCardEntry;
import boucoiran.fr.shortgamebuddy.models.GolfPracticeContract.PuttingDrillEntry;

/*
 * 30/6/2018:  Created this class to store all the Putting Drill Create / Read / Update / Delete
 * methods called from GolfPractice DBHelper
 *
 * Most methods here will need to be passed a DB connection of some sort.
 */

public class PDHelper {

    private final static String TAG = "PDHelper";

    /*
     * Get single Putting Drill
     * This will attempt to retrieve data from table PUTTING DRILL TABLE and create and
     * instantiate the corresponding object.
     *
     * @parameter: pid
     *      The id of the Drill object we want to retrieve
     */
    @SuppressLint("Recycle")
    public static GenericPuttingDrill getDrill(long pid, SQLiteDatabase db) {
        String selectQuery = "SELECT  * FROM " + PuttingDrillEntry.TABLE_NAME + " WHERE "
                + PuttingDrillEntry._ID + " = " + pid;
        Cursor c = db.rawQuery(selectQuery, null);
        try {
            return new GenericPuttingDrill(c);
        } catch (Exception e) {
            Log.e(TAG, "Error retrieving Putting Drill " + e.getMessage());
            return null;

        }
    }

    /*
     * Attempts to create a DB entry into PUTTING DRILL TABLE from the corresponding object
     * passed as param.
     *
     * Actually just calls a save method into this generic object..
     *
     * Note: this method also checks to see if by updating this drill we need to update the card's
     * total score and handicap (if the card already had 6 putting drills attached)
     */

    public static long createDrill(GenericPuttingDrill pd, SQLiteDatabase db) {
        long sgdID;
        sgdID = pd.saveToDB(db);
        updateCardCompleteness(pd.getCardId(), db);
        return sgdID;
    }

    /*
     * Update a Generic Putting Drill.
     * I ended up just building the update method in the java object.
     *
     * Note: this method also checks to see if by updating this drill we need to update the card's
     * total score and handicap (if the card already had 6 putting drills attached)
     */

    public static int updateDrill(GenericPuttingDrill pd, SQLiteDatabase db) {
        int toRet = pd.updateToDB(db);
        updateCardCompleteness(pd.getCardId(), db);
        return toRet;
    }

    /*
     * Delete a Generic Putting Drill.
     */

    public static int deleteDrill(long shortGameDrillId, SQLiteDatabase db) {
        int cId;
        GenericPuttingDrill pd = getDrill(shortGameDrillId, db);
        if (pd == null) {
            return 0;
        } else {
            cId = pd.getId();
            int rowsDeleted = db.delete(PuttingDrillEntry.TABLE_NAME, PuttingDrillEntry._ID + " = ?", new String[]{String.valueOf(shortGameDrillId)});
            updateCardCompleteness(cId, db);
            return rowsDeleted;
        }
    }


    /***********************************************************************************************
     * Below are the 4 main DB helper methods for the <b>Putting Card Object</b>:                  *
     *  - Read from DB and instantiate java object                                                 *
     *  - Create DB entry from object                                                              *
     *  - Update DB entry from object                                                              *
     *  - Delete DB Entry                                                                          *
     **********************************************************************************************/

    /*
     * Get single Putting Card from DB and instantiate object
     * This will attempt to retrieve data from table PUTTING CARD TABLE and create and
     * instantiate the corresponding object.
     *
     * @parameter: pcard_id
     *      The id of the Putting Card object we want to retrieve
     */
    public static PuttingCard getCard(long pcardId, SQLiteDatabase db) throws Exception {
        PuttingCard pc;
        String selectQuery = "SELECT  * FROM " + PuttingCardEntry.TABLE_NAME + " WHERE "
                + PuttingCardEntry._ID + " = " + pcardId;

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null) {
            c.moveToFirst();
            pc = new PuttingCard(c);
            c.close();
            return pc;

        } else {
            throw new Exception("Could not retrieve Putting Card from database of SGC ID " + pcardId);
        }
    }

    /*
     * Attempts to create a DB entry into Putting CARD from the corresponding object
     * passed as param
     *
     */
    public static long createCard(PuttingCard pc, SQLiteDatabase db) {
        long newRowId;
        ContentValues values = new ContentValues();

        values.put(PuttingCardEntry.COLUMN_IS_COMPLETE, pc.getIsComplete());
        values.put(PuttingCardEntry.COLUMN_TOTAL_SCORE, pc.getTotalScore());
        values.put(PuttingCardEntry.COLUMN_LAST_UPDATE, pc.getLastUpdated());
        values.put(PuttingCardEntry.COLUMN_GRADE, pc.getGrade());
        values.put(PuttingCardEntry.COLUMN_HANDICAP, pc.getHandicap());

        newRowId = db.insert(PuttingCardEntry.TABLE_NAME, null, values);

        Log.v(TAG, "just created a new putting card");
        return newRowId;
    }

    /*
     * Attempts to Update a putting Card object
     */

    public static int updateCard(PuttingCard pc, SQLiteDatabase db) {

        ContentValues values = new ContentValues();
        values.put(PuttingCardEntry.COLUMN_IS_COMPLETE, pc.getIsComplete());
        values.put(PuttingCardEntry.COLUMN_TOTAL_SCORE, pc.getTotalScore());
        values.put(PuttingCardEntry.COLUMN_LAST_UPDATE, pc.getLastUpdated());
        values.put(PuttingCardEntry.COLUMN_GRADE, pc.getGrade());
        values.put(PuttingCardEntry.COLUMN_HANDICAP, pc.getHandicap());

        // updating row
        return db.update(PuttingCardEntry.TABLE_NAME, values, PuttingCardEntry._ID + " = ?",
                new String[]{String.valueOf(pc.getId())});
    }


    /*
     * Delete a Putting Card
     * Note that this will delete all drills associated with it too.
     *
     * @parameter: card_id
     *      The id of the Putting Card we want to count the drills for
     */
    public static int deleteCard(long cId, SQLiteDatabase db) {
        //First remove any drills attached to this card
        deleteDrillsFromCard(cId, db);

        //Next delete the card itself
        Log.i(TAG, "Attempting to delete Putting Card");
        int rowsDeleted = db.delete(PuttingCardEntry.TABLE_NAME, PuttingCardEntry._ID + " = ?", new String[]{String.valueOf(cId)});
        Log.i(TAG, rowsDeleted + "rows deleted from Putting Card table");
        Log.i(TAG, "Putting Card of ID " + cId + " has been deleted from database");
        return rowsDeleted;
    }

    /*
     * This will delete all drills from PUTTING DRILL table where the associated card id is pId
     * This method is called when deleting a Putting card
     *
     * @parameter: pId
     *      The id of the Putting Card object associated to the drills we want to delete
     */

    private static void deleteDrillsFromCard(long pId, SQLiteDatabase db) {
        int rowsDeleted = db.delete(PuttingDrillEntry.TABLE_NAME, PuttingDrillEntry.COLUMN_CARD_ID + " = ?", new String[]{String.valueOf(pId)});
        updateCardCompleteness((int) pId, db);
        Log.i(TAG, "We have just deleted " + rowsDeleted + " drills associated to card (" + pId + ") from the Drills table");
    }

    /*
     * Count the number of drills a Card has
     *
     * @parameter: cid
     *      The id of the Putting Card we want to count the drills for
     */
    private static int countDrills(long cid, SQLiteDatabase db) {
        Cursor c = null;
        String selectQuery = "SELECT COUNT(*) FROM " + PuttingDrillEntry.TABLE_NAME + " WHERE "
                + PuttingDrillEntry.COLUMN_CARD_ID + " = " + cid;

        try {
            c = db.rawQuery(selectQuery, null);
            if (c == null) return 0;
            c.moveToFirst();
            int toRet = c.getInt(0);
            c.close();
            return toRet;
        } catch (Exception e) {
            Log.e(TAG, "Tried getting countDrills for drill ID " + cid + " and failed...");
            Log.e(TAG, e.getMessage());
            if (c != null) {
                c.close();
            }
            return 0;
        }
    }

    /*
     * This will update a card's TotalScore, Handicap and isComplete attributes if it has been
     * completed (i.e: if 6 drills are attached to it)
     */

    private static void updateCardCompleteness(int cardID, SQLiteDatabase db) {
        try {
            PuttingCard pc = getCard(cardID, db);
            pc.setTotalScore(getTotalCardScore(cardID, db));

            if (countDrills(cardID, db) == 9) {
                Log.d(TAG, "Hey we got 9 drills!!");
                //Card is now complete
                //let's mark is as complete and update the score
                pc.setIsComplete(1);
                pc.setHandicap(getCardHCap(pc.getTotalScore()));
            } else {
                //if not then mark card as not complete (could have just deleted a drill)
                pc.setIsComplete(0);
                pc.setHandicap(99);
                updateCard(pc, db);
            }
            //update the card
            updateCard(pc, db);

        } catch (Exception e) {
            Log.d(TAG, "Tried to see if card was completed. Failed.");
        }
    }

    /*
     * if card has 6 drills: return sum score of all 6 drills tied to card
     */

    private static int getTotalCardScore(int cardId, SQLiteDatabase db1) {
        int totalScore = 0;

        String testQuery = "SELECT " + PuttingDrillEntry.COLUMN_TOTAL_SCORE + ", " + PuttingDrillEntry.COLUMN_DRILL_TYPE + " FROM "
                + PuttingDrillEntry.TABLE_NAME + " WHERE "
                + PuttingDrillEntry.COLUMN_CARD_ID + " = " + cardId;

        Cursor c1 = db1.rawQuery(testQuery, null);
        Log.d(TAG, "DEBUG QUERY: " + testQuery);

        while (c1.moveToNext()) {
            try {
                Log.i(TAG, "SCORE: " + c1.getInt(0) + " | Drill ID: " + c1.getInt(1));
            } catch (Exception e) {
                Log.e(TAG, "Error trying to get totalScore for a card:" + e.toString());
            }
        }
        c1.close();

        String totScoreQuery = "SELECT SUM(" + PuttingDrillEntry.COLUMN_TOTAL_SCORE + ") FROM "
                + PuttingDrillEntry.TABLE_NAME + " WHERE "
                + PuttingDrillEntry.COLUMN_CARD_ID + " = " + cardId;

        Cursor c = db1.rawQuery(totScoreQuery, null);
        while (c.moveToNext()) {
            try {
                Log.i(TAG, "just ran Query:" + totScoreQuery);
                totalScore = c.getInt(0);
                Log.i(TAG, "result is" + totalScore);
            } catch (Exception e) {
                Log.e(TAG, "Error trying to get totalScore for a card:" + e.toString());
            }
        }
        c.close();
        return totalScore;
    }


    /*
     * This is supposed to return the handicap for a Putting Card.
     * TODO: figure  out what this h'cap tabble should look like knowing that we don't know how to
     * score the elusive 7th drill "green reading".
     *
     * For the moment I've just kept the Short Game Drill lookup table
     */

    private static int getCardHCap(int score) {
        if (score > 155) return -8;

        switch (score) {
            case 0:
                return 39;
            case 1:
                return 39;
            case 2:
                return 38;
            case 3:
                return 37;
            case 4:
                return 37;
            case 5:
                return 37;
            case 6:
                return 37;
            case 7:
                return 37;
            case 8:
                return 36;
            case 9:
                return 35;
            case 10:
                return 35;
            case 11:
                return 35;
            case 12:
                return 34;
            case 13:
                return 34;
            case 14:
                return 33;
            case 15:
                return 33;
            case 16:
                return 32;
            case 17:
                return 32;
            case 18:
                return 32;
            case 19:
                return 31;
            case 20:
                return 31;
            case 21:
                return 31;
            case 22:
                return 30;
            case 23:
                return 29;
            case 24:
                return 29;
            case 25:
                return 29;
            case 26:
                return 29;
            case 27:
                return 28;
            case 28:
                return 28;
            case 29:
                return 27;
            case 30:
                return 27;
            case 31:
                return 27;
            case 32:
                return 27;
            case 33:
                return 26;
            case 34:
                return 25;
            case 35:
                return 25;
            case 36:
                return 25;
            case 37:
                return 24;
            case 38:
                return 24;
            case 39:
                return 24;
            case 40:
                return 24;
            case 41:
                return 23;
            case 42:
                return 23;
            case 43:
                return 22;
            case 44:
                return 22;
            case 45:
                return 22;
            case 46:
                return 21;
            case 47:
                return 21;
            case 48:
                return 21;
            case 49:
                return 20;
            case 50:
                return 20;
            case 51:
                return 20;
            case 52:
                return 19;
            case 53:
                return 19;
            case 54:
                return 19;
            case 55:
                return 19;
            case 56:
                return 18;
            case 57:
                return 18;
            case 58:
                return 18;
            case 59:
                return 17;
            case 60:
                return 17;
            case 61:
                return 16;
            case 62:
                return 16;
            case 63:
                return 16;
            case 64:
                return 16;
            case 65:
                return 16;
            case 66:
                return 15;
            case 67:
                return 15;
            case 68:
                return 14;
            case 69:
                return 14;
            case 70:
                return 14;
            case 71:
                return 13;
            case 72:
                return 13;
            case 73:
                return 13;
            case 74:
                return 12;
            case 75:
                return 12;
            case 76:
                return 12;
            case 77:
                return 12;
            case 78:
                return 11;
            case 79:
                return 11;
            case 80:
                return 10;
            case 81:
                return 10;
            case 82:
                return 10;
            case 83:
                return 10;
            case 84:
                return 9;
            case 85:
                return 9;
            case 86:
                return 9;
            case 87:
                return 9;
            case 88:
                return 8;
            case 89:
                return 8;
            case 90:
                return 7;
            case 91:
                return 7;
            case 92:
                return 7;
            case 93:
                return 7;
            case 94:
                return 6;
            case 95:
                return 6;
            case 96:
                return 6;
            case 97:
                return 6;
            case 98:
                return 6;
            case 99:
                return 5;
            case 100:
                return 5;
            case 101:
                return 4;
            case 102:
                return 4;
            case 103:
                return 4;
            case 104:
                return 4;
            case 105:
                return 4;
            case 106:
                return 3;
            case 107:
                return 3;
            case 108:
                return 3;
            case 109:
                return 3;
            case 110:
                return 2;
            case 111:
                return 2;
            case 112:
                return 2;
            case 113:
                return 1;
            case 114:
                return 1;
            case 115:
                return 1;
            case 116:
                return 1;
            case 117:
                return 0;
            case 118:
                return 0;
            case 119:
                return 0;
            case 120:
                return 0;
            case 121:
                return -1;
            case 122:
                return -1;
            case 123:
                return -1;
            case 124:
                return -1;
            case 125:
                return -2;
            case 126:
                return -2;
            case 127:
                return -3;
            case 128:
                return -3;
            case 129:
                return -3;
            case 130:
                return -3;
            case 131:
                return -3;
            case 132:
                return -3;
            case 133:
                return -3;
            case 134:
                return -4;
            case 135:
                return -4;
            case 136:
                return -4;
            case 137:
                return -4;
            case 138:
                return -5;
            case 139:
                return -5;
            case 140:
                return -5;
            case 141:
                return -5;
            case 142:
                return -5;
            case 143:
                return -6;
            case 144:
                return -6;
            case 145:
                return -6;
            case 146:
                return -6;
            case 147:
                return -6;
            case 148:
                return -7;
            case 149:
                return -7;
            case 150:
                return -7;
            case 151:
                return -7;
            case 152:
                return -7;
            case 153:
                return -7;
            case 154:
                return -7;
            case 155:
                return -8;
        }
        Log.e("TAG", "Can't find hcap for value: " + score);
        return 40;
    }
}
