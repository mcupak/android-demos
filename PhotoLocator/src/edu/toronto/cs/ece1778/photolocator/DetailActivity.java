package edu.toronto.cs.ece1778.photolocator;

import java.io.File;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

/**
 * Activity for displaying the detail of a picture.
 * 
 * @author mcupak
 *
 */
public class DetailActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		
		// fill in the image
		File file = new File(getExternalFilesDir(null), MainActivity.getFileName());
		Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
		((ImageView)findViewById(R.id.imgPicture)).setImageBitmap(bitmap);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_detail, menu);
		return true;
	}

	public void close(View v) {
		this.finish();
	}
}
