package com.hifive.sdk.injection.component

import com.hifive.sdk.injection.CustomScope
import com.hifive.sdk.injection.module.ServiceModule
import com.hifive.sdk.controller.BaseController
import dagger.Component

/**
 * @author Dsh  imkobedroid@gmail.com
 * @date 2019-07-09
 */

@CustomScope
@Component(modules = [ServiceModule::class])
interface ServiceComponent {
    fun inject(service: BaseController)
}