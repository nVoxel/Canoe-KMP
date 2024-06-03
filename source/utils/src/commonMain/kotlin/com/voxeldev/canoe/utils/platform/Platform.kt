package com.voxeldev.canoe.utils.platform

/**
 * @author nvoxel
 */
enum class Platform {
    ANDROID, IOS, DESKTOP
}

expect val currentPlatform: Platform
