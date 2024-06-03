package com.voxeldev.canoe.compose.ui.components

import androidx.compose.runtime.Composable

/**
 * @author nvoxel
 */
@Composable
expect fun PlatformInit()

enum class Orientation {
    PORTRAIT, LANDSCAPE
}

@Composable
expect fun getOrientation(): Orientation
