package com.voxeldev.canoe.utils.parsers

/**
 * @author nvoxel
 */
fun interface AuthenticationCodeParser {
    fun getAuthenticationCode(encodedQuery: String): String?
}
