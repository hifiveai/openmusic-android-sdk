package com.example.open_play_android_sdk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.hfliveplayer.sdk.ui.player.HFLivePlayer;
import com.hifive.sdk.hInterface.DataResponse;
import com.hifive.sdk.manager.HFLiveApi;

public class MainActivity extends AppCompatActivity {
    private boolean flag;
    private String userId = "dongshihong";
    private String secretKey = "259e23ea0c684bd7be";
    private String appId = "1998ca60c18a42b38fa03b80cce1832a";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        HFLiveApi.Companion.registerApp(getApplication(), appId, secretKey);
        findViewById(R.id.play).setOnClickListener(view -> {
            if(!flag){
                Login();
            }else{
                HFLivePlayer.getInstance().remove();
                flag = false;
            }
        });
    }

    private void Login() {
        HFLiveApi.Companion.getInstance().memberLogin(this, userId, userId, null, null, null
                , null, null, null, null, null, new DataResponse() {
                    @Override
                    public void errorMsg(String string, Integer code) {
                        MainActivity.this.runOnUiThread(() -> Toast.makeText(MainActivity.this, string, Toast.LENGTH_SHORT).show());
                    }

                    @Override
                    public void data(Object any) {
                        MainActivity.this.runOnUiThread(() -> {
                            HFLivePlayer.getInstance().add(MainActivity.this);
                            flag = true ;
                        });
                    }
                });
    }

    @Override
    protected void onStart() {
        HFLivePlayer.getInstance().attach(this);
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        HFLivePlayer.getInstance().remove();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        HFLivePlayer.getInstance().detach(this);
        super.onStop();
    }
}