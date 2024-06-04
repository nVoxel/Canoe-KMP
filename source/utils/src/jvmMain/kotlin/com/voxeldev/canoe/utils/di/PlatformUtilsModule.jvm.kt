package com.voxeldev.canoe.utils.di

import com.voxeldev.canoe.utils.analytics.CommonAnalytics
import com.voxeldev.canoe.utils.analytics.DesktopAnalytics
import com.voxeldev.canoe.utils.platform.DesktopNetworkHandler
import com.voxeldev.canoe.utils.platform.NetworkHandler
import com.voxeldev.canoe.utils.providers.token.DesktopTokenProvider
import com.voxeldev.canoe.utils.providers.token.TokenProvider
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * @author nvoxel
 */
actual val platformUtilsModule: Module = module {

    single<NetworkHandler> { DesktopNetworkHandler() }

    single<TokenProvider> { DesktopTokenProvider() }

    single<CommonAnalytics> { DesktopAnalytics() }
}
