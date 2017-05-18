package BaseDeDatos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Eduardo_Chavez on 26/2/2017.
 */

public class EventDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "EventDb.db";

    public EventDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(EventContract.SQL_CREATE_TABLE_EVENT);
        Log.d("Creando tabla 1", EventContract.SQL_CREATE_TABLE_EVENT );

        sqLiteDatabase.execSQL(EventContract.SQL_CREATE_TABLE_UPDATE);
        Log.d("Creando tabla 2",EventContract.SQL_CREATE_TABLE_UPDATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(EventContract.SQL_DELETE_ENTRIES_EVENT);
        sqLiteDatabase.execSQL(EventContract.SQL_DELETE_ENTRIES_UPDATE);

        onCreate(sqLiteDatabase);
    }
}
