# 《通用API》接口文档
[TOC]

## SDK初始化
>  注意：初始化需要clientId，建议在用户登录之后进行初始化

```java 
HFOpenApi.registerApp(Application context,String clientId);
```

| 参数     | 必填 | 描述                               |      |
| -------- | ---- | ---------------------------------- | ---- |
| context  | 是   | 上下文                             |      |
| clientId | 是   | 用户唯一标识（公司自有的用户ID）。 |      |

## 设置SDK版本

```java
HFOpenApi.setVersion(String version);
```

| 参数    | 必填 | 描述                | 示例   |      |
| ------- | ---- | ------------------- | ------ | ---- |
| version | 是   | 操作的 API 的版本。 | V4.1.1 |      |


## 请求响应回调

本方法是所有接口调用后统一返回数据的回调，接口里面包含了错误的信息跟请求的数据信息，具体模式如下：
```java
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


| BaseException字段 | 描述     |      |
| ----------------- | -------- | ---- |
| msg               | 错误描述 |      |
| code              | 错误code |      |




## 设置SDK全局回调
```java
HFOpenApi.configCallBack(HFOpenCallback callback);
```
| 参数     | 必填 | 描述        |      |
| -------- | ---- | ----------- | ---- |
| callback | 是   | SDK全局回调 |      |


## 电台列表


```java
HFOpenApi.getInstance().channel(response: DataResponse<ArrayList<ChannelItem>>)
```

## 电台获取歌单列表

```java
HFOpenApi.getInstance().channelSheet(GroupId: String?,
                     Language: Int?,
                     RecoNum: Int?,
                     Page: Int?,
                     PageSize: Int?,
                    response: DataResponse<ChannelSheet>)
```
| 参数     | 必填 | 描述                       | 可选值        |      |
| -------- | ---- | -------------------------- | ------------- | ---- |
| GroupId  | 否   | 电台id                     | -             |      |
| Language | 否   | 标签、歌单名、歌名语言版本 | 0-中文,1-英文 |      |
| RecoNum  | 否   | 推荐音乐数                 | 0～10         | -    |
| Page     | 否   | 当前页码，默认为1          | 大于0的整数   | -    |
| PageSize | 否   | 每页显示条数，默认为10     | 1～100        |      |


## 歌单获取音乐列表

```java
HFOpenApi.getInstance().sheetMusic( SheetId: Long?,
                       Language: Int?,
                       Page: Int?,
                       PageSize: Int?,
                        response: DataResponse<SheetMusic>)
```

| 参数     | 必填 | 描述                       | 可选值        |      |
| -------- | ---- | -------------------------- | ------------- | ---- |
| SheetId  | 是   | 歌单id                     | -             |      |
| Language | 否   | 标签、歌单名、歌名语言版本 | 0-中文,1-英文 |      |
| Page     | 否   | 当前页码，默认为1          | 大于0的整数   |      |
| PageSize | 否   | 每页显示条数，默认为10     | 1～100        |      |


## 组合搜索

```java
HFOpenApi.getInstance().searchMusic(TagIds: String?,
                    PriceFromCent: Long?,
                    PriceToCent: Long?,
                    BpmFrom: Int?,
                    BpmTo: Int?,
                    DurationFrom: Int?,
                    DurationTo: Int?,
                    Keyword: String?,
                    SearchFiled: String?,
                    SearchSmart: Int?,
                    Language: Int?,
                    Page: Int?,
                    PageSize: Int?,
                    response: DataResponse<SearchMusic>
    )
