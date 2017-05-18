package com.chavez.eduardo.recyclerview;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import BaseDeDatos.EventContract;
import BaseDeDatos.EventModel;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecyclerViewFgr.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecyclerViewFgr#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecyclerViewFgr extends Fragment implements EventItemAdapter.Callback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //PARAMETROS
    ArrayList<Event> events = new ArrayList<>();
    EventModel eventModel;
    private String nombre, descripcion, fecha;
    private double progreso;
    private int ID;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    EventItemAdapter eventItemAdapter;

    public RecyclerViewFgr() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecyclerViewFgr.
     */
    // TODO: Rename and change types and number of parameters
    public static RecyclerViewFgr newInstance(String param1, String param2) {
        RecyclerViewFgr fragment = new RecyclerViewFgr();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_recycler_view_fgr, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        eventModel = new EventModel(getContext());
        //getContent();
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

    @Override
    public void onButtonClicked(int ID) {
        //Toast.makeText(getContext(),String.valueOf(ID),Toast.LENGTH_LONG).show();
        Snackbar.make(getView(), "Elemento seleccionado", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        ((MainActivity)getActivity()).setID_ACTUAL(ID);
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

    @Override
    public void onResume() {
        super.onResume();
        getContent();
        ((MainActivity) getActivity()).fab.show();
    }

    @Override
    public void onPause() {
        //this.events.clear();
        ((MainActivity) getActivity()).hideFloat();
        super.onPause();
    }

    private void getContent() {
        events.clear();

        Cursor c = eventModel.listEventsOrder();

        c.moveToFirst();
        while (!c.isAfterLast()){
            ID = c.getInt(c.getColumnIndex(EventContract._ID));
            nombre = c.getString(c.getColumnIndex(EventContract.COLUMN_NAME_EVENT));
            descripcion = c.getString(c.getColumnIndex(EventContract.COLUMN_DESCRIPTION_EVENT));
            fecha = c.getString(c.getColumnIndex(EventContract.COLUMN_DATE_EVENT));
            progreso = c.getDouble(c.getColumnIndex(EventContract.COLUMN_PROGRESS));

            events.add(new Event(ID,nombre,progreso,descripcion,fecha));

            c.moveToNext();

        }

        eventModel.database.close();

        eventItemAdapter = new EventItemAdapter(events,getContext(),this);

        RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.recyclerView);
        recyclerView.setAdapter(eventItemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        eventItemAdapter.setCallback(this);


    }



}
