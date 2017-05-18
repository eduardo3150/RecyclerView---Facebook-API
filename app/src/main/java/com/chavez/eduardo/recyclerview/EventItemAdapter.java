package com.chavez.eduardo.recyclerview;




import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Eduardo_Chavez on 25/2/2017.
 */

public class EventItemAdapter extends RecyclerView.Adapter<EventItemAdapter.EventViewHolder> {
    private ArrayList<Event> events = new ArrayList<>();
    private Context context;
    Fragment fragmentOne;
    private int progress;
    private int ID;


    private Callback callback;


    public EventItemAdapter(ArrayList<Event> events,Context context, Fragment fragment){
        this.events = events;
        this.context = context;
        this.fragmentOne = fragment;

    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row,parent,false);
        return new EventViewHolder(row);
    }

    @Override
    public void onBindViewHolder(final EventItemAdapter.EventViewHolder holder, final int position) {
        final Event event=events.get(position);

        holder.eventName.setText(event.getEventName());
        holder.eventProgress.setText(String.valueOf(event.getEventProgress())+"%");
        progress = (int) (event.getEventProgress());
        holder.eventBar.setProgress(progress);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callback!=null){
                    callback.onButtonClicked(event.getId());
                    Log.d("Exito", "Capturado feedback");
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putInt("ID",event.getId());
                ((MainActivity)fragmentOne.getActivity()).setID_ACTUAL(event.getId());
                Fragment fragment = new ViewEventFragment();
                fragment.setArguments(bundle);

                fragmentOne.getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.viewFragment, fragment)
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();

                return true;
            }
        });

    }
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }


    @Override
    public int getItemCount() {
        return events.size();
    }



    public void setCallback(Callback callback){
        this.callback = callback;
    }

    public class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView eventName;
        private TextView eventProgress;
        private ProgressBar eventBar;

        public EventViewHolder(View itemView) {
            super(itemView);
            eventName = (TextView)itemView.findViewById(R.id.eventName);
            eventProgress = (TextView) itemView.findViewById(R.id.eventProgress);
            eventBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
            eventBar.setMax(100);
            itemView.setOnClickListener(this);

        }

        public TextView getEventName() {
            return eventName;
        }

        public void setEventName(TextView eventName) {
            this.eventName = eventName;
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

        @Override
        public void onClick(View view) {

        }
    }

    public interface Callback{
        void onButtonClicked(int ID);
    }



}
