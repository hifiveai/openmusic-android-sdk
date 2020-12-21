

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
##### 2.3.1 自动集成

 - 在Module的build.gradle文件中添加配置：
```
repositories {
    maven {
        url 'http://172.16.52.62:8081/repository/hifive_repository'
    }
}
```
- 在Module的build.gradle文件中添加依赖：
```
api "com.hifive.sdk:liveplayer:1.0.0"
```
- AndroidX请切换为以下依赖：
```
api "com.hifive.sdk:liveplayer-androidx:1.0.0"
```

- 因项目基于Kotlin开发，在项目级的build.gradle中 buildscript的dependencies里面引入kotlin
```
 buildscript {
     dependencies {
         classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.41"
     }
 }
```
- 同步后可以在External Libraries中查看新加入的包

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
HFLiveApi.registerApp(Application context,HFLiveCallback callback);
```
callback将在SDK异常时返回错误信息，可以根据错误信息做相应的措施。

#### 3.3 播放器UI使用

##### 3.3.1 注意事项
- 由于播放器UI基于DialogFragment开发，需要使用播放器UI的Activity务必继承FragmentActivity或FragmentActivity的子类。
- 使用播放器UI前请确保SDK已初始化，并完成用户登录操作。
```
 HFLiveApi.getInstance().memberLogin()
```
用户登录参数参考[open_api_android_sdk](https://gitlab.ilongyuan.cn/hifive/open_api_android_sdk/-/blob/master/hifivesdk/ReadMe.md)文档

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













