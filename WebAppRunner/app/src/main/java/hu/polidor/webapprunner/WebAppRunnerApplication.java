package hu.polidor.webapprunner;

import android.app.Application;
import android.util.Log;

import com.evernote.android.job.JobManager;
import com.google.firebase.FirebaseApp;

import hu.polidor.webapprunner.common.ApplicationJobCreator;
import hu.polidor.webapprunner.rate.AppRate;

import static hu.polidor.webapprunner.Const.TAG;

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
		Log.d(TAG, "Application on create start.");
		AppRate.getInstance().monitor(this);
		JobManager.create(this).addJobCreator(new ApplicationJobCreator());
		FirebaseApp.initializeApp(this);
		Log.d(TAG, "application on create end.");
	}

}
