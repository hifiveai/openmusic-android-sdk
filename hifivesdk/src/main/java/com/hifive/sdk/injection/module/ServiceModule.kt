package com.hifive.sdk.injection.module

import com.hifive.sdk.service.Service
import com.hifive.sdk.service.impl.ServiceImpl
import dagger.Module
import dagger.Provides

/**
 * @author Dsh  imkobedroid@gmail.com
 * @date 2019-07-09
 */

@Module
class ServiceModule {
    @Provides
    fun provideService(service: ServiceImpl): Service {
        return service
    }
}