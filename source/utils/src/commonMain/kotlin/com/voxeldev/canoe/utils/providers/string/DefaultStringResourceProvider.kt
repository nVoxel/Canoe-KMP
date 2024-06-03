package com.voxeldev.canoe.utils.providers.string

import Canoe.source.utils.BuildConfig

/**
 * @author nvoxel
 */
internal class DefaultStringResourceProvider : StringResourceProvider {
    override fun getWakaTimeApiBaseUrl(): String = BuildConfig.wakatime_api_base_url
    override fun getWakaTimeOAuthBaseUrl(): String = BuildConfig.wakatime_oauth_base_url
    override fun getWakaTimePhotoBaseUrl(): String = BuildConfig.wakatime_photo_base_url
    override fun getWakaTimeProfileBaseUrl(): String = BuildConfig.wakatime_profile_base_url
    override fun getOAuthAuthorizeUrl(): String = "${getWakaTimeOAuthBaseUrl()}authorize?client_id=${getOAuthClientId()}" +
            "&scope=email,read_logged_time,read_stats&response_type=code&redirect_uri=${getOAuthRedirectUrl()}"

    override fun getOAuthClientId(): String = BuildConfig.oauth_client_id
    override fun getOAuthClientSecret(): String = BuildConfig.oauth_client_secret
    override fun getOAuthRedirectUrl(withDeeplink: Boolean): String =
        if (withDeeplink) BuildConfig.oauth_redirect_url_deeplink else BuildConfig.oauth_redirect_url_inbrowser

    override fun getJavascriptString(): String = "JavaScript"
    override fun getPythonString(): String = "Python"
    override fun getGoString(): String = "Go"
    override fun getJavaString(): String = "Java"
    override fun getKotlinString(): String = "Kotlin"
    override fun getPhpString(): String = "PHP"
    override fun getCSharpString(): String = "C#"
    override fun getIndiaString(): String = "India"
    override fun getChinaString(): String = "China"
    override fun getUsString(): String = "US"
    override fun getBrazilString(): String = "Brazil"
    override fun getRussiaString(): String = "Russia"
    override fun getJapanString(): String = "Japan"
    override fun getGermanyString(): String = "Germany"
}
