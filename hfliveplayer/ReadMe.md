

# HFLivePlayer Android SDK 使用指南
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
##### 2.3.1 下载SDK库文件
<!--//https://gitlab.ilongyuan.cn/hifive/open_api_android_sdk/-/blob/master/hifivesdk/ReadMe.md-->
 - 下载HFLivePlayer的[Android SDK包]()；
 - 如果您的项目使用了AndroidX，请下载此[Android SDK包]()；

##### 2.3.2 手动集成SDK包

- 将SDK文件加入到libs中
- 在项目级的build.gradle中 buildscript的dependencies里面引入kotlin

```
 buildscript {
     dependencies {
         classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.41"
     }
 }
```
- 在module的build.gradle中的dependencies里引入aar文件
```
api fileTree(include: ['*.jar','*.aar'], dir: 'libs')
```
- 同步后可以在External Libraries中查看新加入的包

##### 2.3.3 引入第三方依赖包

- 因为本SDK需要第三方库支持，所以必须添加一下依赖,可根据项目需求本身进行版本选择
```
api "io.reactivex.rxjava2:rxjava:2.2.10"
api "io.reactivex.rxjava2:rxandroid:2.1.1"
api "com.squareup.retrofit2:retrofit:2.6.0"
api "com.squareup.retrofit2:adapter-rxjava2:2.6.0"
api "com.squareup.okhttp3:okhttp:4.0.0"
api "com.squareup.okhttp3:logging-interceptor:4.0.0"
api 'com.scwang.smartrefresh:SmartRefreshLayout:1.1.3'
api 'com.github.bumptech.glide:glide:4.8.0'
api "com.android.support:recyclerview-v7:28.0.0"

//如果您的项目使用了AndroidX，升级对应的support依赖库
api group: 'com.github.bumptech.glide', name: 'glide', version: '4.11.0'
api 'androidx.recyclerview:recyclerview:1.1.0'
```


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
-dontwarn com.hfliveplayer.sdk.**
-keep public class com.hfliveplayer.sdk.**{*;}

-dontwarn com.hifive.sdk.**
-keep public class com.hifive.sdk.**{*;}
```

#### 3.2 SDK初始化
建议在应用一启动就初始化，例如Application中

```
HFLiveApi.Companion.registerApp(Application context);
```

#### 3.3 播放器UI使用

##### 3.3.1 注意事项
- 由于播放器UI基于DialogFragment开发，需要使用播放器UI的Activity务必继承FragmentActivity或FragmentActivity的子类。
- 使用播放器UI前请确保SDK已初始化，并完成用户登录操作。
```
 HFLiveApi.Companion.getInstance().memberLogin(context: Context, memberName: String, memberId: String, societyName: String?, societyId: String?, headerUrl: String?, gender: String?, birthday: String?, location: String?, favoriteSinger: String?, phone: String?, dataResponse: DataResponse)
```

##### 3.3.2 使用

- 显示播放器方法

```
HFLivePlayer.getInstance().add(FragmentActivity activity);
或者
HFLivePlayer.getInstance().add(FragmentActivity activity,int marginTop,int marginBottom);
参数说明：marginTop表示播放器可移动范围距离屏幕上方的间距。（默认为0，表示可以拖至屏幕最上方）
         marginBottom表示播放器可移动范围距离屏幕下方的间距。（默认为0，表示可以拖至屏幕最底部，只对未显示音乐列表弹窗时有效）
注意事项：播放器UI相关接口都是基于登录后的操作，为了正常使用，请确保已完成登录操作。
```
- 关闭播放器方法
```
HFLivePlayer.getInstance().remove();
```













