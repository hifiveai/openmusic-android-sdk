# Android SDK 使用指南

## 一、SDK集成
#### 1.1 系统支持

Android 5.0以上

minSdkVersion    : 19

targetSdkVersion : 30

#### 1.2运行环境

建议使用Android Studio 3.4 以上版本进行编译。

#### 1.3集成SDK

##### 1.3.1 手动集成

- 下载SDK

下载 [HFPlayer Android SDK](https://www.baidu.com)

- 将SDK文件加入到libs中
- 在module的build.gradle中与android{}平级下加入
```
 repositories {
       flatDir {
       dirs 'libs'
           }
       }
```

- 添加一下依赖,可根据项目需求本身进行版本选择

```
api 'androidx.appcompat:appcompat:1.2.0'
api 'androidx.media:media:1.2.1'

```

## 二、SDK使用
#### 2.1 初始化播放器

建议在应用一启动就初始化，例如Application中

```
HFPlayer.init(this).apply()

```

可以配置更多的功能：

```
 HFPlayer.init(this)
        .setDebug(..)         //是否打印log,默认关闭
        .setReconnect(..)     //配置是否断线重连,默认开启
        .setMaxBufferSize(..) //配置最大缓冲大小，默认200 * 1024 b
        .apply();
```
#### 2.2 播放器接口列表

#####  根据 url 播放
```
HFPlayer.with().playWhitUrl(String url);
```

参数  | 必填  |描述|
---|---|---
url | 是| 音乐地址

##### 暂停
```
HFPlayer.with().pause();
```
##### 结束播放
```
HFPlayer.with().stop();
```

##### 播放或暂停(根据当前状态)
```
HFPlayer.with().playPause();
```

##### 调节进度
```
HFPlayer.with().seekTo(int progress);
```
参数  | 必填  |描述|
---|---|---
progress | 是| 播放进度

##### 退出播放器
```
HFPlayer.with().quit();
```

##### 获取音量
```
HFPlayer.with().getVolume();
```

##### 获取最大音量
```
HFPlayer.with().getMaxVolume();
```

##### 设置音量
```
HFPlayer.with().setVolume(int volume);
```
参数  | 必填  |描述|
---|---|---
volume | 是| 音量(0~MaxVolume)

##### 获取时长
```
HFPlayer.with().getDuration();
```

##### 设置速率
```
HFPlayer.with().setSpeed(float speed);
```
参数  | 必填  |描述|
---|---|---
speed | 是| 速率(0.5~2)

##### 设置播放器状态回调
```
HFPlayer.with().setOnPlayEventListener(OnPlayerEventListener listener);
```
参数  | 必填  |描述|
---|---|---
listener | 是| 播放器回调


#### 2.3 播放器回调

##### 2.3.1 播放状态改变
```
    void onPlayStateChanged(int state);
```
    
- 回调参数
  
| 参数 | 描述 |
|---|---|
| state | 播放器状态,详见播放状态码 |


##### 2.3.2 更新进度
```
    void onProgressUpdate(int progress,int duration);
```
- 回调参数
  
| 参数 | 描述 |
|---|---|
| progress | 当前播放进度 |
| totalDuration | 资源总播放时长 ，毫秒|


##### 2.3.3 缓冲百分比

```
    void onBufferingUpdate(int percent);
```
- 回调参数
  
| 参数 | 描述 |
|---|---|
| percent | 缓冲百分比 |


#### 2.4 播放状态码

| 状态 | 状态码 | 状态描述 |
|----------|:--------|:-------- |
| STATE_IDLE | 100 | 默认状态 |
| STATE_PREPARING | 101 | 正在准备中 |
| STATE_PLAYING | 102 | 正在播放中 |
| STATE_BUFFERING | 103 | 缓冲中 |
| STATE_PAUSE | 104 | 暂停状态 |
| STATE_ERROR | 105 | 出错 |
| STATE_ERROR_AUDIO | 106 | 资源出错，一般是url有问题或者数据格式不支持 |