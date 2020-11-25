


## 一、方案对比

这里提供两种参考：
- jar包
- arr包

他们两者是有区别的，区别在于：
1. 打包出来的位置不同

```
AS低版本
jar: /build/intermediates/bundles/debug(release)/classes.jar
AS高版本
jar: /build/intermediates/packaged-classes/release/classes.jar

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
##### 2.3.1 集成准备
本SDK依赖于open_api_android_sdk，使用前请确保已正确集成open_api_android_sdk。

- 参考文档 https://gitlab.ilongyuan.cn/hifive/open_api_android_sdk/-/blob/master/hifivesdk/ReadMe.md

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
## 三、SDK使用

#### 3.1 日志输出与相关说明

控制SDK相关信息打印
```
SDK默认开启debug模式，输出日志可在控制台进行查看。

开发接口以kotlin的方式输出
```

#### 3.2 SDK初始化
建议在应用一启动就初始化，例如Application中

```
HFLiveApi.Companion.registerApp(Application context, String APP_ID,String SECRET );

```

#### 3.3 播放器UI使用

##### 3.3.1 注意事项
- 使用播放器UI前请确保SDK已初始化，并完成登录操作。
```
 登录操作调用sdk中的memberLogin(...)或者societyLogin(...)

```
- 由于播放器UI和SDK基于androidX工程开发，防止兼容性问题，推荐项目升级到AndroidX，并且需要使用播放器UI的Activity务必继承FragmentActivity或FragmentActivity的子类。
##### 3.3.2 使用

- 请在使用播放器UI的Activity中的生命周期中完成如下操作

```
   @Override
    protected void onStart() {
        HFLivePlayer.getInstance().attach(this);
        super.onStart();
    }
    @Override
    protected void onStop() {
        HFLivePlayer.getInstance().detach(this);
        super.onStop();
    }
    @Override
    protected void onDestroy() {
        HFLivePlayer.getInstance().destory();
        super.onDestroy();
    }

```

- 显示播放器方法

```
    HFLivePlayer.getInstance().add(FragmentActivity);
    或者
    HFLivePlayer.getInstance().add(FragmentActivity,int marginTop,int marginBottom);
    参数说明
      marginTop表示播放器可移动范围距离屏幕上方的间距。（默认为0，表示可以拖至屏幕最上方）
      marginBottom表示播放器可移动范围距离屏幕下方的间距。（默认为0，表示可以拖至屏幕最底部，只对未显示音乐列表弹窗时有效）
      
```
- 关闭播放器方法
```
    HFLivePlayer.getInstance().remove();

```













