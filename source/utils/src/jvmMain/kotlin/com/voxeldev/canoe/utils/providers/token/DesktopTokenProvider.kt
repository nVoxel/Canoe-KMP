package com.voxeldev.canoe.utils.providers.token

import java.util.prefs.Preferences

/**
 * @author nvoxel
 */
class DesktopTokenProvider : TokenProvider {

    private val preferences: Preferences = Preferences.userRoot().node(PREFERENCES_NODE)

    override fun getAccessToken(): String? = preferences.get(ACCESS_TOKEN_PREF_NAME, null)

    override fun setAccessToken(accessToken: String) = preferences.put(ACCESS_TOKEN_PREF_NAME, accessToken)

    override fun clearAccessToken() = preferences.remove(ACCESS_TOKEN_PREF_NAME)

    override fun getRefreshToken(): String? = preferences.get(REFRESH_TOKEN_PREF_NAME, null)

    override fun setRefreshToken(refreshToken: String) = preferences.put(REFRESH_TOKEN_PREF_NAME, refreshToken)

    override fun clearRefreshToken() = preferences.remove(REFRESH_TOKEN_PREF_NAME)

    private companion object {
        const val PREFERENCES_NODE = "com.voxeldev.canoe"
        const val ACCESS_TOKEN_PREF_NAME = "accessToken"
        const val REFRESH_TOKEN_PREF_NAME = "refreshToken"
    }
}
