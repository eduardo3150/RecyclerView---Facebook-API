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
import android.widget.DatePicker;
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
 * {@link AddElement.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddElement#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddElement extends Fragment implements Validator.ValidationListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    //PARAMETROS A USAR
    @NotEmpty(message = "Ingresa un nombre")
    EditText eventName;

    @NotEmpty(message = "Ingresa una descripcion")
    EditText eventDescription;

    @NotEmpty(message = "Ingresa un valor entre 0 y 100")
    EditText eventStartValue;

    Validator validator;

    private TextView eventProgressView;
    private SeekBar eventBarValue;
    private DatePicker eventDate;
    private Button addEvent;
    private int barValue =0;
    private String name,description,date;
    private double initvalue;
    EventModel eventModel;

    private ArrayList<Event> events = new ArrayList<>();

    public AddElement() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddElement.
     */
    // TODO: Rename and change types and number of parameters
    public static AddElement newInstance(String param1, String param2) {
        AddElement fragment = new AddElement();
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
        return inflater.inflate(R.layout.fragment_add_element, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        eventModel = new EventModel(getContext());
        validator = new Validator(this);
        validator.setValidationListener(this);

        eventName = (EditText) getActivity().findViewById(R.id.eventTitle);
        eventDescription = (EditText) getActivity().findViewById(R.id.eventDescription);
        eventStartValue = (EditText) getActivity().findViewById(R.id.eventProgressInit);
        eventProgressView = (TextView) getActivity().findViewById(R.id.progressInt);
        eventBarValue = (SeekBar) getActivity().findViewById(R.id.progressSeekBar);
        eventDate = (DatePicker) getActivity().findViewById(R.id.dateEvent);
        addEvent = (Button) getActivity().findViewById(R.id.addEvent);

        inputListeners();

        addEvent.setOnClickListener(new View.OnClickListener() {
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
        getData();
        Snackbar.make(getView(), "Elemento agregado", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

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

    private void inputListeners(){
        eventBarValue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                eventProgressView.setText(String.valueOf(i)+"%");
                eventStartValue.setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        eventStartValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                eventProgressView.setText(eventStartValue.getText()+"%");
            }
        });
    }

    private void getData(){
        name = eventName.getText().toString();
        description=eventDescription.getText().toString();
        date = String.valueOf(eventDate.getDayOfMonth())+" - " + String.valueOf(eventDate.getMonth()) + " - " + String.valueOf(eventDate.getYear());
        initvalue = Double.parseDouble(eventStartValue.getText().toString());

        if (initvalue<0.0){
            initvalue = 0;
        }

        if (initvalue>100.0){
            initvalue = 100;
        }
        int item=0;

        eventModel.insertEvent(name,description,date,initvalue);


        Cursor c = eventModel.getEventID(name);
        c.moveToFirst();
        while (!c.isAfterLast()){
        item = c.getInt(c.getColumnIndex(EventContract._ID));
            c.moveToNext();
        }
        eventModel.insertUpdate(item,"Se ha creado: "+ name + " con valor de inicial de: "+String.valueOf(initvalue)+"%",initvalue,name);

        events.add(new Event(item,name,initvalue,description,date));

        clearData();
        getActivity().onBackPressed();
    }

    @Override
    public void onPause() {
        eventModel.database.close();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        eventModel = new EventModel(getContext());
    }

    private void clearData(){
        name = "";
        description = "";
        date = "";
        initvalue = 0;
        eventName.setText("");
        eventDescription.setText("");
        eventStartValue.setText("");
        eventProgressView.setText("");
        eventBarValue.setProgress(0);
    }
}
