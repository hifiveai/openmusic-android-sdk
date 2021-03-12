package com.hifive.sdk.rx

import android.util.Log
import com.hifive.sdk.common.BaseConstance.Companion.SUCCEED
import com.hifive.sdk.protocol.BaseResp
import io.reactivex.Flowable
import io.reactivex.functions.Function

/**
 * @author lsh
 * @date 2021-3-11
 */
@Suppress("UNCHECKED_CAST")
class BaseFunc<T> : Function<BaseResp<T>, Flowable<T>> {
    override fun apply(t: BaseResp<T>): Flowable<T> {
        if (t.code != SUCCEED) {
            return Flowable.error(BaseException(t.code, t.msg))
        }
        Log.i("taskId","The current taskId is :"+ t.taskId)
        return if (t.data != null) Flowable.just(t.data) else Flowable.just("" as T)
    }
}