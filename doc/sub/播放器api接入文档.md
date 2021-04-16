# 《播放器API》接口文档
[TOC]
## 初始化
建议在Application中调用
```java
HFPlayer.init(this).apply()
```

可以配置更多的功能：
```java
 HFPlayer.init(this)
        .setDebug(..)         //是否打印log,默认关闭
        .setReconnect(..)     //配置是否断线重连,默认开启
        .setMaxBufferSize(..) //配置最大缓冲大小，默认270 * 1024 b
        .serUseCache(..)      //配置是否开启缓存，默认关闭
        .apply();
```

##  播放器根据 url 播放
```java
HFPlayer.with().playWhitUrl(String url);
```

| 参数 | 必填 | 描述     |      |
| ---- | ---- | -------- | ---- |
| url  | 是   | 音乐地址 |      |

## 播放器暂停
```java
HFPlayer.with().pause();
```
## 播放器结束播放
```java
HFPlayer.with().stop();
```

## 播放器播放或暂停(根据当前状态)
```java
HFPlayer.with().playPause();
```

## 播放器调节进度
```java
HFPlayer.with().seekTo(int progress);
```
| 参数     | 必填 | 描述     |      |
| -------- | ---- | -------- | ---- |
| progress | 是   | 播放进度 |      |

## 退出播放器
```java
HFPlayer.with().quit();
```

## 获取音量
```java
HFPlayer.with().getVolume();
```

## 获取最大音量
```java
HFPlayer.with().getMaxVolume();
```

## 设置音量
```java
HFPlayer.with().setVolume(int volume);
```
| 参数   | 必填 | 描述              |      |
| ------ | ---- | ----------------- | ---- |
| volume | 是   | 音量(0~MaxVolume) |      |

## 获取时长
```java
HFPlayer.with().getDuration();
```

## 设置速率
```java
HFPlayer.with().setSpeed(float speed);
```
| 参数  | 必填 | 描述        |      |
| ----- | ---- | ----------- | ---- |
| speed | 是   | 速率(0.5~2) |      |

## 设置播放器状态回调
```java
HFPlayer.with().setOnPlayEventListener(OnPlayerEventListener listener);
```
| 参数     | 必填 | 描述       |      |
| -------- | ---- | ---------- | ---- |
| listener | 是   | 播放器回调 |      |


## 播放器回调

###  播放状态改变
```java
    void onPlayStateChanged(int state);
```

- 回调参数
  
| 参数  | 描述                      |
| ----- | ------------------------- |
| state | 播放器状态,详见播放状态码 |


### 更新进度
```java
    void onProgressUpdate(int progress,int duration);
```
- 回调参数
  
| 参数          | 描述                  |
| ------------- | --------------------- |
| progress      | 当前播放进度          |
| totalDuration | 资源总播放时长 ，毫秒 |


### 缓冲百分比

```java
    void onBufferingUpdate(int percent);
```
- 回调参数
  
| 参数    | 描述       |
| ------- | ---------- |
| percent | 缓冲百分比 |


##  播放状态码

| 状态              | 状态码 | 状态描述                                    |
| ----------------- | :----- | :------------------------------------------ |
| STATE_IDLE        | 100    | 默认状态                                    |
| STATE_PREPARING   | 101    | 正在准备中                                  |
| STATE_PLAYING     | 102    | 正在播放中                                  |
| STATE_BUFFERING   | 103    | 缓冲中                                      |
| STATE_PAUSE       | 104    | 暂停状态                                    |
| STATE_ERROR       | 105    | 出错                                        |
| STATE_ERROR_AUDIO | 106    | 资源出错，一般是url有问题或者数据格式不支持 |