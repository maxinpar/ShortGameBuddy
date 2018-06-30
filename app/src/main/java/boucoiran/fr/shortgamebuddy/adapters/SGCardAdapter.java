package boucoiran.fr.shortgamebuddy.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import boucoiran.fr.shortgamebuddy.R;
import boucoiran.fr.shortgamebuddy.activities.stats.ShowCardsActivity;
import boucoiran.fr.shortgamebuddy.models.ShortGameCard;
import boucoiran.fr.shortgamebuddy.utils.GolfPracticeDBHelper;

public class SGCardAdapter extends ArrayAdapter<ShortGameCard> implements View.OnClickListener {
    private Context mContext;
    private GolfPracticeDBHelper helper;

    private static class ViewHolder {
        TextView date;
        ImageView complete;
        TextView score;
        TextView hcap;
        ImageView del;
    }

    public SGCardAdapter(ArrayList<ShortGameCard> data, Context context) {
        super(context, R.layout.sg_card_row_item, data);
        this.mContext=context;
        this.helper = new GolfPracticeDBHelper(mContext);
    }

    @Override
    public void onClick(View v) {

    }


    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        final ShortGameCard sgc = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.sg_card_row_item, parent, false);
            viewHolder.date = (TextView) convertView.findViewById(R.id.dateView);
            viewHolder.complete = (ImageView) convertView.findViewById(R.id.completeView);
            viewHolder.score = (TextView) convertView.findViewById(R.id.scoreView);
            viewHolder.hcap = (TextView) convertView.findViewById(R.id.hcapView);
            viewHolder.del = (ImageView) convertView.findViewById(R.id.delView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.del.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.i("testing", "mContext = "+mContext);
                AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
                alertDialog.setTitle("Delete Card");
                alertDialog.setMessage("Are you sure you want to delete this short game card?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "DELETE",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                assert sgc != null;
                                Log.i("testing", "Deleting card"+sgc.getId());
                                helper.deleteShortGameCard(sgc.getId());
                                Intent i = new Intent(mContext, ShowCardsActivity.class);
                                mContext.startActivity(i);
                                dialog.dismiss();
                            }
                        });
               alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                alertDialog.show();
            }
        });


        if (sgc != null) {
            if(sgc.getIsComplete() == 1) {
                viewHolder.complete.setImageDrawable(mContext.getResources().getDrawable(mContext.getResources().getIdentifier("@drawable/cardtick", "drawable", mContext.getPackageName())));
            } else {
                viewHolder.complete.setImageDrawable(mContext.getResources().getDrawable(mContext.getResources().getIdentifier("@drawable/cardcross", "drawable", mContext.getPackageName())));
            }
        }

        String sScore;
        String sCap;
        String sDate;

        if((sgc != null ? sgc.getLastUpdated() : null) == null)
            sDate = "No Date Set";
        else
            sDate = sgc.getLastUpdated();

        sCap = String.valueOf(sgc.getHandicap());
        Log.i("Testing", "setting card hcap of " + sCap);
        sScore = String.valueOf(sgc.getTotalScore());

        viewHolder.date.setText(sDate);
        viewHolder.score.setText(sScore);
        viewHolder.hcap.setTag(position);
        viewHolder.hcap.setText(sCap);
//        viewHolder.hcap.setOnClickListener(this);
        // Return the completed view to render on screen
        return convertView;
    }
}
