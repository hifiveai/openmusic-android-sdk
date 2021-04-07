package com.example.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.hfopen.sdk.common.BaseConstance
import com.hfopen.sdk.common.HFOpenCallback
import com.hfopen.sdk.entity.ChannelItem
import com.hfopen.sdk.hInterface.DataResponse
import com.hfopen.sdk.manager.HFOpenApi
import com.hfopen.sdk.rx.BaseException

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        HFOpenApi.registerApp(application, "userId")
        HFOpenApi.setVersion(BaseConstance.verison)


        HFOpenApi.configCallBack(object  : HFOpenCallback{
            override fun onError(exception: BaseException) {
                Log.d(TAG, "HFOpenApi onError")

            }

            override fun onSuccess() {
                Log.d(TAG, "HFOpenApi onSuccess")

            }

        })

        HFOpenApi.getInstance().channel(object : DataResponse<ArrayList<ChannelItem>>{
            override fun onError(exception: BaseException) {
                Log.d(TAG, "channel onError ${exception.msg}")

            }

            override fun onSuccess(data: ArrayList<ChannelItem>, taskId: String) {
                Log.d(TAG, "channel onSuccess $data")

            }

        })
    }
}