```
| 参数          | 必填 | 描述                                                         | 可选值                                 |      |
| ------------- | ---- | ------------------------------------------------------------ | -------------------------------------- | ---- |
| TagIds        | 否   | 标签Id，多个Id以“,”拼接                                      | -                                      |      |
| PriceFromCent | 否   | 价格区间的最低值，单位分                                     | -                                      |      |
| PriceToCent   | 否   | 价格区间的最高值，单位分                                     | -                                      |      |
| BpmFrom       | 否   | BPM区间的最低值                                              | -                                      |      |
| BpmTo         | 否   | BPM区间的最高值                                              | -                                      |      |
| DurationFrom  | 否   | 时长区间的最低值,单位秒                                      | -                                      |      |
| DurationTo    | 否   | 时长区间的最高值,单位秒                                      | -                                      |      |
| Keyword       | 否   | 搜索关键词，搜索条件歌名、专辑名、艺人名、标签名             | -                                      |      |
| SearchFiled   | 否   | Keywords参数指定搜索条件，不传时默认搜索条件歌名、专辑名、艺人名、标签名 | musicName/albumName/artistName/tagName |      |
| SearchSmart   | 否   | 是否启用分词                                                 | 0/1                                    |      |
| Language      | 否   | 标签、歌单名、歌名语言版本                                   | 0-中文,1-英文                          |      |
| Page          | 否   | 当前页码，默认为1                                            | 大于0的整数                            | -    |
| PageSize      | 否   | 每页显示条数，默认为10                                       | 1～100                                 |      |


## 音乐配置信息

```java
HFOpenApi.getInstance().musicConfig(response: DataResponse<MusicConfig>)
```

## 获取Token

>在接口行为采集、猜你喜欢和单笔付费场景等接口中使用。（7200秒无操作则token自动失效，token不参与签名计算)

```java
HFOpenApi.getInstance().baseLogin(Nickname: String?,
                                  Gender: Int?,
                                  Birthday: Long?,
                                  Location: String?,
                                  Education: Int?,
                                  Profession: Int?,
                                  IsOrganization: Boolean?,
                                  Reserve: String?,
                                  FavoriteSinger: String?,
                                  FavoriteGenre: String?,
                                  response: DataResponse<LoginBean>
    )
```

| 参数           | 必填 | 描述                                        | 可选值             | 示例                   |      |
| -------------- | ---- | ------------------------------------------- | ------------------ | ---------------------- | ---- |
| Nickname       | 否   | 昵称                                        | -                  | -                      |      |
| Gender         | 否   | 性别，默认0                                 | 0:未知,1:男,2:女   | -                      |      |
| Birthday       | 否   | 出生日期，10位秒级时间戳                    | -                  | 1594639058             |      |
| Location       | 否   | 经纬度信息，纬度在前                        | -                  | 30.779164,103.94547    |      |
| Education      | 否   | 所受教育水平                                | 详见[教育水平定义] | 0                      |      |
| Profession     | 否   | 职业                                        | 详见[用户职业定义] | 0                      |      |
| IsOrganization | 否   | 是否属于组织机构类型用户（to B），默认false | true/false         | false                  |      |
| Reserve        | 否   | json字符串，保留字段用于扩展用户其他信息    | -                  | {"language":"English"} |      |
| favoriteSinger | 否   | 喜欢的歌手名，多个用英文逗号隔开            | -                  | Queen,The Beatles      |      |
| FavoriteGenre  | 否   | 喜欢的音乐流派Id，多个用英文逗号拼接        | -                  | 7,8,10                 |      |

**教育水平定义**

| 名称       | 枚举值 | 说明   |      |
| ---------- | ------ | ------ | ---- |
| 未采集     | 0      | 默认值 |      |
| 小学       | 1      | -      |      |
| 初中       | 2      | -      |      |
| 高中       | 3      | -      |      |
| 大学       | 4      | -      |      |
| 硕士及以上 | 5      | -      |      |

**用户职业定义**

| 名称                                             | 枚举值 | 说明   |      |
| ------------------------------------------------ | ------ | ------ | ---- |
| 未采集                                           | 0      | 默认值 |      |
| 政府部门/企事业/公司管理人员                     | 1      | -      |      |
| 私营业主                                         | 2      | -      |      |
| 专业技术人员（教师、医生、工程技术人员、作家等） | 3      | -      |      |
| 企业/公司职员                                    | 4      | -      |      |
| 党政机关事业单位员工                             | 5      | -      |      |
| 第三产业服务人员                                 | 6      | -      |      |
| 学生                                             | 7      | -      |      |
| 军人                                             | 8      | -      |      |
| 失业、自由工作者、离退休人员                     | 9      | -      |      |
| 其他                                             | 10     | -      |      |

**音乐流派定义**

| 名称   | 枚举值 | 说明   |      |
| ------ | ------ | ------ | ---- |
| 未采集 | 0      | 默认值 |      |
| 中国风 | 1      | -      |      |
| 拉丁   | 2      | -      |      |
| 嘻哈   | 3      | -      |      |
| 爵士   | 4      | -      |      |
| 乡村   | 5      | -      |      |
| 流行   | 6      | -      |      |
| 布鲁斯 | 7      | -      |      |
| 民谣   | 8      | -      |      |
| 摇滚   | 9      | -      |      |
| 轻音乐 | 10     | -      |      |
| 管弦乐 | 11     | -      |      |
| 电子   | 12     | -      |      |


## 行为采集

> 注意：此接口需先调用BaseLogin接口获取token

```
HFOpenApi.getInstance().baseReport(Action: Int?,
                   TargetId: String?,
                   Content: String?,
                   Location: String?,
                   response: DataResponse<Object>
    ) 
