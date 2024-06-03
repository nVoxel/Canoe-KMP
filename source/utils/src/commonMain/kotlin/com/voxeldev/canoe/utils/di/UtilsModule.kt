package com.voxeldev.canoe.utils.di

import com.voxeldev.canoe.utils.parsers.AuthenticationCodeParser
import com.voxeldev.canoe.utils.parsers.DefaultAuthenticationCodeParser
import com.voxeldev.canoe.utils.providers.string.DefaultStringResourceProvider
import com.voxeldev.canoe.utils.providers.string.StringResourceProvider
import org.koin.dsl.module

/**
 * @author nvoxel
 */
val utilsModule = module {

    includes(platformUtilsModule)

    single<AuthenticationCodeParser> { DefaultAuthenticationCodeParser() }

    single<StringResourceProvider> { DefaultStringResourceProvider() }
}
