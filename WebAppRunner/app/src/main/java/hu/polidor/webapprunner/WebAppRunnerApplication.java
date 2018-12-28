package hu.polidor.webapprunner;

import android.app.Application;
import com.evernote.android.job.JobManager;

import hu.polidor.webapprunner.common.ApplicationJobCreator;
import hu.polidor.webapprunner.rate.AppRate;

/**
 * WebAppRunner Application base initializatin
 *
 * @author Gábor KOLÁROVICS
 * @since 2018.12.05
 */
public class WebAppRunnerApplication extends Application
{

	@Override
	public void onCreate()
	{
		super.onCreate();
		AppRate.getInstance().monitor(this);
		JobManager.create(this).addJobCreator(new ApplicationJobCreator());
	}

}
