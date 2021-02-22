package com.example.open_play_android_sdk;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.hfliveplayer.sdk.util.HifiveDialogManageUtil;
import com.hifive.sdk.common.HFLiveCallback;
import com.hifive.sdk.hInterface.DataResponse;
import com.hifive.sdk.manager.HFLiveApi;
import com.hifive.sdk.rx.BaseException;
import com.tencent.bugly.crashreport.CrashReport;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class LoginActivity extends AppCompatActivity {
    private EditText et_appid,et_secretkey;
    private EditText et_member_name,et_member_id,et_sociaty_name,et_sociaty_id;
    private AppCompatButton btn_initialize,btn_login;
    private String secretKey, appId ;
    private String memberName,memberId,sociatyName,sociatyId;
    private boolean flag;//是否初始化
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        CrashReport.initCrashReport(getApplicationContext(), "2e151e6755", false);

        initView();
    }

    /**
     *  正式 300a44d050c942eebeae8765a878b0ee   0e31fe11b31247fca8
     *  测试 1998ca60c18a42b38fa03b80cce1832a   259e23ea0c684bd7be
     *
     */
    private void initView() {
        et_appid = findViewById(R.id.et_appid);
        appId = (String) SPUtils.get(this, SPUtils.appId, "300a44d050c942eebeae8765a878b0ee");
        et_appid.setText(appId);

        et_secretkey = findViewById(R.id.et_secretkey);
        secretKey = (String) SPUtils.get(this, SPUtils.secretKey, "0e31fe11b31247fca8");
        et_secretkey.setText(secretKey);

        et_member_name = findViewById(R.id.et_member_name);
        memberName = (String) SPUtils.get(this, SPUtils.memberName, "");
        et_member_name.setText(memberName);

        et_member_id = findViewById(R.id.et_member_id);
        memberId = (String) SPUtils.get(this, SPUtils.memberId, "");
        et_member_id.setText(memberId);

        et_sociaty_name = findViewById(R.id.et_sociaty_name);
        sociatyName = (String) SPUtils.get(this, SPUtils.sociatyName, "");
        et_sociaty_name.setText(sociatyName);

        et_sociaty_id = findViewById(R.id.et_sociaty_id);
        sociatyId = (String) SPUtils.get(this, SPUtils.sociatyId, "");
        et_sociaty_id.setText(sociatyId);

        btn_initialize = findViewById(R.id.btn_initialize);
        btn_initialize.setOnClickListener(view -> {
            secretKey = et_secretkey.getText().toString().trim();
            appId = et_appid.getText().toString().trim();
            if (TextUtils.isEmpty(secretKey)) {
                Toast.makeText(LoginActivity.this, "请输入secretKey", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(appId)) {
                Toast.makeText(LoginActivity.this, "请输入appId", Toast.LENGTH_SHORT).show();
                return;
            }
//            HFLiveApi.registerApp(getApplication(),appId,secretKey);
            HFLiveApi.registerApp(getApplication());

            HFLiveApi.configCallBack(new HFLiveCallback(){
                @Override
                public void onError( BaseException exception) {
                    HifiveDialogManageUtil.getInstance().showToast(LoginActivity.this, exception.getMsg());
                }

                @Override
                public void onSuccess() {

                }
            });

            HifiveDialogManageUtil.getInstance().showToast(this, "初始化SDK成功");
            SPUtils.put(this, SPUtils.appId, appId);
            SPUtils.put(this, SPUtils.secretKey, secretKey);
            flag = true;
        });
        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(view -> {
            if (!flag) {
                HifiveDialogManageUtil.getInstance().showToast(this, "请先初始化SDK");
            } else {
                Login();
            }
        });

        et_member_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String edit = et_member_name.getText().toString();
                String str = stringFilter(edit.toString());
                if (!edit.equals(str)) {
                    et_member_name.setText(str);
                    //设置新的光标所在位置
                    et_member_name.setSelection(str.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_sociaty_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String edit = et_sociaty_name.getText().toString();
                String str = stringFilter(edit.toString());
                if (!edit.equals(str)) {
                    et_sociaty_name.setText(str);
                    //设置新的光标所在位置
                    et_sociaty_name.setSelection(str.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    public static String stringFilter (String str)throws PatternSyntaxException {
// 只允许字母、数字和汉字其余的还可以随时添加比如下划线什么的，但是注意引文符号和中文符号区别
        String regEx = "[^a-zA-Z0-9\u4E00-\u9FA5]";//正则表达式
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    private void Login() {
        memberName = et_member_name.getText().toString().trim();
        memberId = et_member_id.getText().toString().trim();
        sociatyName = et_sociaty_name.getText().toString().trim();
        sociatyId = et_sociaty_id.getText().toString().trim();

        if(TextUtils.isEmpty(memberName)){
            HifiveDialogManageUtil.getInstance().showToast(this, "请输入会员名称");
            return;
        }
        if(TextUtils.isEmpty(memberId)){
            HifiveDialogManageUtil.getInstance().showToast(this, "请输入会员id");
            return;
        }
        HFLiveApi.getInstance().memberLogin(this, memberName, memberId, sociatyName, sociatyId,
                null , null, null, null, null, null, new DataResponse() {
                    @Override
                    public void errorMsg(@NotNull String string, Integer code) {
                        HifiveDialogManageUtil.getInstance().showToast(LoginActivity.this, string);
                    }

                    @Override
                    public void data(@NotNull Object any) {
                        SPUtils.put(LoginActivity.this,SPUtils.memberName,memberName);
                        SPUtils.put(LoginActivity.this,SPUtils.memberId,memberId);
                        HifiveDialogManageUtil.getInstance().showToast(LoginActivity.this, "会员登录成功");
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        finish();
                    }
                });

    }
}
