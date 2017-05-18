package com.chavez.eduardo.recyclerview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.MessageDialog;
import com.facebook.share.widget.ShareDialog;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import BaseDeDatos.EventContract;
import BaseDeDatos.EventModel;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShareFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShareFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShareFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ID = "ID";


    private TextView name, progressText;
    private EventModel eventModel;
    private ArrayList<Event> events = new ArrayList<>();
    private String nombre, descripcion, fecha;
    private double progreso;
    private int progressInt;
    private Button shareButton;

    private int Id_got;


    RelativeLayout relativeLayout;
    CardView cardView;
    ImageView iconShare;
    int touch=0;
    int touchIcon=0;

    private OnFragmentInteractionListener mListener;
    ShareDialog shareDialog;
    MessageDialog messageDialog;


    public ShareFragment() {
        // Required empty public constructor
    }

    public static ShareFragment newInstance(int param1) {
        ShareFragment fragment = new ShareFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ID, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        shareDialog = new ShareDialog(getActivity());
        messageDialog = new MessageDialog(getActivity());

        eventModel = new EventModel(getContext());
        name = (TextView) getActivity().findViewById(R.id.progressName);
        progressText = (TextView) getActivity().findViewById(R.id.progressEvent);
        shareButton = (Button) getActivity().findViewById(R.id.buttonShare);

        relativeLayout = (RelativeLayout) getActivity().findViewById(R.id.relativeLayoutShare);
        relativeLayout.setDrawingCacheEnabled(true);
        relativeLayout.buildDrawingCache();

        cardView = (CardView) getActivity().findViewById(R.id.cardViewShare);
        iconShare = (ImageView) getActivity().findViewById(R.id.iconShare);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (touch){
                    case 0:
                            cardView.setCardBackgroundColor(getActivity().getResources().getColor(R.color.colorAccent));
                            progressText.setTextColor(getActivity().getResources().getColor(R.color.colorAccent));
                            touch = 1;
                        break;
                    case 1:
                            cardView.setCardBackgroundColor(getActivity().getResources().getColor(R.color.bgRed));
                            progressText.setTextColor(getActivity().getResources().getColor(R.color.bgRed));
                            touch = 2;
                        break;
                    case 2:
                            cardView.setCardBackgroundColor(getActivity().getResources().getColor(R.color.bgGreen));
                            progressText.setTextColor(getActivity().getResources().getColor(R.color.bgGreen));
                            touch = 3;
                        break;
                    case 3:
                        cardView.setCardBackgroundColor(getActivity().getResources().getColor(R.color.bgPink));
                        progressText.setTextColor(getActivity().getResources().getColor(R.color.bgPink));
                        touch = 4;
                        break;
                    case 4:
                        cardView.setCardBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
                        progressText.setTextColor(getActivity().getResources().getColor(R.color.colorPrimary));
                        touch = 0;
                        break;

                    default:
                        cardView.setCardBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
                        progressText.setTextColor(getActivity().getResources().getColor(R.color.colorPrimary));
                        touch = 0;
                        break;
                }
            }
        });

        cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (touchIcon==0){
                    iconShare.setVisibility(View.GONE);
                    touchIcon=1;
                    return true;
                } else if (touchIcon==1){
                    iconShare.setVisibility(View.VISIBLE);
                    touchIcon=0;
                    return true;
                } else {
                return true;
                }
            }
        });


        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    generatePicShare();
            }
        });
        shareButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                    generatePicMessage();
                return true;
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Id_got = getArguments().getInt(ARG_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_share, container, false);
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

    @Override
    public void onResume() {
        super.onResume();
        mostrarEvento();
    }

    public void mostrarEvento() {
        events.clear();
        Cursor c = eventModel.getEventData(Id_got);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            nombre = c.getString(c.getColumnIndex(EventContract.COLUMN_NAME_EVENT));
            descripcion = c.getString(c.getColumnIndex(EventContract.COLUMN_DESCRIPTION_EVENT));
            fecha = c.getString(c.getColumnIndex(EventContract.COLUMN_DATE_EVENT));
            progreso = c.getDouble(c.getColumnIndex(EventContract.COLUMN_PROGRESS));

            events.add(new Event(Id_got, nombre, progreso, descripcion, fecha));

            c.moveToNext();
        }

        name.setText(events.get(0).getEventName());
        progressText.setText(String.valueOf(events.get(0).getEventProgress())+"%");


    }


    public void generatePicShare(){
        relativeLayout.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(relativeLayout.getDrawingCache());
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,bytes);

        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(bitmap)
                .build();
        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .setShareHashtag(new ShareHashtag.Builder()
                        .setHashtag("#VamosPorEsosPropositos")
                        .build())
                .build();
        relativeLayout.destroyDrawingCache();
        shareDialog.show(content);
        //getActivity().onBackPressed();
    }

    public void generatePicMessage(){
        relativeLayout.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(relativeLayout.getDrawingCache());
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,bytes);

        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(bitmap)
                .build();
        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();
        relativeLayout.destroyDrawingCache();
        messageDialog.show(getActivity(),content);
        //getActivity().onBackPressed();

    }

}
