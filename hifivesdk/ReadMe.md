
## 一、SDK集成

#### 1.1 系统支持

Android 5.0以上

minSdkVersion    : 21

targetSdkVersion : 28

#### 1.2运行环境

建议使用Android Studio 3.4 以上版本进行编译。

#### 1.3集成SDK
 提供aar包接入方案

##### 1.3.1 手动集成

- 下载SDK[点击下载]()
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
api "com.squareup.retrofit2:converter-gson:2.6.0"
api "com.squareup.retrofit2:adapter-rxjava2:2.6.0"
api "com.squareup.okhttp3:okhttp:4.9.0"
api "com.squareup.okhttp3:logging-interceptor:4.9.0"
```

<!--##### 1.3.2 自动集成-->

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
<!--api "com.hifive.sdk:api:1.0.0"-->
<!--```-->
<!--- 同步后可以在External Libraries中查看新加入的包-->

## 二、SDK使用

##### 2.1 参数配置

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

-添加混淆，在Proguard混淆文件中增加以下配置：
```
-dontwarn com.hifive.sdk.**
-keep public class com.hifive.sdk.**{*;}
```


##### 2.2 SDK初始化
建议在应用一启动就初始化，例如Application中

```
HFLiveApi.registerApp(Application context);
```

## 三 API文档

> 注意：如下api非必填参数中，如果开发者不想传参数时，应当传入null进行占位

> 注意：数据通过DataResponse接口返回，某些返回结果以string字符串输出，需要开发字自行json转换

> 注意：由于每个接口用到了context跟DataResponse，一个是上下文，一个是回调接口，所以下文不再描述


##### 3.1 请求响应回调

本方法是所有接口调用后统一返回数据的回调，接口里面包含了错误的信息跟请求的数据信息，具体模式如下：
```
interface DataResponse {
       /**
        * sdk返回的错误
        */
       void errorMsg(String msg,int code)

       /**
        * sdk返回的数据
        */
       void data(Object object )
}
```


参数  | 必填  |描述|
---|---|---
msg | | 错误描述
code | | 错误code
object | | 返回的数据（string字符串）


##### 3.2 SDK初始化

```
HFLiveApi.registerApp(Application context);
```
参数  | 必填  |描述|
---|---|---
activity | 是| 上下文


设置SDK全局回调
```
HFLiveApi.configCallBack(HFLiveCallback callback);
```
参数  | 必填  |描述|
---|---|---
callback | 是| SDK回调




##### 3.3 会员登录

```
memberLogin(context: Context, memberName: String, memberId: String, societyName: String?, societyId: String?, headerUrl: String?, gender: String?, birthday: String?, location: String?, favoriteSinger: String?, phone: String?, dataResponse: DataResponse)
```

参数  | 必填  |描述|
---|---|---
memberName | 是| 会员名称
memberId | 是| 外部会员ID
sociatyName | 否|公会名称
societyId | 否| 公会外部ID
headerUrl	 | 否| 头像URL
gender	 | 否| 性别,未知：0，男：1，女：2
birthday	 | 否| 生日
location	 | 否| 经纬度信息，纬度在前(30.779164,103.94547)
favoriteSinger	 | 否| 喜欢的歌手名，多个用英文逗号隔开
phone	 | 否| 手机号


##### 3.4 公会登录接口



```
   societyLogin(
            context: Context,
            societyName: String,
            societyId: String,
            dataResponse: DataResponse)
```
参数  | 必填  |描述|
---|---|---
sociatyName | 是|公会名称
sociatyId | 是| 公会外部ID





##### 3.5 解绑会员

```
unbindingMember(
            context: Context,
            memberId: String,
            societyId: String,
            dataResponse: DataResponse
    )
```
参数  | 必填  |描述|
---|---|---
memberId | 是| 外部会员ID
sociatyId | 是| 外部公会ID





##### 3.6 绑定会员

```
    bindingMember(
            context: Context,
            memberId: String,
            societyId: String,
            dataResponse: DataResponse
    )
```

参数  | 必填  |描述|
---|---|---
memberId	 | 是| 外部会员ID
societyId | 是| 外部公会ID


##### 3.7 注销会员

```
deleteMember(
            context: Context,
            memberId: String,
            dataResponse: DataResponse
    )
```

参数  | 必填  |描述|
---|---|---
 memberId | 是| 会员外部ID


##### 3.8 注销公会

```
deleteSociety(
            context: Context,
            societyId: String,
            dataResponse: DataResponse
    )
```

参数  | 必填  |描述|
---|---|---
societyId | 是| 会员外部ID


#####  3.9 获取商户歌单标签列表


```
getCompanySheetTagList(
            context: Context,
            response: DataResponse
    )
```



##### 3.10 获取商户歌单列表

```
getCompanySheetList(
            context: Context,
            groupId: String?,
            language: String?,
            recoNum: String?,
            type: String?,
            tagIdList: String?,
            field: String?,
            pageSize: String?,
            page: String?,
            response: DataResponse
    )
```

