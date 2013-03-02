package edu.toronto.cs.ece1778.favouritegames;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Activity for adding new people to the list.
 * 
 * @author mcupak
 *
 */
public class NewActivity extends Activity implements AdapterView.OnItemSelectedListener {

	private String selectedGame = "";
	private static final String[] sampleGames = { "Board", "Card", "Dice",
			"Video" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new);

		Spinner spin = (Spinner) findViewById(R.id.spnGame);
		spin.setOnItemSelectedListener(this);

		ArrayAdapter<String> aa = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, sampleGames);
		aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spin.setAdapter(aa);
	}

	public void onItemSelected(AdapterView<?> parent, View v, int position,
			long id) {
		selectedGame = sampleGames[position];
	}

	public void onNothingSelected(AdapterView<?> parent) {
		selectedGame = "";
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_new, menu);
		return true;
	}

	/**
	 * Creates a new person if nonempty fields and sets properties accordingly.
	 * 
	 * @param v
	 */
	public void addEntry(View v) {
		String name = ((EditText) findViewById(R.id.edtName)).getText()
				.toString();
		String age = ((EditText) findViewById(R.id.edtAge)).getText()
				.toString();

		// validate fields
		if (name.isEmpty() || age.isEmpty() || selectedGame.isEmpty()) {
			findViewById(R.id.txtStatus).setVisibility(View.VISIBLE);
		} else {
			findViewById(R.id.txtStatus).setVisibility(View.INVISIBLE);

			Person person = new Person();
			person.setName(name);
			person.setAge(Integer.parseInt(age));
			person.setGame(selectedGame);
			StartingActivity.getPersons().add(person);
			StartingActivity.setListModified(true);
			
			// reset fields
			((EditText) findViewById(R.id.edtName)).setText("");
			((EditText) findViewById(R.id.edtAge)).setText("");
			((Spinner) findViewById(R.id.spnGame)).setSelection(0);
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
