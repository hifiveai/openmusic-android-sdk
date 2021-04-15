package com.hfopenmusic.sdk.ui

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hfopen.sdk.entity.*
import com.hfopen.sdk.hInterface.DataResponse
import com.hfopen.sdk.manager.HFOpenApi
import com.hfopen.sdk.rx.BaseException
import com.hfopenmusic.sdk.R
import com.hfopenmusic.sdk.adapter.HifiveHotMusicAdapter
import com.hfopenmusic.sdk.adapter.HifiveMusicSheetListAdapter
import com.hfopenmusic.sdk.HFOpenMusic
import com.hfopenmusic.sdk.util.HifiveDisplayUtils
import com.hfopenmusic.sdk.util.SharedPref
import com.hfopenmusic.sdk.view.HifiveFlowLayout
import com.hfopenmusic.sdk.view.HifiveLoadMoreFooter
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import java.util.*

/**
 * 歌曲搜索的弹窗
 *
 * @author huchao
 */
class HifiveMusicSearchDialoglFragment() : DialogFragment() {
    private lateinit var searchHistory: String
    var isLoadMore = false
    private var tvCancle: TextView? = null
    private var et_content: EditText? = null
    private var iv_clear: ImageView? = null
    private var ll_history: LinearLayout? = null
    private var iv_delete: ImageView? = null
    private var fl_history: HifiveFlowLayout? = null
    private var ll_hot: LinearLayout? = null
    private var mHotRv: RecyclerView? = null
    private var ll_result: LinearLayout? = null
    private var ll_empty: LinearLayout? = null
    private var tv_empty: TextView? = null
    private var refreshLayout: SmartRefreshLayout? = null
    private var mRecyclerView: RecyclerView? = null
    private var page = 1
    private var historyData: List<String>? = null
    private var adapter: HifiveMusicSheetListAdapter? = null
    private var hotAdapter: HifiveHotMusicAdapter? = null
    private var musicModels: List<MusicRecord>? = null
    private var hotModels: List<MusicRecord>? = null
    private var content = "" //搜索的内容
    private var mContext: Context? = null
    private val isAddLike = false //保存是否正在添加喜欢状态，防止重复点击
    private val isAddkaraoke = false //保存是否正在添加K歌状态，防止重复点击
    private var isRecommand = false //是否为无结果时推荐
    private var totalCount = 0

