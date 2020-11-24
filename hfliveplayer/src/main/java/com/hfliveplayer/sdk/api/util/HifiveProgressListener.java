package com.hfliveplayer.sdk.demo.util;

public interface HifiveProgressListener {
    void onProgress(long bytesRead, long contentLength, boolean done);
}
