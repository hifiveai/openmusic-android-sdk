package com.hifive.sdk.demo.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hifive.sdk.R;
import com.hifive.sdk.demo.adapter.HifiveMusicListAdapter;
import com.hifive.sdk.demo.model.HifiveMusicModel;
import com.hifive.sdk.demo.view.HifiveLoadMoreFooter;
import com.hifive.sdk.demo.view.HifiveRefreshHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tsy.sdk.myokhttp.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 音乐列表-K歌的fragment
 *
 * @author huchao
 */
public class HifiveMusicKaraokeListFragment extends Fragment {
    protected static final int Refresh =11;//刷新
    protected static final int LoadMore= 12;//加载
    protected static final int RequstFail= 15;//请求失败
    public boolean isRefresh= false;
    public boolean isLoadMore = false;
    private SmartRefreshLayout refreshLayout;
    private LinearLayout ll_playall;
    private TextView tv_number;
    private RecyclerView mRecyclerView;
    private HifiveMusicListAdapter adapter;
    private  List<HifiveMusicModel> hifiveMusicModels;
    private int page = 1;
    private final int pageSize = 10;
    protected Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Refresh:
                    isRefresh = false;
                    refreshLayout.finishRefresh();
                    adapter.updateDatas(hifiveMusicModels);
                    tv_number.setText(getString(R.string.hifivesdk_music_all_play,adapter.getItemCount()));
                    if( hifiveMusicModels != null && hifiveMusicModels.size() > 0){
                        ll_playall.setVisibility(View.VISIBLE);
                        refreshLayout.setEnableLoadMore(hifiveMusicModels.size() >= pageSize);
                    }else{
                        refreshLayout.setEnableLoadMore(false);
                        ll_playall.setVisibility(View.GONE);
                    }
                    break;
                case LoadMore:
                    isLoadMore = false;
                    adapter.addDatas(hifiveMusicModels);
                    tv_number.setText(getString(R.string.hifivesdk_music_all_play,adapter.getItemCount()));
                    if(hifiveMusicModels.size() < pageSize){//返回的数据小于每页条数表示没有更多数据了不允许上拉加载
                        refreshLayout.finishLoadMoreWithNoMoreData();
                    }else{
                        refreshLayout.finishLoadMore();
                    }
                    break;
                case RequstFail:
                    if (isRefresh) {
                        isRefresh = false;
                        refreshLayout.finishRefresh();
                    }
                    if (isLoadMore) {
                        isLoadMore = false;
                        refreshLayout.finishLoadMore();
                    }
                    break;
            }
            return false;
        }
    });
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hifive_fragment_music_list, container, false);
        initView(view);
        initRecyclerView();
        refreshLayout.autoRefresh();
        return view;
    }
    //初始化view
    private void initView(View view) {
        refreshLayout = view.findViewById(R.id.refreshLayout);
        ll_playall = view.findViewById(R.id.ll_playall);
        tv_number = view.findViewById(R.id.tv_number);
        mRecyclerView = view.findViewById(R.id.rv_music);
        refreshLayout.setRefreshHeader(new HifiveRefreshHeader(getContext()));
        refreshLayout.setRefreshFooter(new HifiveLoadMoreFooter(getContext()));
    }
    //初始化RecyclerView
    private void initRecyclerView() {
        adapter = new HifiveMusicListAdapter(getActivity(), new ArrayList<HifiveMusicModel>());
        adapter.setOnItemClickListener(new HifiveMusicListAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {


            }
        });
        adapter.setOnItemDeleteClickListener(new HifiveMusicListAdapter.OnItemDeleteClickListener() {
            @Override
            public void onClick(View v, int position) {


            }
        });
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));//调整RecyclerView的排列方向
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                LogUtils.e( "onRefresh");
                if(!isRefresh){
                    page = 1;
                    isRefresh=true;
                    getData(Refresh);
                }
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                LogUtils.e( "onLoadMore");
                if (!isLoadMore) {
                    page++;
                    isLoadMore = true;
                    getData(LoadMore);
                }
            }
        });
    }
    //根据电台获取歌单数据
    private void getData(int ty) {
        hifiveMusicModels = new ArrayList<>();
        for(int i= pageSize*(page-1); i < pageSize*page ;i++){
            HifiveMusicModel musicModel = new HifiveMusicModel();
            musicModel.setId(i);
            musicModel.setName("告白气球"+(i+1));
            musicModel.setAuthor("周杰伦");
            musicModel.setAlbum("东风破");
            musicModel.setIntroduce("这是一段浪漫的故事！");
            hifiveMusicModels.add(musicModel);
        }
        mHandler.sendEmptyMessageDelayed(ty,1000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}
