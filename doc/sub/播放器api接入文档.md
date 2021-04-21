# 《播放器API》接口文档
[TOC]
## 初始化SDK
建议在Application中调用
```java
HFPlayerApi.init(this).apply()
```

可以配置更多的功能：
```java
 HFPlayerApi.init(this)
        .setDebug(..)         //是否打印log,默认关闭
        .setReconnect(..)     //配置是否断线重连,默认开启
        .setMaxBufferSize(..) //配置最大缓冲大小，默认270 * 1024 b
        .serUseCache(..)      //配置是否开启缓存，默认关闭
        .apply();
```

##  播放器设置播放url
设置后会马上播放
```java
HFPlayerApi.with().playWhitUrl(String url);
```

| 参数 | 必填 | 描述     |      |
| ---- | ---- | -------- | ---- |
| url  | 是   | 音乐地址 |      |

## 播放器暂停
```java
HFPlayerApi.with().pause();
```
## 播放器播放或暂停(根据当前状态)
```java
HFPlayerApi.with().playPause();
```
## 播放器停止播放
```java
HFPlayerApi.with().stop();
```


## 销毁播放器
```java
HFPlayerApi.with().quit();
```

## 获取音量
```java
HFPlayerApi.with().getVolume();
```

## 获取最大音量
```java
HFPlayerApi.with().getMaxVolume();
```

## 设置音量
```java
HFPlayerApi.with().setVolume(int volume);
```
| 参数   | 必填 | 描述              |      |
| ------ | ---- | ----------------- | ---- |
| volume | 是   | 音量(0~MaxVolume) |      |


## 设置播放位置
```java
HFPlayerApi.with().seekTo(int progress);
```
| 参数     | 必填 | 描述     |      |
| -------- | ---- | -------- | ---- |
| progress | 是   | 播放进度 |      |


## 设置播放速率
```java
HFPlayerApi.with().setSpeed(float speed);
```
| 参数  | 必填 | 描述        |      |
| ----- | ---- | ----------- | ---- |
| speed | 是   | 速率(0.5~2) |      |

## 设置播放器回调
```java
HFPlayerApi.with().setOnPlayEventListener(new HFPlayerEventListener() {
    @Override
    public void onPlayStateChanged(int state) {
        //播放器状态改变 （状态值详见播放器状态码）
    }

    @Override
    public void onProgressUpdate(int progress, int duration) {
        //播放进度更新
    }

    @Override
    public void onBufferingUpdate(int percent) {
        //缓冲进度更新
    }
});
```
| 参数     | 必填 | 描述       |      |
| -------- | ---- | ---------- | ---- |
| listener | 是   | 播放器回调 |      |




##  播放状态码

| 状态              | 状态码 | 状态描述                                    |
| ----------------- | :----- | :------------------------------------------ |
| MusicPlayAction.STATE_IDLE        | 100    | 默认状态                                    |
| MusicPlayAction.STATE_PREPARING   | 101    | 正在准备中                                  |
| MusicPlayAction.STATE_PLAYING     | 102    | 正在播放中                                  |
| MusicPlayAction.STATE_BUFFERING   | 103    | 缓冲中                                      |
| MusicPlayAction.STATE_PAUSE       | 104    | 暂停状态                                    |
| MusicPlayAction.STATE_ERROR       | 105    | 出错                                        |
| MusicPlayAction.STATE_ERROR_AUDIO | 106    | 资源出错，一般是url有问题或者数据格式不支持 |