package com.voxeldev.canoe.utils.di

import com.voxeldev.canoe.utils.analytics.AndroidAnalytics
import com.voxeldev.canoe.utils.analytics.CommonAnalytics
import com.voxeldev.canoe.utils.platform.AndroidNetworkHandler
import com.voxeldev.canoe.utils.platform.NetworkHandler
import com.voxeldev.canoe.utils.providers.token.AndroidTokenProvider
import com.voxeldev.canoe.utils.providers.token.TokenProvider
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * @author nvoxel
 */
actual val platformUtilsModule: Module = module {

    single<NetworkHandler> { AndroidNetworkHandler(context = androidContext()) }

    single<TokenProvider> { AndroidTokenProvider(context = androidContext()) }

    single<CommonAnalytics> { AndroidAnalytics() }
}
