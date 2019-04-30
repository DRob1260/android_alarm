package com.isu.android_alarm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

interface IAdapter extends Serializable {
    void setDataAt(int index, Alarm alarm);
    void notifyItemChanged(int index);
}

public class AlarmRecyclerViewAdapter extends RecyclerView.Adapter<AlarmRecyclerViewAdapter.ViewHolder> {
    private ArrayList<Alarm> alarms;
    private IAdapter adapter;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtHeader;
        public TextView txtFooter;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            txtHeader = (TextView) v.findViewById(R.id.firstLine);
            txtFooter = (TextView) v.findViewById(R.id.secondLine);
        }
    }

    public AlarmRecyclerViewAdapter(ArrayList<Alarm> alarms) {
        this.alarms = alarms;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AlarmRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view =  inflater.inflate(R.layout.alarm_row_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Alarm alarm = alarms.get(position);
        final String message = alarm.getMessage();
        holder.txtHeader.setText(alarm.getTimeStr());
        holder.txtFooter.setText(message);
        holder.itemView.setTag(alarm);
        holder.txtHeader.setOnClickListener(v -> {
            //remove(position);
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        System.out.println("Size of alarms from Adapter is " + alarms.size());
        return alarms.size();
    }

    public void add(int position, Alarm alarm) {
        alarms.add(position, alarm);
        notifyItemInserted(position);
        notifyDataSetChanged();
    }

    public void add(Alarm alarm) {
        alarms.add(alarm);
        notifyItemInserted(getItemCount()-1);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        alarms.remove(position);
        notifyItemRemoved(position);
    }

    public void update(ArrayList<Alarm> alarms){
        this.alarms.clear();
        this.alarms.addAll(alarms);
        notifyDataSetChanged();
    }
}
