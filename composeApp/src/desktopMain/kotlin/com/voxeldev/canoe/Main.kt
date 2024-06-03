package com.voxeldev.canoe

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.voxeldev.canoe.compose.ui.content.root.RootContent
import com.voxeldev.canoe.dashboard.di.dashboardFeatureModule
import com.voxeldev.canoe.leaderboards.di.leaderboardsFeatureModule
import com.voxeldev.canoe.projects.di.projectsFeatureModule
import com.voxeldev.canoe.root.integration.LinkHandler
import com.voxeldev.canoe.root.integration.RootComponent
import com.voxeldev.canoe.settings.di.settingsFeatureModule
import org.koin.compose.KoinApplication
import org.koin.core.KoinApplication
import org.koin.core.logger.Level
import org.koin.core.logger.Logger
import org.koin.core.logger.MESSAGE
import java.awt.Desktop
import java.net.URI

/**
 * @author nvoxel
 */
fun main() {
    val lifecycle = LifecycleRegistry()

    application {
        val state = rememberWindowState()

        val linkHandler = LinkHandler {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(URI.create(it))
            }
        }

        KoinApplication(application = { initKoin() }) {
            val root = runOnUiThread {
                RootComponent(
                    componentContext = DefaultComponentContext(
                        lifecycle = lifecycle,
                    ),
                    storeFactory = DefaultStoreFactory(),
                    linkHandler = linkHandler,
                    deepLink = null,
                )
            }

            Window(
                state = state,
                onCloseRequest = ::exitApplication,
                title = "Canoe",
            ) {
                RootContent(component = root)
            }
        }
    }
}

private fun KoinApplication.initKoin() {
    logger(object : Logger() {
        override fun display(level: Level, msg: MESSAGE) {
            println("[$level]: $msg")
        }
    })

    modules(dashboardFeatureModule, leaderboardsFeatureModule, projectsFeatureModule, settingsFeatureModule)
}
