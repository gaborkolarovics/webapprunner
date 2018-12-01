package hu.polidor.webapprunner.nfc;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.util.Log;

import hu.polidor.webapprunner.MainActivity;
import hu.polidor.webapprunner.R;
import hu.polidor.webapprunner.common.AsyncTaskListener;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static hu.polidor.webapprunner.Const.TAG;

public class NdefReaderTask extends AsyncTask<Tag, Void, NfcResponse>
{
	private AsyncTaskListener<NfcResponse> listener;
	private Map<Integer, String> resValues;

	NdefReaderTask(final AsyncTaskListener<NfcResponse> listener, Map<Integer, String> resValues) {
		this.listener = listener;
		this.resValues = resValues;
	}
	
    @Override
    protected NfcResponse doInBackground(Tag... params)
	{
		Tag tag = params[0];
		Ndef ndef = Ndef.get(tag);
        if (ndef == null)
		{
			Log.e(TAG, resValues.get(R.string.ndef_not_support_tag));
			TagRecord tagRecord = new TagRecord();
			tagRecord.setData(resValues.get(R.string.ndef_not_support_tag));
			NfcResponse response = new NfcResponse();
			response.setStatus(NfcReaderStatus.ERROR);
			response.addRecord(tagRecord);
			return response;
        }

        NdefMessage ndefMessage = ndef.getCachedNdefMessage();

        NdefRecord[] records = ndefMessage.getRecords();
		List<TagRecord> tagRecords = new ArrayList<>();
        for (NdefRecord ndefRecord : records)
		{
			TagRecord tagRecord = new TagRecord();
			tagRecord.setMimeType(ndefRecord.toMimeType());
			try
			{
				if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN)
				{
					tagRecord.setData(readText(ndefRecord));
				}
				else
				{
					tagRecord.setData(new String(ndefRecord.getPayload()));
				}
			}
			catch (UnsupportedEncodingException e)
			{
				Log.e(TAG, resValues.get(R.string.unsupported_encoding), e);
				tagRecord.setData(resValues.get(R.string.unsupported_encoding));
			}
			tagRecords.add(tagRecord);
        }
		NfcResponse response = new NfcResponse();
		response.setStatus(NfcReaderStatus.OK);
		response.setData(tagRecords);
        return response;
    }

    private String readText(NdefRecord record) throws UnsupportedEncodingException
	{
        /*
         * See NFC forum specification for "Text Record Type Definition" at 3.2.1 
         * 
         * http://www.nfc-forum.org/specs/
         * 
         * bit_7 defines encoding
         * bit_6 reserved for future use, must be 0
         * bit_5..0 length of IANA language code
         */

        byte[] payload = record.getPayload();

        // Get the Text Encoding
        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";

        // Get the Language Code
        int languageCodeLength = payload[0] & 0063;

        // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
        // e.g. "en"

        // Get the Text
        return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
    }

    @Override
    protected void onPostExecute(NfcResponse result)
	{
		if (listener != null) {
			listener.OnTaskCompleted(result);
		}
    }
}
