package boucoiran.fr.shortgamebuddy.activities.menus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import boucoiran.fr.shortgamebuddy.R;
import boucoiran.fr.shortgamebuddy.activities.putting.PuttingDrillsMenuActivity;


/*
 * This is the Activity for the Putting Menu. Really only used to send the user to one of the three
 * places they can go to based on button clicks:
 *      - Score Input (for putting drills)
 *      - View (Putting) cards
 *      - View (Putting) stats
 */
public class PuttingMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_putting_menu);
    }

    public void scoreInput(View view) {
        //open the chipping drills menu
        Intent i = new Intent(this, PuttingDrillsMenuActivity.class);
        startActivity(i);
    }

    public void viewCards(View view) {
        //open the chipping drills menu
        //TODO uncomment this once built
//        Intent i = new Intent(this, PuttingViewCardsActivity.class);
        //      startActivity(i);
    }

    public void stats(View view) {
        //open the chipping drills menu
        //TODO uncomment this once built
        //    Intent i = new Intent(this, PuttingStatsActivity.class);
        //  startActivity(i);
    }
}
