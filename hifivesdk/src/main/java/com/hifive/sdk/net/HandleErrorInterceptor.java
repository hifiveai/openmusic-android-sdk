package com.hifive.sdk.net;

import android.util.Log;

import com.hifive.sdk.common.BaseConstance;

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
        Log.w("hao", "拦截器拦到的返回数据：" + body);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(body);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jsonObject != null) {
            try {
                if (jsonObject.getInt("code") != BaseConstance.SUCCEED) {
                    if (response.body() != null) {
                        String json = response.body().string().replace("\"data\":\"\"", "\"data\":{}");
                        ResponseBody boy = ResponseBody.create(response.body().contentType(), json);
                        return response.newBuilder().body(boy).build();
                    }
                }
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        }
        return response;
    }
}
