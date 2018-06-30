package boucoiran.fr.shortgamebuddy;

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


public class LongSandScoreInputActivity extends AppCompatActivity {

    private static String TAG = "LongSandScoreInputActivity";
    private int numberOf1pt = 0;
    private int numberOf2pt = 0;
    private int numberOf4pt = 0;

    private static NumberPicker np1;
    private static NumberPicker np2;
    private static NumberPicker np3;

    private GolfPracticeDBHelper scDbHelper;
    private static GenericShortGameDrill ssd;
    private static int card_id;

    private Class iDest;


    private String getStringDate() {
        String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        return date;
    }

    /*
     * Returns the total Score for this drill.
     */
    private int getTotalScore() {
        int totScore = numberOf1pt + 2* numberOf2pt + 4* numberOf4pt;
        return totScore;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_long_sand_score_input);
        scDbHelper = new GolfPracticeDBHelper(this);

        setTitle("Long Sand Drill");

        card_id = getIntent().getIntExtra("CARD_ID", -1);
        Log.i(TAG, "Creating the LS Drill. We have gotten Card Id: " + card_id);
        if (card_id != -1) {
            try{
                ssd = scDbHelper.getShortGameDrill(card_id, scDbHelper.SC_LONG_SAND_DRILL_ID);
            } catch (Exception e) {
                Log.i(TAG, "Failed to load long sand object when starting Activity. Card id is " + card_id);
                Log.i(TAG, "setting drill to null");
                ssd = null;
            }
        }

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

        //Set a value change listener for NumberPicker
        //don't really need these listeners tbh..
        np1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                numberOf1pt = newVal;
            }
        });


        np2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                numberOf2pt = newVal;
            }
        });

        np3.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                numberOf4pt = newVal;
            }
        });


        if(ssd != null) {
            //attempt to instantiate the Long Sand Drill object from DB
            np1.setValue(ssd.getNumberInside6Ft());
            np2.setValue(ssd.getNumberInside3Ft());
            np3.setValue(ssd.getNumberInHole());
            numberOf1pt = ssd.getNumberInside6Ft();
            numberOf2pt =ssd.getNumberInside3Ft();
            numberOf4pt = ssd.getNumberInHole();
        } else {
            //create a blank SC Drill object
            ssd = new GenericShortGameDrill();
            ssd.setId(-1);
            ssd.setDrillHandicap(999);
            ssd.setGrade("XXX");
            ssd.setNumberOutside(0);
            ssd.setNumberInside6Ft(0);
            ssd.setNumberInside3Ft(0);
            ssd.setNumberInHole(0);
            ssd.setNotes("");
            ssd.setCardId(-1);
            ssd.setTotalScore(0);
            ssd.setDate_played("");
            ssd.setDrillType(scDbHelper.SC_LONG_SAND_DRILL_ID);
        }

        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.main_layout);

        mainLayout.setOnTouchListener(new OnSwipeTouchListener(LongSandScoreInputActivity.this) {
            public void onSwipeLeft() {
                iDest = ShortPitchScoreInputActivity.class;
                saveDrill(null);
            }
            public void onSwipeRight() {
                iDest = ShortSandScoreInputActivity.class;
                saveDrill(null);
            }
        });
    }

    /*
     * This will update the ShortSand Drill Java object and save it to DB.
     * If necessary, a Card Object will also be saved.
     */

    public void saveDrill(View view){
        Log.i(TAG,"Saving LS Drill");
        long ssDrillID;

        //If view is null then this means we are calling this save method from a SWIPE event.
        //If view is not null, it means we are calling this from the save button in Activity
        if(view != null) iDest = ShortGameDrillsActivity.class;

        if ((numberOf2pt+numberOf4pt+numberOf1pt) > 10) {
            Toast.makeText(view.getContext(), "oops, you hit too many balls! Max of 10!", Toast.LENGTH_LONG).show();
            return;
        } else {

            //First, check if a Card already exists.
            if(card_id == -1) {
                //If no card exists, create one.
                Log.i(TAG, "We are creating a new card as none seems to exist");
                ShortGameCard sgc = new ShortGameCard(0, getStringDate(), -1, "", 99);
                card_id = (int)scDbHelper.createShortGameCard(sgc);
            }

            int hcap = getHcapFromScore(getTotalScore());
            ssd.setDrillHandicap(hcap);
            ssd.setDate_played(getStringDate());
            ssd.setTotalScore(getTotalScore());
            ssd.setCardId(card_id);
            ssd.setNumberInHole(numberOf4pt);
            ssd.setNumberInside3Ft(numberOf2pt);
            ssd.setNumberInside6Ft(numberOf1pt);
            ssd.setNumberOutside(10-numberOf4pt-numberOf2pt-numberOf1pt);
            ssd.setDrillHandicap(getHcapFromScore(getTotalScore()));
            ssd.setDrillType(scDbHelper.SC_LONG_SAND_DRILL_ID);

            if (ssd.getId()==-1) {
                //Create the drill as it is new
                ssDrillID = scDbHelper.createShortGameDrill(ssd);
                ssd.setId((int)ssDrillID);
                //Toast.makeText(this, "Short Game Drill Saved ["+ssDrillID+"]", Toast.LENGTH_LONG).show();
                Log.i(TAG, "just CREATED a row!!");
            } else {
                //Update the drill as it exists
                int rowsUpdated = scDbHelper.updateShortGameDrill(ssd);
                //ssDrillID = ssd.getId();
                //Toast.makeText(this, "Short Game Drill Updated ["+ssDrillID+"]", Toast.LENGTH_LONG).show();
                Log.v(TAG, "just UPDATED a row!!");
            }

            //send us back to the SG Drills menu and pop-up message saying the drill has been saved
            // also send back the card_id as this may have been created here

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
        if (score > 12) return -8;

        switch (score) {
            case 0: return 38;
            case 1: return 32;
            case 2: return 26;
            case 3: return 21;
            case 4: return 16;
            case 5: return 11;
            case 6: return 8;
            case 7: return 5;
            case 8: return 2;
            case 9: return -1;
            case 10: return -4;
            case 11: return -6;
            case 12: return -8;
        }
        Log.e(TAG, "SC H'cap Lookup: Can't find hcap for value: "+score);
        return 39;
    }

    /*
     * This method should clear the data from this drill. What it will actually do it delete it.
     * This is actually different to having all zero values.
     */

    public void clearLongSandDrill(View view){
        Log.i(TAG,"clearing LS Drill");

        if(ssd.getId() != -1) {
            try {
                //if the drill exists, then delete it.
                scDbHelper.deleteShortGameDrill(ssd.getId());
                Log.i(TAG,"LS Drill deleted successfully");
            }catch (Exception e) {
                Log.e(TAG, "Failed to delete a Long Sand Drill on clearing");
            }
        }
        ssd = null;
        Intent i = new Intent(this, ShortGameDrillsActivity.class);
        i.putExtra("CARD_ID", card_id);
        startActivity(i);
    }
}
