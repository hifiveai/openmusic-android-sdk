package com.example.open_play_android_sdk;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.hfopen.sdk.rx.BaseException;
import com.hfopenmusic.sdk.HifiveMusicManage;
import com.hfopen.sdk.common.HFOpenCallback;
import com.hfopen.sdk.manager.HFOpenApi;
import com.tencent.bugly.crashreport.CrashReport;

public class LoginActivity extends AppCompatActivity {
    private EditText et_appid, et_secretkey;
    private EditText et_member_id;
    private AppCompatButton btn_initialize;
    private String secretKey, appId;
    private String memberId;

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
                HifiveMusicManage.getInstance().showToast(this, "请输入会员id");
                return;
            }

//,"https://gateway.open.hifiveai.com"
            HFOpenApi.registerApp(getApplication(), memberId);
//        HifiveMusicManage.getInstance().showToast(this, "初始化SDK成功");


            HFOpenApi.configCallBack(new HFOpenCallback() {
                @Override
                public void onError(BaseException exception) {
                    HifiveMusicManage.getInstance().showToast(LoginActivity.this, exception.getMsg());
                }

                @Override
                public void onSuccess() {

                }
            });

//
            SPUtils.put(this, SPUtils.appId, appId);
            SPUtils.put(this, SPUtils.secretKey, secretKey);
            startActivity(new Intent(this, MainActivity.class));
        });


    }

//    public static String stringFilter (String str)throws PatternSyntaxException {
//// 只允许字母、数字和汉字其余的还可以随时添加比如下划线什么的，但是注意引文符号和中文符号区别
//        String regEx = "[^a-zA-Z0-9\u4E00-\u9FA5]";//正则表达式
//        Pattern p = Pattern.compile(regEx);
//        Matcher m = p.matcher(str);
//        return m.replaceAll("").trim();
//    }
}
