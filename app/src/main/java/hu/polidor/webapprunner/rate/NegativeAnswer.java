package hu.polidor.webapprunner.rate;

import android.app.Activity;
import android.content.DialogInterface;

import hu.polidor.webapprunner.common.PreferenceHelper;

/**
 * Negative answer to applicaton rate question
 *
 * @author Gábor KOLÁROVICS
 * @since 2018.11.28
 */
public class NegativeAnswer implements DialogInterface.OnClickListener {

    private final Activity activity;

    public NegativeAnswer(final Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int action) {
        PreferenceHelper.setRateDialogApproved(activity, true);
    }

}
