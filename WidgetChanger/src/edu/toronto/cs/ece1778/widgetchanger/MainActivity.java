package edu.toronto.cs.ece1778.widgetchanger;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Main activity of the application.
 * 
 * @author mcupak
 * 
 */
public class MainActivity extends Activity {

	private Integer changeCount = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);

		// change the settings button to reset
		menu.clear();
		menu.add(R.string.reset).setOnMenuItemClickListener(
				new OnMenuItemClickListener() {

					@Override
					public boolean onMenuItemClick(MenuItem item) {
						// reset the counter and text
						changeCount = 0;
						((TextView) findViewById(R.id.txtCounter))
								.setText(getString(R.string.initialThings));
						return true;
					}
				});
		return true;
	}

	/**
	 * Increments the change counter and changes the top text.
	 * 
	 * @param changeButton
	 */
	public void changeText(View view) {
		((TextView) findViewById(R.id.txtCounter))
				.setText(getString(R.string.pressed) + (++changeCount)
						+ getString(R.string.times));
	}

	/**
	 * Shows a picture of a dog if not already visible, hides the picture
	 * otherwise.
	 * 
	 * @param view
	 */
	public void displayPicture(View view) {
		ImageView picture = (ImageView) findViewById(R.id.imgDog);
		picture.setVisibility((picture.getVisibility() != View.VISIBLE) ? View.VISIBLE
				: View.INVISIBLE);
	}
}
