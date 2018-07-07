package boucoiran.fr.shortgamebuddy.activities.stats;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import boucoiran.fr.shortgamebuddy.R;
import boucoiran.fr.shortgamebuddy.activities.putting.PuttingDrillsMenuActivity;
import boucoiran.fr.shortgamebuddy.adapters.PCardAdapter;
import boucoiran.fr.shortgamebuddy.models.PuttingCard;
import boucoiran.fr.shortgamebuddy.utils.GolfPracticeDBHelper;

public class ShowPuttingCardsActivity extends AppCompatActivity {

    private String TAG = "in ShowPuttCardsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_cards);

        GolfPracticeDBHelper practiceDBHelper = new GolfPracticeDBHelper(this);
        ListView mListView = (ListView) findViewById(R.id.recipe_list_view);

        ArrayList<PuttingCard> cardList = practiceDBHelper.getPuttingCardsFromDB();

        PCardAdapter adapter = new PCardAdapter(cardList, this);

        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "View clicked is " + id);
                Log.d(TAG, "View clicked is " + view.getId());
                // Log.d(TAG, "View text clicked is ");
                Intent i = new Intent(ShowPuttingCardsActivity.this, PuttingDrillsMenuActivity.class);
                PuttingCard entry = (PuttingCard) parent.getItemAtPosition(position);
                i.putExtra("CARD_ID", entry.getId());
                startActivity(i);
            }
        });
    }
}
