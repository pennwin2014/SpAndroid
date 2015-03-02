package com.supwisdom.activities;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.supwisdom.R;
import com.supwisdom.cardlib.EcardLib;


public class BaseActivity extends ActionBarActivity {

    public static EcardLib globalCard = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }

    @Override
    protected void onPause() {
        super.onPause();
        NfcCard.getInstance().disableNfcForeground(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        NfcCard.getInstance().enableNfcForeground(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            Parcelable nfcTag = intent
                    .getParcelableExtra("android.nfc.extra.TAG");

            if (nfcTag != null) {
                Tag t = (Tag) nfcTag;
                globalCard = YktSession.getInstance().createFromTag(t);
                if (null == globalCard) {
                    Toast.makeText(this, "Cannot initialize card object",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
            } else {
                Toast.makeText(this, "Not ISO14443-A card", Toast.LENGTH_LONG)
                        .show();
            }
        }
//        Parcelable nfcTag = intent.getParcelableExtra("android.nfc.extra.TAG");
//        if (nfcTag != null) {
//            YktSession.getInstance().resetNfcTag();
//            YktSession.getInstance().createFromTag((Tag) nfcTag);
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_base, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
