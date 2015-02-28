package com.supwisdom.activities;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.supwisdom.R;
import com.supwisdom.cardlib.CardCommand;
import com.supwisdom.cardlib.CardException;
import com.supwisdom.cardlib.CardNotSupportException;
import com.supwisdom.cardlib.CardRecord;
import com.supwisdom.cardlib.CardValueException;
import com.supwisdom.cardlib.EcardLib;
import com.supwisdom.swpos.ErrorDef;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ReadCardActivity extends BaseActivity {

    private static final String tag = "com.supwisdom.activities.readcard";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readcard);
        InitView();
    }

    private void InitView() {
        //消费按钮
        Button btn = (Button)findViewById(R.id.btn_purchase);
        btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                //扣费
                Toast.makeText(ReadCardActivity.this, "消费成功!!",
                        Toast.LENGTH_SHORT).show();
            }
        });
        //返回按钮
        btn = (Button)findViewById(R.id.btn_back);
        btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(ReadCardActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (!NfcCard.getInstance().checkNfcAdpater(this)) {
            this.finish();
            return;
        }

//        EcardLib card = YktSession.getInstance().testAndGetDefaultCardLib();
//        if (card != null) {
//            doReadCard(card);
//        }

    }

//    @Override
//    protected void onResume(){
//        EditText et = (EditText)findViewById(R.id.et_money);
//        et.setFocusable(true);
//        et.setFocusableInTouchMode(true);
//        et.requestFocus();
//        et.requestFocusFromTouch();
//    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            Log.i(tag, "request Card");
            Parcelable nfcTag = intent.getParcelableExtra("android.nfc.extra.TAG");
            if (nfcTag != null) {
                Tag t = (Tag) nfcTag;
                EcardLib card = YktSession.getInstance().createFromTag(t);
                if (null == card) {
                    Toast.makeText(this, "Cannot initialize card object",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                int retcode = ErrorDef.SPSUCCESS;
                while(true) {
                     retcode = doReadCard(card);
                     if(retcode != ErrorDef.SPSUCCESS){
                         Toast.makeText(this, "read card error["+Integer.toHexString(retcode)+"]", Toast.LENGTH_SHORT).show();
                         break;
                     }
                }
            } else {
                Toast.makeText(this, "Not ISO14443-A card", Toast.LENGTH_LONG)
                        .show();
            }
        }
    }


    private int doReadCard(EcardLib card) {
        String errmsg = null;
        int retcode = ErrorDef.SPSUCCESS;
        try {
            card.setFieldRead(EcardLib.FIELD_CUSTNAME);
            card.setFieldRead(EcardLib.FIELD_STUEMPNO);
            card.setFieldRead(EcardLib.FIELD_EXPIREDATE);
            card.setFieldRead(EcardLib.FIELD_BALANCE);
            card.setFieldRead(EcardLib.FIELD_CARDNO);

            card.readCard();

            String custName = card.getFieldValue(EcardLib.FIELD_CUSTNAME);
            String stuEmpNo = card.getFieldValue(EcardLib.FIELD_STUEMPNO);
            String expireDate = card.getFieldValue(EcardLib.FIELD_EXPIREDATE);
            String cardNo = card.getFieldValue(EcardLib.FIELD_CARDNO);

            EditText et = (EditText)findViewById(R.id.et_cardno);
            et.setText("卡号:     "+cardNo);

            et = (EditText)findViewById(R.id.et_name);
            et.setText("姓名:     "+custName);

            et = (EditText)findViewById(R.id.et_stuempno);
            et.setText("学工号:     "+stuEmpNo);

            et = (EditText)findViewById(R.id.et_expiredate);
            et.setText("有效期:     "+expireDate);

        } catch (IOException e) {
            Log.e(tag, "Connect to card , " + e);
            errmsg = getString(R.string.connfail);
            retcode = ErrorDef.REQUESTCARDERROR;
        } catch (CardException e) {
            Log.e(tag, "Read card error, " + e);
            errmsg = getString(R.string.msg_read_card_error);
            retcode = ErrorDef.READCARDERROR;
        } catch (CardNotSupportException e) {
            Log.e(tag, "Read card error, " + e);
            errmsg = getString(R.string.msg_notsupport_card_oper);
            retcode = ErrorDef.CARDNOTSUPPORT;
        } catch (CardValueException e) {
            Log.e(tag, "Read card error, " + e);
            errmsg = getString(R.string.msg_card_data_error);
            retcode = ErrorDef.CARDDATAERROR;
        }
        if (errmsg != null) {
            Toast.makeText(this, errmsg, Toast.LENGTH_SHORT).show();
        }
        return retcode;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_readcard, menu);
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
