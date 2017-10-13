package com.cili.ivan.belotblock.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cili.ivan.belotblock.R;
import com.cili.ivan.belotblock.model.Game;

import java.util.ArrayList;
import java.util.Calendar;

public class GamesAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Game> data;
    private static LayoutInflater inflater = null;

    public GamesAdapter(Context context, ArrayList<Game> data) {
        this.context = context;
        this.data = data;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;

        if (vi == null) {
            vi = inflater.inflate(R.layout.row_games_list, null);
        }

        Game game = data.get(position);

        TextView title = (TextView) vi.findViewById(R.id.item_games_list_title);
        TextView date = (TextView) vi.findViewById(R.id.item_games_list_date);

        title.setText(game.getTeamOneName() + " vs. " + game.getTeamTwoName());
        date.setText(getDateFromMillis(game.getGameCreatedOn()));

        return vi;
    }

    private static String getDateFromMillis(String millis) {
        Calendar calendar;

        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(millis));

        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        int mHour = calendar.get(Calendar.HOUR_OF_DAY);
        int mMinute = calendar.get(Calendar.MINUTE);

        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        int nowYear = calendar.get(Calendar.YEAR);
        int nowMonth = calendar.get(Calendar.MONTH);
        int nowDay = calendar.get(Calendar.DAY_OF_MONTH);

        if (nowDay == mDay && nowMonth == mMonth && nowYear == mYear) {
            return String.format("%02d:%02d", mHour, mMinute);
        }

        return String.format("%d/%d/%d", mDay, mMonth, mYear % 100);
    }

}
