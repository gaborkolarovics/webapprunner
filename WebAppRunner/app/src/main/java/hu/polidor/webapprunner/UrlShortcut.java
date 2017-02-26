package hu.polidor.webapprunner;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebIconDatabase;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class UrlShortcut extends Activity
{
	public String mUrl;
	public String mTitle;
	public Bitmap mIcon;

	public void addShortcut(String etName, String etURL, Bitmap faviconBitmap)
	{
		Intent shortcutIntent = new Intent(getApplicationContext(), MainActivity.class);
		shortcutIntent.setAction(Intent.ACTION_MAIN);
		shortcutIntent.putExtra(MainActivity.WEBAPP_INTENT_URL, etURL);

		Intent addIntent = new Intent();
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, etName);
		if (faviconBitmap != null)
		{
			Bitmap scaledBitmap = Bitmap.createScaledBitmap(faviconBitmap, 64, 64, true);
			addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON, scaledBitmap);	
		}
		else
		{
			addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.ic_launcher));
		}
		addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
		getApplicationContext().sendBroadcast(addIntent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		WebIconDatabase.getInstance().open(getDir("icons", MODE_PRIVATE).getPath());
		setContentView(R.layout.urlshortcut);

		final Button btnGetData = (Button) findViewById(R.id.btnGetData);
		btnGetData.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					btnGetData.setEnabled(false);
					EditText etURL = (EditText) findViewById(R.id.etUrl);
					getPageData(etURL.getText().toString());
				}
			});

		Button btnAddIcon = (Button) findViewById(R.id.btnAddIcon);
		btnAddIcon.setEnabled(false);
		btnAddIcon.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					addShortcut(mTitle, mUrl, mIcon);
					finish();
				}
			});
	}

	public void getPageData(String url)
	{
		WebView wvPage = new WebView(this);
		wvPage.setWebViewClient(new WebViewClient(){

				@Override
				public void onPageFinished(WebView view, String url)
				{
					mUrl = view.getUrl();
					mTitle = view.getTitle();
					EditText etName = (EditText) findViewById(R.id.etName);
					etName.setText(mTitle);
					EditText etURL = (EditText) findViewById(R.id.etUrl);
					etURL.setText(mUrl);
					ProgressBar pbStatus = (ProgressBar) findViewById(R.id.pbStatus);
					pbStatus.setVisibility(ProgressBar.INVISIBLE);
					Button btnGetData = (Button) findViewById(R.id.btnGetData);
					btnGetData.setEnabled(true);
					if (!mTitle.isEmpty() && !mUrl.isEmpty())
					{
						Button btnAddIcon = (Button) findViewById(R.id.btnAddIcon);
						btnAddIcon.setEnabled(true);
					}
				}
			});
		wvPage.setWebChromeClient(new WebChromeClient(){
				@Override
				public void onProgressChanged(WebView view, int newProgress)
				{
					ProgressBar pbStatus = (ProgressBar) findViewById(R.id.pbStatus);
					pbStatus.setProgress(newProgress);
					super.onProgressChanged(view, newProgress);
				}

				@Override
				public void onReceivedIcon(WebView view, Bitmap icon)
				{
					super.onReceivedIcon(view, icon);
					mIcon = icon;
					ImageView ivFavicon = (ImageView) findViewById(R.id.ivFavicon);
					ivFavicon.setImageBitmap(mIcon);
				}
			});

		ProgressBar pbStatus = (ProgressBar) findViewById(R.id.pbStatus);
		pbStatus.setVisibility(ProgressBar.VISIBLE);
		if (!url.contains("://"))
		{
			url = "http://" + url.trim();
		}
		wvPage.loadUrl(url);
	}

}
