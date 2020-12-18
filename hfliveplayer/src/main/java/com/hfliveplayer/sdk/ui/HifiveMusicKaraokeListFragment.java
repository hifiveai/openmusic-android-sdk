package com.hfliveplayer.sdk.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hfliveplayer.sdk.R;
import com.hfliveplayer.sdk.adapter.BaseRecyclerViewAdapter;
import com.hfliveplayer.sdk.adapter.HifiveMusicListAdapter;
import com.hfliveplayer.sdk.listener.HifiveAddMusicListener;
import com.hfliveplayer.sdk.util.HifiveDialogManageUtil;
import com.hfliveplayer.sdk.view.HifiveRefreshHeader;
import com.hifive.sdk.entity.HifiveMusicBean;
import com.hifive.sdk.entity.HifiveMusicModel;
import com.hifive.sdk.hInterface.DataResponse;
import com.hifive.sdk.manager.HFLiveApi;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * 音乐列表-K歌的fragment
 *
 * @author huchao
 */
public class HifiveMusicKaraokeListFragment extends Fragment implements Observer {
    protected static final int RequstSuccess =11;//请求成功
    protected static final int RequstFail= 12;//请求失败
    protected static final int deleteSuccess= 13;//删除成功
    protected static final int UPDATE_CURRENT_SONG= 99;//切歌
    public boolean isRefresh= false;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView mRecyclerView;
    private HifiveMusicListAdapter adapter;
    private List<HifiveMusicModel> hifiveMusicModels;
    private long sheetId;
    private HifiveAddMusicListener addMusicListener;

