package boucoiran.fr.shortgamebuddy.activities.menus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import boucoiran.fr.shortgamebuddy.R;
import boucoiran.fr.shortgamebuddy.activities.stats.ShowSGCardsActivity;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

    public void openChippingActivity(View view) {
        //open the chipping drills menu
        Intent i = new Intent(this, ShortGameMenuActivity.class);
        startActivity(i);
    }

    public void openPuttingActivity (View view) {
        //opens the putting drills menu
        Intent i = new Intent(this, PuttingMenuActivity.class);
        startActivity(i);
    }

    public void openStatsActivity (View view) {
        //open stats and games menu
        Intent i = new Intent(this, ShowSGCardsActivity.class);
        startActivity(i);
    }
}
