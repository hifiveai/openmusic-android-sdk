package com.hfliveplayer.sdk.view.magicindicator.abs;

/**
 * 自定义滚动状态，消除对ViewPager的依赖
 * Created by huchao
 */

public interface ScrollState {
    int SCROLL_STATE_IDLE = 0;
    int SCROLL_STATE_DRAGGING = 1;
}
