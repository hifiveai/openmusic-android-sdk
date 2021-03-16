
## 一、SDK集成

#### 1.1 系统支持

Android 4.4以上

minSdkVersion    : 19

targetSdkVersion : 30

#### 1.2运行环境

建议使用Android Studio 3.4 以上版本进行编译。

#### 1.3集成SDK
 提供aar包接入方案

##### 1.3.1 手动集成

- [下载SDK]()
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
   implementation(name: 'hfopenapi', ext:'aar')//注意这里加入的名字没有后缀名
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
>  注意：初始化需要clientId，建议在用户登录之后进行初始化

```
HFOpenApi.registerApp(Application context,String clientId);
```

参数  | 必填  |描述|
---|---|---
context | 是| 上下文|
clientId | 是| 用户唯一标识（公司自有的用户ID）。|

##### 2.3 设置SDK版本

HFOpenApi.setVersion(String version);

参数  | 必填  |描述| 示例|
---|---|---|---
version | 是| 操作的 API 的版本。| V4.0.1 |

## 三 API文档

> 注意：如下api非必填参数中，如果开发者不想传参数时，应当传入null进行占位

> 注意：数据通过DataResponse接口返回。



##### 3.1 请求响应回调

本方法是所有接口调用后统一返回数据的回调，接口里面包含了错误的信息跟请求的数据信息，具体模式如下：
```
interface DataResponse {
       /**
        * exception：sdk返回的错误
        */
       void onError(BaseException exception)

       /**
        * data：sdk返回的数据
        * taskId：任务ID
        */
       void onSuccess(Object data , String taskId)
}
```


BaseException字段   |描述|
---|---
msg |  错误描述|
code |  错误code|


##### 3.2 SDK初始化

```
HFOpenApi.registerApp(Application context);
```
参数  | 必填  |描述|
---|---|---
context | 是| 上下文|


**设置SDK全局回调**
```
HFOpenApi.configCallBack(HFOpenCallback callback);
```
参数  | 必填  |描述|
---|---|---
callback | 是| SDK全局回调|


##### 3.3 获取Token

```
HFOpenApi.getInstance().baseLogin(Nickname: String?,
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

参数  | 必填  |描述| 可选值| 示例|
---|---|---|---|---
Nickname | 否| 昵称 | - | - |
Gender | 否| 性别，默认0 | 0:未知,1:男,2:女| -|
Birthday | 否|出生日期，10位秒级时间戳| - |1594639058|
Location | 否| 经纬度信息，纬度在前| - | 30.779164,103.94547|
Education	 | 否| 所受教育水平 | 详见[教育水平定义]|0|
Profession	 | 否| 职业 | 详见[用户职业定义]|0|
IsOrganization	 | 否| 是否属于组织机构类型用户（to B），默认false| - |false|
Reserve	 | 否| json字符串，保留字段用于扩展用户其他信息| - |{"language":"English"}|
favoriteSinger	 | 否| 喜欢的歌手名，多个用英文逗号隔开| - |Queen,The Beatles|
FavoriteGenre	 | 否| 喜欢的音乐流派Id，多个用英文逗号拼接| - |7,8,10|

**教育水平定义**

名称  | 枚举值  |说明| 
---|---|---
未采集 | 0| 默认值|
小学 | 1| - |
初中 | 2| - |
高中 | 3| - |
大学	 | 4| - |
硕士及以上	 | 5| - |

##### 3.4 电台列表


```
HFOpenApi.getInstance().channel(response: DataResponse<ArrayList<ChannelItem>>)
```

##### 3.5 电台获取歌单列表

```
HFOpenApi.getInstance().channelSheet(GroupId: String?,
                     Language: Int?,
                     RecoNum: Int?,
                     Page: Int?,
                     PageSize: Int?,
                    response: DataResponse<ChannelSheet>)
```
参数  | 必填  |描述| 可选值|
---|---|---|---
GroupId | 否| 电台id | - |
Language | 否| 标签、歌单名、歌名语言版本 | 0-中文,1-英文|
RecoNum | 否|推荐音乐数|0～10 | - |
Page | 否| 当前页码，默认为1|大于0的整数| - |
PageSize	 | 否| 每页显示条数，默认为10 | 1～100|


##### 3.6 歌单获取音乐列表

```
HFOpenApi.getInstance().sheetMusic( SheetId: Long?,
                       Language: Int?,
                       Page: Int?,
                       PageSize: Int?,
                        response: DataResponse<SheetMusic>)
