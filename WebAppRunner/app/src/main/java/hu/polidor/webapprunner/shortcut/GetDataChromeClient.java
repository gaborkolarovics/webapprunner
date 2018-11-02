package hu.polidor.webapprunner.shortcut;

import android.graphics.Bitmap;
import android.webkit.WebView;

import hu.polidor.webapprunner.common.BaseWebChromeClient;

/**
 * Download Icon and Title. Use BaseWebChromeClient with progress bar.
 *
 * @author Gábor KOLÁROVICS
 * @since 2018.11.01
 */
public class GetDataChromeClient extends BaseWebChromeClient
{

	/**
	 * Parnet activity
	 */
	private UrlShortcutActivity urlShortcut;

	/**
	 * Construct with BaseWebChromeClient
	 */
	public GetDataChromeClient(final UrlShortcutActivity urlShortcut)
	{
		super(urlShortcut.getProgressBar());
		this.urlShortcut = urlShortcut;
	}

	@Override
	public void onReceivedIcon(WebView view, Bitmap icon)
	{
		urlShortcut.getIvIcon().setImageBitmap(icon);
		urlShortcut.getStore().setIcon(icon);
		super.onReceivedIcon(view, icon);
	}

	@Override
	public void onReceivedTitle(WebView view, String title)
	{
		urlShortcut.getEtTitle().setText(title);
		urlShortcut.getStore().setTitle(title);
		urlShortcut.enableAddIconBtn();
		super.onReceivedTitle(view, title);
	}

}
