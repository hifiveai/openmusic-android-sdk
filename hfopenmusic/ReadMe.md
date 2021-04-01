

# Android SDK 使用指南
## 一、方案对比

这里提供两种参考：
- jar包
- arr包

他们两者是有区别的，区别在于：
1. 打包出来的位置不同

```
AS低版本
jar: /build/intermediates/packaged-classes/release/classes.jar
AS高版本
jar: /build/intermediates/aar_main_jar/release/classes.jar
aar: /build/outputs/aar/libraryname.aar
```
2. jar 中只包含了class文件与清单文件,
aar中除了包含jar中的class文件还包含工程中使用的所有资源，class及res资源文件全部包含
3. 使用方式不同
4. 本sdk选择aar的集成方式进行开发


## 二、SDK集成

#### 2.1 系统支持

Android 5.0以上

minSdkVersion    : 21

targetSdkVersion : 30

#### 2.2运行环境

建议使用Android Studio 3.4 以上版本进行编译。

#### 2.3集成SDK

##### 2.3.1 手动集成

- 下载SDK

support版本SDK[点击下载]()

AndroidX适配版本SDK[点击下载]()

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


- 因为本SDK需要第三方网络库支持，所以必须添加一下依赖,可根据项目需求本身进行版本选择
```
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

<!--##### 2.3.2 自动集成-->

<!-- - 在Module的build.gradle文件中添加配置：-->
<!--```-->
<!--repositories {-->
<!--    maven {-->
<!--        url 'http://172.16.52.62:8081/repository/hifive_repository'-->
<!--    }-->
<!--}-->
<!--```-->
<!--- 在Module的build.gradle文件中添加依赖：-->
<!--```-->
<!--api "com.hifive.sdk:liveplayer:1.0.0"-->
<!--```-->
<!--- AndroidX请切换为以下依赖：-->
<!--```-->
<!--api "com.hifive.sdk:liveplayer-androidx:1.0.0"-->
<!--```-->

<!--- 因项目基于Kotlin开发，在项目级的build.gradle中 buildscript的dependencies里面引入kotlin-->
<!--```-->
<!-- buildscript {-->
<!--     dependencies {-->
<!--         classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.41"-->
<!--     }-->
<!-- }-->
<!--```-->
<!--- 同步后可以在External Libraries中查看新加入的包-->

## 三、SDK使用

#### 3.1 参数配置

- 在“AndroidManifest.xml”的“Application”中添加“meta-data”配置项：
```
<meta-data
    android:name="HIFIVE_APPID"
    android:value="注册时申请的APPID" >
</meta-data>
<meta-data
    android:name="HIFIVE_SECRET"
    android:value="注册时申请的SECRET" />
```

-请避免混淆HFLivePlayer，在Proguard混淆文件中增加以下配置：
```
-dontwarn com.hfopen.sdk.**
-keep public class com.hfopen.sdk.**{*;}

-dontwarn com.hfopemmusic.sdk.**
-keep public class com.hfopemmusic.sdk.**{*;}
```

#### 3.2 SDK初始化
>  注意：初始化需要clientId，建议在用户登录之后进行初始化

```
HFOpenApi.registerApp(Application context,String clientId);
```
参数  | 必填  |描述|
---|---|---
activity | 是| 上下文
clientId | 是| 用户唯一标识（公司自有的用户ID）。|

#### 3.3 设置SDK全局回调
```
HFOpenApi.configCallBack(HFOpenCallback callback);
```
参数  | 必填  |描述|
---|---|---
callback | 是| SDK回调


#### 3.4 列表UI使用

##### 3.4.1 注意事项
- 由于列表UI基于DialogFragment开发，需要使用UI的Activity务必继承FragmentActivity或FragmentActivity的子类。
- 使用列表UI前请确保SDK已初始化。

##### 3.4.2 播放器使用

- 显示列表方法

```
HFOpenMusic.getInstance().showOpenMusic(FragmentActivity activity);
```
参数  | 必填  |描述|
---|---|---
activity | 是| 上下文

- 关闭列表方法
```
HFOpenMusic.getInstance().closeOpenMusic();
```

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













