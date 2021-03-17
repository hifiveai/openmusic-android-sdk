package com.hfopenmusic.sdk.util;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.hfopen.sdk.entity.MusicRecord;
import com.hfopen.sdk.entity.Version;
import com.hfopen.sdk.manager.HFOpenApi;
import com.hfopenmusic.sdk.ui.HifiveUpdateObservable;
import com.hfopenmusic.sdk.ui.player.HifivePlayerView;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 弹窗管理工具
 *
 * @author huchao
 */
public class HifiveDialogManageUtil {
    public static String field = "album,artist,musicTag";
    public HifiveUpdateObservable updateObservable;
    public static final int UPDATEPALY = 1;//通知相关页面更新当前播放歌曲
    public static final int UPDATEPALYLIST = 2;//通知相关页面更新当前播放列表
    public static final int UPDATELIKELIST = 3;//通知相关页面更新喜欢列表
    public static final int UPDATEKARAOKLIST = 4;//通知相关页面更新k歌列表
    public static final int PALYINGMUSIC = 5;//通知播放器开始播放新歌曲
    public static final int PALYINGCHANGEMUSIC = 6;//通知播放器改变播放模式
    public static final int STOPPALYINGMUSIC = 7;//通知播放器停止播放
    private  static volatile  HifiveDialogManageUtil singleManage;
    private Toast toast;
    private HifiveDialogManageUtil(){

    }
    public static  HifiveDialogManageUtil getInstance(){
        if (singleManage == null) {
            synchronized (HifiveDialogManageUtil.class) {
                if (singleManage == null) {
                    singleManage = new HifiveDialogManageUtil();
                }
            }
        }
        return singleManage;
    }
    public static List<DialogFragment> dialogFragments;//维护当前所打开的dialog

