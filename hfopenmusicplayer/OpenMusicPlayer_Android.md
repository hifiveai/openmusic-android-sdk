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

下载 [MusicPlayer Android SDK](https://www.baidu.com)

- 将SDK文件加入到libs中
- 在module的build.gradle中与android{}平级下加入
```
 repositories {
       flatDir {
       dirs 'libs'
           }
       }
```

- 在module的build.gradle中的dependencies里加入

```  
implementation(name: 'demo', ext:'aar')//注意这里加入的名字没有后缀名
```


- 添加一下依赖,可根据项目需求本身进行版本选择

```
 api "io.reactivex.rxjava2:rxjava:2.2.10"
    api "io.reactivex.rxjava2:rxandroid:2.1.1"
    api "com.squareup.retrofit2:retrofit:2.9.0"
    api "com.squareup.retrofit2:converter-gson:2.9.0"
    api "com.squareup.retrofit2:adapter-rxjava2:2.9.0"
    api "com.squareup.okhttp3:okhttp:3.9.0"
    api "com.squareup.okhttp3:logging-interceptor:4.0.0"

    api group: 'com.github.bumptech.glide', name: 'glide', version: '4.11.0'
    api 'com.scwang.smartrefresh:SmartRefreshLayout:1.1.3'
    api 'androidx.recyclerview:recyclerview:1.1.0'

    api 'androidx.media:media:1.2.1'
    api 'tv.danmaku.ijk.media:ijkplayer-java:0.8.8'

```
## 二、SDK使用

#### 2.1 参数配置

- 在“AndroidManifest.xml”的“Application”中添加“meta-data”配置项：
```
<meta-data
    android:name="HIFIVE_APPID"
    android:value="注册时申请的APPID" >
</meta-data>
<meta-data
    android:name="HIFIVE_SERVERCODE"
    android:value="注册时申请的SECRET" />
```

-请避免混淆，在Proguard混淆文件中增加以下配置：
```
-dontwarn com.hf.openplayer.**
-keep public class com.hf.openplayer.**{*;}

-dontwarn com.hf.player.**
-keep public class com.hf.player.**{*;}

-dontwarn com.hf.playerkernel.**
-keep public class com.hf.playerkernel.**{*;}

-dontwarn com.hfopen.sdk.**
-keep public class com.hfopen.sdk.**{*;}

-dontwarn com.hfopemmusic.sdk.**
-keep public class com.hfopemmusic.sdk.**{*;}

```

## 二、播放器UISDK使用

建议在应用一启动就初始化，例如Application中
```
HFOpenMusicPlayer.getInstance()
                .registerApp(..)
                .apply();
```

可以配置更多的功能：

```
HFOpenMusicPlayer.getInstance()
                .registerApp(..)
                .setListenType(..)
                .setDebug(..)
                .setMaxBufferSize(..)
                .setUseCache(..)
                .apply();
```

#### 2.1 初始化播放器

>  注意：初始化需要clientId，建议在用户登录之后进行初始化

```
HFOpenMusicPlayer.getInstance().registerApp(Application context,String clientId);
```
参数  | 必填  |描述|
---|---|---
activity | 是| 上下文 |
clientId | 是| 用户唯一标识（公司自有的用户ID）。|

#### 2.2 置音乐授权类型
```
HFOpenMusicPlayer.getInstance().setListenType(String type);
```
参数  | 必填  | 描述| 可选值 |
---|---|--- | ---
type | 是| 音乐授权类型 | 详见[音乐授权类型] |

**音乐授权类型**

名称  |  值  |
---|---
BGM音乐播放 | Traffic | 
音视频作品BGM音乐播放 | UGC| 
K歌音乐播放 | K | 


#### 2.3 是否打印打印log
```
 HFOpenMusicPlayer.getInstance().setDebug(boolean debug)         

```
参数  | 必填  |描述| 默认值 |
---|---|--- | ---
debug | 是| 是否打印log | false |

#### 2.4 配置断线重连
```
 HFOpenMusicPlayer.getInstance().setReconnect(boolean reconnect) 
```
参数  | 必填  |描述| 默认值 |
---|---|--- | ---
reconnect | 是| 是否断线重连 | true |

#### 2.5 配置最大缓冲大小
```
 HFOpenMusicPlayer.getInstance().setMaxBufferSize(long bytes) 

```
参数  | 必填  |描述| 默认值 |
---|---|--- | ---
bytes | 是| 最大缓冲大小字节数 | 200 * 1024 |

#### 2.6 配置是否开启缓存，默认关闭

> 需要存储权限
```
 HFOpenMusicPlayer.getInstance().serUseCache(boolean useCache)
```

#### 2.7 初始化播放器服务

> 建议在application初始化
```
 HFOpenMusicPlayer.getInstance().apply()
```

参数  | 必填  |描述| 默认值 |
---|---|--- | ---
useCache | 是| 是否开启缓存 | false |

#### 2.8 显示播放器方法

> 在需要播放器的界面使用
- 由于列表UI基于DialogFragment开发，需要使用UI的Activity务必继承FragmentActivity或FragmentActivity的子类。
- 使用列表UI前请确保SDK已初始化。

```
HFOpenMusicPlayer.getInstance().showPlayer(FragmentActivity activity);
或者
HFOpenMusicPlayer.getInstance().showPlayer(FragmentActivity activity,int marginTop,int marginBottom);

```

| 参数 | 是否必填 | 描述 |
|----------|:--------|:-------- |
| activity | 是 | 上下文 |
| marginTop | 否 | 播放器可拖拽范围上限（默认为0，表示可以拖至屏幕最上方） |
| marginBottom | 否 | 播放器可拖拽范围下限（默认为0，表示可以拖至屏幕最底部，只对未显示音乐列表弹窗时有效） |

#### 2.9 移除播放器

```
HFOpenMusicPlayer.getInstance().removePlayer();
```
