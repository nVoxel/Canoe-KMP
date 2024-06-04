package com.voxeldev.canoe.shared.di

import com.voxeldev.canoe.utils.analytics.CommonAnalytics
import org.koin.dsl.module

/**
 * @author nvoxel
 */
internal fun getIosUtilsModule(iosDependencies: IosDependencies) = module {
    single<CommonAnalytics> { iosDependencies.commonAnalytics }
}
