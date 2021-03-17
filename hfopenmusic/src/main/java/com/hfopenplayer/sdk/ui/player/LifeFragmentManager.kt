package com.hfopenplayer.sdk.ui.player

import android.app.Activity
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.hfopenplayer.sdk.listener.LifeCycleListener
import com.hfopenplayer.sdk.ui.LifeFragment

/**
 * 生命周期管理类
 */
class LifeFragmentManager {
    private var tag:String = this::class.java.name

    companion object{
        fun getInstances():LifeFragmentManager{
            return LifeFragmentManager()
        }
    }


    fun addLifeListener(activity: Activity?, tag:String?, lifeListener: LifeCycleListener?) {
        if(tag != null){
            this.tag = tag
        }
        if(activity != null){
            val fragment: LifeFragment? = getLifeListenerFragment(activity)
            fragment?.addLifeListener(lifeListener)
        }
    }

    private fun getLifeListenerFragment(activity: Activity):LifeFragment? {
        val manager: FragmentManager?
        when(activity){
            is AppCompatActivity -> manager = activity.supportFragmentManager
            is FragmentActivity -> manager = activity.supportFragmentManager
            else -> manager = null
        }
        if(manager == null) {
            Log.w(tag,"暂时仅支持获取FragmentActivity和AppCompatActivity的生命周期")
            return null
        }
        return getLifeListenerFragment(manager)
    }

    //添加空白fragment
    private fun getLifeListenerFragment(manager:FragmentManager):LifeFragment{
        var fragment:LifeFragment? = manager.findFragmentByTag(tag) as LifeFragment?
        if (fragment == null) {
            fragment = LifeFragment()
            manager.beginTransaction().add(fragment, tag).commitAllowingStateLoss()
        }
        return fragment
    }
}