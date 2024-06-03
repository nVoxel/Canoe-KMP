package com.voxeldev.canoe.utils.di

import com.voxeldev.canoe.utils.platform.IosNetworkHandler
import com.voxeldev.canoe.utils.platform.NetworkHandler
import com.voxeldev.canoe.utils.providers.token.IosTokenProvider
import com.voxeldev.canoe.utils.providers.token.TokenProvider
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * @author nvoxel
 */
actual val platformUtilsModule: Module = module {

    single<NetworkHandler> { IosNetworkHandler() }

    single<TokenProvider> { IosTokenProvider() }
}
