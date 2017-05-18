package com.chavez.eduardo.recyclerview;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import BaseDeDatos.EventContract;
import BaseDeDatos.EventModel;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ViewEventFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ViewEventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewEventFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ID_ARG = "ID";

    // TODO: Rename and change types of parameters
    private int ID_GOT;


    private TextView name, description, date, progressText;
    private ProgressBar progressEvent;
    private EventModel eventModel;
    private ArrayList<Event> events = new ArrayList<>();
    private ArrayList<Updates> updates = new ArrayList<>();
    private String nombre, descripcion, fecha;
    private double progreso;
    private int progressInt;
    private Button deleteButton, updateButton;

    //UPDATES
    private String updateDescription, updateNombre;
    private int updateID;
    private double updateProgress;

    RecyclerView recyclerView;


    private OnFragmentInteractionListener mListener;

    public ViewEventFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ViewEventFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewEventFragment newInstance(int param1) {
        ViewEventFragment fragment = new ViewEventFragment();
        Bundle args = new Bundle();
        args.putInt(ID_ARG, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ID_GOT = getArguments().getInt(ID_ARG);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        eventModel = new EventModel(getContext());
        name = (TextView) getActivity().findViewById(R.id.progressName);
        description = (TextView) getActivity().findViewById(R.id.progressDescription);
        date = (TextView) getActivity().findViewById(R.id.progressDate);
        progressText = (TextView) getActivity().findViewById(R.id.progressEvent);
        progressEvent = (ProgressBar) getActivity().findViewById(R.id.progressBar);
        deleteButton = (Button) getActivity().findViewById(R.id.buttonDelete);
        updateButton = (Button) getActivity().findViewById(R.id.buttonUpdate);
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recyclerUpdates);


        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Eliminar elemento")
                        .setMessage("¿Desea eliminar: " + nombre + "?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                eventModel.delete(ID_GOT);
                                eventModel.deleteUpdates(ID_GOT);
                                Snackbar.make(getView(), "Elemento eliminado", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                                ((MainActivity)getActivity()).setID_ACTUAL(0);
                                getActivity().getSupportFragmentManager().popBackStack();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("ID", ID_GOT);

                Fragment fragment = new EditEventFragment();
                fragment.setArguments(bundle);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.viewFragment, fragment)
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_event, container, false);
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
    public void onResume() {
        super.onResume();
        mostrarEvento();

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

    public void mostrarEvento() {
        events.clear();
        updates.clear();
        Cursor c = eventModel.getEventData(ID_GOT);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            nombre = c.getString(c.getColumnIndex(EventContract.COLUMN_NAME_EVENT));
            descripcion = c.getString(c.getColumnIndex(EventContract.COLUMN_DESCRIPTION_EVENT));
            fecha = c.getString(c.getColumnIndex(EventContract.COLUMN_DATE_EVENT));
            progreso = c.getDouble(c.getColumnIndex(EventContract.COLUMN_PROGRESS));

            events.add(new Event(ID_GOT, nombre, progreso, descripcion, fecha));

            c.moveToNext();
        }

        Cursor d = eventModel.getUpdateData(ID_GOT);
        d.moveToFirst();
        while (!d.isAfterLast()) {
            updateID = d.getInt(d.getColumnIndex(EventContract._ID_ACTUALIZACION));
            updateDescription = d.getString(d.getColumnIndex(EventContract.UPDATE_DETAIL));
            updateNombre = d.getString(d.getColumnIndex(EventContract.UPDATE_NAME));
            updateProgress = d.getDouble(d.getColumnIndex(EventContract.UPDATE_PROGRESS));

            updates.add(new Updates(updateID, updateDescription, updateProgress, updateNombre));
            d.moveToNext();
        }

        name.setText(events.get(0).getEventName());
        description.setText("Descripcion: "+events.get(0).getEventDescription());
        date.setText("Fecha programada: "+events.get(0).getEventDate());
        progressText.setText("Progreso actual: "+String.valueOf(events.get(0).getEventProgress())+"%");
        progressInt = (int) events.get(0).getEventProgress();
        progressEvent.setProgress(progressInt);

        recyclerView.setAdapter(new UpdatesItemAdapter(updates, getContext()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    }
}
