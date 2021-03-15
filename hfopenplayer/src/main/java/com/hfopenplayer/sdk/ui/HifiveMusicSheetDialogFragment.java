package com.hfopenplayer.sdk.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.hfopenplayer.sdk.R;
import com.hfopenplayer.sdk.adapter.HifiveViewPagerAdapter;
import com.hfopenplayer.sdk.util.HifiveDialogManageUtil;
import com.hfopenplayer.sdk.util.HifiveDisplayUtils;
import com.hfopenplayer.sdk.view.magicindicator.CommonNavigator;
import com.hfopenplayer.sdk.view.magicindicator.CommonNavigatorAdapter;
import com.hfopenplayer.sdk.view.magicindicator.CommonPagerTitleView;
import com.hfopenplayer.sdk.view.magicindicator.MagicIndicator;
import com.hfopenplayer.sdk.view.magicindicator.ViewPagerHelper;
import com.hfopenplayer.sdk.view.magicindicator.abs.IPagerIndicator;
import com.hfopenplayer.sdk.view.magicindicator.abs.IPagerTitleView;
import com.hifive.sdk.entity.HifiveMusicChannelModel;
import com.hifive.sdk.hInterface.DataResponse;
import com.hifive.sdk.manager.HFLiveApi;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


/**
 * 音乐歌单列表的弹窗
 *
 * @author huchao
 */
public class HifiveMusicSheetDialogFragment extends DialogFragment {
    private MagicIndicator magicIndicator;
    private ViewPager viewPager;
    private List<HifiveMusicChannelModel> companyChannelLists = new ArrayList<>();
    private  Context mContext;
    @Override
    public void onStart() {
        super.onStart();
        if(getDialog() != null) {
            Window window = getDialog().getWindow();
            if (window != null && mContext != null) {
                WindowManager.LayoutParams params = window.getAttributes();
                params.gravity = Gravity.BOTTOM;
                params.width = WindowManager.LayoutParams.MATCH_PARENT;
                params.height = HifiveDisplayUtils.getPlayerHeight(mContext);
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
    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
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
        View view = inflater.inflate(R.layout.hifive_dialog_music_sheet, container);
        initView(view);
        getRadioStationData();
        HifiveDialogManageUtil.getInstance().addDialog(this);
        return view;
    }
    //初始化view
    private void initView(View view) {
        view.findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                HifiveDialogManageUtil.getInstance().removeDialog(1);
            }
        });
        magicIndicator = view.findViewById(R.id.magic_indicator);
        viewPager = view.findViewById(R.id.viewpager);
    }
    //初始化指示器
    private void initMagicIndicator() {
        CommonNavigator commonNavigator = new CommonNavigator(getActivity());
        commonNavigator.setAdjustMode(false);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return companyChannelLists == null ? 0 : companyChannelLists.size();
            }

            @Override
            public IPagerTitleView getTitleView(final Context context, final int index) {
                final CommonPagerTitleView cptv = new CommonPagerTitleView(context);
                cptv.setContentView(R.layout.hifive_layout_indecator);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                cptv.setLayoutParams(lp);
                // 初始化
                final TextView titleText = cptv.findViewById(R.id.tv_indicator);
                final View vvDown = cptv.findViewById(R.id.vv_line);
                if(companyChannelLists.get(index) != null && !TextUtils.isEmpty(companyChannelLists.get(index).getChannelName()))
                    titleText.setText(companyChannelLists.get(index).getChannelName());
                cptv.setOnPagerTitleChangeListener(new CommonPagerTitleView.OnPagerTitleChangeListener() {
                    @Override
                    public void onSelected(int index, int totalCount) {
                        titleText.setTextColor(Color.parseColor("#FF000000"));
                        titleText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//加粗
                        titleText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                        vvDown.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onDeselected(int index, int totalCount) {
                        titleText.setTextColor(Color.parseColor("#FFC7C7CC"));
                        titleText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                        titleText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        vvDown.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {

                        cptv.setScaleX(1.0f - (0.1f) * leavePercent);
                        cptv.setScaleY(1.0f - (0.1f) * leavePercent);
                    }

                    @Override
                    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
                        cptv.setScaleX(0.9f + (0.1f) * enterPercent);
                        cptv.setScaleY(0.9f + (0.1f) * enterPercent);
                    }
                });

                cptv.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                viewPager.setCurrentItem(index);
                                            }
                                        }
                );
                return cptv;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                return null;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, viewPager);

    }
    //初始化viewpager页卡
    private void initPage() {
        List<Fragment> fragments = new ArrayList<>();
        for (HifiveMusicChannelModel model : companyChannelLists) {
            if(model != null) {
                HifiveMusicRadioStationFragment radioStationFragment = new HifiveMusicRadioStationFragment();
                Bundle recommendBundle = new Bundle();
                recommendBundle.putString(HifiveMusicRadioStationFragment.TYPE_ID, model.getChannelId());
                radioStationFragment.setArguments(recommendBundle);
                fragments.add(radioStationFragment);
            }
        }
        HifiveViewPagerAdapter adapter = new HifiveViewPagerAdapter(getChildFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
        int firstPageIndex = 0;
        viewPager.setCurrentItem(firstPageIndex);
    }
    //获取电台列表
    private  void  getRadioStationData(){
        try {
            if (HFLiveApi.getInstance() == null || mContext == null)
                return;
            HFLiveApi.getInstance().getCompanyChannelList(mContext, new DataResponse<List<HifiveMusicChannelModel>>() {
                @Override
                public void errorMsg(@NotNull String string, @org.jetbrains.annotations.Nullable Integer code) {
                    HifiveDialogManageUtil.getInstance().showToast(getActivity(),string);
                }

                @Override
                public void data(@NotNull List<HifiveMusicChannelModel> any) {
//                    Log.e("TAG", "电台数据==" + any);

                    companyChannelLists = any;
                    initMagicIndicator();
                    initPage();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
