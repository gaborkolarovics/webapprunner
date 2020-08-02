package hu.polidor.webapprunner.shortcut;

import android.graphics.Bitmap;

/**
 * ShortCut base data
 *
 * @author Gábor Kolárovics
 * @since 2018.10.21
 */
public class ShortcutDto
{

	private String url;

	private String title;

	private Bitmap icon;

	private Bitmap defaultIcon;

	public String getUrl()
	{
		return url;
	}

	public void setUrl(final String url)
	{
		this.url = url;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(final String title)
	{
		this.title = title;
	}

	public Bitmap getIcon()
	{
		return icon;
	}

	public Bitmap getScaledBitmapIcon()
	{
		return Bitmap.createScaledBitmap(icon == null ? defaultIcon : icon, 64, 64, true);
	}

	public void setIcon(final Bitmap icon)
	{
		this.icon = icon;
	}

	public void setDefaultIcon(final Bitmap defaultIcon)
	{
		this.defaultIcon = defaultIcon;
	}

}
