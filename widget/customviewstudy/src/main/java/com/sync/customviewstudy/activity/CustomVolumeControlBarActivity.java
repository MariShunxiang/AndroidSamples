package com.sync.customviewstudy.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.sync.customviewstudy.R;

/**
 * @Description:
 * @Author 罗顺翔
 * @date 2017年04月06日 18:08
 */
public class CustomVolumeControlBarActivity extends AppCompatActivity {

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_custom_volume_control_bar);
    setTitle("自定义View (四) 视频音量调控");
  }
}
