package com.hfliveplayer.sdk.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hfliveplayer.sdk.R;
import com.hfliveplayer.sdk.adapter.HifiveMusicListAdapter;
import com.hfliveplayer.sdk.model.HifiveMusicModel;
import com.hfliveplayer.sdk.util.HifiveDialogManageUtil;
import com.hfliveplayer.sdk.view.HifiveRefreshHeader;
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
 * 音乐列表-我喜欢的fragment
 *
 * @author huchao
 */
public class HifiveMusicLikeListFragment extends Fragment implements Observer {
    protected static final int RequstSuccess =11;//请求成功
    protected static final int RequstFail= 12;//请求失败
    protected static final int deleteSuccess= 13;//删除成功
    public boolean isRefresh= false;
    private SmartRefreshLayout refreshLayout;
    private TextView tv_number;
    private RecyclerView mRecyclerView;
    private HifiveMusicListAdapter adapter;
    private List<HifiveMusicModel> hifiveMusicModels;
    private LinearLayout ll_empty;
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
                        adapter.updateDatas(hifiveMusicModels);
                        tv_number.setText(getString(R.string.hifivesdk_music_all_play,adapter.getItemCount()));
                        updateView();
                        HifiveDialogManageUtil.getInstance().setLikeList(adapter.getDatas());
                        break;
                    case RequstFail:
                        if (isRefresh) {
                            isRefresh = false;
                            refreshLayout.finishRefresh();
                        }
                        break;
                    case deleteSuccess:
                        adapter.getDatas().remove(msg.arg1);
                        adapter.notifyDataSetChanged();
                        updateView();
                        if(getActivity() != null){
                            HifiveDialogManageUtil.getInstance().showToast(getActivity(),getActivity().getString(R.string.hifivesdk_comfirm_dialog_delete));
                        }
                        HifiveDialogManageUtil.getInstance().setLikeList(adapter.getDatas());
                        tv_number.setText(getString(R.string.hifivesdk_music_all_play,adapter.getItemCount()));
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
            sheetId = HifiveDialogManageUtil.getInstance().getUserSheetIdByName(getActivity().getString(R.string.hifivesdk_music_like));
        initView(view);
        initRecyclerView();
        if(HifiveDialogManageUtil.getInstance().getLikeList() !=null
                && HifiveDialogManageUtil.getInstance().getLikeList().size() >0){
            hifiveMusicModels = HifiveDialogManageUtil.getInstance().getLikeList();
            mHandler.sendEmptyMessageDelayed(RequstSuccess,1000);
        }else{
            refreshLayout.autoRefresh();
        }
        return view;
    }
    //初始化view
    private void initView(View view) {
        refreshLayout = view.findViewById(R.id.refreshLayout);
        LinearLayout ll_playall = view.findViewById(R.id.ll_playall);
        tv_number = view.findViewById(R.id.tv_number);
        mRecyclerView = view.findViewById(R.id.rv_music);
        refreshLayout.setRefreshHeader(new HifiveRefreshHeader(getContext()));
        refreshLayout.setEnableLoadMore(false);
        ll_playall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adapter != null){
                    HifiveDialogManageUtil.getInstance().updateCurrentList(getActivity(),adapter.getDatas());
                }
            }
        });
        ll_empty =  view.findViewById(R.id.ll_empty);
        TextView tv_add = view.findViewById(R.id.tv_add);
        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addMusicListener != null){
                    addMusicListener.onAddMusic();
                }
            }
        });
    }
    //初始化RecyclerView
    private void initRecyclerView() {
        adapter = new HifiveMusicListAdapter(getActivity(), new ArrayList<HifiveMusicModel>());
        adapter.setOnItemClickListener(new HifiveMusicListAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                HifiveDialogManageUtil.getInstance().addCurrentSingle(getActivity(),adapter.getDatas().get(position),"2");
            }
        });
        adapter.setOnItemDeleteClickListener(new HifiveMusicListAdapter.OnItemDeleteClickListener() {
            @Override
            public void onClick(View v, int position) {
                showConfirmDialog(position);
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
    //判断空view是否显示
    private void updateView() {
        if(adapter.getItemCount() >0){
            ll_empty.setVisibility(View.GONE);
        }else{
            ll_empty.setVisibility(View.VISIBLE);
        }
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
        if (HFLiveApi.Companion.getInstance() == null || getContext() == null)
            return;
        HFLiveApi.Companion.getInstance().deleteMemberSheetMusic(getContext(), String.valueOf(sheetId),
                adapter.getDatas().get(position).getMusicId(), new DataResponse() {
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
    }

    //根据用户歌单id获取歌曲数据
    private void getData() {
        if (HFLiveApi.Companion.getInstance() == null || getContext() == null)
            return;
        HFLiveApi.Companion.getInstance().getMemberSheetMusicList(getContext(), String.valueOf(sheetId), null, HifiveDialogManageUtil.field,
                "100", "1", new DataResponse() {
                    @Override
                    public void errorMsg(@NotNull String string, @org.jetbrains.annotations.Nullable Integer code) {
                        HifiveDialogManageUtil.getInstance().showToast(getActivity(),string);
                        mHandler.sendEmptyMessage(RequstFail);
                    }

                    @Override
                    public void data(@NotNull Object any) {
                        Log.e("TAG", "喜欢数据==" + any);
                        hifiveMusicModels = JSON.parseArray(JSONObject.parseObject(String.valueOf(any)).getString("records"), HifiveMusicModel.class);
                        mHandler.sendEmptyMessage(RequstSuccess);
                    }
                });
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
                }else if(type == HifiveDialogManageUtil.UPDATELIKELIST){
                    adapter.updateDatas(HifiveDialogManageUtil.getInstance().getLikeList());
                    if(ll_empty != null)
                        updateView();
                    if(tv_number != null && getContext()!= null)
                        tv_number.setText(getContext().getString(R.string.hifivesdk_music_all_play,adapter.getItemCount()));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
