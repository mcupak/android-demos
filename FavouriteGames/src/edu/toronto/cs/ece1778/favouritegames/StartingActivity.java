package edu.toronto.cs.ece1778.favouritegames;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.view.Menu;
import android.view.View;

/**
 * Main activity of the app.
 * 
 * @author mcupak
 *
 */
public class StartingActivity extends Activity {

	private static List<Person> items = new ArrayList<Person>();
	private static String file = "";
	private static boolean isListModified = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_starting);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_starting, menu);
		return true;
	}

	/**
	 * Goes to the new activity.
	 * 
	 * @param view
	 */
	public void showNew(View view) {
		startActivity(new Intent(this, NewActivity.class));
	}

	/**
	 * Goes to the view activity.
	 * 
	 * @param view
	 */
	public void showView(View view) {
		startActivity(new Intent(this, ViewActivity.class));
	}

	/**
	 * Goes to the store activity.
	 * 
	 * @param view
	 */
	public void showStore(View view) {
		startActivity(new Intent(this, StoreActivity.class));
	}

	/**
	 * Goes to the load activity.
	 * 
	 * @param view
	 */
	public void showLoad(View view) {
		startActivity(new Intent(this, LoadActivity.class));
	}

	/**
	 * Closes the application. If the file has been modified, stores it.
	 * 
	 * @param view
	 */
	public void exit(View view) {
		if (isListModified) {
			// store modified file
			if (file.isEmpty()) {
				// generate name
				Time time = new Time();
				time.setToNow();
				System.out.println(time.format2445());
				file = time.format2445() + ".txt";
			}

			File f = new File(getExternalFilesDir(null), file);
			FileOutputStream fos;
			try {
				fos = new FileOutputStream(f);
				OutputStreamWriter out = new OutputStreamWriter(fos);

				out.write(StartingActivity.getOutput());
				out.flush();
				fos.getFD().sync();
				out.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// tidy up
		setListModified(false);
		items.clear();
		setFile("");

		this.finish();
	}

	public static List<Person> getPersons() {
		return items;
	}

	public static void setPersons(List<Person> items) {
		StartingActivity.items = items;
	}

	public static String getFile() {
		return file;
	}

	public static void setFile(String file) {
		StartingActivity.file = file;
	}

	public static boolean isListModified() {
		return isListModified;
	}

	public static void setListModified(boolean isListModified) {
		StartingActivity.isListModified = isListModified;
	}

	/**
	* Prepares the output from loaded person data.
	*
	*/
	public static String getOutput() {
		StringBuilder output = new StringBuilder();

		for (Person p : getPersons()) {
			output.append(p.toString());
			output.append("\n");
		}

		return output.toString();
	}

}
