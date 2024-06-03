package com.voxeldev.canoe.compose.ui.components

import android.content.res.Configuration
import androidx.activity.ComponentActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import com.voxeldev.canoe.compose.ui.content.root.navigationTonalElevation
import com.voxeldev.canoe.compose.ui.theme.BarStyle
import com.voxeldev.canoe.compose.ui.theme.enableEdgeToEdge

/**
 * @author nvoxel
 */
@Composable
actual fun PlatformInit() {
    val navigationBarScrim = MaterialTheme.colorScheme.surfaceColorAtElevation(
        elevation = navigationTonalElevation,
    ).toArgb()

    (LocalContext.current as ComponentActivity).enableEdgeToEdge(
        navigationBarStyle = BarStyle(
            lightScrim = navigationBarScrim,
            darkScrim = navigationBarScrim,
        ),
    )
}

@Composable
actual fun getOrientation(): Orientation =
    when (LocalConfiguration.current.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> Orientation.PORTRAIT
        else -> Orientation.LANDSCAPE
    }