    //关闭所有dialog
    public void CloseDialog(){
            if(dialogFragments != null && dialogFragments.size() >0){
                for(DialogFragment dialogFragment:dialogFragments){
                    if(dialogFragment != null){
                        dialogFragment.dismissAllowingStateLoss();
                    }
                }
                dialogFragments.clear();
            }
            dialogFragments = null;
    }
    //添加dialog
    public void addDialog(DialogFragment dialogFragment){
        if(dialogFragments == null )
            dialogFragments = new ArrayList<>();
        if(dialogFragment != null)
            dialogFragments.add(dialogFragment);

    }
    //移除dialog
    public  void removeDialog(int position){
        try {
            if(dialogFragments != null && dialogFragments.size() > position){
                dialogFragments.remove(position);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public MusicRecord playMusic;//维护当前所播放的音乐，方便当前播放显示播放效果。

    public MusicRecord getPlayMusic() {
        return playMusic;
    }

    public void setPlayMusic(MusicRecord playMusic) {
        this.playMusic = playMusic;
    }
    //因为同一首歌原声版和伴奏版的歌名可能不一样，所以需要同时维护两个对象，方便切换播放模式时，改变歌曲名字，
    public MusicRecord playMusicDetail;//主版本的详情
    public MusicRecord accompanyDetail;//伴奏的详情
    private  List<MusicRecord> currentList;//维护当前播放的音乐列表
    public List<MusicRecord> getCurrentList() {
        return currentList;
    }

    //更新当前播放列表
    public void updateCurrentList(Activity activity,List<MusicRecord> musicModels){
        if(musicModels == null || musicModels.size() == 0)
            return;
        currentList = new ArrayList<>();
        currentList.addAll(musicModels);
        setCurrentPlay(activity,currentList.get(0));
        updateObservable.postNewPublication(UPDATEPALYLIST);
    }
    //设置当前播放的歌曲
    public void setCurrentPlay(Activity activity,MusicRecord musicModel){
        try {
            if(musicModel == null ){
                return;
            }
            cleanPlayMusic(false);
            playMusic = musicModel;
            updateObservable.postNewPublication(UPDATEPALY);
            getMusicDetail(activity,musicModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //添加某一首歌曲到当前播放中mediaType	类型：1-k歌；2-听歌
    public void addCurrentSingle(Activity activity, MusicRecord musicModel){
        if(musicModel == null ){
            return;
        }
        if(playMusic != null && playMusic.getMusicId().equals(musicModel.getMusicId())){//播放的同一首=歌
            return;
        }
        getMusicDetail(activity,musicModel);
    }

    //获取歌曲详情成功后才添加到播放列表以及播放
    private void updatePlayList(MusicRecord musicModel){
        cleanPlayMusic(false);
        playMusic = musicModel;
        updateObservable.postNewPublication(UPDATEPALY);
        if(currentList != null && currentList.size() >0){
            if(!currentList.contains(playMusic)){
                currentList.add(0,playMusic);
                updateObservable.postNewPublication(UPDATEPALYLIST);
            }
        }else{
            currentList = new ArrayList<>();
            currentList.add(playMusic);
            updateObservable.postNewPublication(UPDATEPALYLIST);
        }
    }

    /**
     *  清空播放缓存数据，重置播放效果
     * @param isStop 是否重置播放器播放效果
     * @author huchao
     */
    public void cleanPlayMusic(boolean isStop){
        playMusic = null;
        playMusicDetail = null;
        accompanyDetail = null;
        deleteFile();
        if(isStop)
            updateObservable.postNewPublication(UPDATEPALY);
    }
    //按顺序播放上一首歌
    public void playLastMusic(Activity activity){
        try {
            if(currentList!= null && currentList.size() >1){
                int positon = currentList.indexOf(playMusic);//获取当前播放歌曲的序号
                if(positon<0)
                    return;
                if(positon != 0){//不是第一首
                    setCurrentPlay(activity,currentList.get(positon-1));
                }else{
                    setCurrentPlay(activity,currentList.get(currentList.size()-1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //按顺序播放下一首歌
    public void playNextMusic(Activity activity){
        try {
            if(currentList != null && currentList.size()>0){
                int positon = currentList.indexOf(playMusic);//获取当前播放歌曲的序号
                if(positon != (currentList.size()-1)){//不是最后一首
                    setCurrentPlay(activity,currentList.get(positon+1));
                }else{
                    setCurrentPlay(activity,currentList.get(0));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private  List<MusicRecord> userSheetModels;//维护用户歌单列表

    public List<MusicRecord> getUserSheetModels() {
        return userSheetModels;
    }

    //获取歌曲详情
    public void getMusicDetail(final Activity activity, final MusicRecord musicModel){
        try {
            if (HFOpenApi.getInstance() == null)
                return;



//            HFLiveApi.getInstance().getMusicDetail(activity, musicModel.getMusicId(), null,
//                    mediaType, null, null, field, new DataResponse<HifiveMusicDetailModel>() {
//                        @Override
//                        public void errorMsg(@NotNull String string, @Nullable Integer code) {
//                            showToast(activity,string);
//                            //454 版权过期
//                            if(code != null && code == 454 && currentList != null){
//                                currentList.remove(musicModel);
//                            }
//                            if(currentList != null && currentList.size() >0 && code != null && code == 454 ){
//                                playNextMusic(activity);
//                            }else{
//                                cleanPlayMusic(true);
//                            }
//                        }
//                        @Override
//                        public void data(@NotNull HifiveMusicDetailModel any) {
//                            updatePlayList(musicModel);
//                            updatePlayMusicDetail(any);
//                            updateObservable.postNewPublication(PALYINGMUSIC);
//                        }
//                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //显示自定义toast信息
    public void showToast(final Activity activity,final String msg){
        if(activity != null){
            activity.runOnUiThread(new Runnable() {
                @SuppressLint("ShowToast")
                @Override
                public void run() {
                    if(toast != null){
                        toast.cancel();
                        toast = null;
                    }
                    toast = Toast.makeText(activity,msg,Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
        }
    }
    //清除缓存数据
    public  void clearData() {
        playMusic = null;
        playMusicDetail = null;
        accompanyDetail = null;
        currentList = null;
        userSheetModels =null;
        deleteFile();
    }
    //删除伴奏文件
    private void deleteFile() {
        File file = HifivePlayerView.musicFile;
        //切歌后删除上一首歌下载的伴奏
        if(file != null && file.exists() && file.isFile()) {
            if(file.delete()) Log.e("TAG", "文件删除成功");
            else Log.e("TAG", "文件删除失败");
        }
    }
}
