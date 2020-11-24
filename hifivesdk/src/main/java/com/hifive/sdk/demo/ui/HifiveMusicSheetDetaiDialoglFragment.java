package com.hifive.sdk.demo.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.hifive.sdk.R;
import com.hifive.sdk.demo.adapter.HifiveMusicSearchAdapter;
import com.hifive.sdk.demo.model.HifiveMusicModel;
import com.hifive.sdk.demo.model.HifiveMusicSheetModel;
import com.hifive.sdk.demo.model.HifiveMusicTagModel;
import com.hifive.sdk.demo.util.HifiveDialogManageUtil;
import com.hifive.sdk.demo.util.HifiveDisplayUtils;
import com.hifive.sdk.demo.view.HifiveLoadMoreFooter;
import com.hifive.sdk.hInterface.DataResponse;
import com.hifive.sdk.manager.HFLiveApi;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import org.jetbrains.annotations.NotNull;

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
    public static final int AddLike= 14;//添加喜欢的回调
    public static final int Addkaraoke =15;//添加K歌的回调
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
    private List<HifiveMusicModel> musicModels;
    private Context mContext;
    private boolean isAddLike;//保存是否正在添加喜欢状态，防止重复点击
    private boolean isAddkaraoke;//保存是否正在添加K歌状态，防止重复点击
    private Toast toastStyle;//自定义样式的toast
    private Toast toast;//系统自带样式的toast
    private TextView toastTextview;
    private int totalPage =1;//总页卡
    private final Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Refresh:
                    if(musicModels != null && musicModels.size() > 0){
                        adapter.updateDatas(musicModels);
                        ll_playall.setVisibility(View.VISIBLE);
                    }else{
                        ll_playall.setVisibility(View.GONE);
                    }
                    tv_number.setText(getString(R.string.hifivesdk_music_all_play,adapter.getItemCount()));
                    refreshLayout.setEnableLoadMore(page < totalPage);
                    break;
                case LoadMore:
                    isLoadMore = false;
                    if(musicModels != null)
                        adapter.addDatas(musicModels);
                    tv_number.setText(getString(R.string.hifivesdk_music_all_play,adapter.getItemCount()));
                    if(page < totalPage){
                        refreshLayout.finishLoadMore();
                    }else{
                        refreshLayout.finishLoadMoreWithNoMoreData();
                    }
                    break;
                case RequstFail:
                    if (isLoadMore) {
                        isLoadMore = false;
                        refreshLayout.finishLoadMore();
                    }
                    break;
                case AddLike:
                    isAddLike = false;
                    showToast(R.string.hifivesdk_music_add_like_msg);
                    HifiveDialogManageUtil.getInstance().addLikeSingle((HifiveMusicModel) msg.obj);
                    adapter.notifyItemChanged(msg.arg1);
                    break;
                case Addkaraoke:
                    isAddkaraoke = false;
                    showToast(R.string.hifivesdk_music_add_karaoke_msg);
                    HifiveDialogManageUtil.getInstance().addKaraokeSingle((HifiveMusicModel) msg.obj);
                    adapter.notifyItemChanged(msg.arg1);
                    break;
            }
            return false;
        }
    });
    @Override
    public void onStart() {
        super.onStart();
        if(getDialog() != null) {
            Window window = getDialog().getWindow();
            if (window != null && mContext != null) {
                WindowManager.LayoutParams params = window.getAttributes();
                params.gravity = Gravity.BOTTOM;
                params.width = WindowManager.LayoutParams.MATCH_PARENT;
                params.height = HifiveDisplayUtils.dip2px(mContext, 440);
                window.setAttributes(params);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
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
        if(getDialog() != null) {
            Window window = getDialog().getWindow();
            if (window != null) {
                window.setWindowAnimations(R.style.AnimationRightFade);
                window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
                window.setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
            }
        }
        View view = inflater.inflate(R.layout.hifive_dialog_music_sheet_detail, container);
        if(getArguments() != null)
            musicSheetModel = (HifiveMusicSheetModel) getArguments().getSerializable(MODEL);
        initView(view);
        updateSheetView();
        ininReclyView();
        getData(Refresh);
        HifiveDialogManageUtil.getInstance().addDialog(this);
        return view;
    }
    //初始化view
    private void initView(View view) {
        view.findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                HifiveDialogManageUtil.getInstance().removeDialog(2);
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
        ll_playall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adapter != null){
                    HifiveDialogManageUtil.getInstance().updateCurrentList(adapter.getDatas());
                }
            }
        });
    }
    //初始化ReclyView
    private void ininReclyView() {
        adapter = new HifiveMusicSearchAdapter(getContext(),new ArrayList<HifiveMusicModel>(),true);
        adapter.setOnAddkaraokeClickListener(new HifiveMusicSearchAdapter.OnAddkaraokeClickListener() {
            @Override
            public void onClick(View v, int position) {
                if(!isAddkaraoke){
                    addUserSheet(position,Addkaraoke);
                }
            }
        });
        adapter.setOnAddLikeClickListener(new HifiveMusicSearchAdapter.OnAddLikeClickListener() {
            @Override
            public void onClick(View v, int position) {
                if(!isAddLike){
                    addUserSheet(position,AddLike);
                }
            }
        });
        adapter.setOnItemClickListener(new HifiveMusicSearchAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                HifiveDialogManageUtil.getInstance().addCurrentSingle(getActivity(),adapter.getDatas().get(position),"2");
            }
        });
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));//调整RecyclerView的排列方向
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (!isLoadMore) {
                    isLoadMore = true;
                    getData(LoadMore);
                }
            }
        });
    }
    //添加歌曲到会员歌单列表
    private void addUserSheet(final int position, final int type) {
        if (mContext != null && HFLiveApi.Companion.getInstance() != null) {
            long sheetId;
            if (type == Addkaraoke) {//加入到我的K歌
                sheetId = HifiveDialogManageUtil.getInstance().getUserSheetIdByName(mContext.getString(R.string.hifivesdk_music_karaoke));
            } else {//加入到我的喜欢
                sheetId = HifiveDialogManageUtil.getInstance().getUserSheetIdByName(mContext.getString(R.string.hifivesdk_music_like));
            }
            HFLiveApi.Companion.getInstance().saveMemberSheetMusic(mContext, String.valueOf(sheetId),
                    adapter.getDatas().get(position).getMusicId(), new DataResponse() {
                        @Override
                        public void errorMsg(@NotNull String string, @org.jetbrains.annotations.Nullable Integer code) {
                            isAddLike = false;
                            isAddkaraoke = false;
                            showToast(string);
                        }

                        @Override
                        public void data(@NotNull Object any) {
                            Log.e("TAG", "==加入成功==");
                            Message message = mHandler.obtainMessage();
                            message.what = type;
                            message.arg1 = position;
                            message.obj = adapter.getDatas().get(position);
                            mHandler.sendMessage(message);
                        }
                    });
        }
    }
    //更新ui
    private void updateSheetView() {
        if(musicSheetModel != null && mContext != null) {
            sheetId = musicSheetModel.getSheetId();
            if(musicSheetModel.getCover() != null && !TextUtils.isEmpty(musicSheetModel.getCover().getUrl())){
                Glide.with(mContext).asBitmap().load(musicSheetModel.getCover().getUrl())
                        .error(R.mipmap.hifvesdk_sheet_default)
                        .placeholder(R.mipmap.hifvesdk_sheet_default)
                        .into(iv_image);//四周都是圆角的圆角矩形图片。
            }else{
                Glide.with(mContext).load(R.mipmap.hifvesdk_sheet_default)
                        .into(iv_image);//四周都是圆角的圆角矩形图片。
            }
            tv_name.setText(musicSheetModel.getSheetName());
            tv_introduce.setText(musicSheetModel.getDescribe());
            if (musicSheetModel.getTag() != null && musicSheetModel.getTag().size() > 0) {
                tv_tips.setVisibility(View.VISIBLE);
                StringBuilder tip = new StringBuilder();
                for (HifiveMusicTagModel tipsModel : musicSheetModel.getTag()) {
                    if (tip.length() > 0) {
                        tip.append("，");
                    }
                    tip.append(tipsModel.getTagName());
                }
                tv_tips.setText(tip.toString());
            } else {
                tv_tips.setVisibility(View.GONE);
            }
        }
    }
    //显示自定义toast信息
    private void showToast(int msgId){
        if(mContext != null){
            if(toastStyle == null){
                toastStyle = new Toast(mContext);
                View layout = View.inflate(mContext, R.layout.hifive_layout_toast, null);
                toastTextview = layout.findViewById(R.id.tv_content);
                toastTextview.setText(mContext.getString(msgId));
                toastStyle.setView(layout);
                toastStyle.setGravity(Gravity.CENTER, 0, 0);
                toastStyle.setDuration(Toast.LENGTH_LONG);
            }else {
                toastTextview.setText(mContext.getString(msgId));
            }
            toastStyle.show();
        }
    }
    //显示自定义toast信息
    @SuppressLint("ShowToast")
    private void showToast(String msg){
        if(getActivity() != null){
            if(toast == null){
                toast = Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT);
            }else {
                toast.setText(msg);
            }
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    toast.show();
                }
            });
        }
    }
    //根据歌单id获取歌曲信息
    private void getData(final int ty) {
        if (HFLiveApi.Companion.getInstance() == null || mContext == null)
            return;
        if (ty == Refresh) {
            page = 1;
        } else {
            page++;
        }
        HFLiveApi.Companion.getInstance().getCompanySheetMusicList(mContext, String.valueOf(sheetId), null, null,
                "10", String.valueOf(page), new DataResponse() {
                    @Override
                    public void errorMsg(@NotNull String string, @org.jetbrains.annotations.Nullable Integer code) {
                        if (ty != Refresh) {//上拉加载请求失败后，还原页卡
                            page--;
                        }
                        showToast(string);
                        mHandler.sendEmptyMessage(RequstFail);
                    }

                    @Override
                    public void data(@NotNull Object any) {
                        Log.e("TAG","歌曲=="+any);
                        JSONObject jsonObject = JSONObject.parseObject(String.valueOf(any));
                        musicModels = JSON.parseArray(jsonObject.getString("records"), HifiveMusicModel.class);
                        totalPage =jsonObject.getInteger("totalPage");
                        mHandler.sendEmptyMessage(ty);
                    }
                });

    }
    @Override
    public void dismiss() {
        mHandler.removeCallbacksAndMessages(null);
        super.dismissAllowingStateLoss();
    }
}
