package hu.polidor.webapprunner.donate;
import hu.polidor.webapprunner.*;
import hu.polidor.webapprunner.donate.google.util.*;

/**
 * Implementation of Iab setup finis listener
 *
 * @author Gábor KOLÁROVICS
 * @since 2019/10/13
 */
public class SetupFinisListener implements IabHelper.OnIabSetupFinishedListener
{

	/**
	 * Parent donation activity
	 */
	private final DonationActivity donationActivity;
	
	/**
	 * Constructor with parent activity
	 */
	public SetupFinisListener(DonationActivity donationActivity)
	{
		this.donationActivity = donationActivity;
	}
	
	@Override
	public void onIabSetupFinished(IabResult result)
	{
		donationActivity.log("Setup finished.");
		if (!result.isSuccess())
		{
			donationActivity.openDialog(
				android.R.drawable.ic_dialog_alert, 
				R.string.donations__google_android_market_not_supported_title, 
				donationActivity.getString(R.string.donations__google_android_market_not_supported)
			);
		}
	}

}
