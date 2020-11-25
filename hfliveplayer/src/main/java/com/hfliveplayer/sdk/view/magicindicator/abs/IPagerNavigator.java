package com.hfliveplayer.sdk.view.magicindicator.abs;

/**
 * 抽象的ViewPager导航器
 * Created by huchao
 */
public interface IPagerNavigator {

    ///////////////////////// ViewPager的3个回调
    void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

    void onPageSelected(int position);

    void onPageScrollStateChanged(int state);
    /////////////////////////

    /**
     * 当IPagerNavigator被添加到MagicIndicator时调用
     */
    void onAttachToMagicIndicator();

    /**
     * 当IPagerNavigator从MagicIndicator上移除时调用
     */
    void onDetachFromMagicIndicator();

}
