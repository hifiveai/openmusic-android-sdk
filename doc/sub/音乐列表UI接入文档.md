# 《音乐列表UI》接口文档
[TOC]
## 初始化SDK
>  注意：初始化需要clientId，建议在用户登录之后进行初始化

```java
HFOpenApi.registerApp(Application context,String clientId);
```
| 参数     | 必填 | 描述                               |      |
| -------- | ---- | ---------------------------------- | ---- |
| activity | 是   | 上下文                             |      |
| clientId | 是   | 用户唯一标识（公司自有的用户ID）。 |      |

## 设置SDK全局回调
```java
HFOpenApi.configCallBack(HFOpenCallback callback);
```
| 参数     | 必填 | 描述    |      |
| -------- | ---- | ------- | ---- |
| callback | 是   | SDK回调 |      |


## 显示音乐列表
<font color='#FF0000'>由于列表UI基于DialogFragment开发，需要使用UI的Activity务必继承FragmentActivity或FragmentActivity的子类。</font>
```java
HFOpenMusic.getInstance().showOpenMusic(FragmentActivity activity);
```
| 参数     | 必填 | 描述   |      |
| -------- | ---- | ------ | ---- |
| activity | 是   | 上下文 |      |

## 隐藏音乐列表
```java
HFOpenMusic.getInstance().closeOpenMusic();
```

## 设置音乐授权类型
```java
HFOpenMusic.getInstance().setListenType(String type);
```
| 参数 | 必填 | 描述         | 可选值             |      |
| ---- | ---- | ------------ | ------------------ | ---- |
| type | 是   | 音乐授权类型 | 详见[音乐授权类型] |      |

**音乐授权类型**

| 名称                  | 值      |      |
| --------------------- | ------- | ---- |
| BGM音乐播放           | Traffic |      |
| 音视频作品BGM音乐播放 | UGC     |      |
| K歌音乐播放           | K       |      |


## 事件监听
```java
HFOpenMusic.getInstance().setPlayListen(HFPlayMusicListener listener);
```


| 参数     | 必填 | 描述     |      |
| -------- | ---- | -------- | ---- |
| listener | 是   | 监听接口 |      |

HFPlayMusicListener说明

```java
interface HFPlayMusicListener {
    /**
     * 音乐详情
     */
    void onPlayMusic(MusicRecord musicDetail,String url);

    /**
     * 播放列表删除所有歌曲
     */
    void onStop();

    /**
     * 关闭界面
     */
    void onCloseOpenMusic();
}
```

MusicRecord说明
| 字段        | 类型    |   说明   |
| -----------| ------- | ---- |
| musicId    | String  |  音乐ID    |
| musicName  | String  |    音乐名  |
| albumId    | String  |   专辑ID   |
| albumName  | String  |   专辑名   |
| artist     | List    |   表演者   |
| composer   | List    |  作曲者    |
| cover      | List    |     封面图 |
| duration   | Int     |   时长   |
