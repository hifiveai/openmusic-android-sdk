package com.hifive.sdk.demo.player;

import android.app.Activity;
import android.widget.FrameLayout;

import androidx.fragment.app.FragmentActivity;

public interface IFloatingView {

    HifivePlayerManger remove();

    HifivePlayerManger add(FragmentActivity context);

    HifivePlayerManger attach(Activity activity);

    HifivePlayerManger attach(FrameLayout container);

    HifivePlayerManger detach(Activity activity);

    HifivePlayerManger detach(FrameLayout container);
}
