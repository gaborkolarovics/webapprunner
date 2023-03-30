package hu.polidor.webapprunner.common;

import androidx.annotation.NonNull;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

import hu.polidor.webapprunner.widget.HttpGetJob;

/**
 * Evernote Job creator
 *
 * @author Gábor KOLÁROVICS
 * @since 2018.12.05
 */
public class ApplicationJobCreator implements JobCreator {

    @Override
    public Job create(@NonNull String tag) {
        if (HttpGetJob.JOB_TAG.equals(tag)) {
            return new HttpGetJob();
        }
        return null;
    }

}
