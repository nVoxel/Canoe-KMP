package com.voxeldev.canoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.defaultComponentContext
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.google.firebase.FirebaseApp
import com.voxeldev.canoe.compose.ui.content.root.RootContent
import com.voxeldev.canoe.root.integration.RootComponent
import com.voxeldev.canoe.utils.extensions.checkNotificationsPermission
import com.voxeldev.canoe.utils.extensions.lazyUnsafe
import com.voxeldev.canoe.utils.extensions.registerNotificationsPermissionLauncher

internal class MainActivity : ComponentActivity() {

    private val androidLinkHandler by lazyUnsafe { AndroidLinkHandler(context = this) }

    private val launcher = registerNotificationsPermissionLauncher()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(applicationContext)

        checkNotificationsPermission(launcher)

        val root =
            RootComponent(
                componentContext = defaultComponentContext(),
                storeFactory = DefaultStoreFactory(),
                linkHandler = androidLinkHandler,
                deepLink = intent.data?.toString(),
            )

        setContent {
            RootContent(component = root)
        }
    }
}
