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


public class RoughChipScoreInputActivity extends AppCompatActivity {

    private static String TAG = "RoughChipScoreInputActivity";
    private static NumberPicker np1;
    private static NumberPicker np2;
    private static NumberPicker np3;

    private GolfPracticeDBHelper scDbHelper;
    private static GenericShortGameDrill scd;
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
        int totScore = np1.getValue() + 2* np2.getValue() + 4* np3.getValue();
        return totScore;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rough_chip_score_input);
        scDbHelper = new GolfPracticeDBHelper(this);

        setTitle("Chip From Rough Drill");

        card_id = getIntent().getIntExtra("CARD_ID", -1);
        if (card_id != -1) {
            try{
                scd = scDbHelper.getShortGameDrill(card_id, scDbHelper.SC_ROUGH_CHIP_DRILL_ID);
            } catch (Exception e) {
                Log.i(TAG, "Failed to load rough chip object when starting Activity. Card id is " + card_id);
                Log.i(TAG, "setting drill to null");
                scd = null;
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

        if(scd != null) {
            //attempt to instantiate the Short Chip Drill object from DB
            np1.setValue(scd.getNumberInside6Ft());
            np2.setValue(scd.getNumberInside3Ft());
            np3.setValue(scd.getNumberInHole());
        } else {
            //create a blank SC Drill object
            scd = new GenericShortGameDrill(-1, getStringDate(), 0, card_id, "", 0, 0, 0, 0, "", 999, scDbHelper.SC_ROUGH_CHIP_DRILL_ID);
        }

        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.main_layout);

        mainLayout.setOnTouchListener(new OnSwipeTouchListener(RoughChipScoreInputActivity.this) {
            public void onSwipeLeft() {
                iDest = LobShotScoreInputActivity.class;
                saveDrill(null);
            }
            public void onSwipeRight() {
                iDest = LongPitchScoreInputActivity.class;
                saveDrill(null);
            }
        });
    }

    /*
     * This will update the  Drill Java object and save it to DB.
     * If necessary, a Card Object will also be saved.
     */

    public void saveDrill(View view){
        long scDrillID;

        //If view is null then this means we are calling this save method from a SWIPE event.
        //If view is not null, it means we are calling this from the save button in Activity
        if(view != null) iDest = ShortGameDrillsActivity.class;

        if ((np1.getValue()+np2.getValue()+np3.getValue()) > 10) {
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
            scd.setDrillHandicap(hcap);
            scd.setDate_played(getStringDate());
            scd.setTotalScore(getTotalScore());
            scd.setCardId(card_id);
            scd.setNumberInHole(np3.getValue());
            scd.setNumberInside3Ft(np2.getValue());
            scd.setNumberInside6Ft(np1.getValue());
            scd.setNumberOutside(10-np3.getValue()-np2.getValue()-np1.getValue());
            scd.setDrillType(scDbHelper.SC_ROUGH_CHIP_DRILL_ID);

            if (scd.getId()==-1) {
                //Create the drill as it is new
                scDrillID = scDbHelper.createShortGameDrill(scd);
                scd.setId((int)scDrillID);
            } else {
                //Update the drill as it exists
                int rowsUpdated = scDbHelper.updateShortGameDrill(scd);
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
        if (score > 17) return -8;

        switch (score) {
            case 0: return 38;
            case 1: return 33;
            case 2: return 29;
            case 3: return 26;
            case 4: return 23;
            case 5: return 20;
            case 6: return 17;
            case 7: return 14;
            case 8: return 11;
            case 9: return 8;
            case 10: return 5;
            case 11: return 2;
            case 12: return 0;
            case 13: return -2;
            case 14: return -4;
            case 15: return -6;
            case 16: return -7;
            case 17: return -8;
        }
        Log.e("RC Hcap Lookup", "Can't find hcap for value: "+score);
        return 40;
    }

    /*
     * This method should clear the data from this drill. What it will actually do it delete it.
     * This is actually different to having all zero values.
     */

    public void clearDrill(View view){
        Log.i(TAG,"clearing RC Drill");

        if(scd.getId() != -1) {
            try {
                //if the drill exists, then delete it.
                scDbHelper.deleteShortGameDrill(scd.getId());
                Log.i(TAG,"RC Drill deleted successfully");
            }catch (Exception e) {
                Log.e(TAG, "Failed to delete a Rough Chip Drill on clearing");
            }
        }

        Intent i = new Intent(this, ShortGameDrillsActivity.class);
        i.putExtra("CARD_ID", card_id);
        startActivity(i);
    }
}
