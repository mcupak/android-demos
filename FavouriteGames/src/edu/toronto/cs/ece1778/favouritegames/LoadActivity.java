package edu.toronto.cs.ece1778.favouritegames;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Activity for loading files.
 * 
 * @author mcupak
 *
 */
public class LoadActivity extends ListActivity {

	private String fileName = "";

	/**
	 * Gets the list of all the modified files.
	 * 
	 * @return
	 */
	private List<String> getFiles() {
		List<String> items = new ArrayList<String>();

		File[] files = getExternalFilesDir(null).listFiles();
		for (File f : files) {
			items.add(f.getName());
		}

		return items;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_load);

		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, getFiles()));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_load, menu);
		return true;
	}

	@Override
	public void onListItemClick(ListView parent, View v, int position, long id) {
		fileName = getFiles().get(position);
		((TextView) findViewById(R.id.txtFileName)).setText(fileName);
	}

	/**
	 * Loads the selected file.
	 */
	public void load(View v) {
		List<Person> persons = new ArrayList<Person>();

		// validate
		if (!fileName.isEmpty()) {
			findViewById(R.id.pbrLoad).setVisibility(View.VISIBLE);

			// read file
			try {
				File file = new File(getExternalFilesDir(null), fileName);
				InputStream in = new FileInputStream(file);
				if (in != null) {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(in));

					String str;
					try {
						while ((str = reader.readLine()) != null) {
							// parse each line
							String[] data = str.split(",");
							if (data.length != 3) {
								// error, stop reading
								break;
							}

							Person person = new Person();
							person.setName(data[0]);
							person.setAge(Integer.parseInt(data[1]));
							person.setGame(data[2]);
							persons.add(person);
						}

						reader.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} catch (java.io.FileNotFoundException e) {
				e.printStackTrace();
			}

			// export data to starting activity
			StartingActivity.setFile(fileName);
			StartingActivity.setPersons(persons);
			StartingActivity.setListModified(false);

			// hide progressbar and close the dialog
			findViewById(R.id.pbrLoad).setVisibility(View.INVISIBLE);

			this.finish();
		}
	}

	/**
	 * Closes the activity.
	 * 
	 * @param v
	 */
	public void close(View v) {
		this.finish();
	}
}
