package com.example.open_play_android_sdk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private boolean flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        // HFLiveApi.registerApp(application, appId, secretKey)
        findViewById(R.id.play).setOnClickListener(view -> {
            if(flag){
                Login();
            }else{
               /* HifivePlayerManger.getInstance().remove()
                flag = false*/


            }

        });





    }

    private void Login() {
        /*HFLiveApi.getInstance()?.memberLogin(this,
                userId,
                userId,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null, object : DataResponse {
            override fun errorMsg(string: String, code: Int?) {
                Toast.makeText(this@MainActivity, string, Toast.LENGTH_SHORT).show()
            }
            override fun data(any: Any) {
                (findViewById<View>(R.id.textView) as TextView).text = accessTokenMember
                        ?: ""
                runOnUiThread(Runnable {
                    HifivePlayerManger.getInstance().add(this@MainActivity)
                    flag = true
                })
            }
        })*/

    }

    @Override
    protected void onStart() {
       // HifivePlayerManger.getInstance().attach(this);
        super.onStart();
    }

    @Override
    protected void onDestroy() {
      //  HifivePlayerManger.getInstance().remove();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
      // HifivePlayerManger.getInstance().detach(this);
        super.onStop();
    }
}