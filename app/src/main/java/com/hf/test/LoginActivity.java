package com.hf.test;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.hf.test.R;
import com.hf.playerkernel.manager.HFPlayerApi;
import com.hfopen.sdk.rx.BaseException;
import com.hfopenmusic.sdk.HFOpenMusic;
import com.hfopen.sdk.common.HFOpenCallback;
import com.hfopen.sdk.manager.HFOpenApi;
import com.tencent.bugly.crashreport.CrashReport;

public class LoginActivity extends AppCompatActivity {
    private EditText et_appid, et_secretkey;
    private EditText et_member_id;
    private AppCompatButton btn_initialize, play, play2, play3;
    private String secretKey, appId;
    private String memberId;
    private boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        CrashReport.initCrashReport(getApplicationContext(), "2e151e6755", false);

        initView();
    }

    /**
     * 正式 300a44d050c942eebeae8765a878b0ee   0e31fe11b31247fca8
     * 测试 1998ca60c18a42b38fa03b80cce1832a   259e23ea0c684bd7be
     * 沙箱 6jg58jx4aa9t7305dyck4ckvbyhk7duk   wnzwnkevnc74uym5
     */
    private void initView() {
        et_appid = findViewById(R.id.et_appid);
        appId = (String) SPUtils.get(this, SPUtils.appId, "300a44d050c942eebeae8765a878b0ee");
        et_appid.setText(appId);

        et_secretkey = findViewById(R.id.et_secretkey);
        secretKey = (String) SPUtils.get(this, SPUtils.secretKey, "0e31fe11b31247fca8");
        et_secretkey.setText(secretKey);


        et_member_id = findViewById(R.id.et_member_id);
        memberId = (String) SPUtils.get(this, SPUtils.memberId, "hifivetest");
        et_member_id.setText(memberId);

        btn_initialize = findViewById(R.id.btn_initialize);
        btn_initialize.setOnClickListener(view -> {
            secretKey = et_secretkey.getText().toString().trim();
            appId = et_appid.getText().toString().trim();
            memberId = et_member_id.getText().toString().trim();
            if (TextUtils.isEmpty(secretKey)) {
                Toast.makeText(LoginActivity.this, "请输入secretKey", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(appId)) {
                Toast.makeText(LoginActivity.this, "请输入appId", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(memberId)) {
                HFOpenMusic.getInstance().showToast(this, "请输入会员id");
                return;
            }

//,"https://gateway.open.hifiveai.com"
            HFOpenApi.registerApp(getApplication(), memberId);
//        HFOpenMusic.getInstance().showToast(this, "初始化SDK成功");
            HFOpenApi.configCallBack(new HFOpenCallback() {
                @Override
                public void onError(BaseException exception) {
                    HFOpenMusic.getInstance().showToast(LoginActivity.this, exception.getMsg());
                }

                @Override
                public void onSuccess() {

                }
            });

            SPUtils.put(this, SPUtils.appId, appId);
            SPUtils.put(this, SPUtils.secretKey, secretKey);
            flag = true;
            Toast.makeText(LoginActivity.this, "初始化成功", Toast.LENGTH_SHORT).show();
        });


        play = findViewById(R.id.play);
        play2 = findViewById(R.id.play2);
        play3 = findViewById(R.id.play3);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!flag){
                    Toast.makeText(LoginActivity.this, "请先初始化SDK", Toast.LENGTH_SHORT).show();
                    return;
                }
                initPlayer();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("type",1);
                startActivity(intent);
            }
        });
        play2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!flag){
                    Toast.makeText(LoginActivity.this, "请先初始化SDK", Toast.LENGTH_SHORT).show();
                    return;
                }
                initPlayer();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("type",2);
                startActivity(intent);
            }
        });
        play3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!flag){
                    Toast.makeText(LoginActivity.this, "请先初始化SDK", Toast.LENGTH_SHORT).show();
                    return;
                }
                initPlayer();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("type",3);
                startActivity(intent);
            }
        });

    }

    private void initPlayer(){
        HFPlayerApi.init(getApplication())
                .setDebug(true)
                .setMaxBufferSize(200 * 1024)
                .setUseCache(true)
                .apply();
    }

}
