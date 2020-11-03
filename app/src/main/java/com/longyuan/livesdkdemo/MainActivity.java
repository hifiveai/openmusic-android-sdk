package com.longyuan.livesdkdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.hifive.sdk.common.BaseConstance;
import com.hifive.sdk.controller.MusicManager;
import com.hifive.sdk.entity.Token;
import com.hifive.sdk.hInterface.DataResponse;
import com.hifive.sdk.manager.HiFiveManager;
import com.hifive.sdk.net.Encryption;
import com.hifive.sdk.net.LiveRetrofitFactory;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
       HiFiveManager. Companion.start(getApplication(), "8365eb6054eeaed261ae526c46ebf58f", "6cd6f71004344acbb478a7f2f3b44bc5");

        findViewById(R.id.button).setOnClickListener(view -> {
            String secretKey="f653ca0d989340708a";
            String appId="e77e0f0ac5d54209850d7d720cacff64";
            String deviceId=Encryption.Companion.requestDeviceId(this);
            String huiyuanId="dongshihong";
            String huiyuanName="dongshihong";
            String time=String.valueOf(System.currentTimeMillis());
            String message= appId+huiyuanId+deviceId+time;
            String sign= BaseConstance.Companion.getSign(secretKey,message).trim();


            Log.d("签名",sign);


            assert sign != null;
            Objects.requireNonNull(HiFiveManager.Companion.getInstance()).token(this, sign,
                    appId,
                    huiyuanName,
                    huiyuanId,
                    null,
                    null,
                    deviceId,
                    time,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null, new DataResponse() {
                        @Override
                        public void errorMsg(@NotNull String string, @Nullable Integer code) {
                        }

                        @Override
                        public void data(@NotNull Object any) {
                            ((TextView)findViewById(R.id.textView)).setText(((Token)any).getAccessToken());
                        }
                    });
        });
    }
}