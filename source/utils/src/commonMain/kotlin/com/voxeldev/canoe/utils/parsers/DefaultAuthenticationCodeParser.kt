package com.voxeldev.canoe.utils.parsers

import com.voxeldev.canoe.utils.platform.Platform
import com.voxeldev.canoe.utils.platform.currentPlatform

/**
 * @author nvoxel
 */
internal class DefaultAuthenticationCodeParser : AuthenticationCodeParser {

    override fun getAuthenticationCode(encodedQuery: String): String? =
        if (currentPlatform == Platform.DESKTOP) {
            encodedQuery
        } else {
            encodedQuery.split('=').getOrNull(1)
        }
}
