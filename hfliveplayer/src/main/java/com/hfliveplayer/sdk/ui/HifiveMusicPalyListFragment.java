package com.hfliveplayer.sdk.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.hifive.sdk.entity.HifiveMusicModel;

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
    private HifiveAddMusicListener addMusicListener;

    protected static final int UPDATE_CURRENT_SONG= 99;//切歌

    public void setAddMusicListener(HifiveAddMusicListener addMusicListener) {
        this.addMusicListener = addMusicListener;
    }

    protected Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == UPDATE_CURRENT_SONG) {
                HifiveMusicModel hifiveMusicModel = (HifiveMusicModel) msg.obj;
                HifiveDialogManageUtil.getInstance().addCurrentSingle(getActivity(), hifiveMusicModel, "2");
            }
            return true;
        }
    });

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hifive_fragment_music_list_current, container, false);
        mRecyclerView =  view.findViewById(R.id.rv_music);
        initRecyclerView();
        return view;
    }
    //初始化view
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

        adapter.setOnItemDeleteClickListener(new HifiveMusicListAdapter.OnItemDeleteClickListener() {
            @Override
            public void onClick(View v, int position) {
                showConfirmDialog((HifiveMusicModel) adapter.getDatas().get(position));
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

        if(HifiveDialogManageUtil.getInstance().getCurrentList() !=null
                && HifiveDialogManageUtil.getInstance().getCurrentList().size() >0){
            adapter.updateDatas(HifiveDialogManageUtil.getInstance().getCurrentList());
        }else{
            adapter.addEmptyView(R.layout.hifive_recycler_emptyview2);
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
                        HifiveDialogManageUtil.getInstance().cleanPlayMusic(true);
                    }
                }
                HifiveDialogManageUtil.getInstance().getCurrentList().remove(musicModel);
                if(adapter.getDatas().size()==0){
                    adapter.addEmptyView(R.layout.hifive_recycler_emptyview);
                }
                adapter.notifyDataSetChanged();
                if(getActivity() != null){
                    HifiveDialogManageUtil.getInstance().showToast(getActivity(),getActivity().getString(R.string.hifivesdk_comfirm_dialog_delete));
                }
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
        mHandler.removeCallbacksAndMessages(null);
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
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
