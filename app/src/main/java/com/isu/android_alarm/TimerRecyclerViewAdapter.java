package com.isu.android_alarm;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class TimerRecyclerViewAdapter extends RecyclerView.Adapter<TimerRecyclerViewAdapter.ViewHolder> {
    private ArrayList<Timer> timers;
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

    public TimerRecyclerViewAdapter(Context context, ArrayList<Timer> timers) {
        this.context = context;
        this.timers = timers;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TimerRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view =  inflater.inflate(R.layout.timer_row_layout, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Timer timer = timers.get(position);
        if(timer.getTimeStr().equals("0 min, 0 sec")) {
            remove(position);
        }
        else {
            final String message = timer.getMessage();
            holder.txtHeader.setText(timer.getTimeStr());
            holder.txtFooter.setText(message);
            holder.itemView.setTag(timer);
            holder.txtHeader.setOnClickListener(v -> {
                deleteDialog(position);
            });
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return timers.size();
    }

    public void add(int position, Timer timer) {
        timers.add(position, timer);
        notifyItemInserted(position);
        notifyDataSetChanged();
    }

    public void add(Timer timer) {
        timers.add(timer);
        notifyItemInserted(getItemCount()-1);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        if(!timers.get(position).getTimeStr().equals("0 min, 0 sec")) {
            timers.get(position).CancelCountDown();
        }
        timers.remove(position);
        //notifyItemRemoved(position);
    }

    private void deleteDialog(int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete timer");

        // Set up the buttons
        builder.setPositiveButton("Delete", (dialog, which) -> {
            remove(position);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }
}