```
| 参数     | 必填 | 描述                           | 可选值                             | 示例                |                                                         |
| -------- | ---- | ------------------------------ | ---------------------------------- | ------------------- | ------------------------------------------------------- |
| Action   | 是   | 枚举定义用户行为               | 详见[用户行为定义]                 | -                   | 1001                                                    |
| TargetId | 是   | 行为操作的对象（音乐或分类id） | -                                  | B75C80A41E3A        |                                                         |
| Content  | 否   | 根据action传入格式             | 详见[用户行为定义]和[行为内容定义] | -                   | {"point":"15","duration":"52","musicId":"B7B610A7537A"} |
| Location | 否   | 经纬度信息，纬度在前           | -                                  | 30.779164,103.94547 |                                                         |

### 用户行为定义

| 行为         | 枚举值 | TargetId     | Content                |      |
| ------------ | ------ | ------------ | ---------------------- | ---- |
| 播放音乐     | 1000   | 音乐id       | point/duration         |      |
| 手动切歌     | 1001   | 被切换音乐id | point/duration/musicId |      |
| 暂停音乐     | 1002   | 音乐id       | point/duration         |      |
| 进度拖动     | 1003   | 音乐id       | from/to/duration       |      |
| 收藏音乐     | 1004   | 音乐id       | 空                     |      |
| 收藏标签     | 1005   | 标签id       | 空                     |      |
| 切换标签     | 1006   | 被切换标签id | tagId                  |      |
| 取消收藏音乐 | 1007   | 音乐id       | 空                     |      |
| 取消收藏标签 | 1008   | 标签id       | 空                     |      |
| 播放结束     | 1009   | 音乐id       | 空                     |      |
| 下载音乐     | 1010   | 音乐id       | 空                     |      |

### 行为内容定义

>行为内容采用json字符串保存，根据行为不同其参数不同。例如：播放音乐时content字段为point/duration，代表json包含point和duration这2个字段，字段具体含义如下：

| 参数     | 类型   | 说明                                       |      |
| -------- | ------ | ------------------------------------------ | ---- |
| point    | Int    | 当前播放时长(秒)                           |      |
| duration | Int    | 总时长(秒)                                 |      |
| musicId  | String | 切换音乐后的音乐id                         |      |
| tagId    | String | 切换标签后的标签id                         |      |
| from     | Int    | 用于变化的操作，该值代表进度变化前的值(秒) |      |
| to       | Int    | 用于变化的操作，该值代表进度变化后的值(秒) |      |

## 猜你喜欢

> 注意：此接口需先调用BaseLogin接口获取token

```java
HFOpenApi.getInstance().baseFavorite(Page: Int?,
                     PageSize: Int?,
                     response: DataResponse<BaseFavorite>
    )
