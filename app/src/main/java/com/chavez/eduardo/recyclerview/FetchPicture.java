package com.chavez.eduardo.recyclerview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.EOFException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by Eduardo_Chavez on 4/3/2017.
 */

public class FetchPicture extends AsyncTask<String,Void,Bitmap> {
    ImageView profPic;

    public FetchPicture(ImageView imageView){
        this.profPic = imageView;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        String urlDisplay = strings[0];
        Bitmap mIcon = null;

        try {
            InputStream inputStream = new URL(urlDisplay).openStream();
            mIcon = BitmapFactory.decodeStream(inputStream);
        } catch (Exception e){
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }


        return mIcon;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        profPic.setImageBitmap(bitmap);
    }
}
