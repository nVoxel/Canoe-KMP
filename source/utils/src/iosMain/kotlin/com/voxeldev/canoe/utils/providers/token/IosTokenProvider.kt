package com.voxeldev.canoe.utils.providers.token

import platform.Foundation.NSUserDefaults

/**
 * @author nvoxel
 */
class IosTokenProvider : TokenProvider {

    private val userDefaults = NSUserDefaults.standardUserDefaults()

    override fun getAccessToken(): String? = userDefaults.objectForKey(ACCESS_TOKEN_KEY) as? String

    override fun setAccessToken(accessToken: String) = userDefaults.setObject(accessToken, ACCESS_TOKEN_KEY)

    override fun clearAccessToken() = userDefaults.removeObjectForKey(ACCESS_TOKEN_KEY)

    override fun getRefreshToken(): String? = userDefaults.objectForKey(REFRESH_TOKEN_KEY) as? String

    override fun setRefreshToken(refreshToken: String) = userDefaults.setObject(refreshToken, REFRESH_TOKEN_KEY)

    override fun clearRefreshToken() = userDefaults.removeObjectForKey(REFRESH_TOKEN_KEY)

    private companion object {
        const val ACCESS_TOKEN_KEY = "accessToken"
        const val REFRESH_TOKEN_KEY = "refreshToken"
    }
}
