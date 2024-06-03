package com.voxeldev.canoe.shared.di

import com.voxeldev.canoe.dashboard.di.dashboardFeatureModule
import com.voxeldev.canoe.leaderboards.di.leaderboardsFeatureModule
import com.voxeldev.canoe.projects.di.projectsFeatureModule
import com.voxeldev.canoe.settings.di.settingsFeatureModule
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.logger.Logger
import org.koin.core.logger.MESSAGE

/**
 * @author nvoxel
 */
fun initKoin() {
    startKoin {
        logger(object : Logger() {
            override fun display(level: Level, msg: MESSAGE) {
                println("[$level]: $msg")
            }
        })

        modules(dashboardFeatureModule, leaderboardsFeatureModule, projectsFeatureModule, settingsFeatureModule)
    }
}
