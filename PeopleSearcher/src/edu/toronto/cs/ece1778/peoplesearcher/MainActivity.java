package edu.toronto.cs.ece1778.peoplesearcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * Main activity of the program.
 * 
 * @author mcupak
 * 
 */
public class MainActivity extends Activity {
	private DatabaseHelper db = null;

	/**
	 * Task for cleaning the database by deleting all the names.
	 * 
	 * @author mcupak
	 * 
	 */
	private class CleanTask extends AsyncTask<Void, Void, Void> {
		private static final String DELETE_QUERY = "DELETE FROM "
				+ DatabaseHelper.TABLE;

		@Override
		protected Void doInBackground(Void... unused) {
			db.getWritableDatabase().execSQL(DELETE_QUERY);
			return null;
		}

		@Override
		protected void onPreExecute() {
			((ProgressBar) findViewById(R.id.pbrLoading))
					.setVisibility(View.VISIBLE);
			findViewById(R.id.btnPopulate).setEnabled(false);
		}

		@Override
		protected void onPostExecute(Void unused) {
			((ProgressBar) findViewById(R.id.pbrLoading))
					.setVisibility(View.INVISIBLE);
			findViewById(R.id.btnPopulate).setEnabled(true);
		}
	}

	/**
	 * Task for adding the names from the file to the database.
	 * 
	 * @author mcupak
	 * 
	 */
	private class PopulateTask extends AsyncTask<URL, Void, Boolean> {
		@Override
		protected Boolean doInBackground(URL... urls) {
			boolean success = true;
			if (urls.length < 1) {
				// should never happen, called internally
				showStatus(R.string.unexpectedError);
			} else {
				// read the file from the URL and populate DB
				BufferedReader in = null;
				try {
					in = new BufferedReader(new InputStreamReader(
							urls[0].openStream()));
					// Read all the text returned by the server
					String name;
					while ((name = in.readLine()) != null) {
						// store name in the database
						ContentValues values = new ContentValues(1);
						values.put(DatabaseHelper.NAME, name);
						db.getWritableDatabase().insert(DatabaseHelper.TABLE,
								DatabaseHelper.NAME, values);
					}
				} catch (IOException e) {
					success = false;
				} finally {
					try {
						if (in != null)
							in.close();
					} catch (IOException e) {
						// stream not open, ignore
					}

				}
			}

			return success;
		}

		@Override
		protected void onPreExecute() {
			((ProgressBar) findViewById(R.id.pbrLoading))
					.setVisibility(View.VISIBLE);
			findViewById(R.id.btnPopulate).setEnabled(false);
			findViewById(R.id.btnSearch).setEnabled(false);
		}

		@Override
		protected void onPostExecute(Boolean success) {
			((ProgressBar) findViewById(R.id.pbrLoading))
					.setVisibility(View.INVISIBLE);
			findViewById(R.id.btnPopulate).setEnabled(true);

			if (success) {
				findViewById(R.id.btnSearch).setEnabled(true);
				showStatus(R.string.loaded);
			} else {
				showStatus(R.string.unreadableUrl);
			}
		}
	}

	/**
	 * Task for creating web results for the names.
	 * 
	 * @author mcupak
	 * 
	 */
	private class SearchTask extends AsyncTask<Void, String, Void> {
		private static final String SELECT_QUERY = "SELECT DISTINCT "
				+ DatabaseHelper.NAME + " FROM " + DatabaseHelper.TABLE
				+ " ORDER BY " + DatabaseHelper.NAME + " DESC";
		private Cursor cursor = null;

		@Override
		protected Void doInBackground(Void... unused) {
			cursor = db.getReadableDatabase().rawQuery(SELECT_QUERY, null);
			cursor.getCount();

			// go through the names
			while (cursor.moveToNext()) {
				String name = cursor.getString(0);
				publishProgress(name);
			}

			return null;
		}

		@Override
		protected void onProgressUpdate(String... values) {
			if (values.length >= 1) {
				Intent lookup = new Intent(MainActivity.this, WebActivity.class);
				lookup.putExtra(DatabaseHelper.NAME, values[0]);
				startActivity(lookup);
			}
		}

		@Override
		protected void onPreExecute() {
			((ProgressBar) findViewById(R.id.pbrLoading))
					.setVisibility(View.VISIBLE);
			findViewById(R.id.btnPopulate).setEnabled(false);
			findViewById(R.id.btnSearch).setEnabled(false);
		}

		@Override
		protected void onPostExecute(Void unused) {
			((ProgressBar) findViewById(R.id.pbrLoading))
					.setVisibility(View.INVISIBLE);
			findViewById(R.id.btnPopulate).setEnabled(true);
			findViewById(R.id.btnSearch).setEnabled(true);

			cursor.close();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// initialize DB
		db = DatabaseHelper.getInstance(getApplicationContext());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		new CleanTask().execute();
		db.close();
	}

	/**
	 * Helper method for showing a status message in the form of a toast.
	 * 
	 * @param message
	 */
	private void showStatus(int message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}

	/**
	 * Adds the names to the database in a separate thread.
	 * 
	 * @param v
	 */
	public void populate(View v) {
		// parse URL
		String url = ((EditText) findViewById(R.id.edtDBURL)).getText()
				.toString();
		if (!url.isEmpty()) {
			try {
				URL dbUrl = new URL(url);
				new PopulateTask().execute(dbUrl, null, null);
			} catch (MalformedURLException e) {
				showStatus(R.string.invalidUrl);
			}
		}
	}

	/**
	 * Searches for the names on Google using a separate thread.
	 * 
	 * @param v
	 */
	public void search(View v) {
		new SearchTask().execute(null, null, null);
	}
}
