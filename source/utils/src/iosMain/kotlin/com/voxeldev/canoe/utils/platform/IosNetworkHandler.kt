package com.voxeldev.canoe.utils.platform

import platform.Network.nw_path_get_status
import platform.Network.nw_path_monitor_create
import platform.Network.nw_path_monitor_set_queue
import platform.Network.nw_path_monitor_set_update_handler
import platform.Network.nw_path_monitor_start
import platform.Network.nw_path_status_satisfied
import platform.darwin.dispatch_get_main_queue

/**
 * @author nvoxel
 */
class IosNetworkHandler : NetworkHandler {

    private val monitor = nw_path_monitor_create()

    private var isNetworkAvailable = true

    init {
        nw_path_monitor_set_update_handler(monitor) { path ->
            val pathStatus = nw_path_get_status(path)
            isNetworkAvailable = pathStatus == nw_path_status_satisfied
        }

        nw_path_monitor_set_queue(monitor, dispatch_get_main_queue())
        nw_path_monitor_start(monitor)
    }

    override fun isNetworkAvailable(): Boolean = isNetworkAvailable
}
