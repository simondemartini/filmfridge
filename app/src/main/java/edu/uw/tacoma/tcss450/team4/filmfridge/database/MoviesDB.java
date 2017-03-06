package edu.uw.tacoma.tcss450.team4.filmfridge.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import edu.uw.tacoma.tcss450.team4.filmfridge.R;

/**
 * Database for user thresholds on the movies.
 * Thresholds will be held in notrecom for not recommended,
 * athome for see at home, and intheaters for see in theaters.
 * Implementation will be taking care of inclusive and exclusive rules.
 *
 * Created by Samantha Ong on 3/5/2017.
 */

public class MoviesDB {
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "movies.db";
    public static final String MOVIES_TABLE = "movies";

    private MoviesDBHelper mMoviesDBHelper;
    private SQLiteDatabase mSQLiteDatabase;


    public MoviesDB(Context context) {
        mMoviesDBHelper = new MoviesDBHelper(context, DB_NAME, null, DB_VERSION);
    }
    /**
     * Inserts the movies into the local sqlite table.
     * Returns true if successful, false otherwise.
     * @param theEmail
     * @param theMovieID
     * @return true or false
     */
    public boolean insertMovies(String theEmail,
                                   String theMovieID) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", theEmail);
        contentValues.put("movieid", theMovieID);

        long rowId = mSQLiteDatabase.insert("movies", null, contentValues);
        return rowId != -1;
    }

    /**
     * Removes the movies into the local sqlite table.
     * Returns true if successful, false otherwise.
     * @param theEmail
     * @param theMovieID
     * @return true or false
     */
    public void deleteMovie(String theEmail,
                                String theMovieID) {

        mSQLiteDatabase.delete("movies", "email = ? AND movieid = ?",
                new String[] {theEmail, theMovieID});
    }


    /**
     * Delete all the data from the COURSE_TABLE
     */
    public void deleteMovies() {
        mSQLiteDatabase.delete(MOVIES_TABLE, null, null);
    }


    /**
     * Gets the users not recommended threshold.
     * @param theEmail
     * @return double not recommended
     */
    public List<String> getMyMovies(String theEmail) {
        String[] columns = {
                "email", "movieid"
        };

        List<String> list = new ArrayList<String>();

        Cursor c = mSQLiteDatabase.query(MOVIES_TABLE,
                columns,
                "email = ?",
                new String[] {theEmail},
                null,
                null,
                null);
        c.moveToFirst();
        for (int i=0; i<c.getCount(); i++) {
            String id = c.getString(1);
            list.add(id);
            c.moveToNext();
        }
        return list;
    }

    class MoviesDBHelper extends SQLiteOpenHelper {

        private final String CREATE_MOVIES_SQL;

        private final String DROP_MOVIES_SQL;

        public MoviesDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
            CREATE_MOVIES_SQL = context.getString(R.string.CREATE_MOVIES_SQL);
            DROP_MOVIES_SQL = context.getString(R.string.DROP_MOVIES_SQL);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_MOVIES_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL(DROP_MOVIES_SQL);
            onCreate(sqLiteDatabase);
        }
    }
}
