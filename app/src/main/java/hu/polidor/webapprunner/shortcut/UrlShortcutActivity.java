package hu.polidor.webapprunner.shortcut;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.webkit.WebIconDatabase;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import hu.polidor.webapprunner.R;

/**
 * Create shortcut activity
 *
 * @author Gábor KOLÁROVICS
 * @since 2018.10.21
 */
public class UrlShortcutActivity extends Activity
{

	/**
	 * GetData Button
	 */
	private Button btnGetData;

	/**
	 * AddIcon Button
	 */
	private Button btnAddIcon;

	/**
	 * Url EditText
	 */
	private EditText etUrl;

	/**
	 * Title EditText
	 */
	private EditText etTitle;

	/**
	 * Icon ImageView
	 */
	private ImageView ivIcon;

	/**
	 * Download progress bar
	 */
	private ProgressBar progressBar;

	/**
	 * Shortcut data
	 */
	private ShortcutDto store;

	/**
	 * Singleton GetData button
	 */
	public Button getBtnGetData()
	{
		if (btnGetData == null)
		{
			btnGetData = findViewById(R.id.btnGetData);
		}
		return btnGetData;
	}

	/**
	 * Singleton AddIcon button (default disabled)
	 */
	public Button getBtnAddIcon()
	{
		if (btnAddIcon == null)
		{
			btnAddIcon = findViewById(R.id.btnAddIcon);
			btnAddIcon.setEnabled(false);
		}
		return btnAddIcon;
	}

	/**
	 * Singleton Url edit text
	 */
	public EditText getEtUrl()
	{
		if (etUrl == null)
		{
			etUrl = findViewById(R.id.etUrl);
		}
		return etUrl;
	}

	/**
	 * Singleton Title edit text
	 */
	public EditText getEtTitle()
	{
		if (etTitle == null)
		{
			etTitle = findViewById(R.id.etName);
		}
		return etTitle;
	}

	/**
	 * Singleton Favicon image view
	 */
	public ImageView getIvIcon()
	{
		if (ivIcon == null)
		{
			ivIcon = findViewById(R.id.ivFavicon);
		}
		return ivIcon;
	}

	/**
	 * Singleton ProgressBar
	 */
	public ProgressBar getProgressBar()
	{
		if (progressBar == null)
		{
			progressBar = findViewById(R.id.pbStatus);
		}
		return progressBar;
	}

	/**
	 * Singleton Shortcut data store
	 */
	public ShortcutDto getStore()
	{
		if (store == null)
		{
			store = new ShortcutDto();
			store.setDefaultIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
		}
		return store;
	}

	/**
	 * Enable AddIcon button if shortcut data is exist
	 */
	public void enableAddIconBtn()
	{
		if (!getEtTitle().getText().toString().isEmpty() && !getEtUrl().getText().toString().isEmpty())
		{
			getBtnAddIcon().setEnabled(true);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		WebIconDatabase.getInstance().open(getDir("icons", MODE_PRIVATE).getPath());
		setContentView(R.layout.urlshortcut);

		getBtnGetData().setOnClickListener(new GetDataListener(this));
		getBtnAddIcon().setOnClickListener(new AddIconListener(this));
	}

}
