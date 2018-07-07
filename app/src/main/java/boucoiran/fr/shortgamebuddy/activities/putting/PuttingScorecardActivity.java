package boucoiran.fr.shortgamebuddy.activities.putting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import boucoiran.fr.shortgamebuddy.R;
import boucoiran.fr.shortgamebuddy.activities.menus.MainMenuActivity;
import boucoiran.fr.shortgamebuddy.utils.GolfPracticeDBHelper;


public class PuttingScorecardActivity extends AppCompatActivity {

    private static int card_id = -1;
    private GolfPracticeDBHelper scDbHelper;
    private final String TAG = "PuttingScorecardAct";
    private Class iDest = null;

    /***********************************************************************************************
     * OnCreate will do several things:
     *      - It will attempt to get a Card_Id
     *      - For each drill contained in the card it will display the drill score and appropriate
     *      gradient
     **********************************************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "we are creating the scorecard");
        super.onCreate(savedInstanceState);
        scDbHelper = new GolfPracticeDBHelper(this);
        setContentView(R.layout.activity_putting_scorecard);

        card_id = getIntent().getIntExtra("CARD_ID", -1);
        updateTextViews();
        //updateGradients();
    }

    private void updateTextViews() {
        TextView[] textViewArray = new TextView[10];
        TextView[] hcapArray = new TextView[10];
        ImageView[] gradientArray = new ImageView[10];

        hcapArray[0] = (TextView) findViewById(R.id.P_Scorecard_3F_Hcap);
        hcapArray[1] = (TextView) findViewById(R.id.P_Scorecard_6F_Hcap);
        hcapArray[2] = (TextView) findViewById(R.id.P_Scorecard_MaP_Hcap);
        hcapArray[3] = (TextView) findViewById(R.id.P_Scorecard_MeP_Hcap);
        hcapArray[4] = (TextView) findViewById(R.id.P_Scorecard_LP_Hcap);
        hcapArray[5] = (TextView) findViewById(R.id.P_Scorecard_BB_Hcap);
        hcapArray[6] = (TextView) findViewById(R.id.P_Scorecard_TOTAL_Hcap);

        gradientArray[0] = (ImageView) findViewById(R.id.S3F_Gradient);
        gradientArray[1] = (ImageView) findViewById(R.id.S6F_Gradient);
        gradientArray[2] = (ImageView) findViewById(R.id.MaP_Gradient);
        gradientArray[3] = (ImageView) findViewById(R.id.MeP_Gradient);
        gradientArray[4] = (ImageView) findViewById(R.id.LP_Gradient);
        gradientArray[5] = (ImageView) findViewById(R.id.BB_Gradient);
        gradientArray[6] = (ImageView) findViewById(R.id.TOTAL_Gradient);


        TextView scTV = (TextView) findViewById(R.id.P_Scorecard_3F_Score);
        TextView lcTV = (TextView) findViewById(R.id.P_Scorecard_6F_Score);
        TextView ssTV = (TextView) findViewById(R.id.P_Scorecard_MaP_Score);
        TextView lsTV = (TextView) findViewById(R.id.P_Scorecard_MeP_Score);
        TextView spTV = (TextView) findViewById(R.id.P_Scorecard_LP_Score);
        TextView mpTV = (TextView) findViewById(R.id.P_Scorecard_BB_Score);
        TextView totalTV = (TextView) findViewById(R.id.P_Scorecard_TOTAL_Score);

        textViewArray[0] = scTV;
        textViewArray[1] = lcTV;
        textViewArray[2] = ssTV;
        textViewArray[3] = lsTV;
        textViewArray[4] = spTV;
        textViewArray[5] = mpTV;
        textViewArray[6] = totalTV;

        if (card_id == -1) {
            for (int i = 0; i < 7; i++) {
                textViewArray[i].setText("No Data for this drill");
            }
            return;
        }

        //Below is case where we actually do have a card!!
        String[][] dataDisp = scDbHelper.getPuttScoreCardData(card_id);

        for (int i = 0; i < 7; i++) {
            Log.i(TAG, i + ") " + dataDisp[i][0] + "  |  " + dataDisp[i][2]);
        }

        for (int i = 0; i < 7; i++) {
            textViewArray[i].setText(dataDisp[i][0]);
            hcapArray[i].setText(dataDisp[i][1]);
            Log.d(TAG, "trying to get: " + "@drawable/" + dataDisp[i][2]);
            gradientArray[i].setImageDrawable(getResources().getDrawable(getResources().getIdentifier("@drawable/" + dataDisp[i][2], "drawable", getPackageName())));
        }

    }

    private void goToDrill() {
        Intent i = new Intent(this, iDest);
        i.putExtra("CARD_ID", card_id);
        startActivity(i);
    }

    public void open3f(View view) {
        iDest = Short3ftPuttScoreInputActivity.class;
        goToDrill();
    }

    public void open6f(View view) {
        iDest = Short6ftPuttScoreInputActivity.class;
        goToDrill();
    }

    public void openMaP(View view) {
        iDest = MakeablePuttScoreInputActivity.class;
        goToDrill();
    }

    public void openMeP(View view) {
        iDest = MediumPuttScoreInputActivity.class;
        goToDrill();
    }

    public void openLP(View view) {
        iDest = LagPuttScoreInputActivity.class;
        goToDrill();
    }

    public void openBB(View view) {
        iDest = BigBreakPuttScoreInputActivity.class;
        goToDrill();
    }

    public void goToPDrills(View view) {
        iDest = PuttingDrillsMenuActivity.class;
        goToDrill();
    }

    public void goToMainMenu(View view) {
        iDest = MainMenuActivity.class;
        goToDrill();
    }
}
