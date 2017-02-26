package hu.polidor.webapprunner;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import java.net.MalformedURLException;
import java.net.URL;

public class WebAppWebViewClient extends WebViewClient
{

    ProgressBar pbStatus;
    private boolean localURL;
    private String appUrl;
	private String lastUrl;
    private Context activity;
	private SwipeRefreshLayout swipeRL;

    public WebAppWebViewClient(Context activity, ProgressBar pbStatus, SwipeRefreshLayout swipeRL)
	{
        this.activity = activity;
        this.pbStatus = pbStatus;
		this.swipeRL = swipeRL;
    }

	public void setAppUrl(String appUrl)
	{
		this.appUrl = appUrl;
	}

	public void setLastUrl(String lastUrl)
	{
		this.lastUrl = lastUrl;
	}

	public String getLastUrl()
	{
		return this.lastUrl;
	}

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url)
	{
        pbStatus.setProgress(0);
        pbStatus.setVisibility(pbStatus.VISIBLE);
        setLocalUrl(url);
        view.loadUrl(url);
        return true;
    }

    @Override
    public void onPageFinished(WebView view, String url)
	{
        pbStatus.setVisibility(pbStatus.INVISIBLE);
		swipeRL.setRefreshing(false);
        setLocalUrl(url);
		setLastUrl(url);
        super.onPageFinished(view, url);
    }

    public boolean getLocalUrl()
	{
        return localURL;
    }

    public boolean setLocalUrl(String url)
	{
        try
		{
            URL webappURL = new URL(appUrl);
            URL loadURL = new URL(url);
            this.localURL = webappURL.getHost().equalsIgnoreCase(loadURL.getHost());
        }
		catch (MalformedURLException e)
		{
            e.printStackTrace();
        }

		return this.localURL;
    }
}
