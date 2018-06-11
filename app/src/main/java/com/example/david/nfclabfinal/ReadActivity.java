package com.example.david.nfclabfinal;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ReadActivity extends AppCompatActivity {

    private TextView textView;
    private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;
    private Ndef ndef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        textView = (TextView) findViewById(R.id.textView);

        mAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mAdapter == null) {
            Toast.makeText(this, "Your Device does not support NFC", Toast.LENGTH_SHORT).show();
        }
        mPendingIntent = PendingIntent.getActivity(this,
                0, new Intent(this,
                        getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null && NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {

            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            ndef = Ndef.get(tag);
            Parcelable[] rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            if(rawMessages != null) {
                NdefMessage[] messages = new NdefMessage[rawMessages.length];
                for (int i=0; i < rawMessages.length; i++) {
                    messages[i] = (NdefMessage) rawMessages[i];
                }

                NdefRecord record = messages[0].getRecords()[0];
                byte[] payload = record.getPayload();
                String content = new String(payload);
                textView.setText(content);
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAdapter != null) {
            mAdapter.disableForegroundDispatch(this);
        }
    }
}
