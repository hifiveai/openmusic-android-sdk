package com.hf.playerkernel.manager;

import android.os.Build;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.hf.playerkernel.model.AudioBean;
import com.hf.playerkernel.service.PlayService;


public class MediaSessionManager {

    private static final String TAG = "MediaSessionManager";
    private static final long MEDIA_SESSION_ACTIONS = PlaybackStateCompat.ACTION_PLAY
            | PlaybackStateCompat.ACTION_PAUSE
            | PlaybackStateCompat.ACTION_PLAY_PAUSE
            | PlaybackStateCompat.ACTION_SKIP_TO_NEXT
            | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
            | PlaybackStateCompat.ACTION_STOP
            | PlaybackStateCompat.ACTION_SEEK_TO;

    private PlayService mPlayService;
    private boolean isOpenNotification;
    private MediaSessionCompat mMediaSession;

    /**
     * 音乐的控制逻辑都在PlayService服务中，将service实例传递过来，与MediaSessionManager进行交互
     * @param playService
     * @param isOpenNotification
     */
    public MediaSessionManager(PlayService playService, boolean isOpenNotification) {
        mPlayService = playService;
        this.isOpenNotification = isOpenNotification;
        setupMediaSession();
    }

    /**
     *初始化并激活MediaSession
     */
    private void setupMediaSession() {
        mMediaSession = new MediaSessionCompat(mPlayService, TAG);
        //FLAG_HANDLES_MEDIA_BUTTONS 控制媒体按钮  FLAG_HANDLES_TRANSPORT_CONTROLS 控制传输命令
        mMediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS | MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS);
        mMediaSession.setCallback(callback);
        //MediaSession必须激活才能去使用，当你不希望使用MediaSession的是，可以设置false
        mMediaSession.setActive(isOpenNotification);
    }

    /**
     * 更新播放状态，播放/暂停/拖动进度条时调用
     */
    public void updatePlaybackState() {
        if(!isOpenNotification) return;
        int state = (mPlayService.playback.isPlaying() ||
                mPlayService.playback.isPreparing()) ? PlaybackStateCompat.STATE_PLAYING :
                PlaybackStateCompat.STATE_PAUSED;
        //第三个参数必须为1，否则锁屏上面显示的时长会有问题
        mMediaSession.setPlaybackState(
                //监听的事件（播放，暂停，上一曲，下一曲）
                new PlaybackStateCompat.Builder()
                        .setActions(MEDIA_SESSION_ACTIONS)
                        .setState(state, mPlayService.playback.getCurrentPosition(), 1)
                        .build());
    }

    /**
     * 播放歌曲时，需要更新屏幕上的歌曲信息
     * @param music     歌曲信息
     */
    public void updateMetaData(AudioBean music) {
        if(!isOpenNotification) return;
        if (music == null ) {
            mMediaSession.setMetadata(null);
            return;
        }

        MediaMetadataCompat.Builder metaData = new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, music.getTitle())
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, music.getArtist())
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, music.getAlbum())
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST, music.getArtist())
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, music.getDuration());
                //.putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, CoverLoader.getInstance().loadThumbnail(music));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            metaData.putLong(MediaMetadataCompat.METADATA_KEY_NUM_TRACKS,
                    HFPlayer.getMusicList().size());
        }
        mMediaSession.setMetadata(metaData.build());
    }

    public MediaSessionCompat.Token getMediaSession() {
        return mMediaSession.getSessionToken();
    }

    public void release() {
        mMediaSession.setCallback(null);
        mMediaSession.setActive(false);
        mMediaSession.release();
    }

    /**
     * 初始化回调，用于监听锁屏界面上的按钮事件
     */
    private MediaSessionCompat.Callback callback = new MediaSessionCompat.Callback() {
        @Override
        public void onPlay() {
            mPlayService.playback.playPause();
        }

        @Override
        public void onPause() {
            mPlayService.playback.playPause();
        }

        @Override
        public void onSkipToNext() {
            mPlayService.playback.next();
        }

        @Override
        public void onSkipToPrevious() {
            mPlayService.playback.prev();
        }

        @Override
        public void onStop() {
            mPlayService.playback.stop();
        }

        @Override
        public void onSeekTo(long pos) {
            mPlayService.playback.seekTo((int) pos);
        }
    };
}