```
| 参数     | 必填 | 描述                   | 可选值      |      |
| -------- | ---- | ---------------------- | ----------- | ---- |
| Page     | 否   | 当前页码，默认为1      | 大于0的整数 |      |
| PageSize | 否   | 每页显示条数，默认为10 | 1～100      |      |


## 热门推荐

```java
HFOpenApi.getInstance().baseHot(StartTime: Long?,
                Duration: Int?,
                Page: Int?,
                PageSize: Int?,
                response: DataResponse<BaseHot>
    )
```

| 参数      | 必填 | 描述                    | 可选值      |      |
| --------- | ---- | ----------------------- | ----------- | ---- |
| StartTime | 是   | 10位秒级时间戳          | -           |      |
| Duration  | 是   | 距离StartTime过去的天数 | 1～365      |      |
| Page      | 否   | 当前页码，默认为1       | 大于0的整数 |      |
| PageSize  | 否   | 每页显示条数，默认为10  | 1～100      |      |


## BGM音乐播放-歌曲试听

```java
HFOpenApi.getInstance().trafficTrial(MusicId: String?,
              response: DataResponse<TrialMusic>
    )
```
| 参数    | 必填 | 描述   |      |
| ------- | ---- | ------ | ---- |
| MusicId | 是   | 音乐id |      |


## BGM音乐播放-获取音乐HQ播放信息

```java
HFOpenApi.getInstance().trafficHQListen(MusicId: String?,
                        AudioFormat: String?,
                        AudioRate: String?,
                        response: DataResponse<TrafficHQListen>
    )

```

| 参数        | 必填 | 描述                              | 可选值    |      |
| ----------- | ---- | --------------------------------- | --------- | ---- |
| MusicId     | 是   | 音乐id                            | -         |      |
| AudioFormat | 否   | 文件编码,默认mp3                  | mp3 / aac |      |
| AudioRate   | 否   | 音质，音乐播放时的比特率，默认320 | 320 / 128 |      |


## 音视频作品BGM音乐播放-歌曲试听

```java
HFOpenApi.getInstance().ugcTrial(MusicId: String?,
              response: DataResponse<TrialMusic>
    )
```
| 参数    | 必填 | 描述   |      |
| ------- | ---- | ------ | ---- |
| MusicId | 是   | 音乐id |      |


## 音视频作品BGM音乐播放-获取音乐HQ播放信息

```java
HFOpenApi.getInstance().ugcHQListen(MusicId: String?,
                        AudioFormat: String?,
                        AudioRate: String?,
                        response: DataResponse<TrafficHQListen>
    )

```

| 参数        | 必填 | 描述                              | 可选值    |      |
| ----------- | ---- | --------------------------------- | --------- | ---- |
| MusicId     | 是   | 音乐id                            | -         |      |
| AudioFormat | 否   | 文件编码,默认mp3                  | mp3 / aac |      |
| AudioRate   | 否   | 音质，音乐播放时的比特率，默认320 | 320 / 128 |      |

## K歌音乐播放-歌曲试听

```java
HFOpenApi.getInstance().kTrial(MusicId: String?,
              response: DataResponse<TrialMusic>
    )
```
| 参数    | 必填 | 描述   |      |
| ------- | ---- | ------ | ---- |
| MusicId | 是   | 音乐id |      |


## K歌音乐播放-获取音乐HQ播放信息

```java
HFOpenApi.getInstance().kHQListen(MusicId: String?,
                        AudioFormat: String?,
                        AudioRate: String?,
                        response: DataResponse<TrafficHQListen>
    )

```
| 参数        | 必填 | 描述                              | 可选值    |      |
| ----------- | ---- | --------------------------------- | --------- | ---- |
| MusicId     | 是   | 音乐id                            | -         |      |
| AudioFormat | 否   | 文件编码,默认mp3                  | mp3 / aac |      |
| AudioRate   | 否   | 音质，音乐播放时的比特率，默认320 | 320 / 128 |      |

## 音乐售卖-歌曲试听

```java
HFOpenApi.getInstance().orderTrial(MusicId: String?,
              response: DataResponse<TrialMusic>
    )
