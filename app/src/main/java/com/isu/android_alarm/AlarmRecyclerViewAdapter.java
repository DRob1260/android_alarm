package com.isu.android_alarm;

import android.app.AlertDialog;
import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Date;

import androidx.recyclerview.widget.RecyclerView;

public class AlarmRecyclerViewAdapter extends RecyclerView.Adapter<AlarmRecyclerViewAdapter.ViewHolder> {
    private ArrayList<Alarm> alarms;
    private Context context;

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

    public AlarmRecyclerViewAdapter(Context context, ArrayList<Alarm> alarms) {
        this.context = context;
        this.alarms = alarms;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AlarmRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view =  inflater.inflate(R.layout.alarm_row_layout, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Alarm alarm = alarms.get(position);
        final String message = alarm.getMessage();
        holder.txtHeader.setText(alarm.getTimeStr());
        if(alarm.getRepeating())
            holder.txtFooter.setText(message + ", every " + alarm.getDay());
        else holder.txtFooter.setText(message);
        holder.itemView.setTag(alarm);
        holder.txtHeader.setOnClickListener(v -> {
            deleteDialog(position);
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

    private void deleteDialog(int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Alarm");

        // Set up the buttons
        builder.setPositiveButton("Delete", (dialog, which) -> {
           remove(position);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }
}
