
package com.example.open_play_android_sdk;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hfopen.sdk.entity.ChannelItem;
import com.hfopen.sdk.entity.ChannelSheet;
import com.hfopen.sdk.entity.LoginBean;
import com.hfopen.sdk.entity.MusicConfig;
import com.hfopen.sdk.entity.MusicList;
import com.hfopen.sdk.entity.OrderAuthorization;
import com.hfopen.sdk.entity.OrderMusic;
import com.hfopen.sdk.entity.OrderPublish;
import com.hfopen.sdk.entity.HQListen;
import com.hfopen.sdk.entity.TrialMusic;
import com.hfopen.sdk.entity.VipSheet;
import com.hfopen.sdk.hInterface.DataResponse;
import com.hfopen.sdk.manager.HFOpenApi;
import com.hfopen.sdk.net.Encryption;
import com.hfopen.sdk.rx.BaseException;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class LoginActivity extends AppCompatActivity {
    private EditText editText;
    private String json = "{}";
    private String groupID, musicID, orderId;
    private Long sheetID;
    private String sheetName="kobeMemberSheet";
    private int sheetId=38737;
    private String musicId="2F0864DEC7";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_openapi);
        initView();
    }

    /**
     * 正式 300a44d050c942eebeae8765a878b0ee   0e31fe11b31247fca8
     * 沙箱 6jg58jx4aa9t7305dyck4ckvbyhk7duk   wnzwnkevnc74uym5
     * 测试 25861e5063284e38a40bc960070b34ab   7a4e2914d1b647b98a
     */
    private void initView() {
        HFOpenApi.setVersion("V4.1.2").registerApp(getApplication(), "3faeec81030444e98acf6af9ba32752a", "59b1aff189b3474398", "test_hifive_kobe2");

//        HFOpenApi.setVersion("V4.0.1").registerApp(getApplication(), Encryption.Companion.requestDeviceId(this),"https://hifive-openapi-qa.hifiveai.com");

        findViewById(R.id.btn_login).setOnClickListener(v -> Login());
        findViewById(R.id.btn_channel).setOnClickListener(v -> channel());
        findViewById(R.id.btn_channelsheet).setOnClickListener(v -> channelSheet());
        findViewById(R.id.btn_sheetmusic).setOnClickListener(v -> sheetMusic());
        findViewById(R.id.btn_search).setOnClickListener(v -> search());
        findViewById(R.id.btn_config).setOnClickListener(v -> config());
        findViewById(R.id.btn_favorite).setOnClickListener(v -> baseFavorite());
        findViewById(R.id.btn_hot).setOnClickListener(v -> baseHot());
        findViewById(R.id.btn_test).setOnClickListener(v -> test());
        findViewById(R.id.btn_TrafficHQListen).setOnClickListener(v -> TrafficHQListen());
        findViewById(R.id.btn_TrafficListenMixed).setOnClickListener(v -> TrafficListenMixed());
        findViewById(R.id.btn_order).setOnClickListener(v -> order());
        findViewById(R.id.btn_order2).setOnClickListener(v -> order2());
        findViewById(R.id.btn_order3).setOnClickListener(v -> order3());
        findViewById(R.id.btn_report).setOnClickListener(v -> report());
        findViewById(R.id.btn_publish).setOnClickListener(v -> publish());
        findViewById(R.id.btn_test3).setOnClickListener(v -> test3());
        findViewById(R.id.btn_test4).setOnClickListener(v -> test4());
        findViewById(R.id.btn_test5).setOnClickListener(v -> test5());
        findViewById(R.id.btn_test6).setOnClickListener(v -> test6());
        findViewById(R.id.btn_test7).setOnClickListener(v -> test7());
        findViewById(R.id.btn_report2).setOnClickListener(v -> report2());
        findViewById(R.id.btn_report3).setOnClickListener(v -> report3());
        findViewById(R.id.btn_report4).setOnClickListener(v -> report4());


        findViewById(R.id.CreateMemberSheet).setOnClickListener(v -> testOne());
        findViewById(R.id.DeleteMemberSheet).setOnClickListener(v -> testTwo());
        findViewById(R.id.MemberSheet).setOnClickListener(v -> testThree());
        findViewById(R.id.MemberSheetMusic).setOnClickListener(v -> testFour());
        findViewById(R.id.AddMemberSheetMusic).setOnClickListener(v -> testFive());
        findViewById(R.id.RemoveMemberSheetMusic).setOnClickListener(v -> testSix());
        findViewById(R.id.ClearMemberSheetMusic).setOnClickListener(v -> testSeven());

    }

    private void testSeven() {
        int sheetId = this.sheetId;
        HFOpenApi.getInstance().clearMemberSheetMusic(sheetId, new DataResponse<Object>() {
            @Override
            public void onError(@NotNull BaseException exception) {
                Toast.makeText(LoginActivity.this, exception.getMsg(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onSuccess(Object data, @NotNull String taskId) {
                Toast.makeText(LoginActivity.this, "成功", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void testSix() {
        int sheetId = this.sheetId;
        String musicId = this.musicId;
        HFOpenApi.getInstance().removeMemberSheetMusic(sheetId, musicId, new DataResponse<Object>() {
            @Override
            public void onError(@NotNull BaseException exception) {
                Toast.makeText(LoginActivity.this, exception.getMsg(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onSuccess(Object data, @NotNull String taskId) {
                Toast.makeText(LoginActivity.this, "成功", Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void testFive() {
        int sheetId = this.sheetId;
        String musicId = this.musicId;
        HFOpenApi.getInstance().addMemberSheetMusic(sheetId, musicId, new DataResponse<Object>() {

            @Override
            public void onError(@NotNull BaseException exception) {
                Toast.makeText(LoginActivity.this, exception.getMsg(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onSuccess(Object data, @NotNull String taskId) {
                Toast.makeText(LoginActivity.this, "成功", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void testFour() {
        int sheetId = this.sheetId;
        HFOpenApi.getInstance().memberSheetMusic(sheetId, 1, 10, new DataResponse<Object>() {
            @Override
            public void onError(@NotNull BaseException exception) {
                Toast.makeText(LoginActivity.this, exception.getMsg(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onSuccess(Object data, @NotNull String taskId) {
                Toast.makeText(LoginActivity.this, data.toString(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void testThree() {
        String memberOutId = "test_hifive_kobe2";
        HFOpenApi.getInstance().memberSheet(memberOutId, 1, 10, new DataResponse<VipSheet>() {
            @Override
            public void onError(@NotNull BaseException exception) {
                Toast.makeText(LoginActivity.this, exception.getMsg(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onSuccess(VipSheet data, @NotNull String taskId) {
                Toast.makeText(LoginActivity.this, data.toString(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void testTwo() {
        int sheetId = this.sheetId;
        HFOpenApi.getInstance().deleteMemberSheet(sheetId, new DataResponse<Object>() {

            @Override
            public void onError(@NotNull BaseException exception) {
                Toast.makeText(LoginActivity.this, exception.getMsg(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(Object data, @NotNull String taskId) {
                Toast.makeText(LoginActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void testOne() {
        sheetName= sheetName+System.currentTimeMillis();
        HFOpenApi.getInstance().createMemberSheet(sheetName, new DataResponse<Object>() {

            @Override
            public void onError(@NotNull BaseException exception) {
                Toast.makeText(LoginActivity.this, exception.getMsg(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(Object data, @NotNull String taskId) {
                Toast.makeText(LoginActivity.this, "创建成功", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void Login() {
        json = "{}";
        showDialog((dialog, which) -> {

            JSONObject object = new JSONObject();
            try {
                object.put("Nickname", "kobe");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            json = object.toString();
//            json = editText.getText().toString();
            String Nickname = getValue(json, "Nickname");

            String gender = getValue(json, "Gender");
            Integer Gender = gender == null ? 1 : Integer.parseInt(gender);

            String birthday = getValue(json, "Birthday");
            Long Birthday = birthday == null ? null : Long.parseLong(birthday);
            String Location = getValue(json, "Location");
            String education = getValue(json, "Education");
            Integer Education = education == null ? null : Integer.parseInt(education);
            String profession = getValue(json, "Profession");
            Integer Profession = profession == null ? null : Integer.parseInt(profession);
            boolean IsOrganization = Boolean.parseBoolean(getValue(json, "IsOrganization"));
            String Reserve = getValue(json, "Reserve");
            String FavoriteSinger = getValue(json, "FavoriteSinger");
            String FavoriteGenre = getValue(json, "FavoriteGenre");
            HFOpenApi.getInstance().baseLogin(Nickname, Gender, Birthday, Location, Education,
                    Profession, IsOrganization, Reserve, FavoriteSinger, FavoriteGenre, String.valueOf(System.currentTimeMillis()), new DataResponse<LoginBean>() {
                        @Override
                        public void onError(@NotNull BaseException exception) {
                            Log.e("TAG", "errorMsg==" + exception.getCode());
                            showResult(exception.getMsg());
                        }

                        @Override
                        public void onSuccess(@NotNull LoginBean any, String taskId) {
                            Log.e("TAG", "data==" + any);
                            showResult(any.toString());

                        }
                    });
        });

    }

    private void channel() {
        HFOpenApi.getInstance().channel(new DataResponse<ArrayList<ChannelItem>>() {
            @Override
            public void onError(@NotNull BaseException exception) {
                Log.e("TAG", "errorMsg==" + exception.getCode());
                showResult(exception.getMsg());
            }

            @Override
            public void onSuccess(@NotNull ArrayList<ChannelItem> any, String taskId) {
                Log.e("TAG", "data==" + any);
                groupID = any.get(0).getGroupId();
                showResult(any.toString());
            }
        });
    }

    private void channelSheet() { //测试 kbm4wsfn33  正式 rO0B29leSyS
        json = "{\"GroupID\":\"" + groupID + "\",\"RecoNum\":\"10\",\"Language\":\"0\",\"Page\":\"1\",\"PageSize\":\"20\"}";
        showDialog((dialog, which) -> {
            json = editText.getText().toString();
            groupID = getValue(json, "GroupID");
            String language = getValue(json, "Language");
            Integer Language = language == null ? 0 : Integer.parseInt(language);
            String recoNum = getValue(json, "RecoNum");
            Integer RecoNum = recoNum == null ? 10 : Integer.parseInt(recoNum);
            String page = getValue(json, "Page");
            Integer Page = page == null ? 1 : Integer.parseInt(page);
            String pageSize = getValue(json, "PageSize");
            Integer PageSize = pageSize == null ? 20 : Integer.parseInt(pageSize);

//            if (groupID == null) {
//                showResult("请输入电台ID");
//                return;
//            }

            HFOpenApi.getInstance().channelSheet(groupID, Language, RecoNum, Page, PageSize, new DataResponse<ChannelSheet>() {
                @Override
                public void onError(@NotNull BaseException exception) {
                    Log.e("TAG", "errorMsg==" + exception.getCode());
                    showResult(exception.getMsg());
                }

                @Override
                public void onSuccess(@NotNull ChannelSheet any, String taskId) {
                    Log.e("TAG", "data==" + any);
                    if (any.getRecord().size() > 0) {
                        sheetID = any.getRecord().get(0).getSheetId();
                    }
                    showResult(any.toString());
                }
            });

            HFOpenApi.getInstance().channelSheet(groupID, Language, RecoNum, Page, PageSize, new DataResponse<ChannelSheet>() {
                @Override
                public void onError(@NotNull BaseException exception) {
                    Log.e("TAG", "errorMsg==" + exception.getCode());
                    showResult(exception.getMsg());
                }

                @Override
                public void onSuccess(@NotNull ChannelSheet any, String taskId) {
                    Log.e("TAG", "data==" + any);
                    if (any.getRecord().size() > 0) {
                        sheetID = any.getRecord().get(0).getSheetId();
                    }
                    showResult(any.toString());
                }
            });
        });
    }

    private void sheetMusic() { //2689  2657
        json = "{\"SheetID\":\"" + sheetID + "\",\"Language\":\"0\",\"Page\":\"1\",\"PageSize\":\"20\"}";
        showDialog((dialog, which) -> {
            json = editText.getText().toString();
            String SheetID = getValue(json, "SheetID");
            sheetID = SheetID == null ? null : Long.parseLong(SheetID);
            String language = getValue(json, "Language");
            Integer Language = language == null ? 0 : Integer.parseInt(language);
            String page = getValue(json, "Page");
            Integer Page = page == null ? 1 : Integer.parseInt(page);
            String pageSize = getValue(json, "PageSize");
            Integer PageSize = pageSize == null ? 20 : Integer.parseInt(pageSize);

            if (sheetID == null) {
                showResult("请输入歌单ID");
                return;
            }
            HFOpenApi.getInstance().sheetMusic(sheetID, Language, Page, PageSize, new DataResponse<MusicList>() {
                @Override
                public void onError(@NotNull BaseException exception) {
                    Log.e("TAG", "errorMsg==" + exception.getCode());
                    showResult(exception.getMsg());
                }

                @Override
                public void onSuccess(@NotNull MusicList any, String taskId) {
                    Log.e("TAG", "data==" + any);
                    if (any.getRecord() != null && any.getRecord().size() > 0) {
                        musicID = any.getRecord().get(0).getMusicId();
                    }
                    showResult(any.toString());
                }
            });
        });

    }

    private void search() {
        json = "{\"Keyword\":\"测试\",\"Language\":\"0\",\"Page\":\"1\",\"PageSize\":\"20\"}";
        showDialog((dialog, which) -> {
            json = editText.getText().toString();
            String TagIds = getValue(json, "TagIds");
            String priceCent = getValue(json, "priceFromCent");
            Long priceFromCent = priceCent == null ? null : Long.parseLong(priceCent);
            String priceCent2 = getValue(json, "priceToCent");
            Long priceToCent = priceCent2 == null ? null : Long.parseLong(priceCent2);
            String bpmFrom = getValue(json, "BpmFrom");
            Integer BpmFrom = bpmFrom == null ? null : Integer.parseInt(bpmFrom);
            String bpmTo = getValue(json, "BpmTo");
            Integer BpmTo = bpmTo == null ? null : Integer.parseInt(bpmTo);
            String durationFrom = getValue(json, "DurationFrom");
            Integer DurationFrom = durationFrom == null ? null : Integer.parseInt(durationFrom);
            String durationTo = getValue(json, "DurationTo");
            Integer DurationTo = durationTo == null ? null : Integer.parseInt(durationTo);
            String Keyword = getValue(json, "Keyword");
            String language = getValue(json, "Language");
            Integer Language = language == null ? 0 : Integer.parseInt(language);
            String page = getValue(json, "Page");
            Integer Page = page == null ? 1 : Integer.parseInt(page);
            String pageSize = getValue(json, "PageSize");
            Integer PageSize = pageSize == null ? 20 : Integer.parseInt(pageSize);

            HFOpenApi.getInstance().searchMusic(TagIds, priceFromCent, priceToCent, BpmFrom, BpmTo, DurationFrom, DurationTo, Keyword,
                    null, 1, Language, Page, PageSize, new DataResponse<MusicList>() {
                        @Override
                        public void onError(@NotNull BaseException exception) {
                            Log.e("TAG", "errorMsg==" + exception.getCode());
                            showResult(exception.getMsg());
                        }

                        @Override
                        public void onSuccess(@NotNull MusicList any, String taskId) {
                            Log.e("TAG", "data==" + any);
                            showResult(any.toString());
                        }
                    });
        });
    }

    private void config() {
        HFOpenApi.getInstance().musicConfig(new DataResponse<MusicConfig>() {
            @Override
            public void onError(@NotNull BaseException exception) {
                Log.e("TAG", "errorMsg==" + exception.getCode());
                showResult(exception.getMsg());
            }

            @Override
            public void onSuccess(@NotNull MusicConfig any, String taskId) {
                Log.e("TAG", "data==" + any);
                showResult(any.toString());
            }
        });
    }


    private void report() {
        musicID = "2F0864DEC7";
        json = "{\"Action\":\"1001\",\"TargetId\":\"" + musicID + "\"," +
                "\"Content\":\"{\\\"point\\\":\\\"15\\\",\\\"duration\\\":\\\"52\\\",\\\"musicId\\\":\\\"" + musicID + "\\\"}\"}";
        showDialog((dialog, which) -> {
            json = editText.getText().toString();
            String action = getValue(json, "Action");
            Integer Action = action == null ? null : Integer.parseInt(action);
            String TargetID = getValue(json, "TargetId");
            String Content = getValue(json, "Content");
            String Location = getValue(json, "Location");

            HFOpenApi.getInstance().baseReport(Action, TargetID, Content, Location, new DataResponse<Object>() {
                @Override
                public void onError(@NotNull BaseException exception) {
                    Log.e("TAG", "errorMsg==" + exception.getCode());
                    showResult(exception.getMsg());
                }

                @Override
                public void onSuccess(@NotNull Object any, String taskId) {
                    Log.e("TAG", "data==" + any);
                    showResult("采集成功");
                }
            });
        });
    }

    private void baseFavorite() {
        json = "{\"Page\":\"1\",\"PageSize\":\"20\"}";
        showDialog((dialog, which) -> {
            json = editText.getText().toString();
            String page = getValue(json, "Page");
            Integer Page = page == null ? 1 : Integer.parseInt(page);
            String pageSize = getValue(json, "PageSize");
            Integer PageSize = pageSize == null ? 20 : Integer.parseInt(pageSize);

            HFOpenApi.getInstance().baseFavorite(Page, PageSize, new DataResponse<MusicList>() {
                @Override
                public void onError(@NotNull BaseException exception) {
                    Log.e("TAG", "errorMsg==" + exception.getCode());
                    showResult(exception.getMsg());
                }

                @Override
                public void onSuccess(@NotNull MusicList any, String taskId) {
                    Log.e("TAG", "data==" + any);
                    showResult(any.toString());
                }
            });
        });
    }

    private void baseHot() {
        json = "{\"StartTime\":\"1591696315\",\"Duration\":\"365\",\"Page\":\"1\",\"PageSize\":\"20\"}";
        showDialog((dialog, which) -> {
            json = editText.getText().toString();
            String startTime = getValue(json, "StartTime");
            Long StartTime = startTime == null ? null : Long.parseLong(startTime);
            String duration = getValue(json, "Duration");
            Integer Duration = duration == null ? null : Integer.parseInt(duration);
            String page = getValue(json, "Page");
            Integer Page = page == null ? 1 : Integer.parseInt(page);
            String pageSize = getValue(json, "PageSize");
            Integer PageSize = pageSize == null ? 20 : Integer.parseInt(pageSize);

            if (StartTime == null) {
                showResult("StartTime为空");
                return;
            }

            HFOpenApi.getInstance().baseHot(StartTime, Duration, Page, PageSize, new DataResponse<MusicList>() {
                @Override
                public void onError(@NotNull BaseException exception) {
                    Log.e("TAG", "errorMsg==" + exception.getCode());
                    showResult(exception.getMsg());
                }

                @Override
                public void onSuccess(@NotNull MusicList any, String taskId) {
                    Log.e("TAG", "data==" + any);
                    showResult(any.toString());
                }
            });
        });
    }

    private void test() {
        json = "{\"MusicID\":\"" + musicID + "\"}";
        showDialog((dialog, which) -> {
            json = editText.getText().toString();
            String MusicID = getValue(json, "MusicID");

            HFOpenApi.getInstance().trafficTrial(MusicID, new DataResponse<TrialMusic>() {
                @Override
                public void onError(@NotNull BaseException exception) {
                    Log.e("TAG", "errorMsg==" + exception.getCode());
                    showResult(exception.getMsg());
                }

                @Override
                public void onSuccess(@NotNull TrialMusic any, String taskId) {
                    Log.e("TAG", "data==" + any);
                    showResult(any.toString());
                }
            });
        });
    }

    private void TrafficHQListen() {
        json = "{\"MusicID\":\"" + musicID + "\",\"AudioFormat\":\"mp3\",\"AudioRate\":\"320\"}";
        showDialog((dialog, which) -> {
            json = editText.getText().toString();
            String MusicID = getValue(json, "MusicID");
            String AudioFormat = getValue(json, "AudioFormat");
            String AudioRate = getValue(json, "AudioRate");

            HFOpenApi.getInstance().trafficHQListen(MusicID, AudioFormat, AudioRate, new DataResponse<HQListen>() {
                @Override
                public void onError(@NotNull BaseException exception) {
                    Log.e("TAG", "errorMsg==" + exception.getCode());
                    showResult(exception.getMsg());
                }

                @Override
                public void onSuccess(@NotNull HQListen any, String taskId) {
                    Log.e("TAG", "data==" + any);
                    showResult(any.toString());
                }
            });
        });

    }

    private void TrafficListenMixed() {
//        json ="{\"MusicID\":\""+musicID+"\"}";
//        showDialog((dialog, which) -> {
//            json = editText.getText().toString();
//            String MusicID = getValue(json, "MusicID");
//
//            HFOpenApi.getInstance().trafficListenMixed(MusicID, new DataResponse<TrafficListenMixed>() {
//                @Override
//                public void onError(@NotNull BaseException exception) {
//                    Log.e("TAG", "errorMsg==" + exception.getCode());
//                    showResult(exception.getMsg());
//                }
//
//                @Override
//                public void onSuccess(@NotNull TrafficListenMixed any, String taskId) {
//                    Log.e("TAG", "data==" + any);
//                    showResult(any.toString());
//                }
//            });
//        });
    }

    private void test3() {
        json = "{\"MusicID\":\"" + musicID + "\"}";
        showDialog((dialog, which) -> {
            json = editText.getText().toString();
            String MusicID = getValue(json, "MusicID");

            HFOpenApi.getInstance().ugcTrial(MusicID, new DataResponse<TrialMusic>() {
                @Override
                public void onError(@NotNull BaseException exception) {
                    Log.e("TAG", "errorMsg==" + exception.getCode());
                    showResult(exception.getMsg());
                }

                @Override
                public void onSuccess(@NotNull TrialMusic any, String taskId) {
                    Log.e("TAG", "data==" + any);
                    showResult(any.toString());
                }
            });
        });

    }

    private void test4() {
        json = "{\"MusicID\":\"" + musicID + "\"}";
        showDialog((dialog, which) -> {
            json = editText.getText().toString();
            String MusicID = getValue(json, "MusicID");

            HFOpenApi.getInstance().kTrial(MusicID, new DataResponse<TrialMusic>() {
                @Override
                public void onError(@NotNull BaseException exception) {
                    Log.e("TAG", "errorMsg==" + exception.getCode());
                    showResult(exception.getMsg());
                }

                @Override
                public void onSuccess(@NotNull TrialMusic any, String taskId) {
                    Log.e("TAG", "data==" + any);
                    showResult(any.toString());
                }
            });
        });
    }

    private void test5() {
        json = "{\"MusicID\":\"" + musicID + "\"}";
        showDialog((dialog, which) -> {
            json = editText.getText().toString();
            String MusicID = getValue(json, "MusicID");

            HFOpenApi.getInstance().orderTrial(MusicID, new DataResponse<TrialMusic>() {
                @Override
                public void onError(@NotNull BaseException exception) {
                    Log.e("TAG", "errorMsg==" + exception.getCode());
                    showResult(exception.getMsg());
                }

                @Override
                public void onSuccess(@NotNull TrialMusic any, String taskId) {
                    Log.e("TAG", "data==" + any);
                    showResult(any.toString());
                }
            });
        });
    }

    private void test6() {
        json = "{\"MusicID\":\"" + musicID + "\",\"AudioFormat\":\"mp3\",\"AudioRate\":\"320\"}";
        showDialog((dialog, which) -> {
            json = editText.getText().toString();
            String MusicID = getValue(json, "MusicID");
            String AudioFormat = getValue(json, "AudioFormat");
            String AudioRate = getValue(json, "AudioRate");

            HFOpenApi.getInstance().ugcHQListen(MusicID, AudioFormat, AudioRate, new DataResponse<HQListen>() {
                @Override
                public void onError(@NotNull BaseException exception) {
                    Log.e("TAG", "errorMsg==" + exception.getCode());
                    showResult(exception.getMsg());
                }

                @Override
                public void onSuccess(@NotNull HQListen any, String taskId) {
                    Log.e("TAG", "data==" + any);
                    showResult(any.toString());
                }
            });
        });
    }

    private void test7() { // CFE6475822DF 2F084491A5
        json = "{\"MusicID\":\"" + musicID + "\",\"AudioFormat\":\"mp3\",\"AudioRate\":\"320\"}";
        showDialog((dialog, which) -> {
            json = editText.getText().toString();
            String MusicID = getValue(json, "MusicID");
            String AudioFormat = getValue(json, "AudioFormat");
            String AudioRate = getValue(json, "AudioRate");

            HFOpenApi.getInstance().kHQListen(MusicID, AudioFormat, AudioRate, new DataResponse<HQListen>() {
                @Override
                public void onError(@NotNull BaseException exception) {
                    Log.e("TAG", "errorMsg==" + exception.getCode());
                    showResult(exception.getMsg());
                }

                @Override
                public void onSuccess(@NotNull HQListen any, String taskId) {
                    Log.e("TAG", "data==" + any);
                    showResult(any.toString());
                }
            });
        });
    }

    private void report2() {
        json = "{\"MusicID\":\"" + musicID + "\",\"AudioFormat\":\"mp3\",\"AudioRate\":\"320\",\"Duration\":\"10000\"}";
        showDialog((dialog, which) -> {
            json = editText.getText().toString();
            String MusicID = getValue(json, "MusicID");
            String AudioFormat = getValue(json, "AudioFormat");
            String AudioRate = getValue(json, "AudioRate");
            String duration = getValue(json, "Duration");
            Integer Duration = duration == null ? null : Integer.parseInt(duration);

            String timestamp = getValue(json, "Timestamp");
            Long Timestamp = timestamp == null ? null : Long.parseLong(timestamp);

            if (MusicID == null || AudioFormat == null || AudioRate == null || Duration == null || Timestamp == null) {
                showResult("必填参数为空");
                return;
            }

//            Long time = System.currentTimeMillis();
            HFOpenApi.getInstance().trafficReportListen(MusicID, Duration, Timestamp, AudioFormat, AudioRate, new DataResponse<Object>() {
                @Override
                public void onError(@NotNull BaseException exception) {
                    Log.e("TAG", "errorMsg==" + exception.getCode());
                    showResult(exception.getMsg());
                }

                @Override
                public void onSuccess(@NotNull Object any, String taskId) {
                    Log.e("TAG", "data==" + any);
                    showResult("上报成功");
                }
            });
        });
    }


    private void report3() {
        json = "{\"MusicID\":\"" + musicID + "\",\"AudioFormat\":\"mp3\",\"AudioRate\":\"320\",\"Duration\":\"10000\"}";
        showDialog((dialog, which) -> {
            json = editText.getText().toString();
            String MusicID = getValue(json, "MusicID");
            String AudioFormat = getValue(json, "AudioFormat");
            String AudioRate = getValue(json, "AudioRate");
            String duration = getValue(json, "Duration");
            Integer Duration = duration == null ? null : Integer.parseInt(duration);

            String timestamp = getValue(json, "Timestamp");
            Long Timestamp = timestamp == null ? null : Long.parseLong(timestamp);

            if (MusicID == null || AudioFormat == null || AudioRate == null || Duration == null || Timestamp == null) {
                showResult("必填参数为空");
                return;
            }
            HFOpenApi.getInstance().ugcReportListen(MusicID, Duration, Timestamp, AudioFormat, AudioRate, new DataResponse<Object>() {
                @Override
                public void onError(@NotNull BaseException exception) {
                    Log.e("TAG", "errorMsg==" + exception.getCode());
                    showResult(exception.getMsg());
                }

                @Override
                public void onSuccess(@NotNull Object any, String taskId) {
                    Log.e("TAG", "data==" + any);
                    showResult("上报成功");
                }
            });
        });
    }

    private void report4() {
        json = "{\"MusicID\":\"" + musicID + "\",\"AudioFormat\":\"mp3\",\"AudioRate\":\"320\",\"Duration\":\"10000\"}";
        showDialog((dialog, which) -> {
            json = editText.getText().toString();
            String MusicID = getValue(json, "MusicID");
            String AudioFormat = getValue(json, "AudioFormat");
            String AudioRate = getValue(json, "AudioRate");
            String duration = getValue(json, "Duration");
            Integer Duration = duration == null ? null : Integer.parseInt(duration);

            String timestamp = getValue(json, "Timestamp");
            Long Timestamp = timestamp == null ? null : Long.parseLong(timestamp);

            if (MusicID == null || AudioFormat == null || AudioRate == null || Duration == null || Timestamp == null) {
                showResult("必填参数为空");
                return;
            }

            HFOpenApi.getInstance().kReportListen(MusicID, Duration, Timestamp, AudioFormat, AudioRate, new DataResponse<Object>() {
                @Override
                public void onError(@NotNull BaseException exception) {
                    Log.e("TAG", "errorMsg==" + exception.getCode());
                    showResult(exception.getMsg());
                }

                @Override
                public void onSuccess(@NotNull Object any, String taskId) {
                    Log.e("TAG", "data==" + any);
                    showResult("上报成功");
                }
            });
        });
    }

    // "[{\"musicId\":\"2F084491A5\",\"price\":2900,\"num\":1}]"
    private void order() {
        json = "{\"Subject\":\"购买单曲\",\"OrderId\":\"143456789056569145\",\"Deadline\":\"365\",\"Music\":\"[{\\\"musicId\\\":\\\"" + musicID + "\\\",\\\"price\\\":\\\"2900\\\",\\\"num\\\":\\\"1\\\"}]\"," +
                "\"Language\":\"0\",\"AudioFormat\":\"mp3\",\"AudioRate\":\"320\",\"TotalFee\":\"2900\"}";
        showDialog((dialog, which) -> {
            json = editText.getText().toString();
            String Subject = getValue(json, "Subject");
            String OrderId = getValue(json, "OrderId");
            orderId = OrderId;
            String deadline = getValue(json, "Deadline");
            Integer Deadline = deadline == null ? 365 : Integer.parseInt(deadline);
            String Music = getValue(json, "Music");
            String language = getValue(json, "Language");
            Integer Language = language == null ? 0 : Integer.parseInt(language);
            String AudioFormat = getValue(json, "AudioFormat");
            String AudioRate = getValue(json, "AudioRate");
            String totalFee = getValue(json, "TotalFee");
            Integer TotalFee = totalFee == null ? 0 : Integer.parseInt(totalFee);
            String Remark = getValue(json, "Remark");
            String WorkId = getValue(json, "WorkId");

            HFOpenApi.getInstance().orderMusic(Subject, OrderId, Deadline,
                    Music, Language, AudioFormat, AudioRate, TotalFee,
                    Remark, WorkId, new DataResponse<OrderMusic>() {
                        @Override
                        public void onError(@NotNull BaseException exception) {
                            Log.e("TAG", "errorMsg==" + exception.getCode());
                            showResult(exception.getMsg());
                        }

                        @Override
                        public void onSuccess(@NotNull OrderMusic any, String taskId) {
                            Log.e("TAG", "data==" + any);
                            showResult(any.toString());
                        }
                    });
        });
    }

    private void order2() {
        json = "{\"OrderId\":\"" + orderId + "\"}";
        showDialog((dialog, which) -> {
            json = editText.getText().toString();
            String OrderId = getValue(json, "OrderId");
            if (OrderId == null) {
                showResult("请输入订单ID");
                return;
            }
            HFOpenApi.getInstance().orderDetail(OrderId, new DataResponse<OrderMusic>() {
                @Override
                public void onError(@NotNull BaseException exception) {
                    Log.e("TAG", "errorMsg==" + exception.getCode());
                    showResult(exception.getMsg());
                }

                @Override
                public void onSuccess(@NotNull OrderMusic any, String taskId) {
                    Log.e("TAG", "data==" + any);
                    showResult(any.toString());
                }
            });
        });
    }

    private void order3() {
        json = "{\"CompanyName\":\"嗨翻屋\",\"ProjectName\":\"宝马汽车2020品牌广告\",\"Brand\":\"HIFIVE音乐开放平台\",\"Period\":\"0\",\"Area\":\"0\",\"OrderIds\":\"" + orderId + "\"}";
        showDialog((dialog, which) -> {
            json = editText.getText().toString();
            String CompanyName = getValue(json, "CompanyName");
            String ProjectName = getValue(json, "ProjectName");
            String Brand = getValue(json, "Brand");
            String period = getValue(json, "Period");
            Integer Period = period == null ? null : Integer.parseInt(period);
            String Area = getValue(json, "Area");
            String OrderIds = getValue(json, "OrderIds");

            if (OrderIds == null) {
                showResult("请输入订单ID");
                return;
            }
            HFOpenApi.getInstance().orderAuthorization(CompanyName, ProjectName, Brand, Period, Area, OrderIds,
                    new DataResponse<OrderAuthorization>() {
                        @Override
                        public void onError(@NotNull BaseException exception) {
                            Log.e("TAG", "errorMsg==" + exception.getCode());
                            showResult(exception.getMsg());
                        }

                        @Override
                        public void onSuccess(@NotNull OrderAuthorization any, String taskId) {
                            Log.e("TAG", "data==" + any);
                            showResult(any.toString());
                        }
                    });
        });
    }


    private void publish() {
        json = "{\"OrderId\":\"" + orderId + "\",\"WorkId\":\"dd72629-03f3-4eb8-8a19-247a6\"}";
        showDialog((dialog, which) -> {
            json = editText.getText().toString();
            String OrderId = getValue(json, "OrderId");
            String WorkId = getValue(json, "WorkId");
            if (OrderId == null || WorkId == null) {
                showResult("请输入订单ID");
                return;
            }
            HFOpenApi.getInstance().orderPublish(OrderId, WorkId,
                    new DataResponse<OrderPublish>() {
                        @Override
                        public void onError(@NotNull BaseException exception) {
                            Log.e("TAG", "errorMsg==" + exception.getCode());
                            showResult(exception.getMsg());
                        }

                        @Override
                        public void onSuccess(@NotNull OrderPublish any, String taskId) {
                            Log.e("TAG", "data==" + any);
                            showResult(any.toString());
                        }
                    });
        });


    }


    private void showResult(String msg) {
        new AlertDialog.Builder(this)
                .setTitle("result")
                .setMessage(msg)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .create()
                .show();

//        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }


    private void showDialog(DialogInterface.OnClickListener listener) {
        editText = new EditText(this);
        editText.setLines(5);
        editText.setText(json);

        new AlertDialog.Builder(this)
                .setTitle("参数(json格式)")
                .setView(editText)
                .setPositiveButton("提交", listener)
                .create()
                .show();

    }

    private String getValue(String json, String key) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            String value = jsonObject.getString(key);
            if (value.equals("") || value.equals("null")) value = null;
            return value;
        } catch (JSONException e) {
            if (e.getMessage() != null && e.getMessage().contains("No value for")) {
            } else {
                showResult("请输入json格式的对应参数");
            }
            e.printStackTrace();
            return null;


        }
    }
}

//package com.example.open_play_android_sdk;
//
//import android.content.DialogInterface;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.hifive.sdk.entity.HifiveMusicDetailModel;
//import com.hifive.sdk.hInterface.DataResponse;
//import com.hifive.sdk.manager.HFLiveApi;
//
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//
//import java.util.List;
//
//
//public class LoginActivity extends AppCompatActivity {
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_start);
//        initView();
//    }
//
//    /**
//     *  正式 300a44d050c942eebeae8765a878b0ee   0e31fe11b31247fca8
//     *  测试 1998ca60c18a42b38fa03b80cce1832a   259e23ea0c684bd7be
//     *
//     */
//    private void initView() {
//        HFLiveApi.registerApp(getApplication(),"300a44d050c942eebeae8765a878b0ee","0e31fe11b31247fca8");
//
////        HFLiveApi.registerApp(getApplication());
//
//        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Login();
//            }
//        });
//
//        findViewById(R.id.btn_test).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                test();
//            }
//        });
//    }
//
//    private void Login() {
//        HFLiveApi.getInstance().memberLogin(this, "123456", "HOomxI+g0HvxGKo", null, null,
//                null , null, null, null, null, null, new DataResponse<Object>() {
//                    @Override
//                    public void errorMsg( String string, Integer code) {
//                        Log.e("TAG", "login==" + code);
//                    }
//
//
//                    @Override
//                    public void data( Object any) {
//                        Log.e("TAG", "login==" + any);
//                    }
//                });
//
//    }
//
//    private void test() {
////        HFLiveApi.getInstance().getCompanySheetMusicList(this, "1953", "0",null,"20","1", new DataResponse<Object>() {
////            @Override
////            public void errorMsg(@NotNull String string, @org.jetbrains.annotations.Nullable Integer code) {
////                Log.e("TAG", "歌曲==" + code);
////            }
////
////            @Override
////            public void data(@NotNull Object any) {
////                Log.e("TAG", "我的歌单==" + any);
////
////            }
////        });
//
////        HFLiveApi.getInstance().getCompanySheetTagList(this,  new DataResponse<Object>() {
////            @Override
////            public void errorMsg( String string, Integer code) {
////                Log.e("TAG", "歌曲==" + code);
////            }
////
////            @Override
////            public void data( Object any) {
////                Log.e("TAG", "我的歌单==" + any);
////
////            }
////        });
//
//        HFLiveApi.getInstance().getMusicDetail(this, "C0266B52EF5E", null, "2", "mp3", "320", null, new DataResponse<HifiveMusicDetailModel>() {
//            @Override
//            public void errorMsg(@NotNull String string, @Nullable Integer code) {
//                Log.e("TAG", "我的歌单==" + string);
//            }
//
//            @Override
//            public void data(HifiveMusicDetailModel any) {
//                Log.e("TAG", "我的歌单==" + any);
//            }
//        });
//
//    }
//
//    private void showResult(String msg) {
//        new AlertDialog.Builder(this)
//                .setTitle("result")
//                .setMessage(msg)
//                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                    }
//                })
//                .create()
//                .show();
//
////        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
//    }
//
//}
