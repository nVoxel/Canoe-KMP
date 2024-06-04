package com.voxeldev.canoe.utils.analytics

/**
 * @author nvoxel
 */
class DesktopAnalytics : CommonAnalytics {

    override fun logEvent(event: CustomEvent) {
        println("Logged ${event.name}")
    }
}
