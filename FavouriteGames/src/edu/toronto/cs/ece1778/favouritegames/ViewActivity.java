package edu.toronto.cs.ece1778.favouritegames;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;

/**
 * Activity for viewing files.
 * 
 * @author mcupak
 *
 */
public class ViewActivity extends ListActivity {

	/**
	 * 
	 * Create a list of people.
	 * 
	 * @return
	 */
	private List<String> getPersons() {
		List<String> items = new ArrayList<String>();
		for (Person p : StartingActivity.getPersons()) {
			items.add(p.toString());
		}

		return items;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view);

		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, getPersons()));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_view, menu);
		return true;
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
