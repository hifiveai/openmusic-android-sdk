package com.hf.test;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.open_play_android_sdk.R;
import com.hfopen.sdk.common.HFOpenCallback;
import com.hfopen.sdk.manager.HFOpenApi;
import com.hfopen.sdk.net.Encryption;
import com.hfopen.sdk.rx.BaseException;
import com.hfopenmusic.sdk.HFOpenMusic;
import com.tencent.bugly.crashreport.CrashReport;

public class LoginActivity extends AppCompatActivity {
    private boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        CrashReport.initCrashReport(getApplicationContext(), "2e151e6755", false);
        HFOpenApi.setVersion("V4.1.2").registerApp(getApplication(), "3faeec81030444e98acf6af9ba32752a", "59b1aff189b3474398", Encryption.Companion.requestDeviceId(this));
        HFOpenApi.configCallBack(new HFOpenCallback() {
            @Override
            public void onError(BaseException exception) {
                HFOpenMusic.getInstance().showToast(LoginActivity.this, exception.getMsg());
            }

            @Override
            public void onSuccess() {

            }
        });
        flag = true;
        Toast.makeText(LoginActivity.this, "初始化成功", Toast.LENGTH_SHORT).show();
        initView();
    }

    private void initView() {
        AppCompatButton play2 = findViewById(R.id.play2);
        play2.setOnClickListener(v -> {
            if (!flag) {
                Toast.makeText(LoginActivity.this, "请先初始化SDK", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("type", 2);
            startActivity(intent);
        });

    }

}
