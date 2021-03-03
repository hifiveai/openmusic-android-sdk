
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
HFOpenApi.registerApp(Application context);
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
       void errorMsg(BaseException exception)

       /**
        * sdk返回的数据
        */
       void data(Object data )
}
```


参数  | 必填  |描述|
---|---|---
msg | | 错误描述
taskId | | 任务id
code | | 错误code
data | | 返回的数据


##### 3.2 SDK初始化

```
HFOpenApi.registerApp(Application context);
```
参数  | 必填  |描述|
---|---|---
activity | 是| 上下文


设置SDK全局回调
```
HFOpenApi.configCallBack(HFOpenCallback callback);
```
参数  | 必填  |描述|
---|---|---
callback | 是| SDK回调




##### 3.3 获取Token

```
baseLogin(Nickname: String?,
            Gender: String?,
            Birthday: String?,
            Location: String?,
            Education: String?,
            Profession: String?,
            IsOrganization: String?,
            Reserve: String?,
            FavoriteSinger: String?,
            FavoriteGenre: String?,
            response: DataResponse<LoginBean>
    )
```

参数  | 必填  |描述| 可选值|
---|---|---|---
Nickname | 否| 昵称 |
Gender | 否| 性别，默认0 | 0:未知,1:男,2:女
Birthday | 否|出生日期，10位秒级时间戳|
Location | 否| 经纬度信息，纬度在前|
Education	 | 否| 所受教育水平 | 详见[教育水平定义]
Profession	 | 否| 职业 | 详见[用户职业定义]
IsOrganization	 | 否| 是否属于组织机构类型用户（to B），默认false|
Reserve	 | 否| json字符串，保留字段用于扩展用户其他信息|
favoriteSinger	 | 否| 喜欢的歌手名，多个用英文逗号隔开|
FavoriteGenre	 | 否| 喜欢的音乐流派Id，多个用英文逗号拼接|



##### 3.4 电台列表


```
channel(response: DataResponse<ArrayList<ChannelItem>>)
```


##### 3.5 电台获取歌单列表

```
channelSheet(GroupId: String?,
                     Language: Int?,
                     RecoNum: Int?,
                     Page: Int?,
                     PageSize: Int?,
                    response: DataResponse<ChannelSheet>)
```
参数  | 必填  |描述| 可选值|
---|---|---|---
GroupId | 否| 电台id |
Language | 否| 标签、歌单名、歌名语言版本 | 0-中文,1-英文
RecoNum | 否|推荐音乐数|0～10 |
Page | 否| 当前页码，默认为1|大于0的整数
PageSize	 | 否| 每页显示条数，默认为10 | 1～100



##### 3.6 歌单获取音乐列表

```
    sheetMusic( SheetId: String?,
                       Language: Int?,
                       Page: Int?,
                       PageSize: Int?,
                        response: DataResponse<SheetMusic>)
```

参数  | 必填  |描述| 可选值|
---|---|---|---
SheetId | 是| 歌单id |
Language | 否| 标签、歌单名、歌名语言版本 | 0-中文,1-英文
Page | 否| 当前页码，默认为1|大于0的整数
PageSize	 | 否| 每页显示条数，默认为10 | 1～100


##### 3.7 组合搜索

```
searchMusic(TagIds: String?,
                    priceFromCent: Long?,
                    priceToCent: Long?,
                    BpmForm: Int?,
                    BpmTo: Int?,
                    DurationFrom: Int?,
                    DurationTo: Int?,
                    Keyword: String?,
                    Language: Int?,
                    Page: Int?,
                    PageSize: Int?,
                    response: DataResponse<SearchMusic>
    )
```
参数  | 必填  |描述| 可选值|
---|---|---|---
TagIds | 否| 标签Id，多个Id以“,”拼接 |
priceFromCent | 否| 价格区间的最低值，单位分 | 
priceToCent | 否| 价格区间的最高值，单位分 |
BpmForm | 否| BPM区间的最低值|
BpmTo	 | 否| BPM区间的最高值|
DurationFrom	 | 否| 时长区间的最低值,单位秒 | 
DurationTo	 | 否| 时长区间的最高值,单位秒|
Keyword	 | 否| 搜索关键词，搜索条件歌名、专辑名、艺人名、标签名|
Language | 否| 标签、歌单名、歌名语言版本 | 0-中文,1-英文
Page | 否| 当前页码，默认为1|大于0的整数
PageSize	 | 否| 每页显示条数，默认为10 | 1～100


##### 3.8 音乐配置信息

```
musicConfig(response: DataResponse<MusicConfig>)
```


#####  3.9 猜你喜欢


```
baseFavorite(Page: Int?,
                     PageSize: Int?,
                     response: DataResponse<BaseFavorite>
    )
