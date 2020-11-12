package com.hifive.sdk.demo.view.magicindicator.abs;

/**
 * 可测量内容区域的指示器标题
 * Created by huchao
 */
public interface IMeasurablePagerTitleView extends IPagerTitleView {
    int getContentLeft();

    int getContentTop();

    int getContentRight();

    int getContentBottom();
}
