package com.chavez.eduardo.recyclerview;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import BaseDeDatos.EventContract;
import BaseDeDatos.EventModel;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GraphItemEvent.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GraphItemEvent#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GraphItemEvent extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ID = "ID";

    // TODO: Rename and change types of parameters
    private int ID;


    private OnFragmentInteractionListener mListener;

    LineChart lineChart;
    TextView titleGraph;
    EventModel eventModel;
    int contador = 0;

    public GraphItemEvent() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment GraphItemEvent.
     */
    // TODO: Rename and change types and number of parameters
    public static GraphItemEvent newInstance(int param1) {
        GraphItemEvent fragment = new GraphItemEvent();
        Bundle args = new Bundle();
        args.putInt(ARG_ID, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ID = getArguments().getInt(ARG_ID);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lineChart = (LineChart) getActivity().findViewById(R.id.chart);
        eventModel = new EventModel(getContext());
        titleGraph = (TextView) getActivity().findViewById(R.id.graphTitle);
    }

    @Override
    public void onResume() {
        super.onResume();
        generateGraph();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_graph_item_event, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void generateGraph() {
        contador=0;

        Cursor c = eventModel.getUpdateData(ID);
        c.moveToFirst();
        List<Entry> entries = new ArrayList<>();
        String name="";
        while (!c.isAfterLast()){

            entries.add(new Entry(contador,
                    c.getFloat(c.getColumnIndex(EventContract.UPDATE_PROGRESS)),
                    c.getString(c.getColumnIndex(EventContract.UPDATE_DETAIL)))
            );
            name = c.getString(c.getColumnIndex(EventContract.UPDATE_NAME));
            contador++;
            c.moveToNext();
        }

        eventModel.database.close();
        titleGraph.setText("Avance para: "+name);

        LineDataSet dataSet = new LineDataSet(entries,"Progreso en proposito");
        dataSet.setLineWidth(2.5f);
        dataSet.setCircleRadius(4.5f);
        dataSet.setHighLightColor(Color.rgb(244, 117, 117));
        dataSet.setDrawValues(true);
        dataSet.setValueTextColor(R.color.colorPrimary);
        dataSet.setValueTextColor(R.color.colorAccent);
        Description description = new Description();
        description.setText(name);
        description.setEnabled(false);
        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.setDescription(description);
        lineChart.animateX(5000, Easing.EasingOption.EaseOutBack);
        lineChart.invalidate();

    }
}
