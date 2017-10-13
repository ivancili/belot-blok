package com.cili.ivan.belotblock.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cili.ivan.belotblock.R;
import com.cili.ivan.belotblock.model.Session;

import java.util.ArrayList;

public class SessionsAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Session> data;
    private static LayoutInflater inflater = null;

    public SessionsAdapter(Context context, ArrayList<Session> data) {
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
            vi = inflater.inflate(R.layout.row_session_list, null);
        }

        Session session = data.get(position);

        TextView us = (TextView) vi.findViewById(R.id.row_session_us);
        TextView them = (TextView) vi.findViewById(R.id.row_session_them);

        us.setText(session.getTeamOneScore());
        them.setText(session.getTeamTwoScore());

        return vi;
    }

}
