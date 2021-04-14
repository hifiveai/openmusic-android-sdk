# 《播放器UI》接口文档
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
        .setMaxBufferSize(..) //配置最大缓冲大小，默认200 * 1024 b
        .serUseCache(..)      //配置是否开启缓存，默认关闭
        .apply();
```
## 初始化播放器

<font color='#FF0000'>由于列表UI基于DialogFragment开发，需要使用UI的Activity务必继承FragmentActivity或FragmentActivity的子类，在需要播放器的界面使用。</font>

```java
HFOpenMusicPlayer.getInstance().showPlayer(FragmentActivity activity);
//或者
HFOpenMusicPlayer.getInstance().showPlayer(FragmentActivity activity,int marginTop,int marginBottom);

```

| 参数         | 是否必填 | 描述                                                         |
| ------------ | :------- | :----------------------------------------------------------- |
| activity     | 是       | 上下文                                                       |
| marginTop    | 否       | 播放器可拖拽范围上限（默认为0，表示可以拖至屏幕最上方）      |
| marginBottom | 否       | 播放器可拖拽范围下限（默认为0，表示可以拖至屏幕最底部，只对未显示音乐列表弹窗时有效） |

## 移除播放器

```java
HFPlayer.getInstance().removePlayer();
```
## 设置歌曲标题

```java
 HFPlayer.setTitle(String title)
```
## 设置版本信息（true 主版本  false 伴奏）
```java
HFPlayer.setMajorVersion(Boolean isMajor)
```
## 设置封面图
```java
HFPlayer.setCover(String coverUrl)
```
## 播放歌曲
```java
HFPlayer.playWithUrl(String url)
```
## 设置状态监听
```java
HFPlayer.setListener(HFPlayerViewListener listener)
```
## 收缩播放器
```java
HFPlayer.getInstance().foldPlayer();
```
## 展开播放器
```java
HFPlayer.getInstance().expandedPlayer();
```

## 停止播放音乐
```java
HFPlayer.stopPlay()
```