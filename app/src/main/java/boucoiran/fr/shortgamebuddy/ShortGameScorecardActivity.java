package boucoiran.fr.shortgamebuddy;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import data.GolfPracticeDBHelper;


public class ShortGameScorecardActivity extends AppCompatActivity {

    private static int card_id = -1;
    private GolfPracticeDBHelper scDbHelper;
    private final String TAG = "ShortGameScorecardAct";
    private Class iDest = null;



    @Override

    /***********************************************************************************************
     * OnCreate will do several things:
     *      - It will attempt to get a Card_Id
     *      - For each drill contained in the card it will display the drill score and appropriate
     *      gradient
     *      - TODO: If the card isComplete, it will also show a total score for the card
     **********************************************************************************************/

    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "we are creating the scorecard");
        super.onCreate(savedInstanceState);
        scDbHelper = new GolfPracticeDBHelper(this);
        setContentView(R.layout.activity_short_game_scorecard);

        card_id = getIntent().getIntExtra("CARD_ID", -1);
        updateTextViews();
        //updateGradients();
    }

    private void updateTextViews() {
        Resources res = getResources();
        TextView[] textViewArray = new TextView[10];
        TextView[] hcapArray = new TextView[10];
        ImageView[] gradientArray = new ImageView[10];

        hcapArray[0] = (TextView)findViewById(R.id.SG_Scorecard_SC_Hcap);
        hcapArray[1] = (TextView)findViewById(R.id.SG_Scorecard_LC_Hcap);
        hcapArray[2] = (TextView)findViewById(R.id.SG_Scorecard_SS_Hcap);
        hcapArray[3] = (TextView)findViewById(R.id.SG_Scorecard_LS_Hcap);
        hcapArray[4] = (TextView)findViewById(R.id.SG_Scorecard_SP_Hcap);
        hcapArray[5] = (TextView)findViewById(R.id.SG_Scorecard_MP_Hcap);
        hcapArray[6] = (TextView)findViewById(R.id.SG_Scorecard_LP_Hcap);
        hcapArray[7] = (TextView)findViewById(R.id.SG_Scorecard_RC_Hcap);
        hcapArray[8] = (TextView)findViewById(R.id.SG_Scorecard_CL_Hcap);
        hcapArray[9] = (TextView)findViewById(R.id.SG_Scorecard_TOTAL_Hcap);

        gradientArray[0] = (ImageView)findViewById(R.id.SC_Gradient);
        gradientArray[1] = (ImageView)findViewById(R.id.LC_Gradient);
        gradientArray[2] = (ImageView)findViewById(R.id.SS_Gradient);
        gradientArray[3] = (ImageView)findViewById(R.id.LS_Gradient);
        gradientArray[4] = (ImageView)findViewById(R.id.SP_Gradient);
        gradientArray[5] = (ImageView)findViewById(R.id.MP_Gradient);
        gradientArray[6] = (ImageView)findViewById(R.id.LP_Gradient);
        gradientArray[7] = (ImageView)findViewById(R.id.RC_Gradient);
        gradientArray[8] = (ImageView)findViewById(R.id.CL_Gradient);
        gradientArray[9] = (ImageView)findViewById(R.id.TOTAL_Gradient);


        TextView scTV = (TextView)findViewById(R.id.SG_Scorecard_SC_Score);
        TextView lcTV = (TextView)findViewById(R.id.SG_Scorecard_LC_Score);
        TextView ssTV = (TextView)findViewById(R.id.SG_Scorecard_SS_Score);
        TextView lsTV = (TextView)findViewById(R.id.SG_Scorecard_LS_Score);
        TextView spTV = (TextView)findViewById(R.id.SG_Scorecard_SP_Score);
        TextView mpTV = (TextView)findViewById(R.id.SG_Scorecard_MP_Score);
        TextView lpTV = (TextView)findViewById(R.id.SG_Scorecard_LP_Score);
        TextView rcTV = (TextView)findViewById(R.id.SG_Scorecard_RC_Score);
        TextView clTV = (TextView)findViewById(R.id.SG_Scorecard_CL_Score);
        TextView totalTV = (TextView)findViewById(R.id.SG_Scorecard_TOTAL_Score);

        textViewArray[0] = scTV;
        textViewArray[1] = lcTV;
        textViewArray[2] = ssTV;
        textViewArray[3] = lsTV;
        textViewArray[4] = spTV;
        textViewArray[5] = mpTV;
        textViewArray[6] = lpTV;
        textViewArray[7] = rcTV;
        textViewArray[8] = clTV;
        textViewArray[9] = totalTV;

        if(card_id==-1) {
            for (int i=0; i<10; i++) {
                textViewArray[i].setText("No Data for this drill");
            }
            return;
        }

        //Below is case where we actually do have a card!!
        String[][] dataDisp = scDbHelper.getSGScoreCardData(card_id);

        Log.i(TAG, "we have the following data to display : \n" + dataDisp);
        for (int i=0; i<10; i++) {
            Log.i(TAG, i+") "+dataDisp[i][0] + "  |  " + dataDisp[i][2]);
        }

        for (int i=0; i<10; i++) {
            textViewArray[i].setText(dataDisp[i][0]);
            hcapArray[i].setText(dataDisp[i][1]);
            Log.d(TAG, "trying to get: "+"@drawable/"+dataDisp[i][2]);
            gradientArray[i].setImageDrawable(getResources().getDrawable(getResources().getIdentifier("@drawable/"+dataDisp[i][2], "drawable", getPackageName())));
        }

    }

    private void goToDrill() {
        Intent i = new Intent(this, iDest);
        i.putExtra("CARD_ID", card_id);
        startActivity(i);
    }

    public void openSC(View view) {
        iDest = ShortChipScoreInputActivity.class;
        goToDrill();
    }

    public void openLC(View view) {
        iDest = LongChipScoreInputActivity.class;
        goToDrill();
    }

    public void openSS(View view) {
        iDest = ShortSandScoreInputActivity.class;
        goToDrill();
    }

    public void openLS(View view) {
        iDest = LongSandScoreInputActivity.class;
        goToDrill();
    }
    public void openSP(View view) {
        iDest = ShortPitchScoreInputActivity.class;
        goToDrill();
    }
    public void openMP(View view) {
        iDest = MediumPitchScoreInputActivity.class;
        goToDrill();
    }
    public void openLP(View view) {
        iDest = LongPitchScoreInputActivity.class;
        goToDrill();
    }
    public void openRC(View view) {
        iDest = RoughChipScoreInputActivity.class;
        goToDrill();
    }
    public void openCL(View view) {
        iDest = LobShotScoreInputActivity.class;
        goToDrill();
    }
    public void goToSGDrills(View view) {
        iDest = ShortGameDrillsActivity.class;
        goToDrill();
    }
    public void goToMainMenu(View view) {
        iDest = MainMenuActivity.class;
        goToDrill();
    }


}
