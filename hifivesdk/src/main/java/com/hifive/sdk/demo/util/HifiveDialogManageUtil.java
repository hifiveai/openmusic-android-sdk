package com.hifive.sdk.demo.util;


import androidx.fragment.app.DialogFragment;

import com.hifive.sdk.demo.model.HifiveMusicModel;
import com.hifive.sdk.demo.ui.HifiveUpdateObservable;

import java.util.ArrayList;
import java.util.List;

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
    private static HifiveDialogManageUtil singleManage;
    private HifiveDialogManageUtil(){

    }
    public static synchronized HifiveDialogManageUtil getInstance(){//同步控制,避免多线程的状况多创建了实例对象
        if (singleManage==null){
            singleManage = new HifiveDialogManageUtil();//在需要的时候在创建
        }
        return singleManage;
    }
    private  List<DialogFragment> dialogFragments; //维护当前所打开的dialog
    //关闭所有dialog
    public  void CloseDialog(){
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
    public void addDialog(DialogFragment dialogFragment){
        if(dialogFragments == null ){
            dialogFragments = new ArrayList<>();
        }
        if(dialogFragment != null)
            dialogFragments.add(dialogFragment);
    }
    //移除dialog
    public  void removeDialog(int position){
        if(dialogFragments != null && dialogFragments.size() > position){
            try {
                dialogFragments.remove(position);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    public  HifiveMusicModel playMusic;//维护当前所播放的音乐

    public HifiveMusicModel getPlayMusic() {
        return playMusic;
    }

    public void setPlayMusic(HifiveMusicModel playMusic) {
        this.playMusic = playMusic;
    }

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
    //添加某一首歌曲到当前播放中
    public void addCurrentSingle(HifiveMusicModel musicModel){
        if(musicModel == null ){
            return;
        }
        if(playMusic != null && playMusic.getId()== musicModel.getId()){//播放的同一首=歌
            return;
        }
        playMusic = musicModel;
        if(currentList != null && currentList.size() >0){
            if(!currentList.contains(musicModel)){
                currentList.add(0,musicModel);
            }
        }else{
            currentList = new ArrayList<>();
            currentList.add(musicModel);
        }
        updateObservable.postNewPublication(UPDATEPALY);
    }
    //按顺序播放某一首歌
    public void setCurrentSingle(HifiveMusicModel musicModel){
        if(musicModel == null ){
            return;
        }
        if(playMusic != null
                && playMusic.getId() == musicModel.getId()){//播放的同一首=歌
            return;
        }
        playMusic = musicModel;
        updateObservable.postNewPublication(UPDATEPALY);
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



}
