package edu.toronto.cs.ece1778.favouritegames;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

/**
 * Activity for storing files.
 * 
 * @author mcupak
 *
 */
public class StoreActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_store, menu);

		// set initial name to the currently open file or empty in case of a new
		// file
		((EditText) findViewById(R.id.edtFileName)).setText(StartingActivity
				.getFile());

		return true;
	}

	/**
	 * Saves the file.
	 * 
	 * @param v
	 */
	public void save(View v) {
		// save file
		String fileName = ((EditText) findViewById(R.id.edtFileName)).getText()
				.toString();

		// validate
		if (fileName.isEmpty()) {
			findViewById(R.id.txtStatus).setVisibility(View.VISIBLE);
		} else {
			findViewById(R.id.txtStatus).setVisibility(View.INVISIBLE);
			findViewById(R.id.pbrSave).setVisibility(View.VISIBLE);

			// write file
			File file = new File(getExternalFilesDir(null), fileName);
			FileOutputStream fos;
			try {
				fos = new FileOutputStream(file);
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

			// export data
			StartingActivity.setFile(fileName);
			StartingActivity.setListModified(false);

			// hide progressbar and close the dialog
			findViewById(R.id.pbrSave).setVisibility(View.INVISIBLE);

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
