package com.zzngame.puzzle;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

/**
 * Created by Host on 2015/2/16.
 */
public class MainPlayingActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 去除程序的标题栏
        super.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 通过程序改变屏幕显示的方向,LANDSCAPE：横屏
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.main_playing);

    }
}
