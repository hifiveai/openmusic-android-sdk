package com.hifive.sdk.demo.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
import com.hifive.sdk.R;
import com.hifive.sdk.demo.adapter.HifiveMusicSearchAdapter;
import com.hifive.sdk.demo.model.HifiveMusicModel;
import com.hifive.sdk.demo.model.HifiveMusicSearchrModel;
import com.hifive.sdk.demo.util.HifiveDialogManageUtil;
import com.hifive.sdk.demo.util.HifiveDisplayUtils;
import com.hifive.sdk.demo.view.HifiveFlowLayout;
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
 * 歌曲搜索的弹窗
 *
 * @author huchao
 */
public class HifiveMusicSearchDialoglFragment extends DialogFragment {
    public static final int HistorySuccess= 1;//请求搜索历史数据成功
    public static final int HistoryDeleteSuccess= 2;//请求搜索历史数据成功
    public static final int RequstFail= 11;//请求失败
    public static final int LoadMore= 12;//加载
    public static final int Refresh =13;//刷新
    public static final int AddLike= 14;//添加喜欢的回调
    public static final int Addkaraoke =15;//添加K歌的回调
    public boolean isLoadMore = false;
    private TextView tv_cancle;//取消搜索按钮
    private EditText et_content;//输出框
    private ImageView iv_clear;//清空输入按钮

    private LinearLayout ll_history;//搜索历史相关view
    private ImageView iv_delete;//清空搜索历史按钮
    private HifiveFlowLayout fl_history;//搜索历史布局

    private LinearLayout ll_result;//搜索结果相关view
    private LinearLayout ll_empty;//空布局
    private TextView tv_empty;//搜索结果为空时提示信息
    private SmartRefreshLayout refreshLayout;//下拉刷新view
    private RecyclerView mRecyclerView;

