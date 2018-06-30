package boucoiran.fr.shortgamebuddy;

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

import ChippingDrills.GenericShortGameDrill;
import ChippingDrills.ShortGameCard;
import data.GolfPracticeDBHelper;

import static boucoiran.fr.shortgamebuddy.R.layout.activity_short_sand_score_input;


public class ShortSandScoreInputActivity extends AppCompatActivity {

    private NumberPicker np1;
    private NumberPicker np2;
    private NumberPicker np3;

    private GolfPracticeDBHelper scDbHelper = new GolfPracticeDBHelper(this);
    private static GenericShortGameDrill scd;
    private static int card_id;
    private Class iDest;

    /*
     * Variable below are drill specific
     */
    private int drillType = GolfPracticeDBHelper.SC_SHORT_SAND_DRILL_ID;
    private Class rClass = LongChipScoreInputActivity.class;
    private Class lClass = LongSandScoreInputActivity.class;
    private static final String title = "Short Sand Drill";
    private static String TAG = "ShortSandScoreInputActivity";
    private static final int activityLayout = activity_short_sand_score_input;


    @SuppressLint("SimpleDateFormat")
    private String getStringDate() {
        return new SimpleDateFormat("dd/MM/yyyy").format(new Date());
    }

    /*
     * Returns the total Score for this drill.
     */
    private int getTotalScore() {
        return np1.getValue() + 2* np2.getValue() + 4* np3.getValue();
    }

    /*
     * This method will set Swipe listeners on the activity's main layout.
     */

    @SuppressLint("ClickableViewAccessibility")
    private void setSwipeListeners() {
        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.main_layout);

        mainLayout.setOnTouchListener(new OnSwipeTouchListener(ShortSandScoreInputActivity.this) {
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
            try{
                scd = scDbHelper.getShortGameDrill(cardId, drillType);
            } catch (Exception e) {
                Log.e(TAG, "Failed to load drill object when starting Activity. Card id is " + card_id);
                Log.e(TAG, "setting drill to null");
                scd = null;
            }
        }
    }

    /*
     * This method will instantiate the number pickers in the layout
     */

    private void setupNumberPickers() {
        np1 = (NumberPicker)findViewById(R.id.ShortChip1PtCounter);
        np2 = (NumberPicker)findViewById(R.id.ShortChip2PtCounter);
        np3 = (NumberPicker)findViewById(R.id.ShortChip4PtCounter);

        np1.setMinValue(0);
        np1.setMaxValue(10);
        np2.setMinValue(0);
        np2.setMaxValue(10);
        np3.setMinValue(0);
        np3.setMaxValue(10);

        //Gets whether the selector wheel wraps when reaching the min/max value.
        np1.setWrapSelectorWheel(true);
        np2.setWrapSelectorWheel(true);
        np3.setWrapSelectorWheel(true);

        if(scd != null) {
            //attempt to instantiate the Drill object from DB
            np1.setValue(scd.getNumberInside6Ft());
            np2.setValue(scd.getNumberInside3Ft());
            np3.setValue(scd.getNumberInHole());
        } else {
            //create a blank Drill object
            scd = new GenericShortGameDrill(-1, getStringDate(), 0, card_id, "", 0, 0, 0, 0, "", 999, drillType);
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

    public void saveDrill(View view){
        Log.i(TAG,"Saving Drill");
        long scDrillID;

        //If view is null then this means we are calling this save method from a SWIPE event.
        //If view is not null, it means we are calling this from the save button in Activity
        if(view != null) iDest = ShortGameDrillsActivity.class;

        //Check to see we have not hit too many balls
        if ((np1.getValue()+np2.getValue()+np3.getValue()) > 10) {
            Toast.makeText(this, "oops, you hit too many balls! Max of 10!", Toast.LENGTH_LONG).show();
        } else {
            //First, check if a Card already exists.
            if(card_id == -1) {
                //If no card exists, create one.
                ShortGameCard sgc = new ShortGameCard(0, getStringDate(), -1, "", 99);
                card_id = (int)scDbHelper.createShortGameCard(sgc);
            }

            //update Drill object
            int hcap = getHcapFromScore(getTotalScore());
            scd.setDrillHandicap(hcap);
            scd.setTotalScore(getTotalScore());
            scd.setCardId(card_id);
            scd.setNumberInHole(np3.getValue());
            scd.setNumberInside3Ft(np2.getValue());
            scd.setNumberInside6Ft(np1.getValue());
            scd.setNumberOutside(10-np1.getValue()-np2.getValue()-np3.getValue());
            scd.setDate_played(getStringDate());
            scd.setDrillType(drillType);

            if (scd.getId()==-1) {
                //Create the drill as it is new
                scDrillID = scDbHelper.createShortGameDrill(scd);
                scd.setId((int)scDrillID);
            } else {
                //Update the drill as it exists
                scDbHelper.updateShortGameDrill(scd);
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
        if (score > 16) return -8;

        switch (score) {
            case 0: return 39;
            case 1: return 34;
            case 2: return 29;
            case 3: return 24;
            case 4: return 20;
            case 5: return 17;
            case 6: return 14;
            case 7: return 11;
            case 8: return 8;
            case 9: return 5;
            case 10: return 3;
            case 11: return 1;
            case 12: return -1;
            case 13: return -3;
            case 14: return -5;
            case 15: return -7;
            case 16: return -8;
        }
        Log.e(TAG, "SC H'cap Lookup: Can't find hcap for value: "+score);
        return 39;
    }

    /*
     * This method should clear the data from this drill. What it will actually do it delete it.
     * This is actually different to having all zero values.
     */

    public void clearDrill(View view){
        Log.i(TAG,"clearing Drill");

        if(scd.getId() != -1) {
            try {
                //if the drill exists, then delete it.
                scDbHelper.deleteShortGameDrill(scd.getId());
                Log.i(TAG,"Drill deleted successfully");
            }catch (Exception e) {
                Log.e(TAG, "Failed to delete a Drill on clearing");
            }
        }

        Intent i = new Intent(this, ShortGameDrillsActivity.class);
        i.putExtra("CARD_ID", card_id);
        startActivity(i);
    }

    /*
     * Open the info box for this drill
     */

    public void openInfo(View view) {

    }
}