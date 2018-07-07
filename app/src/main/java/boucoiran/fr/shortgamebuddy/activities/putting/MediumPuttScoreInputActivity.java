package boucoiran.fr.shortgamebuddy.activities.putting;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import boucoiran.fr.shortgamebuddy.R;
import boucoiran.fr.shortgamebuddy.models.GenericPuttingDrill;
import boucoiran.fr.shortgamebuddy.models.PuttingCard;
import boucoiran.fr.shortgamebuddy.utils.GolfPracticeDBHelper;
import boucoiran.fr.shortgamebuddy.utils.OnSwipeTouchListener;


public class MediumPuttScoreInputActivity extends AppCompatActivity {

    private NumberPicker np1;
    private NumberPicker np2;
    private NumberPicker np3;

    private GolfPracticeDBHelper scDbHelper = new GolfPracticeDBHelper(this);
    private static GenericPuttingDrill gpd;
    private static int card_id;
    private Class iDest;

    /*
     * Variables below are drill specific
     */
    private int drillType = GolfPracticeDBHelper.P_MEDIUM_DRILL_ID;
    private Class rClass = MakeablePuttScoreInputActivity.class;
    private Class lClass = LagPuttScoreInputActivity.class;
    private static final String title = "Medium putt drill";
    private static String TAG = "MediumPuttScoreInputActy";
    private static final int activityLayout = R.layout.activity_putt_medium_score_input;


    @SuppressLint("SimpleDateFormat")
    private String getStringDate() {
        return new SimpleDateFormat("dd/MM/yyyy").format(new Date());
    }

    /*
     * Returns the total Score for this drill.
     * Note that depending on the drill how we calculate this can change.
     */
    private int getTotalScore() {
        return np1.getValue() + 2 * np2.getValue() + 4 * np3.getValue();
    }

    /*
     * This method will set Swipe listeners on the activity's main layout.
     * We use this for setting where to go when user swipes left or right
     */

    @SuppressLint("ClickableViewAccessibility")
    private void setSwipeListeners() {
        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.main_layout);

