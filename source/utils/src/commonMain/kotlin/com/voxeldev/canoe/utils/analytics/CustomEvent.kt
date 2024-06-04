package com.voxeldev.canoe.utils.analytics

/**
 * @author nvoxel
 */
sealed class CustomEvent(
    val name: String,
) {

    data object LoadedSummaries : CustomEvent(name = "loaded_summaries")
    data object LoadedProgramLanguages : CustomEvent(name = "loaded_languages")
    data object LoadedAllTime : CustomEvent(name = "loaded_all_time")
    data object LoadedLeaderboards : CustomEvent(name = "loaded_leaderboards")
    data object LoadedProjects : CustomEvent(name = "loaded_projects")
    data object Login : CustomEvent(name = "login")
    data object Logout : CustomEvent(name = "logout")
}
