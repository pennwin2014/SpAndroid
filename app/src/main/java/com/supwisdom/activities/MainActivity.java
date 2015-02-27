package com.supwisdom.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.supwisdom.R;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitData();
        InitView();
    }

    protected Intent createIntentWithTag() {
        Intent intent = new Intent();
        return intent;
    }

    private void InitView() {
        ImageView btn = (ImageView) findViewById(R.id.iv_purchase);
        btn.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = createIntentWithTag();
                intent.setClass(MainActivity.this, ReadCardActivity.class);
                startActivity(intent);
            }
        });
        //设置
        btn = (ImageView) findViewById(R.id.iv_set);
        btn.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = createIntentWithTag();
                intent.setClass(MainActivity.this, SetActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 初始化数据
     */
    private void InitData() {
        //
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
