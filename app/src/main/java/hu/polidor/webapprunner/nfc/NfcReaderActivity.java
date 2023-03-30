package hu.polidor.webapprunner.nfc;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import hu.polidor.webapprunner.MainActivity;
import hu.polidor.webapprunner.R;
import hu.polidor.webapprunner.common.AsyncTaskListener;

public class NfcReaderActivity extends Activity {

    public static final String MIME_TEXT_PLAIN = "text/plain";

    private TextView mTextView;
    private NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nfclayout);

        mTextView = findViewById(R.id.tvnfcmsg);
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        Button btnCancel = findViewById(R.id.btnnfccancel);

        if (mNfcAdapter == null) {
            mTextView.setText(R.string.nfc_not_supported);
        } else {
            if (!mNfcAdapter.isEnabled()) {
                mTextView.setText(R.string.nfc_disabled);
            } else {
                mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
                mTextView.setText(R.string.nfc_waiting);
            }
        }

        btnCancel.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString(MainActivity.INTENT_STATUS, MainActivity.INTENT_STATUS_CANCEL);
                Intent intent = new Intent();
                intent.putExtras(b);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        if (mNfcAdapter != null && mNfcAdapter.isEnabled()) {
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (mNfcAdapter != null) {
            mNfcAdapter.disableForegroundDispatch(this);
        }
        super.onPause();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

            Map<Integer, String> resValues = new HashMap<>();
            resValues.put(R.string.ndef_not_support_tag, getResources().getString(R.string.ndef_not_support_tag));
            resValues.put(R.string.unsupported_encoding, getResources().getString(R.string.unsupported_encoding));

            AsyncTaskListener<NfcResponse> onTaskCompleted = new AsyncTaskListener<NfcResponse>() {
                @Override
                public void OnTaskCompleted(NfcResponse response) {
                    if (NfcReaderStatus.OK.equals(response.getStatus())) {
                        Gson gson = new Gson();
                        succesRead(gson.toJson(response.getRecords()));
                    } else {
                        if (response.getRecords() != null && response.getRecords().size() > 0) {
                            mTextView.setText(response.getRecords().get(0).getData());
                        }
                    }
                }
            };
            NdefReaderTask r = new NdefReaderTask(onTaskCompleted, resValues);
            r.execute(tag);
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            mTextView.setText("ACTION_TECH_DISCOVERED. It's not implemented yet.");
			/*
			 // In case we would still use the Tech Discovered Intent
			 Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			 String[] techList = tag.getTechList();
			 String searchedTech = Ndef.class.getName();
			 vText = "Action tech discover\n";
			 for (String tech : techList)
			 {
			 if (searchedTech.equals(tech))
			 {
			 vText += reader(tag);
			 break;
			 }
			 }
			 */
        } else if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
            mTextView.setText("ACTION_TAG_DISCOVERED. It's not implemented yet.");
			/*
			 Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			 vText = "Action tag discover - " + tag.getId().toString() + "\n";
			 for (String t : tag.getTechList())
			 {
			 vText += "tag : " + t + "\n";
			 }
			 */
        } else {
            mTextView.setText("Unknow action.");
        }
    }

    private void succesRead(String response) {
        Bundle b = new Bundle();
        b.putString(MainActivity.INTENT_STATUS, MainActivity.INTENT_STATUS_DONE);
        b.putString(MainActivity.INTENT_VALUE, response);
        Intent intent = new Intent();
        intent.putExtras(b);
        setResult(RESULT_OK, intent);
        finish();
    }

}
