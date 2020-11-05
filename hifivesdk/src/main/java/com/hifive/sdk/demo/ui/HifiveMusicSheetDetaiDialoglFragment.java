package com.hifive.sdk.demo.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.hifive.sdk.R;
import com.hifive.sdk.demo.adapter.HifiveMusicSearchAdapter;
import com.hifive.sdk.demo.model.HifiveMusicModel;
import com.hifive.sdk.demo.model.HifiveMusicSheetModel;
import com.hifive.sdk.demo.model.HifiveMusicSheetTipsModel;
import com.hifive.sdk.demo.util.HifiveDialogManageUtil;
import com.hifive.sdk.demo.util.HifiveDisplayUtils;
import com.hifive.sdk.demo.view.HifiveLoadMoreFooter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.tsy.sdk.myokhttp.util.LogUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 音乐歌单详情的弹窗
 *
 * @author huchao
 */
public class HifiveMusicSheetDetaiDialoglFragment extends DialogFragment {
    public final static String MODEL = "sheet_model";
    public static final int RequstFail= 2;//请求失败
    public static final int LoadMore= 12;//加载
    public static final int Refresh =11;//刷新
    public boolean isLoadMore = false;
    private long  sheetId;
    private HifiveMusicSheetModel musicSheetModel;
    private ImageView iv_image;
    private TextView tv_name;
    private TextView tv_introduce;
    private TextView tv_tips;
    private SmartRefreshLayout refreshLayout;
    private LinearLayout ll_playall;
    private TextView tv_number;
    private RecyclerView mRecyclerView;
    private HifiveMusicSearchAdapter adapter;
    private int page = 1;
    private final int pageSize = 10;
    private List<HifiveMusicModel> musicModels;
    private Context mContext;
    private final Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Refresh:
                    adapter.updateDatas(musicModels);
                    tv_number.setText(getString(R.string.hifivesdk_music_all_play,adapter.getItemCount()));
                    if(musicModels != null && musicModels.size() > 0){
                        ll_playall.setVisibility(View.VISIBLE);
                        refreshLayout.setEnableLoadMore(musicModels.size() >= pageSize);
                    }else{
                        refreshLayout.setEnableLoadMore(false);
                        ll_playall.setVisibility(View.GONE);
                    }
                    break;
                case LoadMore:
                    isLoadMore = false;
                    adapter.addDatas(musicModels);
                    tv_number.setText(getString(R.string.hifivesdk_music_all_play,adapter.getItemCount()));
                    if(musicModels.size() < pageSize){//返回的数据小于每页条数表示没有更多数据了不允许上拉加载
                        refreshLayout.finishLoadMoreWithNoMoreData();
                    }else{
                        refreshLayout.finishLoadMore();
                    }
                    break;
                case RequstFail:
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
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        if(window != null && mContext != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.BOTTOM;
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = HifiveDisplayUtils.dip2px(mContext, 440);
            window.setAttributes(params);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.HifiveSdkDialogStyly);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mContext = getContext();
        Window window = this.getDialog().getWindow();
        if(window != null) {
            window.setWindowAnimations(R.style.AnimationRightFade);
            window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
            window.setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                    WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
        }
        View view = inflater.inflate(R.layout.hifive_dialog_music_sheet_detail, container);
        if(getArguments() != null)
            musicSheetModel = (HifiveMusicSheetModel) getArguments().getSerializable(MODEL);
        initView(view);
        updateSheetView();
        ininReclyView();
        getData(Refresh);
        HifiveDialogManageUtil.addDialog(this);
        return view;
    }
    //初始化view
    private void initView(View view) {
        view.findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                HifiveDialogManageUtil.removeDialog(2);
            }
        });
        iv_image = view.findViewById(R.id.iv_image);
        tv_name = view.findViewById(R.id.tv_name);
        tv_introduce = view.findViewById(R.id.tv_introduce);
        tv_tips = view.findViewById(R.id.tv_tips);
        refreshLayout = view.findViewById(R.id.refreshLayout);
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setRefreshFooter(new HifiveLoadMoreFooter(getContext()));
        ll_playall = view.findViewById(R.id.ll_playall);
        tv_number = view.findViewById(R.id.tv_number);
        mRecyclerView = view.findViewById(R.id.rv_music);
    }
    //初始化ReclyView
    private void ininReclyView() {
        adapter = new HifiveMusicSearchAdapter(getContext(),new ArrayList<HifiveMusicModel>(),true);
        adapter.setOnAddkaraokeClickListener(new HifiveMusicSearchAdapter.OnAddkaraokeClickListener() {
            @Override
            public void onClick(View v, long musicId) {

            }
        });
        adapter.setOnAddLikeClickListener(new HifiveMusicSearchAdapter.OnAddLikeClickListener() {
            @Override
            public void onClick(View v, long musicId) {

            }
        });
        adapter.setOnItemClickListener(new HifiveMusicSearchAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {

            }
        });
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));//调整RecyclerView的排列方向
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                LogUtils.e( "onLoadMore");
                if (!isLoadMore) {
                    isLoadMore = true;
                    getData(LoadMore);
                }
            }
        });
    }
    //更新ui
    private void updateSheetView() {
        if(musicSheetModel != null && mContext != null) {
            sheetId = musicSheetModel.getId();
            if (!TextUtils.isEmpty(musicSheetModel.getImageUrl())) {
                Glide.with(mContext).load(musicSheetModel.getImageUrl())
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(HifiveDisplayUtils.dip2px(mContext, 6))))
                        .into(iv_image);//四周都是圆角的圆角矩形图片。
            } else {
                Glide.with(mContext).load(R.drawable.hifve_sheet_default)
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(HifiveDisplayUtils.dip2px(mContext, 6))))
                        .into(iv_image);//四周都是圆角的圆角矩形图片。
            }
            tv_name.setText(musicSheetModel.getName());
            tv_introduce.setText(musicSheetModel.getIntroduce());
            if (musicSheetModel.getTips() != null && musicSheetModel.getTips().size() > 0) {
                tv_tips.setVisibility(View.VISIBLE);
                StringBuilder tip = new StringBuilder();
                for (HifiveMusicSheetTipsModel tipsModel : musicSheetModel.getTips()) {
                    if (tip.length() > 0) {
                        tip.append("，");
                    }
                    tip.append(tipsModel.getName());
                }
                tv_tips.setText(tip.toString());
            } else {
                tv_tips.setVisibility(View.GONE);
            }
        }
    }
    //根据歌单id获取歌曲信息
    private void getData(int ty) {
        if(ty == Refresh){
            page = 1;
        }else{
            page++;
        }
        musicModels = new ArrayList<>();
        for(int i= pageSize*(page-1); i < pageSize*page ;i++){
            HifiveMusicModel musicModel = new HifiveMusicModel();
            musicModel.setId(i);
            musicModel.setName("木偶人"+(i+1));
            musicModel.setAuthor("薛之谦");
            musicModel.setAlbum("悔恨的泪");
            musicModel.setIntroduce("这是一段悲伤的往事！");
            musicModels.add(musicModel);
        }
        mHandler.sendEmptyMessage(ty);
    }
    @Override
    public void dismiss() {
        mHandler.removeCallbacksAndMessages(null);
        super.dismissAllowingStateLoss();
    }
}
