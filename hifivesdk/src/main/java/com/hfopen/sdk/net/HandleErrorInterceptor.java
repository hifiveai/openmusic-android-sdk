package com.hfopen.sdk.net;

import android.util.Log;

import com.hfopen.sdk.common.BaseConstance;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * lsh 2021-3-1
 */
public class HandleErrorInterceptor extends ResponseBodyInterceptor {

    @Override
    Response intercept(@NotNull Response response, String url, String body) {
//        Log.w("hao", "拦截器拦到的返回数据：" + body);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(body);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jsonObject != null) {
            try {
                if (jsonObject.getInt("code") != BaseConstance.SUCCEED) {
                    BaseConstance.taskId = "";
                    if (response.body() != null) {
                        String json = response.body().string().replace("\"data\":\"\"", "\"data\":{}");
                        ResponseBody boy = ResponseBody.create(response.body().contentType(), json);
                        return response.newBuilder().body(boy).build();
                    }
                }else{
                    String taskId = jsonObject.getString("taskId");
                    if(!taskId.isEmpty()){
                        BaseConstance.taskId = taskId;

                    }
//                        String json = response.body().string();
//                        json.replace("\"data\":null", "\"data\":{}");
//                        int index = json.indexOf("\"data\"");
//                        StringBuilder sb=new StringBuilder(json);
//                        sb.insert(index,"\"data\":{").append("}");
//                        ResponseBody boy = ResponseBody.create(response.body().contentType(), sb.toString());
//                        return response.newBuilder().body(boy).build();
//                    }
                }
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        }
        return response;
    }
}
