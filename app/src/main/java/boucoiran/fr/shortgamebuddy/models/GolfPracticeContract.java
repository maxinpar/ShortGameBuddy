package boucoiran.fr.shortgamebuddy.models;

import android.provider.BaseColumns;

public final class GolfPracticeContract {

    private GolfPracticeContract() {}

    /*
     * 13/06/2018: Let's move away from one table per drill type and instead look at generic Drill
     * Tables. Ultimately this will come to replace the ShortChipDrill and LongChipDrill Entries
     * that were (in hindsight) a mistake.
     *
     * Contract Class for the table storing a *generic* Short Game Drill. This drill is part
     * of a Short Game Practice Card.
     *
     * It will contain a card_ID and also a Drill_Type identifier
     *
     */
    public static final class ShortGameDrillEntry implements BaseColumns {
        public final static String TABLE_NAME = "short_game_drill";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_DATE = "date_played";
        public final static String COLUMN_TOTAL_SCORE = "total_score";
        public final static String COLUMN_CARD_ID = "card_id";
        public final static String COLUMN_NOTES = "notes";
        public final static String COLUMN_NUM_IN_HOLE = "number_in_hole";
        public final static String COLUMN_NUM_IN_3FT = "number_in_3ft";
        public final static String COLUMN_NUM_IN_6FT = "number_in_6ft";
        public final static String COLUMN_NUM_OUTSIDE = "number_outside";
        public final static String COLUMN_GRADE = "grade";
        public final static String COLUMN_HANDICAP = "handicap";
        public final static String COLUMN_DRILL_TYPE = "drill_type";
    }


    /*
     * Contract Class for the table storing Short Game Card data.
     */

    public static final class ShortGameCardEntry implements BaseColumns {
        public final static String TABLE_NAME = "short_game_card";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_IS_COMPLETE = "is_complete";
        public final static String COLUMN_LAST_UPDATE = "last_update";
        public final static String COLUMN_TOTAL_SCORE = "total_score";
        public final static String COLUMN_GRADE = "grade";
        public final static String COLUMN_HANDICAP = "handicap";
    }

    /*
     * Contract Class for the table storing a *generic* Putting Drill. This drill is part
     * of a Short Game Practice Card.
     *
     * It will contain a card_ID and also a Drill_Type identifier
     *
     */
    public static final class PuttingDrillEntry implements BaseColumns {
        public final static String TABLE_NAME = "putting_drill";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_DATE = "date_played";
        public final static String COLUMN_TOTAL_SCORE = "total_score";
        public final static String COLUMN_CARD_ID = "card_id";
        public final static String COLUMN_NOTES = "notes";
        public final static String COLUMN_NUM_IN_HOLE = "number_in_hole";
        public final static String COLUMN_NUM_IN_3FT = "number_in_3ft";
        public final static String COLUMN_NUM_IN_6FT = "number_in_6ft";
        public final static String COLUMN_NUM_IN_ZONE = "number_in_zone";
        public final static String COLUMN_NUM_OUTSIDE = "number_outside";
        public final static String COLUMN_GRADE = "grade";
        public final static String COLUMN_HANDICAP = "handicap";
        public final static String COLUMN_DRILL_TYPE = "drill_type";
    }


    /*
     * Contract Class for the table storing Putting Card data.
     */

    public static final class PuttingCardEntry implements BaseColumns {
        public final static String TABLE_NAME = "putting_card";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_IS_COMPLETE = "is_complete";
        public final static String COLUMN_LAST_UPDATE = "last_update";
        public final static String COLUMN_TOTAL_SCORE = "total_score";
        public final static String COLUMN_GRADE = "grade";
        public final static String COLUMN_HANDICAP = "handicap";
    }



}
