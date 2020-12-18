package com.hfliveplayer.sdk.ui;

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


import com.hfliveplayer.sdk.R;
import com.hfliveplayer.sdk.adapter.HifiveMusicSheetAdapter;
import com.hfliveplayer.sdk.util.HifiveDialogManageUtil;
import com.hfliveplayer.sdk.util.HifiveDisplayUtils;
import com.hfliveplayer.sdk.view.HifiveLoadMoreFooter;
import com.hfliveplayer.sdk.view.HifiveRefreshHeader;
import com.hifive.sdk.entity.HifiveMusicBean;
import com.hifive.sdk.entity.HifiveMusicSheetModel;
import com.hifive.sdk.hInterface.DataResponse;
import com.hifive.sdk.manager.HFLiveApi;
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
public class HifiveMusicRadioStationFragment extends Fragment {
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
    private HifiveMusicSheetAdapter adapter;
    private List<HifiveMusicSheetModel> sheetModels = new ArrayList<>();
    private int page = 1;
    private Context mContext;
    private int totalPage =1;//总页卡
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
                        refreshLayout.setEnableLoadMore(page<totalPage);
                        break;
                    case LoadMore:
                        isLoadMore = false;
                        if(sheetModels != null)
                            adapter.addDatas(sheetModels);
                        if(page < totalPage){
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
        adapter = new HifiveMusicSheetAdapter(getActivity(), new ArrayList<HifiveMusicSheetModel>());
        adapter.setOnItemClickListener(new HifiveMusicSheetAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                HifiveMusicSheetDetaiDialoglFragment dialogFragment = new HifiveMusicSheetDetaiDialoglFragment();
                Bundle recommendBundle = new Bundle();
                recommendBundle.putSerializable(HifiveMusicSheetDetaiDialoglFragment.MODEL,adapter.getDatas().get(position));
                dialogFragment.setArguments(recommendBundle);
                if(getFragmentManager() != null)
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
            if (HFLiveApi.getInstance() == null || getContext() == null)
                return;
            HFLiveApi.getInstance().getCompanySheetList(getContext(), id, null, null,
                    null, null, "sheetTag", "10", String.valueOf(page), new DataResponse<HifiveMusicBean<HifiveMusicSheetModel>>() {
                        @Override
                        public void errorMsg(@NotNull String string, @org.jetbrains.annotations.Nullable Integer code) {
                            if (ty != Refresh) {//上拉加载请求失败后，还原页卡
                                page--;
                            }
                            HifiveDialogManageUtil.getInstance().showToast(getActivity(),string);
                            mHandler.sendEmptyMessage(RequstFail);
                        }

                        @Override
                        public void data(@NotNull HifiveMusicBean<HifiveMusicSheetModel> any) {
//                            Log.e("TAG","歌单数据=="+any);

                            sheetModels = any.getRecords();
                            totalPage = any.getTotalPage();

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