        mainLayout.setOnTouchListener(new OnSwipeTouchListener(MediumPuttScoreInputActivity.this) {
            public void onSwipeLeft() {
                iDest = lClass;
                saveDrill(null);
            }

            public void onSwipeRight() {
                iDest = rClass;
                saveDrill(null);
            }
        });
    }

    /*
     * This will instantiate our card if a card id has been passed to the intent
     */
    private void setCard(int cardId) {
        if (cardId != -1) {
            try {
                gpd = scDbHelper.getPuttingDrill(cardId, drillType);
            } catch (Exception e) {
                gpd = null;
            }
        }
    }

    /*
     * This method will instantiate the number pickers in the layout
     */

    private void setupNumberPickers() {
        np1 = (NumberPicker) findViewById(R.id.NP1);
        np1.setMinValue(0);
        np1.setMaxValue(10);

        np2 = (NumberPicker) findViewById(R.id.NP2);
        np2.setMinValue(0);
        np2.setMaxValue(10);

        np3 = (NumberPicker) findViewById(R.id.NP3);
        np3.setMinValue(0);
        np3.setMaxValue(10);

        //Gets whether the selector wheel wraps when reaching the min/max value.
        np1.setWrapSelectorWheel(true);
        np2.setWrapSelectorWheel(true);
        np3.setWrapSelectorWheel(true);

        if (gpd != null) {
            //attempt to instantiate the Drill object from DB
            np1.setValue(gpd.getNumberInside6Ft());
            np2.setValue(gpd.getNumberInside3Ft());
            np3.setValue(gpd.getNumberInHole());
        } else {
            //create a blank Drill object
            gpd = new GenericPuttingDrill(-1, getStringDate(), 0, card_id, "", 0, 0, 0, 0, 0, "", 999, drillType);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activityLayout);
        card_id = getIntent().getIntExtra("CARD_ID", -1);

        setTitle(title);
        setCard(card_id);
        setupNumberPickers();
        setSwipeListeners();
    }

    /*
     * This will update the Drill Java object and save it to DB.
     * If necessary, a Card Object will also be saved.
     */

    public void saveDrill(View view) {
        Log.i(TAG, "Saving Drill");
        long scDrillID;

        //If view is null then this means we are calling this save method from a SWIPE event.
        //If view is not null, it means we are calling this from the save button in Activity
        if (view != null) iDest = PuttingDrillsMenuActivity.class;

        //Check to see we have not hit too many balls
        if (np1.getValue() + np2.getValue() + np3.getValue() > 10) {
            Toast.makeText(this, "oops, you hit too many balls! Max of 10!", Toast.LENGTH_LONG).show();
        } else {
            //First, check if a Card already exists.
            if (card_id == -1) {
                //If no card exists, create one.
                PuttingCard pc = new PuttingCard(0, getStringDate(), -1, "", 99);
                card_id = (int) scDbHelper.createPuttingCard(pc);
            }

            //update Drill object
            int hcap = getHcapFromScore(getTotalScore());
            gpd.setDrillHandicap(hcap);
            gpd.setTotalScore(getTotalScore());
            gpd.setCardId(card_id);
            gpd.setNumberInHole(np3.getValue());
            gpd.setNumberInside3Ft(np2.getValue());
            gpd.setNumberInside6Ft(np1.getValue());
            gpd.setNumberOutside(10 - np1.getValue() - np2.getValue() - np3.getValue());
            gpd.setNumberInZone(0);
            gpd.setDate_played(getStringDate());
            gpd.setDrillType(drillType);

            if (gpd.getId() == -1) {
                //Create the drill as it is new
                scDrillID = scDbHelper.createPuttingDrill(gpd);
                gpd.setId((int) scDrillID);
            } else {
                //Update the drill as it exists
                scDbHelper.updatePuttingDrill(gpd);
            }

            //send us back to the "next" activity. This will either be the main menu (press from
            // button or to the next drill if user swiped
            // As always: set CARD_ID
            Intent i = new Intent(this, iDest);
            i.putExtra("CARD_ID", card_id);
            startActivity(i);
        }

    }

    /*
     * This will do a H'cap lookup based on the Pelz h'caps for this drill
     * We also take care of cases where score too high or low.
     */

    private int getHcapFromScore(int score) {
        if (score > 24) return -8;

        switch (score) {
            case 0:
                return 39;
            case 1:
                return 36;
            case 2:
                return 33;
            case 3:
                return 30;
            case 4:
                return 27;
            case 5:
                return 24;
            case 6:
                return 21;
            case 7:
                return 18;
            case 8:
                return 15;
            case 9:
                return 12;
            case 10:
                return 9;
            case 11:
                return 7;
            case 12:
                return 5;
            case 13:
                return 3;
            case 14:
                return 2;
            case 15:
                return 1;
            case 16:
                return 0;
            case 17:
                return -1;
            case 18:
                return -2;
            case 19:
                return -3;
            case 20:
                return -4;
            case 21:
                return -5;
            case 22:
                return -6;
            case 23:
                return -7;
            case 24:
                return -8;
        }
        Log.e(TAG, "Can't find hcap for value: " + score);
        if (score > 24) return -8;
        return 39;
    }

    /*
     * This method should clear the data from this drill. What it will actually do it delete it.
     * This is actually different to having all zero values.
     */

    public void clearDrill(View view) {
        Log.i(TAG, "clearing Drill");

        if (gpd.getId() != -1) {
            try {
                //if the drill exists, then delete it.
                scDbHelper.deletePuttingDrill(gpd.getId());
                Log.i(TAG, "Drill deleted successfully");
            } catch (Exception e) {
                Log.e(TAG, "Failed to delete a Drill on clearing");
            }
        }

        Intent i = new Intent(this, PuttingDrillsMenuActivity.class);
        i.putExtra("CARD_ID", card_id);
        startActivity(i);
    }

    /*
     * Open the info box for this drill
     */

    public void openInfo(View view) {

    }
}