```
参数  | 必填  |描述| 可选值|
---|---|---|---
Page | 否| 当前页码，默认为1|大于0的整数
PageSize	 | 否| 每页显示条数，默认为10 | 1～100


##### 3.10 热门推荐

```
baseHot(StartTime: Long?,
                Duration: Int?,
                Page: Int?,
                PageSize: Int?,
                response: DataResponse<BaseHot>
    )
```

参数  | 必填  |描述| 可选值|
---|---|---|---
StartTime | 是| 10位秒级时间戳
Duration	 | 是| 距离StartTime过去的天数 | 1～365
Page | 否| 当前页码，默认为1|大于0的整数
PageSize	 | 否| 每页显示条数，默认为10 | 1～100


##### 3.11 歌曲试听

```
trial(MusicId: String?,
              response: DataResponse<TrialMusic>
    )
```
参数  | 必填  |描述| 
---|---|---
MusicId | 是| 音乐id


##### 3.12 获取音乐HQ播放信息

```
trafficHQListen(MusicId: String?,
                        AudioFormat: String?,
                        AudioRate: String?,
                        response: DataResponse<TrafficHQListen>
    )

```

参数  | 必填  |描述| 可选值|
---|---|---|---
MusicId | 是| 音乐id |
AudioFormat	 | 否| 文件编码,默认mp3 | mp3 / aac
AudioRate | 否| 音质，音乐播放时的比特率，默认320 |320 / 128


##### 3.13 获取音乐混音播放信息

```
trafficListenMixed(MusicId: String?,
                           response: DataResponse<TrafficListenMixed>
    )

```

参数  | 必填  |描述| 
---|---|---
MusicId | 是| 音乐id 



##### 3.14 购买音乐

```
orderMusic(Subject: String?,
                   OrderId: Long?,
                   Deadline: Int?,
                   Music: String?,
                   Language: Int?,
                   AudioFormat: String?,
                   AudioRate: String?,
                   TotalFee: Int?,
                   Remark: String?,
                   WorkId: String?,
                   response: DataResponse<OrderMusic>
    )
```

参数  | 必填  |描述| 可选值|
---|---|---|---
Subject | 是| 商品描述 |
OrderId	 | 是| 公司自己生成的订单id | 
Deadline | 是| 作品授权时长，以天为单位，0代表永久授权 |
Music | 是| 购买详情，encode转化后的json字符串 （musicId->音乐id；price->音乐单价，单位分；num->购买数量） |
Language	 | 否| 标签、歌单名、歌名语言版本 | 0-中文,1-英文
AudioFormat | 否| 文件编码,默认mp3 | mp3 / aac
AudioRate | 否| 音质，音乐播放时的比特率，默认320 |320 / 128
TotalFee	 | 是| 售出总价，单位：分 | 
Remark | 否| 备注，最多不超过255字符 |
WorkId | 否| 公司自己生成的作品id,多个以“,”拼接 |


##### 3.15 查询订单

```
orderDetail(OrderId: String?,
                    response: DataResponse<OrderMusic>
    ) 
```

参数  | 必填  |描述| 可选值|
---|---|---|---
OrderId	 | 是| 公司自己生成的订单id | 



##### 3.16 下载授权书

```
orderAuthorization(CompanyName: String?,
                           ProjectName: String?,
                           Brand: String?,
                           Period: Int?,
                           Area: String?,
                           orderIds: String?,
                           response: DataResponse<OrderAuthorization>
    )
```

参数  | 必填  |描述| 可选值|
---|---|---|---
CompanyName | 是 | 公司名称 |
ProjectName	 | 是 | 项目名称 | 
Brand | 是 | 项目品牌 |
Period | 是 |授权期限（0:半年、1:1年、2:2年、3:3年、4:随片永久） | （0:半年、1:1年、2:2年、3:3年、4:随片永久）|
Area	 |  是 | 授权地区（0:中国大陆、1:大中华、2:全球） | （0:中国大陆、1:大中华、2:全球）
orderIds |  是 | 授权订单ID列表，多个ID用","隔开 | 



##### 3.17 行为采集

```
baseReport(Action: Int?,
                   TargetId: String?,
                   Content: String?,
                   Location: Int?,
                   response: DataResponse<TaskId>
    ) 
```
参数  | 必填  |描述| 可选值|
---|---|---|---
Action | 是 | 枚举定义用户行为 | 详见[用户行为定义]
TargetId	 | 是 | 行为操作的对象（音乐或分类id） | 
Content | 否 | 根据action传入格式 | 详见[用户行为定义]和[行为内容定义]|
Location | 否 |经纬度信息，纬度在前 | 


##### 3.18 发布作品

```
 orderPublish(Action: Int?,
                      OrderId: String?,
                      WorkId: String?,
                      response: DataResponse<OrderPublish>
     )
```

参数  | 必填  |描述
---|---|---
Action | 是 | 公共参数，操作的接口名称 
OrderId	 | 是 | 公司自己生成的订单id 
WorkId | 否 | 公司自己生成的作品id,多个以“,”拼接 




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













