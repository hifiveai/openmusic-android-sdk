

# Android SDK 接入指南

**修订记录** 

|    日期    | 版本 | 说明       |  作者  |
| :--------: | :--: | ---------- | :----: |
| 2021-04-08 | 4.1.1  | 《通用API》组合搜索接口新增两个字段：SearchFiled，SearchSmart | 刘松豪 |



[TOC]
## 一、SDK集成

### 系统支持

Android 5.0以上

minSdkVersion    : 21

targetSdkVersion : 30

### 运行环境

建议使用Android Studio 3.4 以上版本进行编译。

### 集成SDK

- 下载SDK

- 将SDK文件加入到libs中
- 在module的build.gradle中与android{}平级下加入

```java
 repositories {
       flatDir {
       dirs 'libs'
           }
       }
```
- 在module的build.gradle中的dependencies里加入

```java
   implementation(name: 'demo', ext:'aar')//注意这里加入的名字没有后缀名
```


- 因为本SDK需要第三方网络库支持，所以必须添加一下依赖,可根据项目需求本身进行版本选择
```java
api "io.reactivex.rxjava2:rxjava:2.2.10"
api "io.reactivex.rxjava2:rxandroid:2.1.1"
api "com.squareup.retrofit2:retrofit:2.6.0"
api "com.squareup.retrofit2:adapter-rxjava2:2.6.0"
api "com.squareup.okhttp3:okhttp:4.0.0"
api "com.squareup.okhttp3:logging-interceptor:4.0.0"
api "com.squareup.okhttp3:okhttp:4.9.0"
api "com.squareup.okhttp3:logging-interceptor:4.9.0"

//使用以下第三方UI依赖库
api 'com.scwang.smartrefresh:SmartRefreshLayout:1.1.3'
api 'com.github.bumptech.glide:glide:4.8.0'
api "com.android.support:recyclerview-v7:28.0.0"
//AndroidX适配使用以下第三方UI依赖库
api 'com.github.bumptech.glide:glide:4.11.0'
api 'com.scwang.smartrefresh:SmartRefreshLayout:1.1.3'
api 'androidx.recyclerview:recyclerview:1.1.0'
```


## 二、参数配置

- 在“AndroidManifest.xml”的“Application”中添加“meta-data”配置项：
```xml
<meta-data
    android:name="HIFIVE_APPID"
    android:value="注册时申请的APPID" >
</meta-data>
<meta-data
    android:name="HIFIVE_SERVERCODE"
    android:value="注册时申请的SECRET" />
```

- 请避免混淆，在Proguard混淆文件中增加以下配置：
```java
-dontwarn com.hfopen.sdk.**
-keep public class com.hfopen.sdk.**{*;}

-dontwarn com.hfopemmusic.sdk.**
-keep public class com.hfopemmusic.sdk.**{*;}
```

## 三、接口说明

<font color='#FF0000'>SDK有五种场景接入方式，开发者可以选其中一种方式接入。</font>

### 接入方式一（带播放器UI和音乐列表UI）
如下图例子：
![Alt text](https://k3-images-test.oss-cn-beijing.aliyuncs.com/M2.png)
[接口文档](./sub/播放器和音乐列表接入文档.html?target="_blank")

### 接入方式二（只有播放器UI）
如下图例子：
![Alt text](https://k3-images-test.oss-cn-beijing.aliyuncs.com/M3.png)
[接口文档](./sub/播放器UI接入文档.html)

### 接入方式三（只有音乐列表UI）
如下图例子：
![Alt text](https://k3-images-test.oss-cn-beijing.aliyuncs.com/M4.png)
[接口文档](./sub/音乐列表UI接入文档.html)

### 接入方式四（通用API接口，无UI）
<font color='#FF0000'>该接入方式适用于开发者自行开发UI，业务逻辑与SDK数据交互。</font>
[接口文档](./sub/通用api接入文档.html)

### 接入方式五（播放器API接口，无UI）
<font color='#FF0000'>该接入方式适用于开发者自行开发UI，通过获取SDK播放器状态的数据去处理UI、业务逻辑。</font>
[接口文档](./sub/播放器api接入文档.html)





## 四、API状态码

SDK错误码

| 错误码 | 错误描述 | 解决方案 |
|----------|:--------|:-------- |
| 10000 | 未初始化ADK | 初始化SDK |
| 10001 | 网络错误 | 请检查网络连接 |
| 10002 | 连接超时 | 请检查网络连接 |
| 10003 | http异常 | 重试 |
| 10097 | JSON转换失败 | 重试 |
| 10098 | JSON格式不匹配 | 检查Json |
| 10099 | 未知错误 |  |


成功响应码

| 响应码 | 描述 |
|----------|:--------|
| 200 | success |













