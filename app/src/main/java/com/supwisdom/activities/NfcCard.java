package com.supwisdom.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.tech.IsoDep;
import android.provider.Settings;
import android.widget.Toast;

import com.supwisdom.R;

import java.util.HashMap;


public final class NfcCard {
    private static NfcCard sInstance = null;

    public static synchronized NfcCard getInstance() {
        if (null == sInstance) {
            sInstance = new NfcCard();
        }
        return sInstance;
    }

    NfcAdapter mAdapter = null;
    String[][] techListArray = null;
    IntentFilter[] intentFilterArray = null;
    HashMap<Activity, PendingIntent> activityIntents;

    private NfcCard() {
        activityIntents = new HashMap<Activity, PendingIntent>();
        mAdapter = NfcAdapter.getDefaultAdapter(SPApplication.getInstance());

        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        try {
            ndef.addDataType("*/*");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            Toast.makeText(SPApplication.getInstance(), "NFC filter not support", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        intentFilterArray = new IntentFilter[]{ndef,};

        techListArray = new String[][]{new String[]{IsoDep.class.getName()}};
    }

    public void disableNfcForeground(Activity activity) {
        mAdapter.disableForegroundDispatch(activity);
    }

    public boolean enableNfcForeground(Activity activity) {
        PendingIntent pendingIntent = null;
        if (activityIntents.containsKey(activity)) {
            pendingIntent = activityIntents.get(activity);
        } else {
            pendingIntent = PendingIntent.getActivity(activity, 0,
                    new Intent(activity, activity.getClass()).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
            activityIntents.put(activity, pendingIntent);
        }
        mAdapter.enableForegroundDispatch(activity, pendingIntent, intentFilterArray, techListArray);
        return true;
    }

    public boolean checkNfcAdpater(Activity activity) {
        if (null == mAdapter) {
            Toast.makeText(activity, activity.getString(R.string.nfc_not_support),
                    Toast.LENGTH_LONG).show();
            return false;
        }
        if (!mAdapter.isEnabled()) {
            showOpenNfc(activity);
        }
        return true;
    }

    private void showOpenNfc(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.nfc_disabled)
                .setMessage(R.string.confirm_open_nfc)
                .setPositiveButton(R.string.action_settings,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                activity.startActivity(new Intent(
                                        Settings.ACTION_NFC_SETTINGS));
                                activity.finish();
                            }
                        })
                .setNegativeButton(R.string.action_exit,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                                activity.finish();
                            }
                        }).show();
    }
}
