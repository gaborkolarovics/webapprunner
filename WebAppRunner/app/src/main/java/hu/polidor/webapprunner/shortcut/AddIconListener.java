package hu.polidor.webapprunner.shortcut;

import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.view.View;
import android.view.View.OnClickListener;

import hu.polidor.webapprunner.MainActivity;
import hu.polidor.webapprunner.R;

import java.util.UUID;

/**
 * Create shortcut
 *
 * @author Gábor KOLÁROVICS
 * @since 2018.10.22
 */
public class AddIconListener implements OnClickListener
{
	/**
	 * Parent activity
	 */
	private UrlShortcutActivity urlShortcut;

	/**
	 * Construcor with parent activity
	 */
	public AddIconListener(final UrlShortcutActivity urlShortcut)
	{
		this.urlShortcut = urlShortcut;
	}

	/**
	 * Create shortcut with ShortcutManager
	 *
	 * @param shortcutDto : Shortcut data
	 */
	private void createShortcutByShortcutManager(final ShortcutDto shortcutDto)
	{
		final ShortcutManager shortcutManager = urlShortcut.getSystemService(ShortcutManager.class);
		if (shortcutManager.isRequestPinShortcutSupported())
		{
			final ShortcutInfo shortcut = new ShortcutInfo.Builder(urlShortcut, UUID.randomUUID().toString())
				.setShortLabel(shortcutDto.getTitle())
				.setIcon(Icon.createWithBitmap(shortcutDto.getScaledBitmapIcon()))
				.setIntent(createShortcutIntent(shortcutDto.getUrl()))
				.build();
			shortcutManager.requestPinShortcut(shortcut, null);
		} 
		else
		{
			MainActivity.makeMsg(urlShortcut.getResources().getString(R.string.shortcut_not_supported));
		}
	}

	/**
	 * Create shortcut with Intent (before Oreo)
	 *
	 * @param shortcutDto : Shortcut data
	 */
	private void createShortcutByIntent(final ShortcutDto shortcutDto)
	{
		final Intent addIntent = new Intent();
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, createShortcutIntent(shortcutDto.getUrl()));
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcutDto.getTitle());
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON, shortcutDto.getScaledBitmapIcon());	
		addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
		urlShortcut.getApplicationContext().sendBroadcast(addIntent);
	}

	/**
	 * Create intent to shortcut
	 *
	 * @param url : Shortcut url
	 * @return Intent : Shortcut intent
	 */
	private Intent createShortcutIntent(final String url)
	{
		final Intent shortcutIntent = new Intent(urlShortcut.getApplicationContext(), MainActivity.class);
		shortcutIntent.setAction(Intent.ACTION_MAIN);
		shortcutIntent.putExtra(MainActivity.WEBAPP_INTENT_URL, url);
		return shortcutIntent;
	}

	@Override
	public void onClick(View view)
	{
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
		{
			createShortcutByIntent(urlShortcut.getStore());
		}
		else
		{
			createShortcutByShortcutManager(urlShortcut.getStore());
		}
		urlShortcut.finish();
	}

}