    public void setAddMusicListener(HifiveAddMusicListener addMusicListener) {
        this.addMusicListener = addMusicListener;
    }
    protected Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case RequstSuccess:
                        isRefresh = false;
                        refreshLayout.finishRefresh();
                        if(hifiveMusicModels == null)
                            hifiveMusicModels = new ArrayList<>();
                        if (hifiveMusicModels.size() > 0) {
                            adapter.addHeaderView(R.layout.hifive_header_playall);
                            adapter.updateDatas(hifiveMusicModels);
                            HifiveDialogManageUtil.getInstance().setKaraokeList(hifiveMusicModels);
                        }else{
                            adapter.addEmptyView(R.layout.hifive_recycler_emptyview);
                        }
                        break;
                    case RequstFail:
                        if (isRefresh) {
                            isRefresh = false;
                            refreshLayout.finishRefresh();
                        }
                        break;
                    case deleteSuccess:
                        if(getActivity() != null){
                            HifiveDialogManageUtil.getInstance().showToast(getActivity(),getActivity().getString(R.string.hifivesdk_comfirm_dialog_delete));
                        }
                        adapter.getDatas().remove(msg.arg1);
                        adapter.notifyDataSetChanged();
                        HifiveDialogManageUtil.getInstance().setKaraokeList(adapter.getDatas());
                        if(adapter.getDatas().size()==0){
                            adapter.removeHeaderView();
                            adapter.addEmptyView(R.layout.hifive_recycler_emptyview);
                        }
                        break;
                    case UPDATE_CURRENT_SONG:
                        HifiveMusicModel hifiveMusicModel = (HifiveMusicModel) msg.obj;
                        HifiveDialogManageUtil.getInstance().addCurrentSingle(getActivity(),hifiveMusicModel,"1");
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
        View view = inflater.inflate(R.layout.hifive_fragment_music_list, container, false);
        if(getActivity() != null)
            sheetId = HifiveDialogManageUtil.getInstance().getUserSheetIdByName(getActivity().getString(R.string.hifivesdk_music_karaoke));
        initView(view);
        initRecyclerView();
        if(HifiveDialogManageUtil.getInstance().getKaraokeList() !=null
                && HifiveDialogManageUtil.getInstance().getKaraokeList().size() >0){
            hifiveMusicModels = HifiveDialogManageUtil.getInstance().getKaraokeList();
            mHandler.sendEmptyMessageDelayed(RequstSuccess,1000);
        }else{
            refreshLayout.autoRefresh();
        }
        return view;
    }
    //初始化view
    private void initView(View view) {
        refreshLayout = view.findViewById(R.id.refreshLayout);
        mRecyclerView = view.findViewById(R.id.rv_music);
        refreshLayout.setRefreshHeader(new HifiveRefreshHeader(getContext()));
        refreshLayout.setEnableLoadMore(false);

//        TextView tv_add = view.findViewById(R.id.tv_add);
//        tv_add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(addMusicListener != null){
//                    addMusicListener.onAddMusic();
//                }
//            }
//        });
    }
    //初始化RecyclerView
    private void initRecyclerView() {
        adapter = new HifiveMusicListAdapter(getActivity(), new ArrayList<HifiveMusicModel>());
        adapter.setOnRecyclerViewContentClick(new BaseRecyclerViewAdapter.OnRecyclerViewContentClick(){
            @Override
            public void OnContentClick(int position) {
                mHandler.removeMessages(UPDATE_CURRENT_SONG);
                HifiveMusicModel hifiveMusicModel = (HifiveMusicModel) adapter.getDatas().get(position);
                Message message = mHandler.obtainMessage();
                message.obj = hifiveMusicModel;
                message.what = UPDATE_CURRENT_SONG;
                mHandler.sendMessageDelayed(message,200);
//                HifiveDialogManageUtil.getInstance().addCurrentSingle(getActivity(), (HifiveMusicModel) adapter.getDatas().get(position), "2");
            }
        });
        adapter.setOnRecyclerViewHeaderClick(new BaseRecyclerViewAdapter.OnRecyclerViewHeaderClick() {
            @Override
            public void OnHeaderClick() {
                HifiveDialogManageUtil.getInstance().updateCurrentList(getActivity(), (List<HifiveMusicModel>)adapter.getDatas());
            }
        });

        adapter.setOnItemDeleteClickListener(new HifiveMusicListAdapter.OnItemDeleteClickListener() {
            @Override
            public void onClick(View v, int position) {
                showConfirmDialog(position);
            }
        });
        adapter.setOnEmptyViewClickListener(new HifiveMusicListAdapter.OnEmptyViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                if(addMusicListener != null){
                    addMusicListener.onAddMusic();
                }
            }
        });

        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));//调整RecyclerView的排列方向
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if(!isRefresh){
                    isRefresh=true;
                    getData();
                }
            }
        });
    }

    //弹窗删除二次确认框
    private void showConfirmDialog(final int position) {
        HifiveComfirmDialogFragment dialog = new HifiveComfirmDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(HifiveComfirmDialogFragment.ContentTx, getString(R.string.hifivesdk_comfirm_delete_music));
        dialog.setArguments(bundle);
        dialog.setOnSureClick(new HifiveComfirmDialogFragment.OnSureClick() {
            @Override
            public void sureClick() {
                deleteMusic(position);
            }
        });
        if(getFragmentManager() != null)
            dialog.show(getFragmentManager(), HifiveComfirmDialogFragment.class.getSimpleName());
    }
    //删除会员歌单歌曲
    private void deleteMusic(final int position) {
        try {
            if (HFLiveApi.getInstance() == null || getContext() == null)
                return;
            HFLiveApi.getInstance().deleteMemberSheetMusic(getContext(), String.valueOf(sheetId),
                    ((HifiveMusicModel)adapter.getDatas().get(position)).getMusicId(), new DataResponse() {
                        @Override
                        public void errorMsg(@NotNull String string, @org.jetbrains.annotations.Nullable Integer code) {
                            HifiveDialogManageUtil.getInstance().showToast(getActivity(),string);
                        }

                        @Override
                        public void data(@NotNull Object any) {
                            Log.e("TAG", "==删除成功==");
                            Message message = mHandler.obtainMessage();
                            message.arg1 = position;
                            message.what = deleteSuccess;
                            mHandler.sendMessage(message);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //根据用户歌单id获取歌曲数据
    private void getData() {
        try {
            if (HFLiveApi.getInstance() == null || getContext() == null)
                return;
            HFLiveApi.getInstance().getMemberSheetMusicList(getContext(), String.valueOf(sheetId), null, HifiveDialogManageUtil.field,
                    "100", "1", new DataResponse<HifiveMusicBean<HifiveMusicModel>>() {
                        @Override
                        public void errorMsg(@NotNull String string, @org.jetbrains.annotations.Nullable Integer code) {
                            HifiveDialogManageUtil.getInstance().showToast(getActivity(),string);
                            mHandler.sendEmptyMessage(RequstFail);
                        }

                        @Override
                        public void data(@NotNull HifiveMusicBean<HifiveMusicModel> any) {
//                            Log.e("TAG", "K歌数据==" + any);
                            hifiveMusicModels = any.getRecords();
                            mHandler.sendEmptyMessage(RequstSuccess);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void update(Observable o, Object arg) {
        try{
            int type = (int) arg;
            if(adapter != null){
                if(type == HifiveDialogManageUtil.UPDATEPALY){
                    adapter.notifyDataSetChanged();
                }else if(type == HifiveDialogManageUtil.UPDATEKARAOKLIST){
                    hifiveMusicModels = HifiveDialogManageUtil.getInstance().getKaraokeList();
                    if (hifiveMusicModels.size() > 0) {
                        adapter.addHeaderView(R.layout.hifive_header_playall);
                        adapter.updateDatas(hifiveMusicModels);
                    }else{
                        adapter.addEmptyView(R.layout.hifive_recycler_emptyview);
                    }
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
