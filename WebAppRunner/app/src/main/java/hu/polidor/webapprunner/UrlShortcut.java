package hu.polidor.webapprunner;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.os.Build;
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

import java.util.Arrays;

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

		Bitmap scaledBitmap = Bitmap.createScaledBitmap(faviconBitmap, 64, 64, true);

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
		{
			Intent addIntent = new Intent();
			addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
			addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, etName);
			if (faviconBitmap != null)
			{
				addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON, scaledBitmap);	
			}
			else
			{
				addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.ic_launcher));
			}
			addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
			getApplicationContext().sendBroadcast(addIntent);
		}
		else
		{
			ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);
			if (shortcutManager.isRequestPinShortcutSupported())
			{
				Icon icon = null;
				if (faviconBitmap != null)
				{
					icon = Icon.createWithBitmap(scaledBitmap);
				}
				else
				{
					icon = Icon.createWithResource(getApplicationContext(), R.drawable.ic_launcher);
				}
				ShortcutInfo shortcut = new ShortcutInfo.Builder(this, "id1")
					.setShortLabel(etName)
					.setIcon(icon)
					.setIntent(shortcutIntent)
					.build();
				shortcutManager.requestPinShortcut(shortcut, null);
			} 
			else
			{
				MainActivity.makeMsg("Pin shortcut request not supported!");
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		WebIconDatabase.getInstance().open(getDir("icons", MODE_PRIVATE).getPath());
		setContentView(R.layout.urlshortcut);

		final Button btnGetData = findViewById(R.id.btnGetData);
		btnGetData.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					btnGetData.setEnabled(false);
					EditText etURL = findViewById(R.id.etUrl);
					getPageData(etURL.getText().toString());
				}
			});

		Button btnAddIcon = findViewById(R.id.btnAddIcon);
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
					EditText etName = findViewById(R.id.etName);
					etName.setText(mTitle);
					EditText etURL = findViewById(R.id.etUrl);
					etURL.setText(mUrl);
					ProgressBar pbStatus = findViewById(R.id.pbStatus);
					pbStatus.setVisibility(ProgressBar.INVISIBLE);
					Button btnGetData = findViewById(R.id.btnGetData);
					btnGetData.setEnabled(true);
					if (!mTitle.isEmpty() && !mUrl.isEmpty())
					{
						Button btnAddIcon = findViewById(R.id.btnAddIcon);
						btnAddIcon.setEnabled(true);
					}
				}
			});
		wvPage.setWebChromeClient(new WebChromeClient(){
				@Override
				public void onProgressChanged(WebView view, int newProgress)
				{
					ProgressBar pbStatus = findViewById(R.id.pbStatus);
					pbStatus.setProgress(newProgress);
					super.onProgressChanged(view, newProgress);
				}

				@Override
				public void onReceivedIcon(WebView view, Bitmap icon)
				{
					super.onReceivedIcon(view, icon);
					mIcon = icon;
					ImageView ivFavicon = findViewById(R.id.ivFavicon);
					ivFavicon.setImageBitmap(mIcon);
				}
			});

		ProgressBar pbStatus = findViewById(R.id.pbStatus);
		pbStatus.setVisibility(ProgressBar.VISIBLE);
		if (!url.contains("://"))
		{
			url = "http://" + url.trim();
		}
		wvPage.loadUrl(url);
	}

}
