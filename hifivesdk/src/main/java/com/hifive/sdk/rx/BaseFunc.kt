package com.hifive.sdk.rx

import com.hifive.sdk.common.BaseConstance.Companion.SUCCEED
import com.hifive.sdk.protocol.BaseResp
import io.reactivex.Flowable
import io.reactivex.functions.Function

/**
 * @author Dsh  imkobedroid@gmail.com
 * @date 2019-07-09
 */
class BaseFunc<T> : Function<BaseResp<T>, Flowable<T>> {
    override fun apply(t: BaseResp<T>): Flowable<T> {
        if (t.code != SUCCEED) {
            return Flowable.error(BaseException(t.code, t.msg))
        }
        return if (t.data != null) Flowable.just(t.data) else Flowable.error(
            BaseException(
                t.code,
                t.msg
            )
        )
    }
}