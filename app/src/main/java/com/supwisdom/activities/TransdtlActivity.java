package com.supwisdom.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.supwisdom.R;
import com.supwisdom.db.TransdtlDao;
import com.supwisdom.utilities.TransRecord;

public class TransdtlActivity extends ActionBarActivity {

    private TransdtlDao transdtlDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transdtl);
        initView();
    }

    private void initView() {
        transdtlDao = TransdtlDao.getInstance(this);
        //查询流水
        Button btn = (Button) findViewById(R.id.btn_showTransdtl);
        btn.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransRecord record = transdtlDao.getTransdtl();
                EditText et = (EditText)findViewById(R.id.et_transNo);
                et.setText("交易流水号:    "+record.getTransno());
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_transdtl, menu);
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
