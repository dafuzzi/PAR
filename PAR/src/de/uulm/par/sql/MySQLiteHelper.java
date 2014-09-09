package de.uulm.par.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @author Fabian Schwab
 *
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

	public static final String TABLE_NOTES = "notes";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_MESSAGE = "message";
	public static final String COLUMN_TYPE = "type";
	public static final String COLUMN_DATE_CREATE = "date1";
	public static final String COLUMN_DATE_ALERT = "date2";
	public static final String COLUMN_PERSON_NAME = "name";
	public static final String COLUMN_PERSON_MAC = "mac";
	public static final String COLUMN_LOCATION = "location";

	private static final String DATABASE_NAME = "notes.db";
	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table " + TABLE_NOTES + "(" + COLUMN_ID + " integer primary key autoincrement, " + COLUMN_TITLE + " text not null, " + COLUMN_MESSAGE
			+ " text, " + COLUMN_TYPE + " integer, " + COLUMN_DATE_CREATE + " text, " + COLUMN_DATE_ALERT + " text, " + COLUMN_PERSON_NAME + " text, " + COLUMN_PERSON_MAC + " text, " + COLUMN_LOCATION + " text " + ");";

	/**
	 * @param context
	 */
	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(MySQLiteHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
		onCreate(db);
	}

}