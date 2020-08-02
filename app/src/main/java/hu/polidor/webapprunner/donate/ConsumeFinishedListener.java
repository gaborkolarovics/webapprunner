package hu.polidor.webapprunner.donate;

import hu.polidor.webapprunner.donate.google.util.IabHelper;
import hu.polidor.webapprunner.donate.google.util.IabResult;
import hu.polidor.webapprunner.donate.google.util.Purchase;

/**
 * Consume finished listener
 *
 * @author Gábor KOLÁROVICS
 * @since 2019.08.24
 */
public class ConsumeFinishedListener implements IabHelper.OnConsumeFinishedListener
{

	/**
	 * Parent activity
	 */
	private final DonationActivity donationActivity;

	/**
	 * Construcor with parent activity
	 */
	public ConsumeFinishedListener(final DonationActivity donationActivity)
	{
		this.donationActivity = donationActivity;
	}

	@Override
	public void onConsumeFinished(Purchase purchase, IabResult result)
	{
		donationActivity.log("Consumption finished. Purchase: " + purchase + ", result: " + result);
		if (donationActivity.getIabHelper() != null && result.isSuccess())
		{
			donationActivity.log("Consumption successful. Provisioning.");
		}
		donationActivity.log("End consumption flow.");
	}

}