    private int page = 1;
    private List<HifiveMusicSearchrModel>  historyData;//搜索历史的数据
    private HifiveMusicSearchAdapter adapter;
    private List<HifiveMusicModel> musicModels;
    private String content= "";//搜索的内容
    private Context mContext;
    private boolean isAddLike;//保存是否正在添加喜欢状态，防止重复点击
    private boolean isAddkaraoke;//保存是否正在添加K歌状态，防止重复点击
    private Toast toastStyle;//自定义样式的toast
    private Toast toast;//系统自带样式的toast
    private TextView toastTextview;
    private boolean isRecommand;//是否为无结果时推荐
    private int totalPage =1;//总页卡
    private final Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case HistorySuccess:
                    if(historyData != null && historyData.size() > 0){
                        iv_delete.setVisibility(View.VISIBLE);
                        fl_history.setVisibility(View.VISIBLE);
                        initFlowLayout();
                    }else{
                        iv_delete.setVisibility(View.GONE);
                        fl_history.setVisibility(View.GONE);
                    }
                    break;
                case HistoryDeleteSuccess:
                    historyData.clear();
                    if (fl_history != null) {
                        fl_history.removeAllViews();
                    }
                    iv_delete.setVisibility(View.GONE);
                    break;
                case Refresh:
                    ll_result.setVisibility(View.VISIBLE);
                    ll_history.setVisibility(View.GONE);
                    hideInput();
                    if(isRecommand){
                        ll_empty.setVisibility(View.VISIBLE);
                        if(mContext != null)
                            tv_empty.setText(mContext.getString(R.string.hifivesdk_music_search_history_empty,content));
                    }else{
                        ll_empty.setVisibility(View.GONE);
                    }
                    if(musicModels != null)
                        adapter.updateDatas(musicModels);
                    refreshLayout.setEnableLoadMore(page<totalPage);
                    break;
                case LoadMore:
                    isLoadMore = false;
                    if(musicModels != null)
                        adapter.addDatas(musicModels);
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
            if (window != null) {
                WindowManager.LayoutParams params = window.getAttributes();
                params.gravity = Gravity.BOTTOM;
                params.width = WindowManager.LayoutParams.MATCH_PARENT;
                params.height = WindowManager.LayoutParams.MATCH_PARENT;
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
            Window window = this.getDialog().getWindow();
            if (window != null) {
                window.setWindowAnimations(R.style.AnimationBottomFade);
                window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
                window.setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
            }
        }
        View view = inflater.inflate(R.layout.hifive_dialog_music_search, container);
        initView(view);
        ininReclyView();
        initEvent();
        getHistoryData(false);
        HifiveDialogManageUtil.getInstance().addDialog(this);
        return view;
    }


    //初始化view
    private void initView(View view) {
        tv_cancle = view.findViewById(R.id.tv_cancle);
        et_content = view.findViewById(R.id.et_content);
        et_content.setHorizontallyScrolling(false);
        et_content.setMaxLines(1);
        iv_clear = view.findViewById(R.id.iv_clear);
        ll_history = view.findViewById(R.id.ll_history);
        iv_delete = view.findViewById(R.id.iv_delete);
        fl_history = view.findViewById(R.id.fl_history);
        ll_result = view.findViewById(R.id.ll_result);
        ll_empty = view.findViewById(R.id.ll_empty);
        tv_empty = view.findViewById(R.id.tv_empty);
        iv_delete = view.findViewById(R.id.iv_delete);
        refreshLayout = view.findViewById(R.id.refreshLayout);
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setRefreshFooter(new HifiveLoadMoreFooter(getContext()));
        mRecyclerView = view.findViewById(R.id.rv_music);
    }
    //初始化ReclyView
    private void ininReclyView() {
        adapter = new HifiveMusicSearchAdapter(getContext(),new ArrayList<HifiveMusicModel>(),false);
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
    //初始化点击事件
    private void initEvent() {
        tv_cancle.setOnClickListener(new View.OnClickListener() {//取消搜索
            @Override
            public void onClick(View v) {
                dismiss();
                HifiveDialogManageUtil.getInstance().removeDialog(1);
            }
        });
        et_content.setOnEditorActionListener(new TextView.OnEditorActionListener() {//开始搜索
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchMusic();
                }
                return false;
            }
        });
        et_content.addTextChangedListener(new TextWatcher() {//监听输入变化
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 0){
                    iv_clear.setVisibility(View.GONE);
                }else{
                    iv_clear.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_content.setOnFocusChangeListener(new View.OnFocusChangeListener(){//监听焦点输入框焦点变化
            @Override
            public void onFocusChange(View arg0, boolean hasFocus) {
                if(hasFocus){
                    ll_history.setVisibility(View.VISIBLE);
                    ll_result.setVisibility(View.GONE);
                }
            }
        });
        iv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_content.setText("");
            }
        });
        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmDialog();
            }
        });
    }
    //弹窗删除二次确认框
    private void showConfirmDialog() {
        HifiveComfirmDialogFragment dialog = new HifiveComfirmDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(HifiveComfirmDialogFragment.ContentTx, getString(R.string.hifivesdk_comfirm_delete_history));
        dialog.setArguments(bundle);
        dialog.setOnSureClick(new HifiveComfirmDialogFragment.OnSureClick() {
            @Override
            public void sureClick() {
                deleteSearchHistory();
            }
        });
        if(getFragmentManager() != null)
            dialog.show(getFragmentManager(), HifiveComfirmDialogFragment.class.getSimpleName());
    }
    //清空搜索历史
    private void deleteSearchHistory() {
        if (HFLiveApi.Companion.getInstance() == null || getContext() == null)
            return;
        HFLiveApi.Companion.getInstance().deleteSearchRecord(getContext(), new DataResponse() {
            @Override
            public void errorMsg(@NotNull String string, @org.jetbrains.annotations.Nullable Integer code) {
                showToast(string);
            }

            @Override
            public void data(@NotNull Object any) {
                Log.e("TAG", "==清空搜索历史==");
                mHandler.sendEmptyMessage(HistoryDeleteSuccess);
            }
        });
    }

    //获取搜索历史数据
    private void getHistoryData(final boolean isUpdate) {
        if (HFLiveApi.Companion.getInstance() == null || getContext() == null)
            return;
        HFLiveApi.Companion.getInstance().getSearchRecordList(getContext(), "10", "1", new DataResponse() {
            @Override
            public void errorMsg(@NotNull String string, @org.jetbrains.annotations.Nullable Integer code) {
                if (!isUpdate) {
                    showToast(string);
                    mHandler.sendEmptyMessage(HistorySuccess);
                }
            }

            @Override
            public void data(@NotNull Object any) {
                Log.e("TAG","==搜索历史=="+any);
                historyData = JSON.parseArray(JSONObject.parseObject(String.valueOf(any)).getString("records"), HifiveMusicSearchrModel.class);
                mHandler.sendEmptyMessage(HistorySuccess);
            }
        });
    }
    //设置搜索历史信息
    private void initFlowLayout() {
        if (fl_history != null) {
            fl_history.removeAllViews();
        }
        //往容器内添加TextView数据
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,HifiveDisplayUtils.dip2px(mContext, 30));
        layoutParams.setMargins(0, HifiveDisplayUtils.dip2px(mContext, 10), HifiveDisplayUtils.dip2px(mContext, 10), 0);
        for (int i = 0; i < historyData.size(); i++) {
            final TextView tv = new TextView(mContext);
            tv.setPadding(HifiveDisplayUtils.dip2px(mContext, 10), 0,
                    HifiveDisplayUtils.dip2px(mContext, 10), 0);
            tv.setText(historyData.get(i).getKeyword());
            tv.setMaxEms(10);
            tv.setSingleLine();
            tv.setGravity(Gravity.CENTER_VERTICAL);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            tv.setTextColor(Color.parseColor("#FF8E8E93"));
            tv.setBackgroundResource(R.drawable.hifive_dialog_music_search_item_bg);
            tv.setLayoutParams(layoutParams);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    et_content.setText(tv.getText().toString());
                    searchMusic();
                }
            });
            fl_history.addView(tv,layoutParams);
        }
    }
    //搜索音乐
    private void searchMusic() {
        content = et_content.getText().toString().trim();
        if(TextUtils.isEmpty(content)){
            if(mContext != null)
                Toast.makeText(mContext,mContext.getString(R.string.hifivesdk_music_search_toast),Toast.LENGTH_SHORT).show();
            return;
        }
        et_content.clearFocus();
        refreshLayout.resetNoMoreData();
        getData(Refresh);
    }
    //根据名称搜索歌曲
    private void getData(final int ty) {
        if (HFLiveApi.Companion.getInstance() == null || getContext() == null)
            return;
        if (ty == Refresh) {
            page = 1;
        } else {
            page++;
        }
        Log.e("TAG","searchPage=="+page);
        HFLiveApi.Companion.getInstance().getMusicList(getContext(), "2", content, null, "musicTag",
                "15", String.valueOf(page), new DataResponse() {
                    @Override
                    public void errorMsg(@NotNull String string, @org.jetbrains.annotations.Nullable Integer code) {
                        if (ty != Refresh) {//上拉加载请求失败后，还原页卡
                            page--;
                        } else {
                            getHistoryData(true);
                        }
                        showToast(string);
                        mHandler.sendEmptyMessage(RequstFail);
                    }

                    @Override
                    public void data(@NotNull Object any) {
                        Log.e("TAG","搜索歌曲=="+ any);
                        JSONObject jsonObject = JSONObject.parseObject(String.valueOf(any));
                        musicModels = JSON.parseArray(jsonObject.getString("records"), HifiveMusicModel.class);
                        totalPage = jsonObject.getInteger("totalPage");
                        if(ty == Refresh){
                            isRecommand = jsonObject.getBoolean("isRecommand");
                            getHistoryData(true);
                        }
                        mHandler.sendEmptyMessage(ty);
                    }
                });
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
    /**
     * 隐藏键盘
     */
    protected void hideInput() {
        Log.e("TAG","==hideInput==");
        if (getActivity() != null &&  getDialog()!= null && getDialog().getWindow()!= null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            View v = getDialog().getWindow().peekDecorView();
            if (null != v) {
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        }
    }
    @Override
    public void dismiss() {
        mHandler.removeCallbacksAndMessages(null);
        super.dismissAllowingStateLoss();
    }
}
