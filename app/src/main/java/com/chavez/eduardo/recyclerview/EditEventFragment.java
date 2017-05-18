package com.chavez.eduardo.recyclerview;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Max;
import com.mobsandgeeks.saripaar.annotation.Min;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.ArrayList;
import java.util.List;

import BaseDeDatos.EventContract;
import BaseDeDatos.EventModel;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditEventFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditEventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditEventFragment extends Fragment implements Validator.ValidationListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ID = "ID";


    // TODO: Rename and change types of parameters
    private int ID_GOT;


    //NUEVOS PARAMETROS
    @NotEmpty(message = "Ingresa un nombre")
    EditText nombreUpdated;

    @NotEmpty(message = "Ingresa una descripcion")
    EditText descripcionUpdated;

    @NotEmpty(message = "Ingresa un valor entre 0 y 100")
    EditText progressUpdated;

    Validator validator;

    private SeekBar seekBarUpdated;
    private TextView seekText;
    private Button updateEvent;
    private EventModel eventModel;
    private ArrayList<Event> events = new ArrayList<>();
    private String nombre, descripcion, fecha;
    private double progreso;
    private int progressInt;

    private OnFragmentInteractionListener mListener;

    public EditEventFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EditEventFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditEventFragment newInstance(int param1) {
        EditEventFragment fragment = new EditEventFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ID, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ID_GOT = getArguments().getInt(ARG_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_event, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        eventModel = new EventModel(getContext());
        validator = new Validator(this);
        validator.setValidationListener(this);

        nombreUpdated = (EditText) getActivity().findViewById(R.id.updateTitleEvent);
        descripcionUpdated = (EditText) getActivity().findViewById(R.id.eventUpdateDescription);
        progressUpdated = (EditText) getActivity().findViewById(R.id.eventUpdatedProgress);

        seekBarUpdated = (SeekBar) getActivity().findViewById(R.id.progressSeekBarUpdate);
        seekText = (TextView) getActivity().findViewById(R.id.progressUpdate);

        updateEvent = (Button) getActivity().findViewById(R.id.updateEvent);

        inputListeners();

        updateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validator.validate();
            }
        });


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
    public void onValidationSucceeded() {
        setNewData();
        Snackbar.make(getView(), "Elemento actualizado", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        getActivity().getSupportFragmentManager().popBackStack();

    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error:errors){
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getContext());

            if (view instanceof EditText){
                ((EditText)view).setError(message);
            } else {
                Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
            }
        }
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
        getLastData();
    }

    @Override
    public void onPause() {
        this.events.clear();
        super.onPause();
    }

    private void getLastData() {
        events.clear();

        Cursor c = eventModel.getEventData(ID_GOT);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            nombre = c.getString(c.getColumnIndex(EventContract.COLUMN_NAME_EVENT));
            descripcion = c.getString(c.getColumnIndex(EventContract.COLUMN_DESCRIPTION_EVENT));
            progreso = c.getDouble(c.getColumnIndex(EventContract.COLUMN_PROGRESS));
            fecha = c.getString(c.getColumnIndex(EventContract.COLUMN_DATE_EVENT));

            events.add(new Event(ID_GOT, nombre, progreso, descripcion, fecha));

            c.moveToNext();
        }

        nombreUpdated.setText(events.get(0).getEventName());
        descripcionUpdated.setText(events.get(0).getEventDescription());
        seekText.setText(String.valueOf(events.get(0).getEventProgress()));
        progressInt = (int) events.get(0).getEventProgress();
        seekBarUpdated.setProgress(progressInt);
        progressUpdated.setText(String.valueOf(events.get(0).getEventProgress()));


    }

    private void inputListeners() {
        seekBarUpdated.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                seekText.setText(String.valueOf(i)+"%");
                progressUpdated.setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        progressUpdated.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                seekText.setText(progressUpdated.getText()+"%");
            }
        });
    }

    private void setNewData() {
        nombre = nombreUpdated.getText().toString();
        descripcion = descripcionUpdated.getText().toString();
        progreso = Double.valueOf(progressUpdated.getText().toString());

        if (progreso<0.0){
            progreso=0;
        }

        if (progreso>100){
            progreso=100;
        }

        eventModel.updateEvent(ID_GOT,nombre,descripcion,progreso);
        eventModel.insertUpdate(ID_GOT,"Actualizacion hecha a: "+nombre+" Descripcion: "+descripcion,progreso,nombre);
        clearData();
    }

    private void clearData() {
        nombre = "";
        descripcion = "";
        fecha = "";
        progressInt = 0;

    }

}