```

参数  | 必填  |描述| 可选值|
---|---|---|---
SheetId | 是| 歌单id | - |
Language | 否| 标签、歌单名、歌名语言版本 | 0-中文,1-英文|
Page | 否| 当前页码，默认为1|大于0的整数
PageSize	 | 否| 每页显示条数，默认为10 | 1～100|


##### 3.7 组合搜索

```
HFOpenApi.getInstance().searchMusic(TagIds: String?,
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
TagIds | 否| 标签Id，多个Id以“,”拼接 | - |
priceFromCent | 否| 价格区间的最低值，单位分 |  - |
priceToCent | 否| 价格区间的最高值，单位分 | - |
BpmForm | 否| BPM区间的最低值| - |
BpmTo	 | 否| BPM区间的最高值| - |
DurationFrom	 | 否| 时长区间的最低值,单位秒 |  - |
DurationTo	 | 否| 时长区间的最高值,单位秒| - |
Keyword	 | 否| 搜索关键词，搜索条件歌名、专辑名、艺人名、标签名| - |
Language | 否| 标签、歌单名、歌名语言版本 | 0-中文,1-英文|
Page | 否| 当前页码，默认为1|大于0的整数| - |
PageSize	 | 否| 每页显示条数，默认为10 | 1～100|


##### 3.8 音乐配置信息

```
HFOpenApi.getInstance().musicConfig(response: DataResponse<MusicConfig>)
```


#####  3.9 猜你喜欢

> 注意：此接口需先调用BaseLogin接口获取token

```
HFOpenApi.getInstance().baseFavorite(Page: Int?,
                     PageSize: Int?,
                     response: DataResponse<BaseFavorite>
    )
```
参数  | 必填  |描述| 可选值|
---|---|---|---
Page | 否| 当前页码，默认为1|大于0的整数|
PageSize	 | 否| 每页显示条数，默认为10 | 1～100|


##### 3.10 热门推荐

```
HFOpenApi.getInstance().baseHot(StartTime: Long?,
                Duration: Int?,
                Page: Int?,
                PageSize: Int?,
                response: DataResponse<BaseHot>
    )
```

参数  | 必填  |描述| 可选值|
---|---|---|---
StartTime | 是| 10位秒级时间戳| - |
Duration	 | 是| 距离StartTime过去的天数 | 1～365|
Page | 否| 当前页码，默认为1|大于0的整数|
PageSize	 | 否| 每页显示条数，默认为10 | 1～100|


##### 3.11 歌曲试听

```
HFOpenApi.getInstance().trial(MusicId: String?,
              response: DataResponse<TrialMusic>
    )
```
参数  | 必填  |描述| 
---|---|---
MusicId | 是| 音乐id|


##### 3.12 获取音乐HQ播放信息

```
HFOpenApi.getInstance().trafficHQListen(MusicId: String?,
                        AudioFormat: String?,
                        AudioRate: String?,
                        response: DataResponse<TrafficHQListen>
    )

```

参数  | 必填  |描述| 可选值|
---|---|---|---
MusicId | 是| 音乐id | - |
AudioFormat	 | 否| 文件编码,默认mp3 | mp3 / aac|
AudioRate | 否| 音质，音乐播放时的比特率，默认320 |320 / 128|


##### 3.13 获取音乐混音播放信息

```
HFOpenApi.getInstance().trafficListenMixed(MusicId: String?,
                           response: DataResponse<TrafficListenMixed>
    )

```

参数  | 必填  |描述| 
---|---|---
MusicId | 是| 音乐id |



##### 3.14 购买音乐

```
HFOpenApi.getInstance().orderMusic(Subject: String?,
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
Subject | 是| 商品描述 | - |
OrderId	 | 是| 公司自己生成的订单id |  - |
Deadline | 是| 作品授权时长，以天为单位，0代表永久授权 | - |
Music | 是| 购买详情，encode转化后的json字符串 （musicId->音乐id；price->音乐单价，单位分；num->购买数量） | - |
Language	 | 否| 标签、歌单名、歌名语言版本 | 0-中文,1-英文|
AudioFormat | 否| 文件编码,默认mp3 | mp3 / aac|
AudioRate | 否| 音质，音乐播放时的比特率，默认320 |320 / 128|
TotalFee	 | 是| 售出总价，单位：分 |  - |
Remark | 否| 备注，最多不超过255字符 | - |
WorkId | 否| 公司自己生成的作品id,多个以“,”拼接 | - |


##### 3.15 查询订单

```
HFOpenApi.getInstance().orderDetail(OrderId: String?,
                    response: DataResponse<OrderMusic>
    ) 
```

参数  | 必填  |描述| 可选值|
---|---|---|---
OrderId	 | 是| 公司自己生成的订单id |  - |


##### 3.16 下载授权书

```
HFOpenApi.getInstance().orderAuthorization(CompanyName: String?,
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
CompanyName | 是 | 公司名称 | - |
ProjectName	 | 是 | 项目名称 |  - |
Brand | 是 | 项目品牌 | - |
Period | 是 |授权期限（0:半年、1:1年、2:2年、3:3年、4:随片永久） | （0:半年、1:1年、2:2年、3:3年、4:随片永久）|
Area	 |  是 | 授权地区（0:中国大陆、1:大中华、2:全球） | （0:中国大陆、1:大中华、2:全球）|
orderIds |  是 | 授权订单ID列表，多个ID用","隔开 |  - |



##### 3.17 行为采集

> 注意：此接口需先调用BaseLogin接口获取token

```
HFOpenApi.getInstance().baseReport(Action: Int?,
                   TargetId: String?,
                   Content: String?,
                   Location: Int?,
                   response: DataResponse<Object>
    ) 
```
参数  | 必填  |描述| 可选值| 示例|
---|---|---|---|---
Action | 是 | 枚举定义用户行为 | 详见[用户行为定义] | - |1001|
TargetId	 | 是 | 行为操作的对象（音乐或分类id） |  - |B75C80A41E3A|
Content | 否 | 根据action传入格式 | 详见[用户行为定义]和[行为内容定义]| - |{"point":"15","duration":"52","musicId":"B7B610A7537A"}|
Location | 否 |经纬度信息，纬度在前 |  - |30.779164,103.94547|

**用户行为定义**

行为  | 枚举值  |TargetId|  Content|
---|---|---|---
播放音乐 | 1000| 音乐id| point/duration |
手动切歌 | 1001| 被切换音乐id |point/duration/musicId|
暂停音乐 | 1002|音乐id | point/duration|
进度拖动 | 1003| 音乐id| from/to/duration|
收藏音乐	 | 1004|音乐id |空|
收藏标签	 | 1005|标签id|空|
切换标签 | 1006| 被切换标签id|tagId|
取消收藏音乐	 | 1007| 音乐id |空|
取消收藏标签	 | 1008|标签id |空|
播放结束	 | 1009| 音乐id |空|
下载音乐	 | 1010|音乐id |空|

**行为内容定义**

参数  | 类型  |说明| 
---|---|---
point | Int| 当前播放时长(秒) |
duration | Int| 总时长(秒)|
musicId |String|切换音乐后的音乐id|
tagId | String| 切换标签后的标签id|
from	 | Int| 用于变化的操作，该值代表进度变化前的值(秒)|
to	 | Int|用于变化的操作，该值代表进度变化后的值(秒)|

##### 3.18 发布作品

```
HFOpenApi.getInstance().orderPublish(Action: Int?,
                      OrderId: String?,
                      WorkId: String?,
                      response: DataResponse<OrderPublish>
     )
```

参数  | 必填  |描述
---|---|---
Action | 是 | 公共参数，操作的接口名称 |
OrderId	 | 是 | 公司自己生成的订单id |
WorkId | 否 | 公司自己生成的作品id,多个以“,”拼接 |



##### 3.19 Report

```
HFOpenApi.getInstance().trafficReportListen(musicId: String?,
                                                    duration: Long,
                                                    timestamp: Long,
                                                    audioFormat: String?,
                                                    audioRate: String?,
                                                    response: DataResponse<Any>
                            )
```

参数  | 必填  |描述| 可选值|
---|---|---|---
musicId | 是 |  音乐id | - |
duration	 | 是 | 播放时长 |  - |
timestamp | 是 | 播放时间，13位毫秒级时间戳 | - |
audioFormat | 否 |音频格式 文件编码, | mp3 / aac |
audioRate	 |  否 | 音频码率 音质 | 320 / 128|

## 四、API状态码

**API错误码**

| code | 说明 | 
|----------|:--------|
|400|参数缺失|
|401|签名错误|
|402|ip地址不在白名单内|
|403|AppId不存在或App状态异常|
|404|找不到请求记录|
|405|您的服务暂未开通，请检查接口的授权状态|
|406|登录过期，请重新登录|
|407|接口授权已过期|
|408|订单授权已过期|
|412|请不要重复请求接口|
|413|签名已过期|
|414|位置信息不正确|
|415|产品不存在|
|416|订单号重复|
|417|订单不存在|
|418|不存在可取消的订单|
|419|取消订单失败|
|420|会员专享|
|421|存在未支付的订单|
|422|找不到服务模块，请检查url是否正确|
|423|已达到今天调用上限|
|500|系统繁忙，请稍后重试|
|503|非法参数|
|504|当前排队人数过多，请稍后再试|
|10200|成功|


**SDK错误码**

| code | 说明 | 解决方案 |
|----------|:--------|:-------- |
| 10000 | 未初始化ADK | 初始化SDK |
| 10001 | 网络错误 | 请检查网络连接 |
| 10002 | 连接超时 | 请检查网络连接 |
| 10003 | http异常 | 重试 |
| 10097 | JSON转换失败 | 重试 |
| 10098 | JSON格式不匹配 | 检查Json |
| 10099 | 未知错误 |  |













