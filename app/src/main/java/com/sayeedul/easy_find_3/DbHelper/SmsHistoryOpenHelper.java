package com.sayeedul.easy_find_3.DbHelper;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SmsHistoryOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String HISTORY_TABLE = "history_list";
    private static final String DATABASE_NAME = "history_db";

    public static final String FROM_NUMBER = "_from_number";
    public static final String SENT_NAME = "_sent_name";
    public static final String SENT_NUMBER = "_sent_number";
    public static final String SENT_DATE = "_date";

    private static final String[] COLUMNS = {FROM_NUMBER, SENT_NAME, SENT_NUMBER, SENT_DATE};

    private static final String HISTORY_LIST_TABLE_CREATE =
            "CREATE TABLE " + HISTORY_TABLE + " (" +
                    FROM_NUMBER + " TEXT PRIMARY KEY, " +SENT_NAME + " TEXT, "+SENT_NUMBER + " TEXT, "+
                    SENT_DATE + " TEXT );";

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(HISTORY_LIST_TABLE_CREATE);

    }

    public long insertProduct(String from_num ,String sent_num,String sent_name, String sent_date)
    {
        ContentValues cv = new ContentValues();
        cv.put(FROM_NUMBER,from_num);
        cv.put(SENT_NAME,sent_name);
        cv.put(SENT_NUMBER,sent_num);
        cv.put(SENT_DATE,sent_date);
        return this.getWritableDatabase().insert(HISTORY_TABLE, null, cv);
    }

    public Cursor getData() {
        return this.getReadableDatabase().rawQuery("Select * from "+HISTORY_TABLE, null);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    public SmsHistoryOpenHelper(Context context)
    {
        super(context, "ProductDatabase", null, 1);
    }

}

