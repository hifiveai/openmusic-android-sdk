package com.longyuan.livesdkdemo

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import com.hifive.sdk.common.BaseConstance
import com.hifive.sdk.entity.Token
import com.hifive.sdk.hInterface.DataResponse
import com.hifive.sdk.manager.HiFiveManager

/**
 * @author Dsh  imkobedroid@gmail.com
 * @date 2020/11/4
 */
class MainActivity : AppCompatActivity() {

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
        HiFiveManager.start(application, "8365eb6054eeaed261ae526c46ebf58f", "6cd6f71004344acbb478a7f2f3b44bc5")
        findViewById<View>(R.id.button).setOnClickListener {

            HiFiveManager.getInstance()?.memberLogin(this, secretKey,
                    appId,
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
                    BaseConstance.accessTokenMember = (any as Token).accessToken
                    (findViewById<View>(R.id.textView) as AppCompatTextView).text = any.accessToken
                }
            })
        }
        findViewById<View>(R.id.button1).setOnClickListener {
            HiFiveManager.getInstance()?.societyLogin(
                    this,
                    secretKey,
                    appId,
                    userId,
                    userId,
                    object : DataResponse {
                        override fun errorMsg(string: String, code: Int?) {}
                        override fun data(any: Any) {
                            BaseConstance.accessTokenSociety = (any as Token).accessToken
                            (findViewById<View>(R.id.textView) as AppCompatTextView).text = any.accessToken
                        }
                    })
        }
        findViewById<View>(R.id.button2).setOnClickListener {
            val time = System.currentTimeMillis().toString()
            HiFiveManager.getInstance()?.unbindingMember(
                    this,
                    appId,
                    userId,
                    null,
                    time,
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
            val time = System.currentTimeMillis().toString()
            HiFiveManager.getInstance()?.bindingMember(
                    this,
                    BaseConstance.accessTokenMember,
                    appId,
                    userId,
                    null,
                    time,
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
            val time = System.currentTimeMillis().toString()
            HiFiveManager.getInstance()?.deleteMember(
                    this,
                    BaseConstance.accessTokenMember,
                    appId,
                    userId,
                    null,
                    time,
                    userId,
                    object : DataResponse {
                        override fun errorMsg(string: String, code: Int?) {}
                        override fun data(any: Any) {
                            (findViewById<View>(R.id.textView) as AppCompatTextView).text = "注销会员成功"
                        }
                    })
        }
        findViewById<View>(R.id.button5).setOnClickListener {
            val time = System.currentTimeMillis().toString()
            HiFiveManager.getInstance()?.deleteSociety(
                    this,
                    BaseConstance.accessTokenMember,
                    appId,
                    userId,
                    null,
                    time,
                    userId,
                    object : DataResponse {
                        override fun errorMsg(string: String, code: Int?) {}
                        override fun data(any: Any) {
                            (findViewById<View>(R.id.textView) as AppCompatTextView).text = "注销公会成功"
                        }
                    })
        }
    }
}