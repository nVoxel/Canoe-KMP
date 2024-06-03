package com.voxeldev.canoe.utils.platform

import Canoe.source.utils.BuildConfig
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import java.net.URLConnection

/**
 * @author nvoxel
 */
class DesktopNetworkHandler : NetworkHandler {

    override fun isNetworkAvailable(): Boolean {
        return try {
            val url = URL(BuildConfig.wakatime_base_url)
            val conn: URLConnection = url.openConnection()
            conn.connectTimeout = CONNECTION_TIMEOUT
            conn.connect()
            conn.getInputStream().close()
            true
        } catch (e: MalformedURLException) {
            throw IllegalStateException(e)
        } catch (e: IOException) {
            false
        }
    }

    private companion object {
        const val CONNECTION_TIMEOUT = 5000
    }
}
