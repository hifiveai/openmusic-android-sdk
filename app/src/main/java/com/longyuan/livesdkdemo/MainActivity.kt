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
        HiFiveManager.start(application, appId, secretKey)
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
                    BaseConstance.accessToken = (any as Token).accessToken
                    (findViewById<View>(R.id.textView) as AppCompatTextView).text = any.accessToken
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
                            BaseConstance.accessToken = (any as Token).accessToken
                            (findViewById<View>(R.id.textView) as AppCompatTextView).text = any.accessToken
                        }
                    })
        }
        findViewById<View>(R.id.button2).setOnClickListener {
            HiFiveManager.getInstance()?.unbindingMember(
                    this,
                    BaseConstance.accessToken,
                    userId,
                    null,
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
                    BaseConstance.accessToken,
                    userId,
                    null,
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
                    BaseConstance.accessToken,
                    userId,
                    null,
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
                    BaseConstance.accessToken,
                    userId,
                    null,
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