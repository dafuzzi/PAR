package de.uulm.par.sql;

import java.util.LinkedList;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import de.uulm.par.SimplePerson;
import de.uulm.par.notes.NoteType;
import de.uulm.par.notes.PlainNote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class NotesDataSource {

	DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

	private static final String LOGTAG = "PAR: SQL";
	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_TITLE, MySQLiteHelper.COLUMN_MESSAGE, MySQLiteHelper.COLUMN_TYPE, MySQLiteHelper.COLUMN_DATE_CREATE,
			MySQLiteHelper.COLUMN_DATE_ALERT, MySQLiteHelper.COLUMN_PERSON_NAME, MySQLiteHelper.COLUMN_PERSON_MAC, MySQLiteHelper.COLUMN_LOCATION };

	public NotesDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public void insertNote(PlainNote note) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_TITLE, note.getTitle());
		values.put(MySQLiteHelper.COLUMN_MESSAGE, note.getMessage());
		values.put(MySQLiteHelper.COLUMN_TYPE, note.getType().ordinal());
		values.put(MySQLiteHelper.COLUMN_DATE_CREATE, note.getCreated().toString());
		switch (note.getType()) {
		case DATETIME:
			values.put(MySQLiteHelper.COLUMN_DATE_ALERT, note.getAlert().toString());
			break;
		case PERSON:
			values.put(MySQLiteHelper.COLUMN_PERSON_NAME, note.getPerson().getName());
			values.put(MySQLiteHelper.COLUMN_PERSON_MAC, note.getPerson().getMac());
			break;
		case LOCATION:
			values.put(MySQLiteHelper.COLUMN_LOCATION, note.getLocation());
			break;
		default:
			break;
		}

		long insertId = database.insert(MySQLiteHelper.TABLE_NOTES, null, values);
		Cursor cursor = database.query(MySQLiteHelper.TABLE_NOTES, allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
		cursor.close();
		note.setId(insertId);
		Log.d(LOGTAG, "Note added with id: " + insertId);
	}

	public void deleteNote(PlainNote note) {
		long id = note.getId();
		database.delete(MySQLiteHelper.TABLE_NOTES, MySQLiteHelper.COLUMN_ID + " = " + id, null);
		Log.d(LOGTAG, "Note deleted with id: " + id);
	}

	public LinkedList<PlainNote> getAllNotes() {
		Cursor cursor = database.query(MySQLiteHelper.TABLE_NOTES, allColumns, null, null, null, null, null);
		cursor.moveToFirst();
		LinkedList<PlainNote> result = new LinkedList<PlainNote>();
		while (!cursor.isAfterLast()) {
			Log.d(LOGTAG,"adding....");
			result.add(cursorToNote(cursor));
			cursor.moveToNext();
		}
		cursor.close();
		return result;
	}

	private PlainNote cursorToNote(Cursor cursor) {
		PlainNote note = new PlainNote();
		note.setId(cursor.getLong(0));
		note.setTitle(cursor.getString(1));
		note.setMessage(cursor.getString(2));
		note.setType(NoteType.fromInteger(cursor.getInt(3)));
		note.setCreated(new DateTime(formatter.parseDateTime(cursor.getString(4))));
		if (note.getType() == NoteType.DATETIME) {
			note.setAlert(new DateTime(formatter.parseDateTime(cursor.getString(5))));
		} else if (note.getType() == NoteType.PERSON) {
			note.setPerson(new SimplePerson(cursor.getString(6), cursor.getString(7)));
		} else if (note.getType() == NoteType.LOCATION) {
			note.setLocation(cursor.getString(8));
		}
		return note;
	}
}