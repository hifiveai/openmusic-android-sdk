package com.longyuan.livesdkdemo

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hifive.sdk.common.BaseConstance.Companion.accessTokenMember
import com.hifive.sdk.common.BaseConstance.Companion.accessTokenUnion
import com.hifive.sdk.demo.ui.player.HifivePlayerManger
import com.hifive.sdk.hInterface.DataResponse
import com.hifive.sdk.manager.HFLiveApi

/**
 * @author Dsh  imkobedroid@gmail.com
 * @date 2020/11/4
 */
class MainActivity : AppCompatActivity() {
    private var flag: Boolean = false
    companion object {
        const val userId = "dongshihong"
        const val secretKey = "2122b24cbe0a461d8d"
        const val appId = "316587ef8bd942bab0bc0a10cbfefe8c"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        HFLiveApi.registerApp(application, appId, secretKey)
        findViewById<View>(R.id.play).setOnClickListener {
            when {
                !flag -> {
                    Login()
                }
                else -> {
                    HifivePlayerManger.getInstance().remove()
                    flag = false
                }
            }
        }
        findViewById<View>(R.id.button).setOnClickListener {
            HFLiveApi.getInstance()?.memberLogin(this,
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
                    (findViewById<View>(R.id.textView) as TextView).text = accessTokenMember
                            ?: ""
                }
            })
        }
        findViewById<View>(R.id.button1).setOnClickListener {
            HFLiveApi.getInstance()?.societyLogin(
                    this,
                    userId,
                    userId,
                    object : DataResponse {
                        override fun errorMsg(string: String, code: Int?) {}
                        override fun data(any: Any) {
                            (findViewById<View>(R.id.textView) as TextView).text = accessTokenUnion
                                    ?: ""
                        }
                    })
        }
        findViewById<View>(R.id.button2).setOnClickListener {
            HFLiveApi.getInstance()?.unbindingMember(
                    this,
                    userId,
                    userId
                    , object : DataResponse {
                override fun errorMsg(string: String, code: Int?) {}
                override fun data(any: Any) {
                    (findViewById<View>(R.id.textView) as TextView).text = "会员解绑成功"
                }
            })
        }
        findViewById<View>(R.id.button3).setOnClickListener {
            HFLiveApi.getInstance()?.bindingMember(
                    this,
                    userId,
                    userId
                    , object : DataResponse {
                override fun errorMsg(string: String, code: Int?) {}
                override fun data(any: Any) {
                    (findViewById<View>(R.id.textView) as TextView).text = "绑定会员成功"
                }
            })
        }
        findViewById<View>(R.id.button4).setOnClickListener {
            HFLiveApi.getInstance()?.deleteMember(
                    this,
                    userId,
                    object : DataResponse {
                        override fun errorMsg(string: String, code: Int?) {}
                        override fun data(any: Any) {
                            (findViewById<View>(R.id.textView) as TextView).text = "注销会员成功"
                        }
                    })
        }
        findViewById<View>(R.id.button5).setOnClickListener {
            HFLiveApi.getInstance()?.deleteSociety(
                    this,
                    userId,
                    object : DataResponse {
                        override fun errorMsg(string: String, code: Int?) {}
                        override fun data(any: Any) {
                            (findViewById<View>(R.id.textView) as TextView).text = "注销公会成功"
                        }
                    })
        }
        findViewById<View>(R.id.button6).setOnClickListener {
            HFLiveApi.getInstance()?.getCompanySheetTagList(
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
            HFLiveApi.getInstance()?.getCompanySheetList(
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
                            Log.e("TAG", "any==$any");

                            Toast.makeText(this@MainActivity, "请求成功", Toast.LENGTH_SHORT).show()

                        }
                    })
        }
        findViewById<View>(R.id.button8).setOnClickListener {
            HFLiveApi.getInstance()?.getCompanySheetMusicList(
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
            HFLiveApi.getInstance()?.getCompanyChannelList(
                    this,
                    object : DataResponse {
                        override fun errorMsg(string: String, code: Int?) {
                            Toast.makeText(this@MainActivity, "请求失败", Toast.LENGTH_SHORT).show()
                        }

                        override fun data(any: Any) {
                            val e = Log.e("TAG", "any==$any");
                            Toast.makeText(this@MainActivity, "请求成功", Toast.LENGTH_SHORT).show()
                        }
                    })
        }
        findViewById<View>(R.id.button10).setOnClickListener {
            HFLiveApi.getInstance()?.getMemberSheetList(
                    this,
                    null,
                    null,
                    object : DataResponse {
                        override fun errorMsg(string: String, code: Int?) {
                            Toast.makeText(this@MainActivity, "请求失败", Toast.LENGTH_SHORT).show()
                        }

                        override fun data(any: Any) {
                            Toast.makeText(this@MainActivity, "请求成功", Toast.LENGTH_SHORT).show()
                            Log.e("TAG", "data==" + any)
                        }
                    })
        }
        findViewById<View>(R.id.button11).setOnClickListener {
            HFLiveApi.getInstance()?.getMemberSheetMusicList(
                    this,
                    "167",
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
                            Log.e("TAG","SheetMusicList=="+any)
                        }
                    })
        }
        findViewById<View>(R.id.button12).setOnClickListener {
            HFLiveApi.getInstance()?.getMusicDetail(
                    this,
                    "167",
                    null,
                    "2",
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
        findViewById<View>(R.id.button13).setOnClickListener {
            HFLiveApi.getInstance()?.saveMemberSheet(
                    this,
                    "167",
                    object : DataResponse {
                        override fun errorMsg(string: String, code: Int?) {
                            Toast.makeText(this@MainActivity, "请求失败", Toast.LENGTH_SHORT).show()
                        }

                        override fun data(any: Any) {
                            Toast.makeText(this@MainActivity, "请求成功", Toast.LENGTH_SHORT).show()
                        }
                    })
        }
        findViewById<View>(R.id.button14).setOnClickListener {
            HFLiveApi.getInstance()?.saveMemberSheetMusic(
                    this,
                    "167",
                    "100",
                    object : DataResponse {
                        override fun errorMsg(string: String, code: Int?) {
                            Toast.makeText(this@MainActivity, "请求失败", Toast.LENGTH_SHORT).show()
                        }

                        override fun data(any: Any) {
                            Toast.makeText(this@MainActivity, "请求成功", Toast.LENGTH_SHORT).show()
                        }
                    })
        }
        findViewById<View>(R.id.button15).setOnClickListener {
            HFLiveApi.getInstance()?.deleteMemberSheetMusic(
                    this,
                    "167",
                    "100",
                    object : DataResponse {
                        override fun errorMsg(string: String, code: Int?) {
                            Toast.makeText(this@MainActivity, "请求失败", Toast.LENGTH_SHORT).show()
                        }

                        override fun data(any: Any) {
                            Toast.makeText(this@MainActivity, "请求成功", Toast.LENGTH_SHORT).show()
                        }
                    })
        }
        findViewById<View>(R.id.button16).setOnClickListener {
            HFLiveApi.getInstance()?.getMemberSheetMusicAll(
                    this,
                    "167",
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
        findViewById<View>(R.id.button17).setOnClickListener {
            HFLiveApi.getInstance()?.updateMusicRecord(
                    this,
                    "167",
                    "100",
                    "2",
                    object : DataResponse {
                        override fun errorMsg(string: String, code: Int?) {
                            Toast.makeText(this@MainActivity, "请求失败", Toast.LENGTH_SHORT).show()
                        }

                        override fun data(any: Any) {
                            Toast.makeText(this@MainActivity, "请求成功", Toast.LENGTH_SHORT).show()
                        }
                    })
        }
        findViewById<View>(R.id.button18).setOnClickListener {
            HFLiveApi.getInstance()?.getConfigList(
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
        findViewById<View>(R.id.button19).setOnClickListener {
            HFLiveApi.getInstance()?.getMusicList(
                    this,
                    "167", null,
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
        findViewById<View>(R.id.button20).setOnClickListener {
            HFLiveApi.getInstance()?.getSearchRecordList(
                    this,
                    "1",
                    "10",
                    object : DataResponse {
                        override fun errorMsg(string: String, code: Int?) {
                            Toast.makeText(this@MainActivity, "请求失败", Toast.LENGTH_SHORT).show()
                        }

                        override fun data(any: Any) {
                            Toast.makeText(this@MainActivity, "请求成功", Toast.LENGTH_SHORT).show()
                        }
                    })
        }
        findViewById<View>(R.id.button21).setOnClickListener {
            HFLiveApi.getInstance()?.deleteSearchRecord(
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
    }
    private  fun Login() {
        HFLiveApi.getInstance()?.memberLogin(this,
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
        })
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