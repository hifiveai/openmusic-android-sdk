package com.hfopenmusic.sdk.ui;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.hfopen.sdk.entity.ChannelSheet;
import com.hfopen.sdk.entity.Record;
import com.hfopen.sdk.manager.HFOpenApi;
import com.hfopen.sdk.rx.BaseException;
import com.hfopenmusic.sdk.R;
import com.hfopenmusic.sdk.adapter.HifiveMusicChannelSheetAdapter;
import com.hfopenmusic.sdk.util.HifiveDialogManageUtil;
import com.hfopenmusic.sdk.util.HifiveDisplayUtils;
import com.hfopenmusic.sdk.view.HifiveLoadMoreFooter;
import com.hfopenmusic.sdk.view.HifiveRefreshHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 音乐电台的fragment
 *
 * @author huchao
 */
public class HifiveMusicChannelSheetFragment extends Fragment {
    public final static String TYPE_ID = "station_id";
    protected static final int Refresh =11;//刷新
    protected static final int LoadMore= 12;//加载
    protected static final int RequstFail= 15;//请求失败
    public boolean isRefresh= false;
    public boolean isLoadMore = false;
    private String id;//电台id
    private LinearLayout ll_empty;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView mRecyclerView;
    private HifiveMusicChannelSheetAdapter adapter;
    private List<Record> sheetModels = new ArrayList<>();
    private int page = 1;
    private Context mContext;
    private int totalCount = 0;
    protected Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case Refresh:
                        isRefresh = false;
                        refreshLayout.finishRefresh();
                        if(sheetModels != null)
                            adapter.updateDatas(sheetModels);
                        updateView();
                        refreshLayout.setEnableLoadMore(adapter.getDatas().size()<totalCount);
                        break;
                    case LoadMore:
                        isLoadMore = false;
                        if(sheetModels != null)
                            adapter.addDatas(sheetModels);
                        if(adapter.getDatas().size()<totalCount){
                            refreshLayout.finishLoadMore();
                        }else{
                            refreshLayout.finishLoadMoreWithNoMoreData();
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
            } catch (Exception e) {
                e.printStackTrace();
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
        ll_empty =  view.findViewById(R.id.ll_empty);
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
        adapter = new HifiveMusicChannelSheetAdapter(getActivity(), new ArrayList<Record>());
        adapter.setOnItemClickListener(new HifiveMusicChannelSheetAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                HifiveMusicChannelSheetDetailFragment dialogFragment = new HifiveMusicChannelSheetDetailFragment();
                Bundle recommendBundle = new Bundle();
                recommendBundle.putSerializable(HifiveMusicChannelSheetDetailFragment.MODEL,adapter.getDatas().get(position));
                dialogFragment.setArguments(recommendBundle);
                if(getFragmentManager() != null)
                    dialogFragment.show(getFragmentManager(), HifiveMusicChannelSheetDetailFragment.class.getSimpleName());
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
                if (!isLoadMore) {
                    page++;
                    isLoadMore = true;
                    getData(LoadMore);
                }
            }
        });
    }
    //根据电台获取歌单数据
    private void getData(final int ty) {
        try {
            if (getContext() == null)
                return;
            HFOpenApi.getInstance().channelSheet(id, 0, null, page, 10, new com.hfopen.sdk.hInterface.DataResponse<ChannelSheet>() {
                @Override
                public void onError(@NotNull BaseException e) {
                    if (ty != Refresh) {//上拉加载请求失败后，还原页卡
                        page--;
                    }
                    HifiveDialogManageUtil.getInstance().showToast(getActivity(),e.getMsg());
                    mHandler.sendEmptyMessage(RequstFail);
                }

                @Override
                public void onSuccess(ChannelSheet channelSheet, @NotNull String s) {
                    sheetModels = channelSheet.getRecord();
                    totalCount = channelSheet.getMeta().getTotalCount();
                    mHandler.sendEmptyMessage(ty);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //判断空view是否显示
    private void updateView() {
        if(adapter.getItemCount() >0){
            ll_empty.setVisibility(View.GONE);
        }else{
            ll_empty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}
