package com.voxeldev.canoe.utils.providers.string

import com.voxeldev.canoe.utils.platform.Platform
import com.voxeldev.canoe.utils.platform.currentPlatform

/**
 * @author nvoxel
 */
interface StringResourceProvider {
    fun getWakaTimeApiBaseUrl(): String
    fun getWakaTimeOAuthBaseUrl(): String
    fun getWakaTimePhotoBaseUrl(): String
    fun getWakaTimeProfileBaseUrl(): String
    fun getOAuthAuthorizeUrl(): String

    fun getOAuthClientId(): String
    fun getOAuthClientSecret(): String
    fun getOAuthRedirectUrl(withDeeplink: Boolean = currentPlatform != Platform.DESKTOP): String

    fun getJavascriptString(): String
    fun getPythonString(): String
    fun getGoString(): String
    fun getJavaString(): String
    fun getKotlinString(): String
    fun getPhpString(): String
    fun getCSharpString(): String
    fun getIndiaString(): String
    fun getChinaString(): String
    fun getUsString(): String
    fun getBrazilString(): String
    fun getRussiaString(): String
    fun getJapanString(): String
    fun getGermanyString(): String
}
