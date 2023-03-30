package hu.polidor.webapprunner.rate;

import android.app.Activity;
import android.app.AlertDialog;

import hu.polidor.webapprunner.R;

/**
 * Create Rate dialog with actions
 *
 * @author Gábor KOLÁROVICS
 * @since 2018.11.19
 */
public final class DialogManager {

    /**
     * Show rate dialog
     *
     * @param activity Application activity
     */
    public static void show(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(R.string.rate_dialog_message);
        builder.setTitle(R.string.rate_dialog_title);

        builder.setPositiveButton(R.string.rate_dialog_ok, new PositiveAnswer(activity));
        builder.setNegativeButton(R.string.rate_dialog_no, new NegativeAnswer(activity));
        builder.setNeutralButton(R.string.rate_dialog_cancel, new NeutralAnswer(activity));

        builder.setCancelable(true);
        builder.show();
    }

}
