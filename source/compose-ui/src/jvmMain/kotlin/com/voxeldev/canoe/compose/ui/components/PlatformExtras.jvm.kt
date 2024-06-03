package com.voxeldev.canoe.compose.ui.components

import androidx.compose.runtime.Composable

/**
 * @author nvoxel
 */
@Composable
actual fun PlatformInit() {
    println("App is running on: ${System.getProperty("os.name")}")
}

@Composable
actual fun getOrientation(): Orientation = Orientation.LANDSCAPE
