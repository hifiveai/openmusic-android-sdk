package com.hifive.sdk.demo.util;


import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hifive.sdk.R;
import com.hifive.sdk.demo.model.HifiveMusicDetailModel;
import com.hifive.sdk.demo.model.HifiveMusicModel;
import com.hifive.sdk.demo.model.HifiveMusicRecordModel;
import com.hifive.sdk.demo.model.HifiveMusicSheetModel;
import com.hifive.sdk.demo.model.HifiveMusicUserSheetModel;
import com.hifive.sdk.demo.ui.HifiveUpdateObservable;
import com.hifive.sdk.hInterface.DataResponse;
import com.hifive.sdk.manager.HiFiveManager;
import com.tsy.sdk.myokhttp.util.LogUtils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 弹窗管理工具
 *
 * @author huchao
 */
public class HifiveDialogManageUtil {
    public HifiveUpdateObservable updateObservable;
    public static final int UPDATEPALY = 1;//通知相关页面更新当前播放歌曲
    public static final int UPDATEPALYLIST = 2;//通知相关页面更新当前播放列表
    public static final int UPDATELIKELIST = 3;//通知相关页面更新喜欢列表
    public static final int UPDATEKARAOKLIST = 4;//通知相关页面更新k歌列表
    public static final int PALYINGMUSIC = 5;//通知播放器开始播放新歌曲
    private static HifiveDialogManageUtil singleManage;
    private HifiveDialogManageUtil(){

    }
    public static synchronized HifiveDialogManageUtil getInstance(){//同步控制,避免多线程的状况多创建了实例对象
        if (singleManage==null){
            singleManage = new HifiveDialogManageUtil();//在需要的时候在创建
        }
        return singleManage;
    }
    public List<DialogFragment> dialogFragments;//维护当前所打开的dialog
    //关闭所有dialog
    public void CloseDialog(){
        try {
            if(dialogFragments != null && dialogFragments.size() >0){
                for(DialogFragment dialogFragment:dialogFragments){
                    if(dialogFragment != null)
                        dialogFragment.dismiss();
                }
                dialogFragments = null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //添加dialog
    public void addDialog(DialogFragment dialogFragment){
        if(dialogFragments == null ){
            dialogFragments = new ArrayList<>();
        }
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
    public HifiveMusicModel playMusic;//维护当前所播放的音乐

    public HifiveMusicModel getPlayMusic() {
        return playMusic;
    }

    public void setPlayMusic(HifiveMusicModel playMusic) {
        this.playMusic = playMusic;
    }

    public HifiveMusicDetailModel playMusicDetail;//维护当前所播放的音乐的详情

    public  List<HifiveMusicModel> currentList;//维护当前播放的音乐列表
    public List<HifiveMusicModel> getCurrentList() {
        return currentList;
    }

    public void setCurrentList(List<HifiveMusicModel> currentList) {
        this.currentList = currentList;
    }
    //更新当前播放列表
    public void updateCurrentList(List<HifiveMusicModel> currentList){
        if(currentList == null ){
            currentList = new ArrayList<>();
        }
        this.currentList = currentList;
        updateObservable.postNewPublication(UPDATEPALYLIST);
    }
    //添加某一首歌曲到当前播放中mediaType	类型：1-k歌；2-听歌
    public void addCurrentSingle(Activity activity,HifiveMusicModel musicModel,String mediaType){
        if(musicModel == null ){
            return;
        }
        if(playMusic != null && playMusic.getMusicId().equals(musicModel.getMusicId())){//播放的同一首=歌
            return;
        }
        playMusic = musicModel;
        if(currentList != null && currentList.size() >0){
            if(!currentList.contains(musicModel)){
                currentList.add(0,musicModel);
                updateObservable.postNewPublication(UPDATEPALYLIST);
            }
        }else{
            currentList = new ArrayList<>();
            currentList.add(musicModel);
            updateObservable.postNewPublication(UPDATEPALYLIST);
        }
        updateObservable.postNewPublication(UPDATEPALY);
        uploadPlayRecord(activity);
        getMusicDetail(activity,musicModel,mediaType);
    }
    //按顺序播放上一首歌
    public void playLastMusic(Activity activity){
        if(currentList!= null){
            int positon = currentList.indexOf(playMusic);//获取当前播放歌曲的序号
            if(positon != 0){//不是第一首
                setCurrentPlay(activity,currentList.get(positon-1));
            }
        }
    }
    //按顺序播放下一首歌
    public void playNextMusic(Activity activity){
        if(currentList != null){
            int positon = currentList.indexOf(playMusic);//获取当前播放歌曲的序号
            if(positon != (currentList.size()-1)){//不是最后一首
                setCurrentPlay(activity,currentList.get(positon+1));
            }else{
                setCurrentPlay(activity,currentList.get(0));
            }
        }
    }
    //设置当前播放的歌曲
    public void setCurrentPlay(Activity activity,HifiveMusicModel musicModel){
        if(musicModel == null ){
            return;
        }
        playMusic = musicModel;
        updateObservable.postNewPublication(UPDATEPALY);
        uploadPlayRecord(activity);
        getMusicDetail(activity,musicModel,"2");
    }

    private  List<HifiveMusicUserSheetModel> userSheetModels;//维护用户歌单列表

    public List<HifiveMusicUserSheetModel> getUserSheetModels() {
        return userSheetModels;
    }

    public void setUserSheetModels(List<HifiveMusicUserSheetModel> userSheetModels) {
        this.userSheetModels = userSheetModels;
    }
    //根据歌单名称获取会员歌单id
    public long getUserSheetIdByName(String name) {
        if(!TextUtils.isEmpty(name) && userSheetModels != null && userSheetModels.size() >0){
            for(HifiveMusicUserSheetModel musicUserSheetModel : userSheetModels){
                if(musicUserSheetModel.getSheetName().contains(name)){
                    return musicUserSheetModel.getSheetId();
                }
            }
        }
        return -1;
    }
    private  List<HifiveMusicModel> likeList;//维护喜欢的音乐列表
    public List<HifiveMusicModel> getLikeList() {
        return likeList;
    }

    public void setLikeList(List<HifiveMusicModel> likeList) {
        this.likeList = likeList;
    }
    //添加某一首歌曲到我喜欢的列表中
    public void addLikeSingle(HifiveMusicModel musicModel){
        if(musicModel == null ){
            return;
        }
        if(likeList != null && likeList.size() >0){
            if(!likeList.contains(musicModel)){
                likeList.add(0,musicModel);
            }
        }else{
            likeList = new ArrayList<>();
            likeList.add(musicModel);
        }
        updateObservable.postNewPublication(UPDATELIKELIST);
    }
    private  List<HifiveMusicModel> KaraokeList;//维护K歌的音乐列表
    public List<HifiveMusicModel> getKaraokeList() {
        return KaraokeList;
    }

    public void setKaraokeList(List<HifiveMusicModel> karaokeList) {
        KaraokeList = karaokeList;
    }
    //添加某一首歌曲到我的K歌的列表中
    public void addKaraokeSingle(HifiveMusicModel musicModel){
        if(musicModel == null ){
            return;
        }
        if(KaraokeList != null && KaraokeList.size() >0){
            if(!KaraokeList.contains(musicModel)){
                KaraokeList.add(0,musicModel);
            }
        }else{
            KaraokeList = new ArrayList<>();
            KaraokeList.add(musicModel);
        }
        updateObservable.postNewPublication(UPDATEKARAOKLIST);
    }

    //获取歌曲详情type表示是获取伴奏版本信息还是主版本信息
    public void getMusicDetail(final Activity activity, final HifiveMusicModel musicModel,String mediaType){
        HiFiveManager.Companion.getInstance().getMusicDetail(activity, musicModel.getMusicId(), null,
                mediaType,null, new DataResponse() {
                    @Override
                    public void errorMsg(@NotNull String string, @org.jetbrains.annotations.Nullable Integer code) {
                        showToast(string,activity);
                    }
                    @Override
                    public void data(@NotNull Object any) {
                        Log.e("TAG", "==音乐详情=="+any);
                        playMusicDetail = JSON.parseObject(String.valueOf(any), HifiveMusicDetailModel.class);
                        addPlayRecord();
                        updateObservable.postNewPublication(PALYINGMUSIC);
                    }
                });
    }


    //显示自定义toast信息
    public void showToast(String msg, Activity activity){
        if(activity != null){
            final Toast  toast = Toast.makeText(activity,msg,Toast.LENGTH_SHORT);
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    toast.show();
                }
            });
        }
    }

    private List<HifiveMusicRecordModel> recordModels;//维护播放记录的列表

    //添加一条播放记录
    private void addPlayRecord() {
        if(playMusicDetail == null)
            return;
        if(recordModels == null){
            recordModels = new ArrayList<>();
        }
        if(playMusicDetail.getIsMajor() == 1){
            recordModels.add(new HifiveMusicRecordModel(playMusicDetail.getRecordId(),0,"2"));
        }else{
            recordModels.add(new HifiveMusicRecordModel(playMusicDetail.getRecordId(),0,"1"));
        }
    }
    //更新播放进度
    public void updatePlayProgress(int duration) {
        if(recordModels == null || playMusicDetail == null){
            return;
        }
        for(HifiveMusicRecordModel recordModel:recordModels){
            if(playMusicDetail.getRecordId() == recordModel.getRecordId()){
                recordModel.setDuration(duration);
                break;
            }
        }
    }
    //上报播放记录
    private void uploadPlayRecord(Activity mContext) {
        final List<HifiveMusicRecordModel> recordModelList = new ArrayList<>();
        if(recordModels == null || recordModels.size() ==0){
            return ;
        }
        recordModelList.addAll(recordModels);
        final int[] number = {0};//异步请求完成的数量
        final List<HifiveMusicRecordModel> models = new ArrayList<>();//上报成功的记录
        for(final HifiveMusicRecordModel recordModel:recordModelList){
            Log.e("TAG",recordModel.getRecordId()+"==getDuration=="+recordModel.getDuration());
            HiFiveManager.Companion.getInstance().updateMusicRecord(mContext,String.valueOf(recordModel.getRecordId()),
                    String.valueOf(recordModel.getDuration()), recordModel.getMediaType(),new DataResponse() {
                        @Override
                        public void errorMsg(@NotNull String string, @org.jetbrains.annotations.Nullable Integer code) {
                            number[0]++;
                            if(number[0] == recordModelList.size()){//异步请求都完成
                                recordModels.removeAll(models);
                            }
                        }
                        @Override
                        public void data(@NotNull Object any) {
                            number[0]++;
                            models.add(recordModel);
                            if(number[0] == recordModelList.size()){//异步请求都完成
                                recordModels.removeAll(models);
                            }
                        }
                    });
        }
    }

}
