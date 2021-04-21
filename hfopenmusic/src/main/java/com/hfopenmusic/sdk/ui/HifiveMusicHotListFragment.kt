package com.hfopenmusic.sdk.ui

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hfopen.sdk.entity.MusicList
import com.hfopen.sdk.entity.MusicRecord
import com.hfopen.sdk.hInterface.DataResponse
import com.hfopen.sdk.manager.HFOpenApi
import com.hfopen.sdk.rx.BaseException
import com.hfopenmusic.sdk.HFOpenMusic
import com.hfopenmusic.sdk.R
import com.hfopenmusic.sdk.adapter.HifiveMusicSheetListAdapter
import com.hfopenmusic.sdk.view.HifiveLoadMoreFooter
import com.hfopenmusic.sdk.view.HifiveRefreshHeader
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import java.util.*

/**
 * 音乐列表-我喜欢的fragment
 *
 * @author huchao
 */
class HifiveMusicHotListFragment : Fragment() {

    companion object {
        val RequstFail = 11 //请求失败
        val LoadMore = 12 //加载
        val Refresh = 13 //刷新
        val UPDATE_CURRENT_SONG = 99 //切歌
    }

    var isRefresh = false
    var isLoadMore = false
    private var refreshLayout: SmartRefreshLayout? = null
    private var mRecyclerView: RecyclerView? = null
    private var adapter: HifiveMusicSheetListAdapter? = null
    private var hotModels: List<MusicRecord>? = null
    private var mContext: Context? = null
    private var page = 1
    private var totalCount = 0

    private var mHandler: Handler? = Handler { msg ->
        try {
            when (msg.what) {
                Refresh -> {
                    isRefresh = false
                    refreshLayout!!.finishRefresh()
                    if (hotModels != null && hotModels!!.isNotEmpty()) {
                        adapter!!.addHeaderView(R.layout.hifive_header_playall)
                        adapter!!.updateDatas(hotModels)
                    }
                    refreshLayout!!.setEnableLoadMore(adapter!!.datas.size < totalCount)
                }
                RequstFail -> if (isRefresh) {
                    isRefresh = false
                    refreshLayout!!.finishRefresh()
                }
                LoadMore -> {
                    isLoadMore = false
                    if (hotModels != null) adapter!!.addDatas(hotModels)
                    if (adapter!!.datas.size < totalCount) {
                        refreshLayout!!.finishLoadMore()
                    } else {
                        refreshLayout!!.finishLoadMoreWithNoMoreData()
                    }
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

    override fun onResume() {
        super.onResume()
        if (hotModels == null) {
            refreshLayout!!.autoRefresh()
        }
    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContext = context
        val view = inflater.inflate(R.layout.hifive_fragment_music_list, container, false)
        initView(view)
        initRecyclerView()
        return view
    }

    //初始化view
    private fun initView(view: View) {
        refreshLayout = view.findViewById(R.id.refreshLayout)
        mRecyclerView = view.findViewById(R.id.rv_music)
        refreshLayout!!.setRefreshHeader(HifiveRefreshHeader(context))
        refreshLayout!!.setEnableLoadMore(true)
        refreshLayout!!.setRefreshFooter(HifiveLoadMoreFooter(context))
    }

    //初始化RecyclerView
    private fun initRecyclerView() {
        adapter = HifiveMusicSheetListAdapter(activity, ArrayList())
        adapter!!.setOnRecyclerViewContentClick { position ->
            mHandler!!.removeMessages(HifiveMusicPalyListFragment.UPDATE_CURRENT_SONG)
            val hifiveMusicModel = adapter!!.datas[position] as MusicRecord
            val message = mHandler!!.obtainMessage()
            message.obj = hifiveMusicModel
            message.what = HifiveMusicPalyListFragment.UPDATE_CURRENT_SONG
            mHandler!!.sendMessageDelayed(message, 200)
        }
        adapter!!.setOnRecyclerViewHeaderClick {
            HFOpenMusic.getInstance().updateCurrentList(adapter!!.datas as List<MusicRecord?>)
            //加入播放列表成功
            HFOpenMusic.getInstance().showToast(activity,"加入播放列表成功")}
        mRecyclerView!!.adapter = adapter
        mRecyclerView!!.layoutManager = LinearLayoutManager(context) //调整RecyclerView的排列方向
        refreshLayout!!.setOnRefreshListener {
            if (!isRefresh) {
                page = 1
                isRefresh = true
                getData(Refresh)
            }
        }
        refreshLayout!!.setOnLoadMoreListener {
            if (!isLoadMore) {
                page++
                isLoadMore = true
                getData(HifiveMusicChannelSheetDetailFragment.LoadMore)
            }
        }
    }
    //根据用户歌单id获取歌曲数据
    private fun getData(ty: Int) {
        try {
            if (mContext == null) return
                    HFOpenApi.getInstance().baseHot(System.currentTimeMillis()/1000-60*60*24*365,  365, page, 20, object : DataResponse<MusicList> {
                        override fun onError(exception: BaseException) {
                            if (ty != Refresh) { //上拉加载请求失败后，还原页卡
                                page--
                            }
                            HFOpenMusic.getInstance().showToast(activity, exception.msg)
                            mHandler!!.sendEmptyMessage(RequstFail)
                        }

                        override fun onSuccess(data: MusicList, taskId: String) {
                            hotModels = data.record
                            totalCount = data.meta.totalCount
                            if (mHandler == null) return
                            mHandler!!.sendEmptyMessage(ty)
                        }
                    })
        } catch (e: Exception) {
            e.printStackTrace()
        }
        }

    override fun onDestroy() {
        super.onDestroy()
        mHandler!!.removeCallbacksAndMessages(null)
        mHandler = null
    }


}