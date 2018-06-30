package boucoiran.fr.shortgamebuddy.stats.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import ChippingDrills.ShortGameCard;
import boucoiran.fr.shortgamebuddy.R;
import boucoiran.fr.shortgamebuddy.ShortGameDrillsActivity;
import data.GolfPracticeDBHelper;
import data.SGCardAdapter;

public class ShowCardsActivity extends AppCompatActivity {

    private ListView mListView;
    private GolfPracticeDBHelper practiceDBHelper;
    private static SGCardAdapter adapter;
    private String TAG = "in ShowCardsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_cards);

        practiceDBHelper = new GolfPracticeDBHelper(this);
        mListView = (ListView) findViewById(R.id.recipe_list_view);

        ArrayList<ShortGameCard> cardList = practiceDBHelper.getCardsFromDB();

        adapter = new SGCardAdapter(cardList, this);

        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "View clicked is "+id);
                Log.d(TAG, "View clicked is "+view.getId());
               // Log.d(TAG, "View text clicked is ");
                Intent i = new Intent(ShowCardsActivity.this, ShortGameDrillsActivity.class);
                ShortGameCard entry = (ShortGameCard) parent.getItemAtPosition(position);
                //TODO: how to get Card_Id
                i.putExtra("CARD_ID", entry.getId());

                startActivity(i);
            }
        });
    }
}
