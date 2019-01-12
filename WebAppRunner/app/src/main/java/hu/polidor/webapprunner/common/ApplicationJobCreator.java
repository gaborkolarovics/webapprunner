package hu.polidor.webapprunner.common;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

import hu.polidor.webapprunner.widget.HttpGetJob;

/**
 * Evernote Job creator
 *
 * @author Gábor KOLÁROVICS
 * @since 2018.12.05
 */
public class ApplicationJobCreator implements JobCreator
{

	@Override
	public Job create(String tag)
	{
		switch (tag)
		{
			case HttpGetJob.JOB_TAG :
				return new HttpGetJob();
			default :
				return null;
		}
	}

}
