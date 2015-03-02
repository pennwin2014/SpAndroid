package com.supwisdom.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.supwisdom.R;
import com.supwisdom.cardlib.CardException;
import com.supwisdom.cardlib.CardNotSupportException;
import com.supwisdom.cardlib.CardValueException;
import com.supwisdom.cardlib.EcardLib;
import com.supwisdom.db.BeanPropEnum;
import com.supwisdom.db.TransdtlDao;
import com.supwisdom.swpos.TransNo;
import com.supwisdom.utilities.ErrorDef;
import com.supwisdom.utilities.ErrorInfo;
import com.supwisdom.utilities.TransRecord;

import java.io.IOException;

public class ReadCardActivity extends BaseActivity {

    private static final String tag = "com.supwisdom.activities.readcard";

    private TransdtlDao transdtlDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readcard);
        InitView();
    }

    private void InitView() {
        transdtlDao = transdtlDao.getInstance(this);
        //消费按钮
        Button btn = (Button)findViewById(R.id.btn_purchase);
        btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransRecord record = new TransRecord();
                ErrorInfo ret = initTransdtl(record);
                if(ret != ErrorDef.SP_SUCCESS){
                    Toast.makeText(ReadCardActivity.this, "操作数据库失败,错误码["+Integer.toHexString(ret.errorCode)+"]", Toast.LENGTH_SHORT).show();
                }
                ret = doPurchaseCard(globalCard);
                if(ErrorDef.SP_SUCCESS == ret){
                    Toast.makeText(ReadCardActivity.this, "消费成功", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ReadCardActivity.this, "消费失败,错误码["+Integer.toHexString(ret.errorCode)+"]", Toast.LENGTH_SHORT).show();
                }
                //根据读到的卡和交易类型来设置交易类别
                record.setTransFlag(getTransFlagByErrorCode(ret.errorCode));
                ret = confirmTransdtl(record);
                if(ErrorDef.SP_SUCCESS != ret){
                    Toast.makeText(ReadCardActivity.this, "操作数据库失败,错误码["+Integer.toHexString(ret.errorCode)+"]", Toast.LENGTH_SHORT).show();
                }
                //showRequestCard();
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
        //输金额按钮
        btn = (Button)findViewById(R.id.btn_input_amount);
        btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                showReadCard();
                EditText et = (EditText)findViewById(R.id.et_money);
                et.setFocusable(true);
                et.requestFocus();
            }
        });
    }

    private int getTransFlagByErrorCode(int errorCode) {
        return 0;
    }

    private ErrorInfo confirmTransdtl(TransRecord record) {
        try {
            record.setTac(globalCard.getFieldValue("tac"));
        } catch (CardException e) {
            e.printStackTrace();
        }
        return ErrorDef.SP_SUCCESS;
    }

    private ErrorInfo initTransdtl(TransRecord record) {
        record.setTransNo(TransNo.generateTransNo(this));
        transdtlDao.saveTransdtl(record);
        return ErrorDef.SP_SUCCESS;
    }

    private ErrorInfo doPurchaseCard(EcardLib card) {
        int amount = 0;
        EditText et = (EditText)findViewById(R.id.et_money);
        amount = (int)Math.ceil(Float.valueOf(et.getText().toString())*100.0);
        return card.purchase(amount);
    }


    @Override
    protected void onStart() {
        super.onStart();
        showRequestCard();
        if (!NfcCard.getInstance().checkNfcAdpater(this)) {
            this.finish();
            return;
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(globalCard!=null){
            showReadCard();
            if(ErrorDef.SP_SUCCESS != doReadCard()){
                showRequestCard();
            }
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(globalCard!=null){
            showReadCard();
            if(ErrorDef.SP_SUCCESS != doReadCard()){
                showRequestCard();
            }
        }
//        String action = intent.getAction();
//        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
//            Log.d(tag, "request Card");
//            Parcelable nfcTag = intent
//                    .getParcelableExtra("android.nfc.extra.TAG");
//
//            if (nfcTag != null) {
//                Tag t = (Tag) nfcTag;
//                //EcardLib
//                globalCard = YktSession.getInstance().createFromTag(t);
//                if (null == globalCard) {
//                    Toast.makeText(this, "Cannot initialize card object",
//                            Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                showReadCard();
//                doReadCard(globalCard);
//            } else {
//                Toast.makeText(this, "Not ISO14443-A card", Toast.LENGTH_LONG)
//                        .show();
//            }
//        }
    }

    private void showReadCard() {
        //隐藏寻卡界面
        TextView tv = (TextView)findViewById(R.id.tv_requestcard);
        tv.setVisibility(View.INVISIBLE);
        Button btn = (Button)findViewById(R.id.btn_input_amount);
        btn.setVisibility(View.INVISIBLE);
        //显示读卡界面
        EditText et = (EditText)findViewById(R.id.et_name);
        et.setVisibility(View.VISIBLE);
        et = (EditText)findViewById(R.id.et_cardno);
        et.setVisibility(View.VISIBLE);
        et = (EditText)findViewById(R.id.et_expiredate);
        et.setVisibility(View.VISIBLE);
        et = (EditText)findViewById(R.id.et_stuempno);
        et.setVisibility(View.VISIBLE);
        et = (EditText)findViewById(R.id.et_money);
        et.setVisibility(View.VISIBLE);
        et = (EditText)findViewById(R.id.et_balance);
        et.setVisibility(View.VISIBLE);
        //按钮
        btn = (Button)findViewById(R.id.btn_purchase);
        btn.setVisibility(View.VISIBLE);
        btn = (Button)findViewById(R.id.btn_back);
        btn.setVisibility(View.VISIBLE);
    }

    private void showRequestCard() {
        //隐藏寻卡界面
        TextView tv = (TextView)findViewById(R.id.tv_requestcard);
        tv.setVisibility(View.VISIBLE);
        Button btn = (Button)findViewById(R.id.btn_input_amount);
        btn.setVisibility(View.VISIBLE);
        //显示读卡界面
        EditText et = (EditText)findViewById(R.id.et_name);
        et.setVisibility(View.INVISIBLE);
        et = (EditText)findViewById(R.id.et_cardno);
        et.setVisibility(View.INVISIBLE);
        et = (EditText)findViewById(R.id.et_expiredate);
        et.setVisibility(View.INVISIBLE);
        et = (EditText)findViewById(R.id.et_stuempno);
        et.setVisibility(View.INVISIBLE);
        et = (EditText)findViewById(R.id.et_money);
        et.setVisibility(View.INVISIBLE);
        et = (EditText)findViewById(R.id.et_balance);
        et.setVisibility(View.INVISIBLE);
        //
        btn = (Button)findViewById(R.id.btn_purchase);
        btn.setVisibility(View.INVISIBLE);
        btn = (Button)findViewById(R.id.btn_back);
        btn.setVisibility(View.INVISIBLE);
    }

    private ErrorInfo doReadCard() {
        ErrorInfo ret = ErrorDef.SP_SUCCESS;
        try {
            globalCard.setFieldRead(EcardLib.FIELD_CUSTNAME);
            globalCard.setFieldRead(EcardLib.FIELD_STUEMPNO);
            globalCard.setFieldRead(EcardLib.FIELD_EXPIREDATE);
            globalCard.setFieldRead(EcardLib.FIELD_BALANCE);
            globalCard.setFieldRead(EcardLib.FIELD_CARDNO);

            globalCard.readCard();
            String cardBalance = globalCard.getFieldValue(EcardLib.FIELD_BALANCE);
            String custName = globalCard.getFieldValue(EcardLib.FIELD_CUSTNAME);
            String stuEmpNo = globalCard.getFieldValue(EcardLib.FIELD_STUEMPNO);
            String expireDate = globalCard.getFieldValue(EcardLib.FIELD_EXPIREDATE);
            String cardNo = globalCard.getFieldValue(EcardLib.FIELD_CARDNO);

            EditText et = (EditText)findViewById(R.id.et_cardno);
            et.setText("卡号:     "+cardNo);

            et = (EditText)findViewById(R.id.et_name);
            et.setText("姓名:     "+custName);

            et = (EditText)findViewById(R.id.et_stuempno);
            et.setText("学工号:     "+stuEmpNo);

            et = (EditText)findViewById(R.id.et_expiredate);
            et.setText("有效期:     "+expireDate);

            et = (EditText)findViewById(R.id.et_balance);
            et.setText("余额:      "+cardBalance);

        } catch (IOException e) {
            Log.e(tag, "Connect to card , " + e);
            ret = ErrorDef.SP_CONNECT_FAIL;
        } catch (CardException e) {
            Log.e(tag, "Read card error, " + e);
            ret = ErrorDef.SP_READ_CARD_ERROR;
        } catch (CardNotSupportException e) {
            Log.e(tag, "Read card error, " + e);
            ret = ErrorDef.SP_CARD_NOT_SUPPORT;
        } catch (CardValueException e) {
            Log.e(tag, "Read card error, " + e);
            ret = ErrorDef.SP_CARD_DATA_ERROR;
        }
        return ret;
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