    private var mHandler = Handler { msg ->
        try {
            when (msg.what) {
                Refresh -> {
                    ll_result!!.visibility = View.VISIBLE
                    ll_hot!!.visibility = View.GONE
                    ll_history!!.visibility = View.GONE
                    hideInput()
                    if (isRecommand) {
                        ll_empty!!.visibility = View.VISIBLE
                        if (mContext != null) {
                            tv_empty!!.text = mContext!!.getString(R.string.hifivesdk_music_search_history_empty, content)
                        }
                    } else {
                        ll_empty!!.visibility = View.GONE
                    }
                    if (musicModels != null) adapter!!.updateDatas(musicModels)
                    refreshLayout!!.setEnableLoadMore(adapter!!.datas.size < totalCount)

                    //保存搜索记录
                    if (searchHistory.isEmpty()) {
                        searchHistory = content
                    } else {
                        if (!searchHistory.contains(content)) {
                            searchHistory = "$searchHistory,$content"
                        }
                    }
                    historyData = searchHistory.split(",")
                    initFlowLayout()
                    SharedPref.setParam(mContext, "searchHistory", searchHistory)
                }
                LoadMore -> {
                    isLoadMore = false
                    if (musicModels != null) adapter!!.addDatas(musicModels)
                    if (adapter!!.datas.size < totalCount) {
                        refreshLayout!!.finishLoadMore()
                    } else {
                        refreshLayout!!.finishLoadMoreWithNoMoreData()
                    }
                }
                RequstFail -> if (isLoadMore) {
                    isLoadMore = false
                    refreshLayout!!.finishLoadMore()
                }
                AddLike -> {
                }
                Addkaraoke -> {
                }
                UPDATE_CURRENT_SONG -> {
                    val hifiveMusicModel = msg.obj as MusicRecord
                    HFOpenMusic.getInstance().addCurrentSingle(hifiveMusicModel)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        false
    }

    override fun onStart() {
        super.onStart()
        if (dialog != null) {
            val window = dialog!!.window
            if (window != null) {
                val params = window.attributes
                params.gravity = Gravity.BOTTOM
                params.width = WindowManager.LayoutParams.MATCH_PARENT
                params.height = WindowManager.LayoutParams.MATCH_PARENT
                window.attributes = params
                window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.HifiveSdkDialogStyly)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContext = context
        if (dialog != null) {
            val window = this.dialog!!.window
            if (window != null) {
                window.setWindowAnimations(R.style.AnimationBottomFade)
                window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL)
                window.setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH)
            }
        }
        val view = inflater.inflate(R.layout.hifive_dialog_music_search, container)
        initView(view)
        ininReclyView()
        initEvent()
        getHot()
        getHistoryData()
        HFOpenMusic.getInstance().addDialog(this)
        return view
    }

    //初始化view
    private fun initView(view: View) {
        tvCancle = view.findViewById(R.id.tv_cancle)
        et_content = view.findViewById(R.id.et_content)
        et_content!!.setHorizontallyScrolling(false)
        et_content!!.maxLines = 1
        iv_clear = view.findViewById(R.id.iv_clear)
        ll_history = view.findViewById(R.id.ll_history)
        iv_delete = view.findViewById(R.id.iv_delete)
        fl_history = view.findViewById(R.id.fl_history)
        ll_result = view.findViewById(R.id.ll_result)
        ll_hot = view.findViewById(R.id.ll_hot)
        mHotRv = view.findViewById(R.id.rv_hot)
        ll_empty = view.findViewById(R.id.ll_empty)
        tv_empty = view.findViewById(R.id.tv_empty)
        iv_delete = view.findViewById(R.id.iv_delete)
        refreshLayout = view.findViewById(R.id.refreshLayout)
        refreshLayout!!.setEnableRefresh(false)
        refreshLayout!!.setRefreshFooter(HifiveLoadMoreFooter(context))
        mRecyclerView = view.findViewById(R.id.rv_music)
    }

    //初始化ReclyView
    private fun ininReclyView() {
        adapter = HifiveMusicSheetListAdapter(context, ArrayList())
        adapter!!.setOnRecyclerViewContentClick { position: Int ->
            mHandler!!.removeMessages(HifiveMusicPalyListFragment.UPDATE_CURRENT_SONG)
            val hifiveMusicModel = adapter!!.datas[position] as MusicRecord
            val message = mHandler!!.obtainMessage()
            message.obj = hifiveMusicModel
            message.what = HifiveMusicPalyListFragment.UPDATE_CURRENT_SONG
            mHandler!!.sendMessageDelayed(message, 200)
        }
        mRecyclerView!!.adapter = adapter
        mRecyclerView!!.layoutManager = LinearLayoutManager(context) //调整RecyclerView的排列方向
        refreshLayout!!.setOnLoadMoreListener {
            if (!isLoadMore) {
                isLoadMore = true
                getData(LoadMore)
            }
        }

        hotAdapter = HifiveHotMusicAdapter(context, ArrayList())
        hotAdapter!!.setOnRecyclerViewContentClick { position: Int ->
            mHandler!!.removeMessages(HifiveMusicPalyListFragment.UPDATE_CURRENT_SONG)
            val hifiveMusicModel = hotAdapter!!.datas[position] as MusicRecord
            val message = mHandler!!.obtainMessage()
            message.obj = hifiveMusicModel
            message.what = HifiveMusicPalyListFragment.UPDATE_CURRENT_SONG
            mHandler!!.sendMessageDelayed(message, 200)
        }
        mHotRv!!.adapter = hotAdapter
        mHotRv!!.layoutManager = LinearLayoutManager(context) //调整RecyclerView的排列方向
    }

    //初始化点击事件
    private fun initEvent() {
        tvCancle!!.setOnClickListener(View.OnClickListener
        //取消搜索
        {
            dismiss()
            HFOpenMusic.getInstance().removeDialog(1)
        })
        et_content!!.setOnEditorActionListener { v, actionId, event ->
            //开始搜索
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchMusic()
            }
            false
        }
        et_content!!.addTextChangedListener(object : TextWatcher {
            //监听输入变化
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isEmpty()) {
                    iv_clear!!.visibility = View.GONE
                } else {
                    iv_clear!!.visibility = View.VISIBLE
                }
                if (historyData != null && historyData!!.isNotEmpty()) {
                    ll_history!!.visibility = View.VISIBLE
                }
                ll_hot!!.visibility = View.VISIBLE
                ll_result!!.visibility = View.GONE
            }

            override fun afterTextChanged(s: Editable) {}
        })
        et_content!!.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            //监听焦点输入框焦点变化
            if (hasFocus) {
                if (historyData != null && historyData!!.isNotEmpty()) {
                    ll_history!!.visibility = View.VISIBLE
                }
            }
        }
        iv_clear!!.setOnClickListener { et_content!!.setText("") }
        iv_delete!!.setOnClickListener { showConfirmDialog() }
    }

    //弹窗删除二次确认框
    private fun showConfirmDialog() {
        val dialog = HifiveComfirmDialogFragment()
        val bundle = Bundle()
        bundle.putString(HifiveComfirmDialogFragment.ContentTx, getString(R.string.hifivesdk_comfirm_delete_history))
        dialog.arguments = bundle
        dialog.setOnSureClick { deleteSearchHistory() }
        if (fragmentManager != null) dialog.show((fragmentManager)!!, HifiveComfirmDialogFragment::class.java.simpleName)
    }

    //清空搜索历史
    private fun deleteSearchHistory() {
        try {
            if (mContext == null) return
            SharedPref.setParam(mContext, "searchHistory", "")
            if (activity != null) {
                HFOpenMusic.getInstance().showToast(activity, activity!!.getString(R.string.hifivesdk_comfirm_dialog_delete))
            }
            searchHistory = ""
            historyData = null
            if (fl_history != null) {
                fl_history!!.removeAllViews()
            }
            iv_delete!!.visibility = View.GONE
            ll_history!!.visibility = View.GONE
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //设置搜索历史信息
    private fun initFlowLayout() {
        if (fl_history != null) {
            fl_history!!.removeAllViews()
        }
        //往容器内添加TextView数据
        val layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, HifiveDisplayUtils.dip2px(mContext, 30f))
        layoutParams.setMargins(0, HifiveDisplayUtils.dip2px(mContext, 10f), HifiveDisplayUtils.dip2px(mContext, 10f), 0)
        for (i in historyData!!.indices) {
            val tv = TextView(mContext)
            tv.setPadding(HifiveDisplayUtils.dip2px(mContext, 10f), 0,
                    HifiveDisplayUtils.dip2px(mContext, 10f), 0)
            tv.text = historyData!![i]
            tv.maxEms = 10
            tv.ellipsize = TextUtils.TruncateAt.END
            tv.setSingleLine()
            tv.gravity = Gravity.CENTER_VERTICAL
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
            tv.setTextColor(Color.parseColor("#999999"))
            tv.setBackgroundResource(R.drawable.hifive_dialog_music_search_item_bg)
            tv.layoutParams = layoutParams
            tv.setOnClickListener {
                et_content!!.setText(tv.text.toString())
                searchMusic()
            }
            fl_history!!.addView(tv, layoutParams)
        }
    }

    //获取搜索历史数据
    private fun getHistoryData() {
        try {
            if (mContext == null) return
            searchHistory = SharedPref.getParam(mContext, "searchHistory", "") as String
            if (searchHistory.isNotEmpty()) {
                iv_delete!!.visibility = View.VISIBLE
                ll_history!!.visibility = View.VISIBLE
                historyData = searchHistory.split(",")
                initFlowLayout()
            } else {
                ll_history!!.visibility = View.GONE
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //热搜
    private fun getHot() {
        try {
            if (mContext == null) return
            HFOpenApi.getInstance().baseHot(System.currentTimeMillis(), 365, 1, 20, object : DataResponse<MusicList> {
                override fun onError(exception: BaseException) {
                    HFOpenMusic.getInstance().showToast(activity, exception.msg)
                }

                override fun onSuccess(data: MusicList, taskId: String) {
                    ll_hot!!.visibility = View.VISIBLE
                    hotModels = data.record
                    hotAdapter!!.updateDatas(hotModels)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //搜索音乐
    private fun searchMusic() {
        content = et_content!!.text.toString().trim()
        if (TextUtils.isEmpty(content)) {
            if (mContext != null) Toast.makeText(mContext, mContext!!.getString(R.string.hifivesdk_music_search_toast), Toast.LENGTH_SHORT).show()
            return
        }
        et_content!!.clearFocus()
        refreshLayout!!.resetNoMoreData()
        getData(Refresh)
    }

    //根据名称搜索歌曲
    private fun getData(ty: Int) {
        try {
            if (mContext == null) return
            if (ty == Refresh) {
                page = 1
            } else {
                page++
            }

            HFOpenApi.getInstance().searchMusic(null, null, null, null, null, null, null,
                    content, null,1,null, page, 100, object : DataResponse<MusicList> {
                override fun onError(exception: BaseException) {
                    if (ty != Refresh) { //上拉加载请求失败后，还原页卡
                        page--
                    } else {
                        getHistoryData()
                    }
                    HFOpenMusic.getInstance().showToast(activity, exception.msg)
                    mHandler.sendEmptyMessage(RequstFail)
                }

                override fun onSuccess(data: MusicList, taskId: String) {
                    musicModels = data.record
                    totalCount = data.meta.totalCount
                    if (ty == Refresh && (totalCount == 0 || musicModels == null || musicModels!!.isEmpty())) {
                        isRecommand = true
                        login()
                    } else {
                        isRecommand = false
                        mHandler.sendEmptyMessage(ty)
                    }
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //获取token
    private fun login() {
        if (mContext == null) return
        HFOpenApi.getInstance().baseLogin(HFOpenApi.Companion.CLIENT_ID, null, null, null, null, null,
                null, null, null, null, object : DataResponse<LoginBean> {
            override fun onError(exception: BaseException) {
            }

            override fun onSuccess(data: LoginBean, taskId: String) {
                getFavorite();
            }

        })
    }

    //推荐
    private fun getFavorite() {
        try {
            if (mContext == null) return
            HFOpenApi.getInstance().baseFavorite(page, 100, object : DataResponse<MusicList> {
                override fun onError(exception: BaseException) {
                    HFOpenMusic.getInstance().showToast(activity, exception.msg)
                    mHandler.sendEmptyMessage(RequstFail)
                }

                override fun onSuccess(data: MusicList, taskId: String) {
                    musicModels = data.record
                    totalCount = data.meta.totalCount
                    mHandler.sendEmptyMessage(Refresh)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private var toastStyle: Toast? = null

    //显示自定义toast信息
    private fun showToast(msgId: Int) {
        if (mContext != null) {
            if (toastStyle != null) {
                toastStyle!!.cancel()
                toastStyle = null
            }
            toastStyle = Toast(mContext)
            val layout = View.inflate(mContext, R.layout.hifive_layout_toast, null)
            val toastTextview = layout.findViewById<TextView>(R.id.tv_content)
            toastTextview.text = mContext!!.getString(msgId)
            toastStyle!!.view = layout
            toastStyle!!.setGravity(Gravity.CENTER, 0, 0)
            toastStyle!!.duration = Toast.LENGTH_SHORT
            toastStyle!!.show()
        }
    }

    /**
     * 隐藏键盘
     */
    protected fun hideInput() {
        Log.e("TAG", "==hideInput==")
        if ((activity != null) && (dialog != null) && (dialog!!.window != null)) {
            val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            val v = dialog!!.window!!.peekDecorView()
            if (null != v) {
                imm.hideSoftInputFromWindow(v.windowToken, 0)
            }
        }
    }

    override fun dismiss() {
        mHandler.removeCallbacksAndMessages(null)
        super.dismissAllowingStateLoss()
    }

    companion object {
        val HistorySuccess = 1 //请求搜索历史数据成功
        val HistoryDeleteSuccess = 2 //请求搜索历史数据成功
        val RequstFail = 11 //请求失败
        val LoadMore = 12 //加载
        val Refresh = 13 //刷新
        val AddLike = 14 //添加喜欢的回调
        val Addkaraoke = 15 //添加K歌的回调
        val UPDATE_CURRENT_SONG = 99 //切歌
    }
}