package com.hifive.sdk.injection

import java.lang.annotation.Documented
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy.RUNTIME
import javax.inject.Scope

/**
 * @author Dsh  on 2018/4/16.
 */
@Scope
@Documented
@Retention(RUNTIME)
annotation class CustomScope