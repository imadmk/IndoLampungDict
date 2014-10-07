package com.ubala.IndoLampungDict;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;


public class DatabaseHandler extends SQLiteOpenHelper {

    private String DATABASE_NAME;
    private String DATABASE_PATH = "/data/data/com.ubala.IndoLampungDict/databases/";
    private final Context mContext;
    private SQLiteDatabase mDatabase;

    public DatabaseHandler(Context context, String DATABASE_NAME, int DATABASE_VERSION)
            throws IOException, SQLException
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
        this.DATABASE_NAME = DATABASE_NAME;
        //DATABASE_PATH = mContext.getFilesDir().getPath();
        createDatabase();
    }

    public void createDatabase() throws IOException, SQLException {
        boolean dbExist = checkDatabase();
        //dbExist = false;

        if (dbExist) {
            openDatabase();
        } else {
            this.getReadableDatabase();

            try {
                copyDatabase();
                //openDatabase();
            } catch (IOException e) {
                throw new Error("ERRRRROOOOORRR!");
            }
        }
    }

    private boolean checkDatabase() {

        SQLiteDatabase checkDB = null;
        try {
            String mPath = DATABASE_PATH + DATABASE_NAME;

            checkDB = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.OPEN_READONLY);

        } catch (SQLiteException e) {
            // database doesn't exist yet
        }

        if (checkDB != null) {

            checkDB.close();
        }

        return checkDB != null;
    }

    private void copyDatabase() throws IOException {

        Resources resources = mContext.getResources();
        InputStream inputDB = resources.openRawResource(R.raw.database);

        String emptyDB = DATABASE_PATH + DATABASE_NAME;

        OutputStream outputDB = new FileOutputStream(emptyDB);

        // transfer bytes from input file to output file
        byte[] buffer = new byte[1024];
        int length;

        while ((length = inputDB.read(buffer)) > 0) {
            outputDB.write(buffer, 0, length);
        }

        outputDB.flush();
        outputDB.close();
        inputDB.close();
    }

    public void openDatabase() throws SQLException {

        String mPath = DATABASE_PATH + DATABASE_NAME;
        //URI uri = URI.parse("android.resource://com.ubala.kamuslampung/raw/database")
        mDatabase = SQLiteDatabase.openDatabase(mPath,
                null, SQLiteDatabase.OPEN_READONLY);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        mDatabase = db;
        //Resources resources = mContext.getResources();
        //String dbSql = resources.getResourceName(R.raw.Database);
        //db.execSQL(".read " + dbSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
