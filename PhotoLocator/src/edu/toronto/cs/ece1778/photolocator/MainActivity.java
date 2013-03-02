package edu.toronto.cs.ece1778.photolocator;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Time;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Main activity.
 * 
 * @author mcupak
 * 
 */
public class MainActivity extends ListActivity {

	/**
	 * Picture delay in ms.
	 */
	public static final int PICTURE_DELAY = 1000;
	// note: changing this to network provider might work better
	public static final String GPS_PROVIDER = LocationManager.NETWORK_PROVIDER;

	// accelerometer fields
	private ShakeListener mShaker;
	private Handler mHandler = new Handler();

	// camera fields
	private static String fileName = "";
	private List<String> pictures = new ArrayList<String>();
	private SurfaceView preview = null;
	private SurfaceHolder holder = null;
	private Camera camera = null;
	private boolean inPreview = false;

	// GPS fields
	private Double latitude = Double.valueOf(0);
	private Double longitude = Double.valueOf(0);
	private LocationManager mlocManager;
	private LocationListener mlocListener;

	// task taking a picture after a delay
	private Runnable PictureTask = new Runnable() {
		public void run() {
			takePicture();
		}
	};

	// task saving a picture in the background
	class SavePhoto extends AsyncTask<byte[], Boolean, Boolean> {
		private String photoName;

		protected void onPreExecute() {
			Time time = new Time();
			time.setToNow();
			photoName = latitude + "_" + longitude + "_" + time.format2445()
					+ ".jpg";
		}

		@Override
		protected Boolean doInBackground(byte[]... params) {
			try {
				FileOutputStream fos = new FileOutputStream(new File(
						getExternalFilesDir(null), photoName));
				fos.write(params[0]);
				fos.close();
			} catch (java.io.IOException e) {
				// error
			}

			mShaker.turnOn();
			return true;
		}
	}

	/**
	 * Gets the list of all the modified files. No need to actually store the
	 * list, can be generated from the file names of the pictures.
	 * 
	 * @return
	 */
	private List<String> loadFiles() {
		List<String> items = new ArrayList<String>();
		pictures.clear();
		File[] files = getExternalFilesDir(null).listFiles();

		// sort files by date descending
		Arrays.sort(files, new Comparator<File>() {
			public int compare(File f1, File f2) {
				return Long.valueOf(f2.lastModified()).compareTo(
						f1.lastModified());
			}
		});

		// prepare the list
		for (File f : files) {
			pictures.add(f.getName());

			// check file name format
			List<String> parts = Arrays.asList(f.getName().split("_"));
			if (parts.size() == 3) {
				items.add("Lat: " + parts.get(0) + "\nLon: " + parts.get(1));
			}
		}

		return items;
	}

	/**
	 * Prints the status message to the app.
	 * 
	 * @param status
	 */
	private void printStatus(String status) {
		((TextView) findViewById(R.id.txtStatus)).setText(status);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// prepare list of files
		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, loadFiles()));

		// set up GPS
		mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		mlocListener = new LocationListener() {

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onLocationChanged(Location location) {
				latitude = location.getLatitude();
				longitude = location.getLongitude();
			}
		};

		mlocManager.requestLocationUpdates(GPS_PROVIDER, 0, 0, mlocListener);

		// since we really detect only proper shaking lasting certain time,
		// schedule a new event PICTURE_DELAY from the time we registered the
		// last completion of shaking
		mShaker = new ShakeListener(this);
		mShaker.setOnShakeListener(new ShakeListener.OnShakeListener() {
			public void onShake() {
				// cancel existing events, schedule a new one happening
				// PICTURE_DELAY from now
				mHandler.removeCallbacks(PictureTask);
				printStatus(getText(R.string.takingPicture).toString());
				mHandler.postDelayed(PictureTask, PICTURE_DELAY);
			}
		});

		// camera setup
		preview = new SurfaceView(this);
		holder = preview.getHolder();
		holder.addCallback(new SurfaceHolder.Callback() {
			public void surfaceCreated(SurfaceHolder holder) {
				// no-op -- wait until surfaceChanged()
			}

			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {
				initPreview(width, height);
				startPreview();
			}

			public void surfaceDestroyed(SurfaceHolder holder) {
				// no-op
			}
		});
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onListItemClick(ListView parent, View v, int position, long id) {
		fileName = pictures.get(position);
		((TextView) findViewById(R.id.txtFileName)).setText(fileName);
		showPicture(v);
	}

	/**
	 * Takes a picture using the camera.
	 */
	public void takePicture() {
		if (camera == null) {
			printStatus(getText(R.string.cameraNotReady).toString());
		} else {
			mShaker.turnOff();
			// open back facing camera by default
			camera.takePicture(new Camera.ShutterCallback() {

				@Override
				public void onShutter() {
					// TODO Auto-generated method stub

				}
			}, null, new Camera.PictureCallback() {
				public void onPictureTaken(byte[] data, Camera camera) {

					// spawn thread, copy data over and save photo
					SavePhoto savePhoto = new SavePhoto();
					savePhoto.execute(data);

					// restart the camera preview, indicate the picture
					// process is done
					camera.startPreview();
					inPreview = true;
					printStatus(getText(R.string.pictureSaved).toString());
				}
			});
			inPreview = false;
		}
	}

	private void initPreview(int width, int height) {
		if (camera != null && holder.getSurface() != null) {
			try {
				camera.setPreviewDisplay(holder);
			} catch (Throwable t) {
			}

			Camera.Parameters parameters = camera.getParameters();
			parameters.setPreviewSize(parameters.getPreviewSize().width,
					parameters.getPreviewSize().height);
			parameters.setPictureFormat(ImageFormat.JPEG);

			camera.setParameters(parameters);
		}
	}

	private void startPreview() {
		if (camera != null) {
			camera.startPreview();
			inPreview = true;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		mShaker.resume();
		mlocManager.requestLocationUpdates(GPS_PROVIDER, 0, 0, mlocListener);
		camera = Camera.open();

		if (camera != null) {
			camera.startPreview();
		} else {
			printStatus(getText(R.string.error).toString());
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		mShaker.pause();
		mlocManager.removeUpdates(mlocListener);
		if (inPreview) {
			camera.stopPreview();
		}

		camera.release();
		camera = null;
		inPreview = false;
	}

	/**
	 * Displays the detail of a picture.
	 * 
	 * @param v
	 */
	public void showPicture(View v) {
		startActivity(new Intent(this, DetailActivity.class));
	}

	/**
	 * Removes an item from the list, deletes the corresponding picture.
	 * 
	 * @param v
	 */
	public void delete(View v) {
		if (fileName.length() != 0) {
			File file = new File(getExternalFilesDir(null), fileName);
			boolean deleted = file.delete();
			reload(v);
			printStatus((deleted) ? getText(R.string.fileDeleted).toString()
					: getText(R.string.error).toString());
		}
	}

	/**
	 * Reloads the main list of items.
	 * 
	 * @param v
	 */
	public void reload(View v) {
		fileName = "";
		((TextView) findViewById(R.id.txtFileName)).setText(fileName);
		((TextView) findViewById(R.id.txtStatus)).setText(getText(R.string.shake).toString());
		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, loadFiles()));
	}

	public static String getFileName() {
		return fileName;
	}

	public static void setFileName(String fileName) {
		MainActivity.fileName = fileName;
	}

}
