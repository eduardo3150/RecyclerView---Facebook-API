package BaseDeDatos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.chavez.eduardo.recyclerview.Event;

/**
 * Created by Eduardo_Chavez on 26/2/2017.
 */

public class EventModel {
    public Context context;
    public EventDbHelper eventDbHelper;
    public SQLiteDatabase database;


    public EventModel(Context context){
        this.context = context;
        eventDbHelper = new EventDbHelper(context);
        database = eventDbHelper.getWritableDatabase();
    }

    /**EVENTS**/

    //INSERTAMOS ACA UN EVENTO
    public long insertEvent(String name, String description, String date, Double progress){
        ContentValues values = new ContentValues();
        values.put(EventContract.COLUMN_NAME_EVENT,name);
        values.put(EventContract.COLUMN_DESCRIPTION_EVENT,description);
        values.put(EventContract.COLUMN_DATE_EVENT,date);
        values.put(EventContract.COLUMN_PROGRESS,progress);

        long newRowID = database.insert(EventContract.TABLE1_NAME,null,values);
        return newRowID;
    }

    //Esto las muestra sin orden
    public Cursor listAllEvents(){
        database = eventDbHelper.getReadableDatabase();

        String[] projection = {
            EventContract._ID,
            EventContract.COLUMN_NAME_EVENT,
            EventContract.COLUMN_DESCRIPTION_EVENT,
            EventContract.COLUMN_DATE_EVENT,
            EventContract.COLUMN_PROGRESS
        };

        Cursor c = database.query(EventContract.TABLE1_NAME,projection
        ,null
        ,null
        ,null
        ,null
        ,null);

        return c;
    }

    //ESTO MUESTRA LAS ACTUALIZACIONES EN ORDEN DESCENDENTE
    public Cursor listEventsOrder(){
        database = eventDbHelper.getReadableDatabase();

        String[] projection = {
                EventContract._ID,
                EventContract.COLUMN_NAME_EVENT,
                EventContract.COLUMN_DESCRIPTION_EVENT,
                EventContract.COLUMN_DATE_EVENT,
                EventContract.COLUMN_PROGRESS
        };

        //ORDENADOR
        String sortOrder = EventContract.COLUMN_PROGRESS + " DESC";

        Cursor c = database.query(EventContract.TABLE1_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder);

        return c;
    }

    //Esto sera llamado solo en agregar evento para
    // poder obtener la llave primaria y agregar una actualizacio en la tabla 2
    public Cursor getEventID(String name){
        database = eventDbHelper.getReadableDatabase();

        String[] projection = {
                EventContract._ID
        };

        String where = EventContract.COLUMN_NAME_EVENT + " = ?";
        String [] seleccionArgs = {name};

        Cursor c = database.query(EventContract.TABLE1_NAME,
                projection,
                where,
                seleccionArgs,
                null,
                null,
                null);

        return c;
    }



    //Esto sera llamado para mostrar toda la informacion de un evento
    //
    public Cursor getEventData(int id){
        database = eventDbHelper.getReadableDatabase();

        String[] projection = {
                EventContract._ID,
                EventContract.COLUMN_NAME_EVENT,
                EventContract.COLUMN_DESCRIPTION_EVENT,
                EventContract.COLUMN_DATE_EVENT,
                EventContract.COLUMN_PROGRESS
        };

        String where = EventContract._ID + " = ?";
        String [] seleccionArgs = {String.valueOf(id)};

        Cursor c = database.query(EventContract.TABLE1_NAME,
                projection,
                where,
                seleccionArgs,
                null,
                null,
                null);

        return c;
    }


    //ACA ELIMINO UN EVENTO
    public void delete(int id){
        String seleccion = EventContract._ID + " =?";
        String[] seleccionArgs = {
                String.valueOf(id)
        };

        database.delete(EventContract.TABLE1_NAME,seleccion,seleccionArgs);
    }


    public void updateEvent(int id,String name, String description, Double progress){
        ContentValues values = new ContentValues();
        values.put(EventContract.COLUMN_NAME_EVENT,name);
        values.put(EventContract.COLUMN_DESCRIPTION_EVENT,description);
        values.put(EventContract.COLUMN_PROGRESS,progress);

        String seleccion = EventContract._ID + " =?";
        String[]seleccionArgs = {
                String.valueOf(id)
        };

        database.update(EventContract.TABLE1_NAME,values,seleccion,seleccionArgs);
    }




    /**UPDATES**/

    public long insertUpdate(int ID, String detail,Double progress,String date){
        ContentValues contentValues = new ContentValues();

        contentValues.put(EventContract._ID_ACTUALIZACION,ID);
        contentValues.put(EventContract.UPDATE_DETAIL,detail);
        contentValues.put(EventContract.UPDATE_PROGRESS,progress);
        contentValues.put(EventContract.UPDATE_NAME,date);

        long newRowUpdateID = database.insert(EventContract.TABLE2_NAME,null,contentValues);
        return newRowUpdateID;
    }


    public Cursor listAllUpdates(){
        database = eventDbHelper.getReadableDatabase();

        String[] projection = {
                EventContract._ID_ACTUALIZACION,
                EventContract.UPDATE_DETAIL,
                EventContract.UPDATE_PROGRESS,
                EventContract.UPDATE_NAME
        };

        Cursor c = database.query(EventContract.TABLE2_NAME,projection
                ,null
                ,null
                ,null
                ,null
                ,null);

        return c;
    }


    public Cursor getUpdateData(int id){
        database = eventDbHelper.getReadableDatabase();

        String[] projection = {
                EventContract._ID_ACTUALIZACION,
                EventContract.UPDATE_DETAIL,
                EventContract.UPDATE_NAME,
                EventContract.UPDATE_PROGRESS
        };

        String where = EventContract._ID_ACTUALIZACION + " = ?";
        String [] seleccionArgs = {String.valueOf(id)};

        Cursor c = database.query(EventContract.TABLE2_NAME,
                projection,
                where,
                seleccionArgs,
                null,
                null,
                null);

        return c;
    }

    //ACA ELIMINO UPDATES
    public void deleteUpdates(int id){
        String seleccion = EventContract._ID_ACTUALIZACION + " =?";
        String[] seleccionArgs = {
                String.valueOf(id)
        };

        database.delete(EventContract.TABLE2_NAME,seleccion,seleccionArgs);
    }


}
