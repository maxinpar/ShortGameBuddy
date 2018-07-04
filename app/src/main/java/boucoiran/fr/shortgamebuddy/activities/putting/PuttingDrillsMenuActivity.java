package boucoiran.fr.shortgamebuddy.activities.putting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Arrays;

import boucoiran.fr.shortgamebuddy.R;
import boucoiran.fr.shortgamebuddy.activities.menus.PuttingMenuActivity;
import boucoiran.fr.shortgamebuddy.utils.GolfPracticeDBHelper;

public class PuttingDrillsMenuActivity extends AppCompatActivity {

    private static long card_id = -1;
    private GolfPracticeDBHelper helper;
    private String TAG = "PuttDrillsMenuActivity";


    @Override
    /*
     * OnCreate will do several things:
     *      - It will attempt to get a Card_Id [This is useful to then fill up score badges for
     *        each drill]
     *      - it will also display drill scores where applicable
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper = new GolfPracticeDBHelper(this);
        setContentView(R.layout.activity_putting_drills);

        card_id = getIntent().getIntExtra("CARD_ID", -1);
        //Display any scores for drills that are completed
        try {
            displayBadges();
        } catch (Exception ignored) {
        }
    }


    /**********************************************************************************************
     * Below are all the intents to open the Putting Drill activities                             *
     **********************************************************************************************/

    /*
     * This will open the 3ft Putt Score Input Activity
     */
    public void openShort3ftPuttActivity(View view) {
        Intent i = new Intent(this, Short3ftPuttScoreInputActivity.class);
        i.putExtra("CARD_ID", (int) card_id);
        startActivity(i);
    }

    /*
     * This will open the 6ft Putt Score Input Activity
     */
    public void openShort6ftPuttActivity(View view) {
        Intent i = new Intent(this, Short6ftPuttScoreInputActivity.class);
        i.putExtra("CARD_ID", (int) card_id);
        startActivity(i);
    }

    /*
     * This will open the makeable Putts Score Input Activity
     */
    public void openMakeablePuttActivity(View view) {
        Intent i = new Intent(this, MakeablePuttScoreInputActivity.class);
        i.putExtra("CARD_ID", (int) card_id);
        startActivity(i);
    }

    /*
     * This will open the medium Putts Score Input Activity
     */
    public void openMediumPuttActivity(View view) {
        Intent i = new Intent(this, MediumPuttScoreInputActivity.class);
        i.putExtra("CARD_ID", (int) card_id);
        startActivity(i);
    }

    /*
     * This will open the lag Putts Score Input Activity
     */
    public void openLagPuttActivity(View view) {
        Intent i = new Intent(this, LagPuttScoreInputActivity.class);
        i.putExtra("CARD_ID", (int) card_id);
        startActivity(i);
    }

    /*
     * This will open the big break Putts Score Input Activity
     */
    public void openBigBreakPuttActivity(View view) {
        Intent i = new Intent(this, BigBreakPuttScoreInputActivity.class);
        i.putExtra("CARD_ID", (int) card_id);
        startActivity(i);
    }

    /*
     * This will open the Score Card Activity
     * It will pass the Putting Card ID if we have one if one has already been created.
     */
    public void openScorecardActivity(View view) {
        //TODO uncomment this once built
/*
        Intent i = new Intent(this, PuttingScorecardActivity.class);
        i.putExtra("CARD_ID", (int) card_id);
        startActivity(i);
*/
    }

    /*
     * This will Go back to the main screen
     */
    public void openMainMenu(View view) {
        Intent i = new Intent(this, PuttingMenuActivity.class);
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
        String[] badgeScores = helper.getPuttingDrillScoresFromCard(card_id);
        Log.i(TAG, "got  the badge scores " + Arrays.toString(badgeScores));

        for (int i = 0; i < 10; i++) {
            Log.i(TAG, "/tbadgescores[" + i + "]" + badgeScores[i]);
            if (!badgeScores[i].equals("-1")) {
                completedDrills++;
                if (i == GolfPracticeDBHelper.P_3FT_PUTT_DRILL_ID)
                    badge = (TextView) findViewById(R.id.short3ftPutt_Badge);
                if (i == GolfPracticeDBHelper.P_6FT_PUTT_DRILL_ID)
                    badge = (TextView) findViewById(R.id.short6ftPutt_Badge);
                if (i == GolfPracticeDBHelper.P_MAKEABLE_DRILL_ID)
                    badge = (TextView) findViewById(R.id.makeable_Badge);
                if (i == GolfPracticeDBHelper.P_MEDIUM_DRILL_ID)
                    badge = (TextView) findViewById(R.id.medium_Badge);
                if (i == GolfPracticeDBHelper.P_LAG_DRILL_ID)
                    badge = (TextView) findViewById(R.id.lag_Badge);
                if (i == GolfPracticeDBHelper.P_BIG_BREAK_DRILL_ID)
                    badge = (TextView) findViewById(R.id.big_break_Badge);

                if (badge != null) {
                    badge.setVisibility(View.VISIBLE);
                    badge.setText(String.valueOf(badgeScores[i]));
                }
            }
        }
    }
}