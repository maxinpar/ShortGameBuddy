package boucoiran.fr.shortgamebuddy.activities.shortGame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import boucoiran.fr.shortgamebuddy.R;
import boucoiran.fr.shortgamebuddy.activities.menus.MainMenuActivity;
import boucoiran.fr.shortgamebuddy.utils.GolfPracticeDBHelper;

public class ShortGameDrillsActivity extends AppCompatActivity {

    //13/6/2018 using a generic drill is the way to go.
    // we will need to deprecate the two variables below
    private static long shortChipDrillID = -1;
    private static long longChipDrillID = -1;

    //13/6/2018 using a generic drill is the way to go
    private static long shortGameDrillID = -1;

    private static long card_id = -1;
    private GolfPracticeDBHelper scDbHelper;
    private final String TAG = "ShortGameDrillsActivity";


    @Override

    /***********************************************************************************************
     * OnCreate will do several things:
     *      - It will attempt to get a Card_Id [Although I don't think this is necessary]
     *      - It will attempt to retrieve Drill ID's
     *      - it will also display drill scores where applicable
     **********************************************************************************************/

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scDbHelper = new GolfPracticeDBHelper(this);
        setContentView(R.layout.activity_short_game_drills);

        card_id = getIntent().getIntExtra("CARD_ID", -1);
        //Display any scores for drills that are completed
        try {
            displayBadges();
        } catch (Exception e) {
        }
    }


    /**********************************************************************************************
     * Below are all the intents to open the Short Game Drill activities                          *
     **********************************************************************************************/

    /*
     * This will open the Short Chip Score Input Activity
     * It will pass the ShortChipDrill ID we have if one has already been created.
     * Note: We may not need to pass the card id as this should be included in the ShortChipDrill
     */
    public void openShortChipActivity(View view) {
        //open the short chip shot input page
        Intent i = new Intent(this, ShortChipScoreInputActivity.class);
        i.putExtra("CARD_ID", (int) card_id);
        i.putExtra("SHORT_CHIP_DRILL_ID", shortChipDrillID);
        startActivity(i);
    }

    /*
     * This will open the Short Chip Score Input Activity
     * It will pass the ShortChipDrill ID we have if one has already been created.
     * Note: We may not need to pass the card id as this should be included in the ShortChipDrill
     */
    public void openLongChipActivity(View view) {
        //open the short chip shot input page
        Intent i = new Intent(this, LongChipScoreInputActivity.class);
        i.putExtra("CARD_ID", (int) card_id);
        i.putExtra("LONG_CHIP_DRILL_ID", longChipDrillID);
        startActivity(i);
    }

    /*
     * This will open the Short Sand Score Input Activity
     * It will pass the card ID we have if one has already been created.
     */
    public void openShortSandActivity(View view) {
        //open the short chip shot input page
        Intent i = new Intent(this, ShortSandScoreInputActivity.class);
        i.putExtra("CARD_ID", (int) card_id);
        startActivity(i);
    }

    /*
     * This will open the Short Sand Score Input Activity
     * It will pass the card ID we have if one has already been created.
     */
    public void openLongSandActivity(View view) {
        //open the short chip shot input page
        Intent i = new Intent(this, LongSandScoreInputActivity.class);
        i.putExtra("CARD_ID", (int) card_id);
        startActivity(i);
    }

    /*
     * This will open the Short Sand Score Input Activity
     * It will pass the card ID we have if one has already been created.
     */
    public void openShortPitchActivity(View view) {
        //open the short chip shot input page
        Intent i = new Intent(this, ShortPitchScoreInputActivity.class);
        i.putExtra("CARD_ID", (int) card_id);
        startActivity(i);
    }

    /*
     * This will open the Short Sand Score Input Activity
     * It will pass the card ID we have if one has already been created.
     */
    public void openLongPitchActivity(View view) {
        //open the short chip shot input page
        Intent i = new Intent(this, LongPitchScoreInputActivity.class);
        i.putExtra("CARD_ID", (int) card_id);
        startActivity(i);
    }

    /*
     * This will open the Short Sand Score Input Activity
     * It will pass the card ID we have if one has already been created.
     */
    public void openRoughChipActivity(View view) {
        //open the short chip shot input page
        Intent i = new Intent(this, RoughChipScoreInputActivity.class);
        i.putExtra("CARD_ID", (int) card_id);
        startActivity(i);
    }

    /*
     * This will open the Short Sand Score Input Activity
     * It will pass the card ID we have if one has already been created.
     */
    public void openLobShotActivity(View view) {
        //open the short chip shot input page
        Intent i = new Intent(this, LobShotScoreInputActivity.class);
        i.putExtra("CARD_ID", (int) card_id);
        startActivity(i);
    }

    /*
     * This will open the Short Sand Score Input Activity
     * It will pass the card ID we have if one has already been created.
     */
    public void openMediumPitchActivity(View view) {
        //open the short chip shot input page
        Intent i = new Intent(this, MediumPitchScoreInputActivity.class);
        i.putExtra("CARD_ID", (int) card_id);
        startActivity(i);
    }


    /*
     * This will open the Score Card Activity
     * It will pass the Short Game Card ID if we have one if one has already been created.
     */
    public void openScorecardActivity(View view) {
        //open the short chip shot input page
        Intent i = new Intent(this, ShortGameScorecardActivity.class);
        i.putExtra("CARD_ID", (int) card_id);
        startActivity(i);
    }

    /*
     * This will Go back to the main screen
     */
    public void openMainMenu(View view) {
        //open the short chip shot input page
        Intent i = new Intent(this, MainMenuActivity.class);
        i.putExtra("CARD_ID", (int) card_id);
        startActivity(i);
    }

    /*
     * DisplayBadges will read a cardId, try and instantiate a card object, get the attached
     * Drills and get he relevant scores.
     *
     * It will then display the score as  badge for each drill.
     */

    private void displayBadges() {
        Log.i(TAG, "We are trying to display some badges");
        TextView badge = null;
        int completedDrills = 0;
        if (card_id == -1) return;

        Log.i(TAG, "getting badge scores..");
        String[] badgeScores = scDbHelper.getSGDrillScoresFromCard(card_id);
        Log.i(TAG, "got  the badge scores "+badgeScores);

        for (int i = 0; i < 10; i++) {
            Log.i(TAG, "/tbadgescores["+i+"]"+badgeScores[i]);
            if (!badgeScores[i].equals("-1")) {
                completedDrills++;
                if (i == GolfPracticeDBHelper.SC_SHORT_CHIP_DRILL_ID)
                    badge = (TextView) findViewById(R.id.shortChip_Badge);
                if (i == GolfPracticeDBHelper.SC_LONG_CHIP_DRILL_ID)
                    badge = (TextView) findViewById(R.id.longChip_Badge);
                if (i == GolfPracticeDBHelper.SC_SHORT_SAND_DRILL_ID)
                    badge = (TextView) findViewById(R.id.shortSand_Badge);
                if (i == GolfPracticeDBHelper.SC_LONG_SAND_DRILL_ID)
                    badge = (TextView) findViewById(R.id.longSand_Badge);
                if (i == GolfPracticeDBHelper.SC_SHORT_PITCH_DRILL_ID)
                    badge = (TextView) findViewById(R.id.shortPitch_Badge);
                if (i == GolfPracticeDBHelper.SC_MEDIUM_PITCH_DRILL_ID)
                    badge = (TextView) findViewById(R.id.mediumPitch_Badge);
                if (i == GolfPracticeDBHelper.SC_LONG_PITCH_DRILL_ID)
                    badge = (TextView) findViewById(R.id.longPitch_Badge);
                if (i == GolfPracticeDBHelper.SC_LOB_SHOT_DRILL_ID)
                    badge = (TextView) findViewById(R.id.LobShot_Badge);
                if (i == GolfPracticeDBHelper.SC_ROUGH_CHIP_DRILL_ID)
                    badge = (TextView) findViewById(R.id.RoughChip_Badge);

                badge.setVisibility(View.VISIBLE);
                badge.setText(String.valueOf(badgeScores[i]));
            }
        }
    }
}