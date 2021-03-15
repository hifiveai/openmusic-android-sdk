package com.hfopenplayer.sdk.ui;

import java.util.Observable;

/**
 * 数据变化需要更新相应页面UI的被观察者
 *
 * @author huchao
 */
public class HifiveUpdateObservable extends Observable {
    public  void  postNewPublication(int type) {
        //通知所有观察者
        setChanged();
        notifyObservers(type);
    }
}