```
| 参数    | 必填 | 描述   |      |
| ------- | ---- | ------ | ---- |
| MusicId | 是   | 音乐id |      |


## 音乐售卖-购买音乐

```java
HFOpenApi.getInstance().orderMusic(Subject: String?,
                   OrderId: String?,
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

| 参数        | 必填 | 描述                                                         | 可选值        |      |
| ----------- | ---- | ------------------------------------------------------------ | ------------- | ---- |
| Subject     | 是   | 商品描述                                                     | -             |      |
| OrderId     | 是   | 公司自己生成的订单id                                         | -             |      |
| Deadline    | 是   | 作品授权时长，以天为单位，0代表永久授权                      | -             |      |
| Music       | 是   | 购买详情，encode转化后的json字符串 （musicId->音乐id；price->音乐单价，单位分；num->购买数量） | -             |      |
| Language    | 否   | 标签、歌单名、歌名语言版本                                   | 0-中文,1-英文 |      |
| AudioFormat | 否   | 文件编码,默认mp3                                             | mp3 / aac     |      |
| AudioRate   | 否   | 音质，音乐播放时的比特率，默认320                            | 320 / 128     |      |
| TotalFee    | 是   | 售出总价，单位：分                                           | -             |      |
| Remark      | 否   | 备注，最多不超过255字符                                      | -             |      |
| WorkId      | 否   | 公司自己生成的作品id,多个以“,”拼接                           | -             |      |


## 音乐售卖-查询订单

```java
HFOpenApi.getInstance().orderDetail(OrderId: String?,
                    response: DataResponse<OrderMusic>
    ) 
```

| 参数    | 必填 | 描述                 | 可选值 |      |
| ------- | ---- | -------------------- | ------ | ---- |
| OrderId | 是   | 公司自己生成的订单id | -      |      |


## 音乐售卖-下载授权书

```java
HFOpenApi.getInstance().orderAuthorization(CompanyName: String?,
                           ProjectName: String?,
                           Brand: String?,
                           Period: Int?,
                           Area: String?,
                           OrderIds: String?,
                           response: DataResponse<OrderAuthorization>
    )
```

| 参数        | 必填 | 描述                                                | 可选值                                      |      |
| ----------- | ---- | --------------------------------------------------- | ------------------------------------------- | ---- |
| CompanyName | 是   | 公司名称                                            | -                                           |      |
| ProjectName | 是   | 项目名称                                            | -                                           |      |
| Brand       | 是   | 项目品牌                                            | -                                           |      |
| Period      | 是   | 授权期限（0:半年、1:1年、2:2年、3:3年、4:随片永久） | （0:半年、1:1年、2:2年、3:3年、4:随片永久） |      |
| Area        | 是   | 授权地区（0:中国大陆、1:大中华、2:全球）            | （0:中国大陆、1:大中华、2:全球）            |      |
| OrderIds    | 是   | 授权订单ID列表，多个ID用","隔开                     | -                                           |      |




## 发布作品

```java
HFOpenApi.getInstance().orderPublish(OrderId: String?,
                      WorkId: String?,
                      response: DataResponse<OrderPublish>
     )
```

| 参数    | 必填 | 描述                               |
| ------- | ---- | ---------------------------------- |
| OrderId | 是   | 公司自己生成的订单id               |
| WorkId  | 是   | 公司自己生成的作品id,多个以“,”拼接 |



## BGM音乐数据上报

```java
HFOpenApi.getInstance().trafficReportListen(MusicId: String?,
                                           Duration: Int,
                                           Timestamp: Long,
                                           AudioFormat: String,
                                           AudioRate: String,
                                           response: DataResponse<Any>
                            )
```

| 参数        | 必填 | 描述                             | 可选值    |      |
| ----------- | ---- | -------------------------------- | --------- | ---- |
| MusicId     | 是   | 音乐id                           | -         |      |
| Duration    | 是   | 当前音频播放持续时长             | -         |      |
| Timestamp   | 是   | 播放完成的时间，13位毫秒级时间戳 | -         |      |
| AudioFormat | 是   | 当前播放音频的文件编码           | mp3 / aac |      |
| AudioRate   | 是   | 当前播放音频的比特率             | 320 / 128 |      |

## 音视频音乐数据上报

```java
HFOpenApi.getInstance().ugcReportListen(MusicId: String?,
                                                    Duration: Int,
                                                    Timestamp: Long,
                                                    AudioFormat: String,
                                                    AudioRate: String,
                                                    response: DataResponse<Any>
                            )
```

| 参数        | 必填 | 描述                             | 可选值    |      |
| ----------- | ---- | -------------------------------- | --------- | ---- |
| MusicId     | 是   | 音乐id                           | -         |      |
| Duration    | 是   | 当前音频播放持续时长             | -         |      |
| Timestamp   | 是   | 播放完成的时间，13位毫秒级时间戳 | -         |      |
| AudioFormat | 是   | 当前播放音频的文件编码           | mp3 / aac |      |
| AudioRate   | 是   | 当前播放音频的比特率             | 320 / 128 |      |

## K歌音乐数据上报

```java
HFOpenApi.getInstance().kReportListen(MusicId: String?,
                                      Duration: Int,
                                      Timestamp: Long,
                                      AudioFormat: String,
                                      AudioRate: String,
                                      response: DataResponse<Any>
                            )
```

| 参数        | 必填 | 描述                             | 可选值    |      |
| ----------- | ---- | -------------------------------- | --------- | ---- |
| MusicId     | 是   | 音乐id                           | -         |      |
| Duration    | 是   | 当前音频播放持续时长             | -         |      |
| Timestamp   | 是   | 播放完成的时间，13位毫秒级时间戳 | -         |      |
| AudioFormat | 是   | 当前播放音频的文件编码           | mp3 / aac |      |
| AudioRate   | 是   | 当前播放音频的比特率             | 320 / 128 |      |


# API状态码

**API错误码**

| code  | 说明                                   |
| ----- | :------------------------------------- |
| 400   | 参数缺失                               |
| 401   | 签名错误                               |
| 402   | ip地址不在白名单内                     |
| 403   | AppId不存在或App状态异常               |
| 404   | 找不到请求记录                         |
| 405   | 您的服务暂未开通，请检查接口的授权状态 |
| 406   | 登录过期，请重新登录                   |
| 407   | 接口授权已过期                         |
| 408   | 订单授权已过期                         |
| 412   | 请不要重复请求接口                     |
| 413   | 签名已过期                             |
| 414   | 位置信息不正确                         |
| 415   | 产品不存在                             |
| 416   | 订单号重复                             |
| 417   | 订单不存在                             |
| 418   | 不存在可取消的订单                     |
| 419   | 取消订单失败                           |
| 420   | 会员专享                               |
| 421   | 存在未支付的订单                       |
| 422   | 找不到服务模块，请检查url是否正确      |
| 423   | 已达到今天调用上限                     |
| 500   | 系统繁忙，请稍后重试                   |
| 503   | 非法参数                               |
| 504   | 当前排队人数过多，请稍后再试           |
| 10200 | 成功                                   |


# SDK错误码

| code  | 说明           | 解决方案       |
| ----- | :------------- | :------------- |
| 10000 | 未初始化ADK    | 初始化SDK      |
| 10001 | 网络错误       | 请检查网络连接 |
| 10002 | 连接超时       | 请检查网络连接 |
| 10003 | http异常       | 重试           |
| 10097 | JSON转换失败   | 重试           |
| 10098 | JSON格式不匹配 | 检查Json       |
| 10099 | 未知错误       |                |