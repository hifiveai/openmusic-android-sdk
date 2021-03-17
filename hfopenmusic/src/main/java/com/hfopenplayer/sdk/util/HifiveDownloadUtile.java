package com.hfopenplayer.sdk.util;


import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @ClassName HifiveDownloadUtile
 * @Description 文件下载辅助工具类
 * Created by huchao on 20/11/10.
 */
public class HifiveDownloadUtile {

    private static OkHttpClient.Builder mClient = null;
    //不验证证书
    private static OkHttpClient.Builder getClient() {
        if (mClient == null) {
            mClient = new OkHttpClient.Builder()
//                    .addInterceptor(new EncodingInterceptor("ISO-8859-1"))
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true);

        }
        return mClient;
    }
    public static void downloadFile(String url,final NetCallback callback) {
        if(!url.startsWith("http"))
            return;
        OkHttpClient.Builder client = getClient();
        StringBuilder builder = new StringBuilder(url);
        String realPath = builder.toString();
        Request request = new Request.Builder()
                .url(realPath)
                .addHeader("Connection", "keep-alive")
                .get()
                .build();
        client.build().newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response){
                try {
                    String s = response.body().string();
//                    String result = EncodUtils.isoToUTF(s);
                    callback.reqYes(response, s);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callback.reqNo("", e);
            }
        });
    }
    /**
     * 网络请求的简单回调
     */
    public interface NetCallback {
        void reqYes(Object o, String s);

        void reqNo(Object o, Exception e);
    }

}
