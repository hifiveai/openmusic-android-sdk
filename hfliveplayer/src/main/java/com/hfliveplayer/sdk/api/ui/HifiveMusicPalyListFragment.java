package com.hfliveplayer.sdk.demo.ui;

import android.os.Bundle;
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

import com.hfliveplayer.sdk.R;
import com.hfliveplayer.sdk.demo.adapter.HifiveMusicListAdapter;
import com.hfliveplayer.sdk.demo.model.HifiveMusicModel;
import com.hfliveplayer.sdk.demo.util.HifiveDialogManageUtil;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * 音乐列表-当前播放的fragment
 *
 * @author huchao
 */
public class HifiveMusicPalyListFragment extends Fragment implements Observer {
    private RecyclerView mRecyclerView;
    private HifiveMusicListAdapter adapter;
    private LinearLayout ll_empty;
    private TextView tv_add;
    private HifiveAddMusicListener addMusicListener;

    public void setAddMusicListener(HifiveAddMusicListener addMusicListener) {
        this.addMusicListener = addMusicListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hifive_fragment_music_list_current, container, false);
        mRecyclerView =  view.findViewById(R.id.rv_music);
        ll_empty =  view.findViewById(R.id.ll_empty);
        tv_add =  view.findViewById(R.id.tv_add);
        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addMusicListener != null){
                    addMusicListener.onAddMusic();
                }
            }
        });
        initRecyclerView();
        if(HifiveDialogManageUtil.getInstance().getCurrentList() !=null
                && HifiveDialogManageUtil.getInstance().getCurrentList().size() >0){
            adapter.updateDatas(HifiveDialogManageUtil.getInstance().getCurrentList());
        }
        updateView();
        return view;
    }
    //初始化view
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
                showConfirmDialog(adapter.getDatas().get(position));
            }
        });
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));//调整RecyclerView的排列方向
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
    private void showConfirmDialog(final HifiveMusicModel musicModel) {
        HifiveComfirmDialogFragment dialog = new HifiveComfirmDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(HifiveComfirmDialogFragment.ContentTx, getString(R.string.hifivesdk_comfirm_delete_music));
        dialog.setArguments(bundle);
        dialog.setOnSureClick(new HifiveComfirmDialogFragment.OnSureClick() {
            @Override
            public void sureClick() {
                //如果删除的是正在播放的歌曲
                if(HifiveDialogManageUtil.getInstance().getPlayMusic() != null
                        && HifiveDialogManageUtil.getInstance().getPlayMusic().getMusicId().equals(musicModel.getMusicId())){
                    if(adapter.getItemCount() > 1){
                        HifiveDialogManageUtil.getInstance().playNextMusic(getActivity());
                    }else{
                        HifiveDialogManageUtil.getInstance().cleanPlayMusic();
                    }
                }
                HifiveDialogManageUtil.getInstance().getCurrentList().remove(musicModel);
                adapter.notifyDataSetChanged();
                updateView();
            }
        });
        if(getFragmentManager() != null)
            dialog.show(getFragmentManager(), HifiveComfirmDialogFragment.class.getSimpleName());
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void update(Observable o, Object arg) {
        try{
            int type = (int) arg;
            if(adapter != null){
                if(type == HifiveDialogManageUtil.UPDATEPALY){
                    adapter.notifyDataSetChanged();
                }else  if(type == HifiveDialogManageUtil.UPDATEPALYLIST){
                    adapter.updateDatas(HifiveDialogManageUtil.getInstance().getCurrentList());
                    updateView();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
