
#不混淆所有的com.czy.bean包下的类和这些类的所有成员变量
-keep class com.hifive.sdk.entity.** { *; }

 # dagger
-dontwarn dagger.**
-dontwarn com.squareup.javapoet.**
-dontwarn com.google.common.**

 # Retrofit
-dontwarn  okhttp3.*
-dontnote okhttp3.**
-keep class okhttp3.* { *; }
-dontnote retrofit2.**
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

# RxJava RxAndroid
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
           long producerIndex;
           long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
            rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
            rx.internal.util.atomic.LinkedQueueNode consumerNode;
}


###########################以下是AndroidStudio自带的混淆配置协议###############################

#表示混淆时不使用大小写混合类名
-dontusemixedcaseclassnames
#表示不跳过library中的非public的类
-dontskipnonpubliclibraryclasses
#打印混淆的详细信息
-verbose

# Optimization is turned off by default. Dex does not like code run
# through the ProGuard optimize and preverify steps (and performs some
# of these optimizations on its own).
-dontoptimize
##表示不进行校验,这个校验作用 在java平台上的
-dontpreverify
# Note that if you want to enable optimization, you cannot just
# include optimization flags in your own project configuration file;
# instead you will need to point to the
# "proguard-android-optimize.txt" file instead of this one from your
# project.properties file.

-keepattributes *Annotation*
-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService

# For native methods, see http://proguard.sourceforge.net/manual/examples.html#native
-keepclasseswithmembernames class * {
    native <methods>;
}

# keep setters in Views so that animations can still work.
# see http://proguard.sourceforge.net/manual/examples.html#beans
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}

# We want to keep methods in Activity that could be used in the XML attribute onClick
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

# For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclassmembers class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator CREATOR;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

# The support library contains references to newer platform versions.
# Don't warn about those in case this app is linking against an older
# platform version.  We know about them, and they are safe.
-dontwarn android.support.**
# Understand the @Keep support annotation.
-keep class android.support.annotation.Keep
-keep @android.support.annotation.Keep class * {*;}
-keepclasseswithmembers class * {
    @android.support.annotation.Keep <methods>;
}
-keepclasseswithmembers class * {
    @android.support.annotation.Keep <fields>;
}
-keepclasseswithmembers class * {
    @android.support.annotation.Keep <init>(...);
}


###########################以下是需要手动的混淆配置协议###############################


-libraryjars 'E:\tool\Java\jdk1.8.0_271\jre\lib\rt.jar'
#-libraryjars "C:\Users\admin\AppData\Local\Android\sdk\platforms\android-26\android.jar"
# 注意：以上两个路径需要将以上路径是自己jar包的位置，需要根据自己情况进行修改，如果报重复配置的错误，注释掉即可

#代码迭代优化的次数，默认5
-optimizationpasses 5
#混淆时不会产生形形色色的类名
-dontusemixedcaseclassnames


#忽略警告
-ignorewarnings
#以下是不需要混淆的文件
 -keep class com.android.sdk.demo.LogUtils{
     *;
 }
 -keep class com.android.sdk.demo.StorageUtils{
     *;
 }



