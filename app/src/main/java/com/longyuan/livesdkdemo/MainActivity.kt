package com.longyuan.livesdkdemo

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import com.hifive.sdk.demo.ui.player.HifivePlayerManger
import com.hifive.sdk.entity.Token
import com.hifive.sdk.hInterface.DataResponse
import com.hifive.sdk.manager.HiFiveManager

/**
 * @author Dsh  imkobedroid@gmail.com
 * @date 2020/11/4
 */
class MainActivity : AppCompatActivity() {
    private var flag: Boolean = false
    companion object {
        const val userId = "dongshihong"
        const val secretKey = "f653ca0d989340708a"
        const val appId = "e77e0f0ac5d54209850d7d720cacff64"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        HiFiveManager.start(application, appId, secretKey)
        findViewById<View>(R.id.play).setOnClickListener {
            if(!flag){
                HifivePlayerManger.getInstance().add(this)
                flag = true
            }else{
                HifivePlayerManger.getInstance().remove()
                flag = false
            }
        }
        findViewById<View>(R.id.button).setOnClickListener {
            HiFiveManager.getInstance()?.memberLogin(this,
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
                override fun errorMsg(string: String, code: Int?) {}
                override fun data(any: Any) {
                    (findViewById<View>(R.id.textView) as AppCompatTextView).text = (any as Token).accessToken
                }
            })
        }
        findViewById<View>(R.id.button1).setOnClickListener {
            HiFiveManager.getInstance()?.societyLogin(
                    this,
                    userId,
                    userId,
                    object : DataResponse {
                        override fun errorMsg(string: String, code: Int?) {}
                        override fun data(any: Any) {
                            (findViewById<View>(R.id.textView) as AppCompatTextView).text = (any as Token).accessToken
                        }
                    })
        }
        findViewById<View>(R.id.button2).setOnClickListener {
            HiFiveManager.getInstance()?.unbindingMember(
                    this,
                    userId,
                    userId
                    , object : DataResponse {
                override fun errorMsg(string: String, code: Int?) {}
                override fun data(any: Any) {
                    (findViewById<View>(R.id.textView) as AppCompatTextView).text = "会员解绑成功"
                }
            })
        }
        findViewById<View>(R.id.button3).setOnClickListener {
            HiFiveManager.getInstance()?.bindingMember(
                    this,
                    userId,
                    userId
                    , object : DataResponse {
                override fun errorMsg(string: String, code: Int?) {}
                override fun data(any: Any) {
                    (findViewById<View>(R.id.textView) as AppCompatTextView).text = "绑定会员成功"
                }
            })
        }
        findViewById<View>(R.id.button4).setOnClickListener {
            HiFiveManager.getInstance()?.deleteMember(
                    this,
                    userId,
                    object : DataResponse {
                        override fun errorMsg(string: String, code: Int?) {}
                        override fun data(any: Any) {
                            (findViewById<View>(R.id.textView) as AppCompatTextView).text = "注销会员成功"
                        }
                    })
        }
        findViewById<View>(R.id.button5).setOnClickListener {
            HiFiveManager.getInstance()?.deleteSociety(
                    this,
                    userId,
                    object : DataResponse {
                        override fun errorMsg(string: String, code: Int?) {}
                        override fun data(any: Any) {
                            (findViewById<View>(R.id.textView) as AppCompatTextView).text = "注销公会成功"
                        }
                    })
        }
        findViewById<View>(R.id.button6).setOnClickListener {
            HiFiveManager.getInstance()?.getCompanySheetTagList(
                    this,
                    object : DataResponse {
                        override fun errorMsg(string: String, code: Int?) {
                            Toast.makeText(this@MainActivity, "请求失败", Toast.LENGTH_SHORT).show()
                        }

                        override fun data(any: Any) {
                            Toast.makeText(this@MainActivity, "请求成功", Toast.LENGTH_SHORT).show()
                        }
                    })
        }
        findViewById<View>(R.id.button7).setOnClickListener {
            HiFiveManager.getInstance()?.getCompanySheetList(
                    this,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    object : DataResponse {
                        override fun errorMsg(string: String, code: Int?) {
                            Toast.makeText(this@MainActivity, "请求失败", Toast.LENGTH_SHORT).show()
                        }

                        override fun data(any: Any) {
                            Log.e("TAG","any=="+any);

                            Toast.makeText(this@MainActivity, "请求成功", Toast.LENGTH_SHORT).show()

                        }
                    })
        }
        findViewById<View>(R.id.button8).setOnClickListener {
            HiFiveManager.getInstance()?.getCompanySheetMusicList(
                    this,
                    null,
                    null,
                    null,
                    null,
                    null,
                    object : DataResponse {
                        override fun errorMsg(string: String, code: Int?) {
                            Toast.makeText(this@MainActivity, "请求失败", Toast.LENGTH_SHORT).show()
                        }

                        override fun data(any: Any) {
                            Toast.makeText(this@MainActivity, "请求成功", Toast.LENGTH_SHORT).show()

                        }
                    })
        }
        findViewById<View>(R.id.button9).setOnClickListener {
            HiFiveManager.getInstance()?.getCompanyChannelList(
                    this,
                    object : DataResponse {
                        override fun errorMsg(string: String, code: Int?) {
                            Toast.makeText(this@MainActivity, "请求失败", Toast.LENGTH_SHORT).show()
                        }

                        override fun data(any: Any) {
                            Log.e("TAG","any=="+any);
                            Toast.makeText(this@MainActivity, "请求成功", Toast.LENGTH_SHORT).show()
                        }
                    })
        }


    }

    override fun onStart() {
        HifivePlayerManger.getInstance().attach(this);
        super.onStart()
    }

    override fun onStop() {
        HifivePlayerManger.getInstance().detach(this);
        super.onStop()
    }

    override fun onDestroy() {
        HifivePlayerManger.getInstance().remove();
        super.onDestroy()
    }
}