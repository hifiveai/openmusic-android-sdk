package com.hifive.sdk.demo.util;

import androidx.fragment.app.DialogFragment;

import com.hifive.sdk.demo.model.HifiveMusicModel;

import java.util.ArrayList;
import java.util.List;
/**
 * 弹窗管理工具
 *
 * @author huchao
 */
public class HifiveDialogManageUtil {

    public static long playId = 2;//维护当前所播放的音乐id
    public static List<HifiveMusicModel> currentList;//维护当前播放的音乐列表
    public static List<DialogFragment> dialogFragments;
    //关闭所有dialog
    public static void StopDialog(){
        if(dialogFragments != null && dialogFragments.size() >0){
            for(DialogFragment dialogFragment:dialogFragments){
                if(dialogFragment != null)
                    try {
                        dialogFragment.dismiss();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
            }
        }
    }
    //添加dialog
    public static void addDialog(DialogFragment dialogFragment){
        if(dialogFragments == null ){
            dialogFragments = new ArrayList<>();
        }
        if(dialogFragment != null)
            dialogFragments.add(dialogFragment);
    }
    //移除dialog
    public static void removeDialog(int position){
        if(dialogFragments != null && dialogFragments.size() > position){
            try {
                dialogFragments.remove(position);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


}
