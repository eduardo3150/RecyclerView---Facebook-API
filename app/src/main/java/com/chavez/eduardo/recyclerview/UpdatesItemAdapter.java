package com.chavez.eduardo.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Eduardo_Chavez on 26/2/2017.
 */

public class UpdatesItemAdapter extends RecyclerView.Adapter<UpdatesItemAdapter.UpdatesViewHolder> {

    ArrayList<Updates> updates = new ArrayList<>();
    Context context;
    private int progress;

    public UpdatesItemAdapter(ArrayList<Updates> updates,Context context){
        this.updates = updates;
        this.context = context;
    }


    @Override
    public UpdatesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_update_row,parent,false);

        return new UpdatesViewHolder(row);
    }

    @Override
    public void onBindViewHolder(UpdatesViewHolder holder, int position) {
        Updates update = updates.get(position);

        holder.eventDescription.setText(update.getUpdateDetail());
        holder.eventProgress.setText(String.valueOf(update.getUpdateProgress())+"%");
        progress = (int) update.getUpdateProgress();
        holder.eventBar.setProgress(progress);


    }

    @Override
    public int getItemCount() {
        return updates.size();
    }

    public class UpdatesViewHolder extends RecyclerView.ViewHolder {
        private TextView eventDescription;
        private TextView eventProgress;
        private ProgressBar eventBar;

        public UpdatesViewHolder(View itemView) {
            super(itemView);
            eventDescription = (TextView) itemView.findViewById(R.id.eventLastDescription);
            eventProgress = (TextView) itemView.findViewById(R.id.eventLastProgress);
            eventBar = (ProgressBar) itemView.findViewById(R.id.lastProgressBar);
            eventBar.setMax(100);
        }

        public TextView getEventDescription() {
            return eventDescription;
        }

        public void setEventDescription(TextView eventDescription) {
            this.eventDescription = eventDescription;
        }

        public TextView getEventProgress() {
            return eventProgress;
        }

        public void setEventProgress(TextView eventProgress) {
            this.eventProgress = eventProgress;
        }

        public ProgressBar getEventBar() {
            return eventBar;
        }

        public void setEventBar(ProgressBar eventBar) {
            this.eventBar = eventBar;
        }
    }
}
