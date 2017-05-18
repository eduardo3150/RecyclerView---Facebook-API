package BaseDeDatos;

import android.provider.BaseColumns;

/**
 * Created by Eduardo_Chavez on 26/2/2017.
 */

public class EventContract implements BaseColumns {
    public static final String TABLE1_NAME = "Eventos";
    public static final String _ID = "ID";
    public static final String COLUMN_NAME_EVENT = "name";
    public static final String COLUMN_DESCRIPTION_EVENT = "description";
    public static final String COLUMN_DATE_EVENT = "date";
    public static final String COLUMN_PROGRESS = "progress";

    public static final String TABLE2_NAME = "Actualizaciones";
    public static final String _ID_ACTUALIZACION ="eventID";
    public static final String UPDATE_DETAIL = "detail";
    public static final String UPDATE_PROGRESS ="proupdate";
    public static final String UPDATE_NAME = "name";


    //CREACION DE TABLA 1
    public static final String SQL_CREATE_TABLE_EVENT = "CREATE TABLE "+
            TABLE1_NAME + " ( " +
            _ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NAME_EVENT + " TEXT NOT NULL, " +
            COLUMN_DESCRIPTION_EVENT + " TEXT NOT NULL, " +
            COLUMN_DATE_EVENT + " TEXT NOT NULL, " +
            COLUMN_PROGRESS + " REAL NOT NULL ) ";

    public static final String SQL_DELETE_ENTRIES_EVENT =
            "DROP TABLE IF EXISTS " + TABLE1_NAME;

    //CREACION DE TABLA 2
    public static final String SQL_CREATE_TABLE_UPDATE = "CREATE TABLE " +
            TABLE2_NAME + " ( " +
            _ID_ACTUALIZACION + " INTEGER NOT NULL, " +
            UPDATE_DETAIL + " TEXT NOT NULL, "+
            UPDATE_PROGRESS + " REAL NOT NULL, "+
            UPDATE_NAME + " TEXT NOT NULL, "+
            "FOREIGN KEY("+_ID_ACTUALIZACION+") REFERENCES "+TABLE1_NAME
            +"("+_ID+") )";

    public static final String SQL_DELETE_ENTRIES_UPDATE =
            "DROP TABLE IF EXISTS " + TABLE2_NAME;
}
