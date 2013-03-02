package edu.toronto.cs.ece1778.peoplesearcher;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Activity showing results of name lookup in a WebView.
 * 
 * @author mcupak
 * 
 */
public class WebActivity extends Activity {

	private static final String url = "http://www.google.com/search?q=";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.web, menu);
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();

		// load URL
		String name = getIntent().getStringExtra(DatabaseHelper.NAME);
		WebView webview = (WebView) findViewById(R.id.webGoogle);
		webview.setWebViewClient(new WebViewClient());
		webview.loadUrl(url + name);
	}

}
