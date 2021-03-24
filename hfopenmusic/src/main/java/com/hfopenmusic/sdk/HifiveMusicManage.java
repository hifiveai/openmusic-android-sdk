package com.hfopenmusic.sdk;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import com.hfopen.sdk.entity.HQListen;
import com.hfopen.sdk.entity.MusicRecord;
import com.hfopen.sdk.hInterface.DataResponse;
import com.hfopen.sdk.manager.HFOpenApi;
import com.hfopen.sdk.rx.BaseException;
import com.hfopenmusic.sdk.ui.HifiveUpdateObservable;
import com.hfopenmusic.sdk.player.HFLivePlayer;

import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

/**
 * 弹窗管理工具
 *
 * @author huchao
 */
public class HifiveMusicManage {
    public HifiveUpdateObservable updateObservable;
    public static final int UPDATEPALY = 1;//通知相关页面更新当前播放歌曲
    public static final int UPDATEPALYLIST = 2;//通知相关页面更新当前播放列表
    public static final int CHANGEMUSIC = 5;//通知播放器开始播放新歌曲
    private  static volatile HifiveMusicManage singleManage;
    private Toast toast;
    private MusicRecord playMusic;//维护当前所播放的音乐，方便当前播放显示播放效果。
    private HQListen playMusicDetail;//歌曲播放信息
    private List<MusicRecord> currentList;//维护当前播放的音乐列表

    private HifiveMusicManage(){

    }
    public static HifiveMusicManage getInstance(){
        if (singleManage == null) {
            synchronized (HifiveMusicManage.class) {
                if (singleManage == null) {
                    singleManage = new HifiveMusicManage();
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

    //添加Observer
    public void addObserver(Observer o){
        if(updateObservable == null )
            updateObservable = new HifiveUpdateObservable();
        updateObservable.addObserver(o);
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


    public void setPlayMusic(MusicRecord playMusic) {
        this.playMusic = playMusic;
    }

    public MusicRecord getPlayMusic() {
        return playMusic;
    }
    public HQListen getPlayMusicDetail() {
        return playMusicDetail;
    }

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

    //获取歌曲详情
    public void getMusicDetail(final Activity activity, final MusicRecord musicModel){
        try {
            if (HFOpenApi.getInstance() == null)
                return;

            HFOpenApi.getInstance().hqListen(HFLivePlayer.listenType + "HQListen", musicModel.getMusicId(), null, null, new DataResponse<HQListen>() {
                @Override
                public void onError(@NotNull BaseException e) {
                    if(currentList != null && currentList.size() >0){
                        playNextMusic(activity);
                    }else{
                        cleanPlayMusic(true);
                    }
                }

                @Override
                public void onSuccess(HQListen hqListen, @NotNull String s) {
                    updatePlayList(musicModel);
                    playMusicDetail = hqListen;
                    updateObservable.postNewPublication(CHANGEMUSIC);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        currentList = null;
    }
}
