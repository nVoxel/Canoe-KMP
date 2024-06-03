package com.voxeldev.canoe.network.mappers.network

import com.voxeldev.canoe.dashboard.api.languages.ProgramLanguage
import com.voxeldev.canoe.dashboard.api.languages.ProgramLanguagesModel
import com.voxeldev.canoe.network.wakatime.datasource.response.ProgramLanguageResponse
import com.voxeldev.canoe.network.wakatime.datasource.response.ProgramLanguagesResponse

/**
 * @author nvoxel
 */
internal class ProgramLanguagesMapper {

    fun toModel(programLanguagesResponse: ProgramLanguagesResponse): ProgramLanguagesModel =
        ProgramLanguagesModel(
            data = programLanguagesResponse.data.map { it.toModel() },
            total = programLanguagesResponse.total,
            totalPages = programLanguagesResponse.totalPages,
        )

    private fun ProgramLanguageResponse.toModel(): ProgramLanguage =
        ProgramLanguage(
            id = id,
            name = name,
            color = color,
            isVerified = isVerified,
        )
}