参数  | 必填  |描述|
---|---|---
groupId | 否| 电台id
language | 否| 0-中文,1-英文
recoNum | 否| 推荐音乐数 0～10
type | 否| 歌单类别
tagIdList | 否| 标签Id列表
field | 否| 歌曲信息扩展查询字段，sheetTag,album,musicTag,artist
pageSize | 否| 每页显示条数，默认 10
page | 否| 当前页



##### 3.11 获取商户歌单歌曲列表

```
getCompanySheetMusicList(
            context: Context,
            sheetId: String?,
            language: String?,
            field: String?,
            pageSize: String?,
            page: String?,
            response: DataResponse
    )
```
参数  | 必填  |描述|
---|---|---
sheetId | 否| 歌单Id
language | 否| 0-中文,1-英文
field | 否| 扩展查询字段，album,musicTag,artist
pageSize | 否| 每页显示条数，默认 10
page | 否| 当前页



##### 3.12 获取商户电台列表

```
getCompanyChannelList(
            context: Context,
            response: DataResponse
    )

```



##### 3.13 获取会员歌单列表

```
 getMemberSheetList(
            context: Context,
            page: String?,
            pageSize: String?,
            dataResponse: DataResponse
    )

```

参数  | 必填  |描述|
---|---|---
pageSize | 否| 每页显示条数，默认 10
page | 否| 当前页



##### 3.14 获取会员歌单歌曲列表

```
getMemberSheetMusicList(
            context: Context,
            sheetId: String,
            language: String?,
            field: String?,
            pageSize: String?,
            page: String?,
            dataResponse: DataResponse
    )

```

参数  | 必填  |描述|
---|---|---
sheetId | 否| 歌单Id
language | 否| 0-中文,1-英文
field | 否| 扩展查询字段，album,musicTag,artist
pageSize | 否| 每页显示条数，默认 10
page | 否| 当前页


##### 3.15 获取音乐详情

```
getMusicDetail(
            context: Context,
            musicId: String, language: String?, mediaType: String,
            audioFormat: String?, audioRate: String?, field: String?,
            dataResponse: DataResponse
    )
```

参数  | 必填  |描述|
---|---|---
musicId | 是| 歌曲Id
language | 否| 0-中文,1-英文
mediaType | 是|类型：1-k歌；2-听歌
audioFormat | 否| 文件编码,默认mp3 可选(mp3 / aac)
audioRate | 否| 音质，音乐播放时的比特率，默认320 可选(320 / 128)
field | 否| 扩展查询字段，album,musicTag,artist



##### 3.16 保存会员歌单

```
saveMemberSheet(
            context: Context,
            sheetName: String,
            dataResponse: DataResponse
    )
```

参数  | 必填  |描述|
---|---|---
sheetName | 是| 歌单名称


##### 3.17 保存会员歌单歌曲

```
saveMemberSheetMusic(
            context: Context,
            sheetId: String,
            musicId: String,
            dataResponse: DataResponse
    )
```

参数  | 必填  |描述|
---|---|---
sheetId | 是| 会员歌单Id
musicId | 是| 音乐Id



##### 3.18 删除会员歌单歌曲

```
 abstract fun deleteMemberSheetMusic(
            context: Context,
            sheetId: String,
            musicId: String,
            dataResponse: DataResponse
    )
```

参数  | 必填  |描述|
---|---|---
sheetId | 是| 会员歌单Id
musicId | 是| 音乐Id



##### 3.19 获取会员所有歌单歌曲列表

```
getMemberSheetMusicAll(
            context: Context,
            sheetId: String,
            language: String?,
            field: String?,
            dataResponse: DataResponse
    )
```

参数  | 必填  |描述|
---|---|---
sheetId | 是| 会员歌单Id
language | 否| 0-中文,1-英文
field | 否| 扩展查询字段，album,musicTag,artist




##### 3.20 更新播放记录

```
updateMusicRecord(
            context: Context,
            recordId: String,
            duration: String,
            mediaType: String,
            dataResponse: DataResponse
    )
```

参数  | 必填  |描述|
---|---|---
recordId | 是| 播放记录Id
duration | 是| 播放记录时长
mediaType | 是| 播放记录类型 1-k歌；2-听歌




##### 3.21 获取配置列表

```
getConfigList(
            context: Context,
            dataResponse: DataResponse
    )
```



##### 3.22 所有歌曲中进行搜索

```
getMusicList(
            context: Context,
            searchId: String,
            keyword: String?,
            language: String?,
            field: String?,
            pageSize: String?,
            page: String?,
            dataResponse: DataResponse
    )
```

参数  | 必填  |描述|
---|---|---
searchId | 是| 搜索类型Id
keyword | 否| 搜索关键字
language | 否| 0-中文,1-英文
field | 否| 扩展查询字段，album,musicTag,artist
pageSize | 否| 每页显示条数，默认 10
page | 否| 当前页



##### 3.23 获取搜索历史记录

```
getSearchRecordList(
            context: Context,
            pageSize: String?,
            page: String?,
            dataResponse: DataResponse
    )
```

参数  | 必填  |描述|
---|---|---
pageSize | 否| 每页显示条数，默认 10
page | 否| 当前页


##### 3.24 清空历史记录

```
 deleteSearchRecord(
            context: Context,
            dataResponse: DataResponse
    )
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













