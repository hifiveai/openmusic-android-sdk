package com.hifive.sdk.demo.ui;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hifive.sdk.R;
import com.hifive.sdk.demo.adapter.HifiveMusicSheetAdapter;
import com.hifive.sdk.demo.model.HifiveMusicSheetModel;
import com.hifive.sdk.demo.model.HifiveMusicSheetTipsModel;
import com.hifive.sdk.demo.util.HifiveDisplayUtils;
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
 * 音乐电台的fragment
 *
 * @author huchao
 */
public class HifiveMusicRadioStationFragment extends Fragment {
    public final static String TYPE_ID = "station_id";
    protected static final int Refresh =11;//刷新
    protected static final int LoadMore= 12;//加载
    protected static final int RequstFail= 15;//请求失败
    public boolean isRefresh= false;
    public boolean isLoadMore = false;
    private String id;//电台id
    private SmartRefreshLayout refreshLayout;
    private RecyclerView mRecyclerView;
    private HifiveMusicSheetAdapter adapter;
    private List<HifiveMusicSheetModel> sheetModels = new ArrayList<>();
    private int page = 1;
    private final int pageSize = 10;
    private Context mContext;
    protected Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Refresh:
                    isRefresh = false;
                    refreshLayout.finishRefresh();
                    adapter.updateDatas(sheetModels);
                    if( sheetModels != null && sheetModels.size() > 0){
                        refreshLayout.setEnableLoadMore(sheetModels.size() >= pageSize);
                    }else{
                        refreshLayout.setEnableLoadMore(false);
                    }
                    break;
                case LoadMore:
                    isLoadMore = false;
                    adapter.addDatas(sheetModels);
                    if(sheetModels.size() < pageSize){//返回的数据小于每页条数表示没有更多数据了不允许上拉加载
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
        mContext = this.getContext();
        View view = inflater.inflate(R.layout.hifive_fragment_radio_station, container, false);
        if(getArguments() != null)
            id = getArguments().getString(TYPE_ID);
        refreshLayout = view.findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshHeader(new HifiveRefreshHeader(getContext()));
        refreshLayout.setRefreshFooter(new HifiveLoadMoreFooter(getContext()));
        mRecyclerView = view.findViewById(R.id.rv_sheet);
        initRecyclerView();
        refreshLayout.autoRefresh();
        return view;
    }
    //初始化view
    private void initRecyclerView() {
        adapter = new HifiveMusicSheetAdapter(getActivity(), new ArrayList<HifiveMusicSheetModel>());
        adapter.setOnItemClickListener(new HifiveMusicSheetAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                HifiveMusicSheetDetaiDialoglFragment dialogFragment = new HifiveMusicSheetDetaiDialoglFragment();
                Bundle recommendBundle = new Bundle();
                recommendBundle.putSerializable(HifiveMusicSheetDetaiDialoglFragment.MODEL,adapter.getDatas().get(position));
                dialogFragment.setArguments(recommendBundle);
                dialogFragment.show(getFragmentManager(), HifiveMusicSheetDetaiDialoglFragment.class.getSimpleName());
            }
        });
        //设置布局管理器
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3);
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.right= HifiveDisplayUtils.dip2px(mContext, 5);
                outRect.left= HifiveDisplayUtils.dip2px(mContext, 5);
                outRect.bottom= HifiveDisplayUtils.dip2px(mContext, 10);
                outRect.top = 0;
            }
        });
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(adapter);
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
        sheetModels = new ArrayList<>();
        for(int i= pageSize*(page-1); i < pageSize*page ;i++){
            HifiveMusicSheetModel musicModel = new HifiveMusicSheetModel();
            musicModel.setId(i);
            musicModel.setName("人气爆棚！直播时爱听的歌"+i);
            musicModel.setIntroduce("歌单介绍歌单介绍歌单介绍歌单介绍歌单介绍歌单介绍歌单介绍歌单介绍歌单介绍歌单歌单介绍歌单介绍歌单介绍歌单介绍歌单介绍歌单介绍歌单介绍歌单介绍歌单介绍歌单");
            List<HifiveMusicSheetTipsModel>  sheetTipsModels = new ArrayList<>();
            for(int j= 0 ; j < 3 ;j++){
                HifiveMusicSheetTipsModel tipsModel = new HifiveMusicSheetTipsModel();
                tipsModel.setId(j);
                tipsModel.setName("标签"+(j+1));
                sheetTipsModels.add(tipsModel);
            }
            musicModel.setTips(sheetTipsModels);
            sheetModels.add(musicModel);
        }
        mHandler.sendEmptyMessageDelayed(ty,1000);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}
