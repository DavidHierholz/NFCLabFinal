package com.example.david.nfclabfinal;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.nio.charset.Charset;

public class WriteActivity extends AppCompatActivity {

    private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;
    private EditText editText;
    private Ndef ndef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        editText = (EditText) findViewById(R.id.editText);

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
        }
    }

    /**
     *
     * This method is called when the Button is clicked.
     * You should get the current content of the EditText and write it onto the NFC Tag.
     *
     * **/
    public void onClickWriteToTagButton(View view) {
        if (ndef != null) {
            try {
                ndef.connect();
                NdefRecord mimeRecord = NdefRecord.createMime("text/plain",
                        editText.getText().toString().getBytes(Charset.forName("US-ASCII")));
                ndef.writeNdefMessage(new NdefMessage(mimeRecord));
                ndef.close();
                Toast.makeText(this, "Write success", Toast.LENGTH_SHORT).show();
            } catch (IOException | FormatException e) {
                e.printStackTrace();
                Toast.makeText(this, "Write error", Toast.LENGTH_SHORT).show();
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
