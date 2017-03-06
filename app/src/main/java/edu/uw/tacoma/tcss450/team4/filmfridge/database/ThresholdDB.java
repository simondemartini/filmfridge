package edu.uw.tacoma.tcss450.team4.filmfridge.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import edu.uw.tacoma.tcss450.team4.filmfridge.R;

/**
 * Created by Samantha Ong on 3/5/2017.
 */

public class ThresholdDB {
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "userthreshold.db";
    public static final String THRESH_TABLE = "userthreshold";

    private ThresholdDBHelper mThresholdDBHelper;
    private SQLiteDatabase mSQLiteDatabase;


    public ThresholdDB(Context context) {
        mThresholdDBHelper = new ThresholdDBHelper(context, DB_NAME, null, DB_VERSION);
    }
    /**
     * Inserts the threshold into the local sqlite table.
     * Returns true if successful, false otherwise.
     * @param theEmail
     * @param theNotRecom
     * @param theAtHome
     * @param theInTheaters
     * @return true or false
     */
    public boolean insertThreshold(String theEmail,
                                   double theNotRecom,
                                   double theAtHome,
                                   double theInTheaters) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", theEmail);
        contentValues.put("notrecom", theNotRecom);
        contentValues.put("athome", theAtHome);
        contentValues.put("intheaters", theInTheaters);

        long rowId = mSQLiteDatabase.insert("userthreshold", null, contentValues);
        return rowId != -1;
    }

    public boolean updateThreshold(String theEmail,
                                   double theNotRecom,
                                   double theAtHome,
                                   double theInTheaters) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("shortDesc", theNotRecom);
        contentValues.put("longDesc", theAtHome);
        contentValues.put("prereqs", theInTheaters);

        long rowId = mSQLiteDatabase.update("userthreshold", contentValues, "email = ?", new String[] {theEmail});
        return rowId != -1;
    }


    /**
     * Delete all the data from the COURSE_TABLE
     */
    public void deleteCourses() {
        mSQLiteDatabase.delete(THRESH_TABLE, null, null);
    }

    /**
     * Gets the users not recommended threshold.
     * @param theEmail
     * @return double not recommended
     */
    public double getNotRecomThreshold(String theEmail) {
        String[] columns = {
                "email", "notrecom", "athome", "intheaters"
        };

        Cursor c = mSQLiteDatabase.query(THRESH_TABLE,
                columns,
                "email = ?",
                new String[] {theEmail},
                null,
                null,
                null);

        return c.getDouble(1);
    }

    /**
     * Gets the users see at home threshold.
     * @param theEmail
     * @return double see at home
     */
    public double getAtHomeThreshold(String theEmail) {
        String[] columns = {
                "email", "notrecom", "athome", "intheaters"
        };

        Cursor c = mSQLiteDatabase.query(THRESH_TABLE,
                columns,
                "email = ?",
                new String[] {theEmail},
                null,
                null,
                null);

        return c.getDouble(2);
    }

    /**
     * Gets the users see in theaters threshold
     * @param theEmail
     * @return double see in theaters
     */
    public double getInTheatersThreshold(String theEmail) {
        String[] columns = {
                "email", "notrecom", "athome", "intheaters"
        };

        Cursor c = mSQLiteDatabase.query(THRESH_TABLE,
                columns,
                "email = ?",
                new String[] {theEmail},
                null,
                null,
                null);

        return c.getDouble(3);
    }



    class ThresholdDBHelper extends SQLiteOpenHelper {

        private final String CREATE_THRESHOLD_SQL;

        private final String DROP_THRESHOLD_SQL;

        public ThresholdDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
            CREATE_THRESHOLD_SQL = context.getString(R.string.CREATE_THRESHOLD_SQL);
            DROP_THRESHOLD_SQL = context.getString(R.string.DROP_THRESHOLD_SQL);

        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_THRESHOLD_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL(DROP_THRESHOLD_SQL);
            onCreate(sqLiteDatabase);
        }
    }
}
