package edu.toronto.cs.ece1778.peoplesearcher;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Helper taking care of the database creation.
 * 
 * @author mcupak
 *
 */
public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "peoplesearch.db";
	private static final int SCHEMA = 1;
	static final String NAME = "name";
	static final String TABLE = "names";
	private static DatabaseHelper singleton = null;

	synchronized static DatabaseHelper getInstance(Context ctxt) {
		if (singleton == null)
			singleton = new DatabaseHelper(ctxt.getApplicationContext());

		return (singleton);
	}

	private DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, SCHEMA);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			db.beginTransaction();
			db.execSQL("CREATE TABLE " + TABLE
					+ " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME
					+ " TEXT);");
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		throw new RuntimeException("Not yet supported.");
	}
}
