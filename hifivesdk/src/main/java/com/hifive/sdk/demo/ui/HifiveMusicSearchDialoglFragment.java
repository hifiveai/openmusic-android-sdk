package com.hifive.sdk.demo.ui;

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

import com.hifive.sdk.R;
import com.hifive.sdk.demo.adapter.HifiveMusicSearchAdapter;
import com.hifive.sdk.demo.model.HifiveMusicModel;
import com.hifive.sdk.demo.util.HifiveDialogManageUtil;
import com.hifive.sdk.demo.util.HifiveDisplayUtils;
import com.hifive.sdk.demo.view.HifiveFlowLayout;
import com.hifive.sdk.demo.view.HifiveLoadMoreFooter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.tsy.sdk.myokhttp.util.LogUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 歌曲搜索的弹窗
 *
 * @author huchao
 */
public class HifiveMusicSearchDialoglFragment extends DialogFragment {
    public static final int SEARCHRESULT= 101;//搜索结果类型
    public static final int RECOMMENTRESULT= 102;//推荐结果类型
    public static final int HistorySuccess= 1;//请求搜索历史数据成功
    public static final int HistoryDeleteSuccess= 2;//请求搜索历史数据成功
    public static final int RequstFail= 11;//请求失败
    public static final int LoadMore= 12;//加载
    public static final int Refresh =13;//刷新
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
    private final int pageSize = 15;
    private List<String>  historyData;//搜索历史的数据
    private HifiveMusicSearchAdapter adapter;
    private List<HifiveMusicModel> musicModels;
    private int type;//查找类型
    private String content= "";//搜索的内容
    private Context mContext;
    private boolean TestSearch;//搜索逻辑测试调试模拟搜索内容为空情况
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
                    break;
                case Refresh:
                    ll_result.setVisibility(View.VISIBLE);
                    ll_history.setVisibility(View.GONE);
                    hideInput();
                    if(type == SEARCHRESULT){//搜索歌曲逻辑
                        if(musicModels != null && musicModels.size() > 0){
                            ll_empty.setVisibility(View.GONE);
                            //搜索结果小于每页数据时说明数据已获取完毕，关闭上拉加载
                            refreshLayout.setEnableLoadMore(musicModels.size() >= pageSize);
                            adapter.updateDatas(musicModels);
                        }else{
                            ll_empty.setVisibility(View.VISIBLE);
                            if(mContext != null)
                                tv_empty.setText(mContext.getString(R.string.hifivesdk_music_search_history_empty,content));
                            TestSearch = true;
                            type = RECOMMENTRESULT;
                            getRecommentData(Refresh);
                        }
                    }else{//查找推荐歌曲逻辑
                        if(musicModels != null && musicModels.size() > 0){
                            adapter.updateDatas(musicModels);
                            refreshLayout.setEnableLoadMore(musicModels.size() >= pageSize);
                        }else{
                            refreshLayout.setEnableLoadMore(false);
                        }
                    }
                    break;
                case LoadMore:
                    isLoadMore = false;
                    adapter.addDatas(musicModels);
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
        if(window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.BOTTOM;
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.MATCH_PARENT;
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
            window.setWindowAnimations(R.style.AnimationBottomFade);
            window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
            window.setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                    WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
        }
        View view = inflater.inflate(R.layout.hifive_dialog_music_search, container);
        initView(view);
        ininReclyView();
        initEvent();
        getHistoryData();
        HifiveDialogManageUtil.addDialog(this);
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
                    if(type == SEARCHRESULT){
                        getData(LoadMore);
                    }else{
                        getRecommentData(LoadMore);
                    }
                }
            }
        });
    }
    //初始化点击事件
    private void initEvent() {
        tv_cancle.setOnClickListener(new View.OnClickListener() {//取消搜索
            @Override
            public void onClick(View v) {
                dismiss();
                HifiveDialogManageUtil.removeDialog(1);
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
                deleteSearchHistory();
            }
        });

    }
    //清空搜索历史
    private void deleteSearchHistory() {
        mHandler.sendEmptyMessage(HistoryDeleteSuccess);
    }

    //获取搜索历史数据
    private void getHistoryData() {
        historyData = new ArrayList<>();
        historyData.add("告白气球");
        historyData.add("丑八怪");
        historyData.add("喜欢你");
        historyData.add("我好想你");
        historyData.add("兄弟");
        historyData.add("新鸳鸯蝴蝶梦");
        historyData.add("on my god");
        mHandler.sendEmptyMessage(HistorySuccess);
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
            tv.setText(historyData.get(i));
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
        type = SEARCHRESULT;
        et_content.clearFocus();
        refreshLayout.resetNoMoreData();
        updateHistoryData();
        getData(Refresh);
    }
    //更新搜索历史
    private void updateHistoryData() {
        historyData.remove(content);
        historyData.add(0,content);
        mHandler.sendEmptyMessage(HistorySuccess);
    }

    //根据名称搜索歌曲
    private void getData(int ty) {
        if(ty == Refresh){
            page = 1;
        }else{
            page++;
        }
        musicModels = new ArrayList<>();
        if(TestSearch){
            for(int i= pageSize*(page-1); i < pageSize*page ;i++){
                HifiveMusicModel musicModel = new HifiveMusicModel();
                musicModel.setId(i);
                musicModel.setName("木偶人"+(i+1));
                musicModel.setAuthor("薛之谦");
                musicModel.setAlbum("悔恨的泪");
                musicModel.setIntroduce("这是一段悲伤的往事！");
                musicModels.add(musicModel);
            }
        }
        mHandler.sendEmptyMessage(ty);
    }
    //搜索歌曲为空时，查找推荐歌曲列表
    private void getRecommentData(int ty) {
        if(ty == Refresh){
            page = 1;
        }else{
            page++;
        }
        musicModels = new ArrayList<>();
        for(int i= pageSize*(page-1); i < pageSize*page ;i++){
            HifiveMusicModel musicModel = new HifiveMusicModel();
            musicModel.setId(i);
            musicModel.setName("推荐木偶人"+(i+1));
            musicModel.setAuthor("推荐薛之谦");
            musicModel.setAlbum("推荐悔恨的泪");
            musicModel.setIntroduce("推荐这是一段悲伤的往事！");
            musicModels.add(musicModel);
        }
        mHandler.sendEmptyMessage(ty);
    }
    /**
     * 隐藏键盘
     */
    protected void hideInput() {
        Log.e("TAG","==hideInput==");
        if (getActivity() != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
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
