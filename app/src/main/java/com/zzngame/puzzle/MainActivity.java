package com.zzngame.puzzle;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;


public class MainActivity extends Activity implements View.OnClickListener{
    //MainLayout按键变量
    private Button mBtn_MainStart, mBtn_MainLoading, mBtn_MainRanking, mBtn_MainSettings, mBtn_MainExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 去除程序的标题栏
        super.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 通过程序改变屏幕显示的方向,LANDSCAPE：横屏
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.main);

        mBtn_MainStart = (Button) findViewById(R.id.btn_MainStart);
        mBtn_MainLoading = (Button) findViewById(R.id.btn_MainLoading);
        mBtn_MainRanking = (Button) findViewById(R.id.btn_MainRanking);
        mBtn_MainSettings = (Button) findViewById(R.id.btn_MainSettings);
        mBtn_MainExit = (Button) findViewById(R.id.btn_MainExit);

        mBtn_MainStart.setOnClickListener(this);
        mBtn_MainLoading.setOnClickListener(this);
        mBtn_MainRanking.setOnClickListener(this);
        mBtn_MainSettings.setOnClickListener(this);
        mBtn_MainExit.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_MainStart:
                Intent intent = new Intent(this, MainPlayingActivity.class);
                startActivity(intent);



                break;
            case R.id.btn_MainLoading:

                break;
            case R.id.btn_MainRanking:

                break;
            case R.id.btn_MainSettings:

                break;
            case R.id.btn_MainExit:

                break;

        }

    }
}
