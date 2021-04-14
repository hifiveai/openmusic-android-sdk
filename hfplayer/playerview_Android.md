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

- 在module的build.gradle中的dependencies里加入

```  
implementation(name: 'demo', ext:'aar')//注意这里加入的名字没有后缀名
```


- 添加一下依赖,可根据项目需求本身进行版本选择

```
api 'androidx.appcompat:appcompat:1.2.0'
api 'androidx.media:media:1.2.1'
api group: 'com.github.bumptech.glide', name: 'glide', version: '4.11.0'

```



## 二、播放器UISDK使用
#### 2.1 初始化播放器

显示播放器方法

```
HFPlayer.getInstance().showPlayer(FragmentActivity activity);
或者
HFPlayer.getInstance().showPlayer(FragmentActivity activity,int marginTop,int marginBottom);

```

| 参数 | 是否必填 | 描述 |
|----------|:--------|:-------- |
| activity | 是 | 上下文 |
| marginTop | 否 | 播放器可拖拽范围上限（默认为0，表示可以拖至屏幕最上方） |
| marginBottom | 否 | 播放器可拖拽范围下限（默认为0，表示可以拖至屏幕最底部，只对未显示音乐列表弹窗时有效） |

#### 2.2 移除播放器

```
HFPlayer.getInstance().removePlayer();
```
#### 2.3 设置歌曲标题

```
 HFPlayer.setTitle(String title)
```
#### 2.4 设置版本信息（true 主版本  false 伴奏）
```
HFPlayer.setMajorVersion(Boolean isMajor)
```
#### 2.5 设置封面图
```
HFPlayer.setCover(String coverUrl)
```
#### 2.6 播放歌曲
```
HFPlayer.playWithUrl(String url)
```
#### 2.7 设置状态监听
```
HFPlayer.setListener(HFPlayerViewListener listener)
```
#### 2.8 收缩播放器
```
HFPlayer.getInstance().foldPlayer();
```
#### 2.9 展开播放器
```
HFPlayer.getInstance().expandedPlayer();
```

#### 2.9 停止播放音乐
```
HFPlayer.stopPlay()
```