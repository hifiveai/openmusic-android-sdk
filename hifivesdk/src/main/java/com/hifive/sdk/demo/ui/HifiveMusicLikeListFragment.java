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
import com.hifive.sdk.demo.util.HifiveDialogManageUtil;
import com.hifive.sdk.demo.view.HifiveRefreshHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tsy.sdk.myokhttp.util.LogUtils;

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
    public boolean isRefresh= false;
    private SmartRefreshLayout refreshLayout;
    private LinearLayout ll_playall;
    private TextView tv_number;
    private RecyclerView mRecyclerView;
    private HifiveMusicListAdapter adapter;
    private  List<HifiveMusicModel> hifiveMusicModels;
    protected Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case RequstSuccess:
                    isRefresh = false;
                    refreshLayout.finishRefresh();
                    adapter.updateDatas(hifiveMusicModels);
                    tv_number.setText(getString(R.string.hifivesdk_music_all_play,adapter.getItemCount()));
                    if( hifiveMusicModels != null && hifiveMusicModels.size() > 0){
                        ll_playall.setVisibility(View.VISIBLE);
                    }else{
                        refreshLayout.setEnableLoadMore(false);
                    }
                    HifiveDialogManageUtil.getInstance().setLikeList(adapter.getDatas());
                    break;
                case RequstFail:
                    if (isRefresh) {
                        isRefresh = false;
                        refreshLayout.finishRefresh();
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
        ll_playall = view.findViewById(R.id.ll_playall);
        tv_number = view.findViewById(R.id.tv_number);
        mRecyclerView = view.findViewById(R.id.rv_music);
        refreshLayout.setRefreshHeader(new HifiveRefreshHeader(getContext()));
        refreshLayout.setEnableLoadMore(false);
        ll_playall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adapter != null){
                    HifiveDialogManageUtil.getInstance().updateCurrentList(adapter.getDatas());
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
                if(HifiveDialogManageUtil.getInstance().playId != adapter.getDatas().get(position).getId()){
                    HifiveDialogManageUtil.getInstance().addCurrentSingle(adapter.getDatas().get(position));
                }
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
                LogUtils.e( "onRefresh");
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
                adapter.getDatas().remove(position);
                HifiveDialogManageUtil.getInstance().setLikeList(adapter.getDatas());
                adapter.notifyDataSetChanged();
            }
        });
        dialog.show(getFragmentManager(), HifiveComfirmDialogFragment.class.getSimpleName());
    }
    //根据电台获取歌单数据
    private void getData() {
        hifiveMusicModels = new ArrayList<>();
        for(int i= 0; i < 100 ;i++){
            HifiveMusicModel musicModel = new HifiveMusicModel();
            musicModel.setId(i);
            musicModel.setName("尘埃"+(i+1));
            musicModel.setAuthor("永彬Ryan.B");
            musicModel.setAlbum("杀破狼");
            musicModel.setIntroduce("这是一段凄惨的故事！");
            hifiveMusicModels.add(musicModel);
        }
        mHandler.sendEmptyMessageDelayed(RequstSuccess,1000);
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
                    if(tv_number != null && getContext()!= null)
                        tv_number.setText(getContext().getString(R.string.hifivesdk_music_all_play,adapter.getItemCount()));
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
