package com.hifive.sdk.demo.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hifive.sdk.R;
import com.hifive.sdk.demo.adapter.HifiveMusicListAdapter;
import com.hifive.sdk.demo.model.HifiveMusicModel;
import com.hifive.sdk.demo.util.HifiveDialogManageUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 音乐列表-当前播放的fragment
 *
 * @author huchao
 */
public class HifiveMusicPalyListFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private HifiveMusicListAdapter adapter;
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
        getdata();
        return view;
    }
    //初始化view
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
    }
    //获取当前播放的列表
    private void getdata() {
        List<HifiveMusicModel> musicModels = new ArrayList<>();
        for(int i= 0 ; i < 15 ;i++){
            HifiveMusicModel musicModel = new HifiveMusicModel();
            musicModel.setId(i);
            musicModel.setName("告白气球"+(i+1));
            musicModel.setAuthor("周杰伦");
            musicModel.setAlbum("东风破");
            musicModel.setIntroduce("这是一段浪漫的故事！");
            musicModels.add(musicModel);
        }
        HifiveDialogManageUtil.currentList = musicModels;
        HifiveDialogManageUtil.playId = 0;
        adapter.updateDatas(HifiveDialogManageUtil.currentList);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(HifiveMusicPalyListFragment.class.getSimpleName(),"onResume");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
