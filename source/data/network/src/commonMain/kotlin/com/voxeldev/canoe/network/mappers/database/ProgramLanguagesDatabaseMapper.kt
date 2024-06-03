package com.voxeldev.canoe.network.mappers.database

import com.voxeldev.canoe.dashboard.api.languages.ProgramLanguage
import com.voxeldev.canoe.dashboard.api.languages.ProgramLanguagesModel
import com.voxeldev.canoe.database.objects.ProgramLanguageObject
import com.voxeldev.canoe.database.objects.ProgramLanguagesObject
import com.voxeldev.canoe.network.wakatime.datasource.response.ProgramLanguageResponse
import com.voxeldev.canoe.network.wakatime.datasource.response.ProgramLanguagesResponse
import io.realm.kotlin.ext.toRealmList
import kotlinx.datetime.Clock

/**
 * @author nvoxel
 */
internal class ProgramLanguagesDatabaseMapper {

    fun toObject(programLanguagesResponse: ProgramLanguagesResponse): ProgramLanguagesObject =
        ProgramLanguagesObject().apply {
            timestamp = Clock.System.now().epochSeconds
            data = programLanguagesResponse.data.map { toObject(it) }.toRealmList()
            total = programLanguagesResponse.total
            totalPages = programLanguagesResponse.totalPages
        }

    private fun toObject(programLanguageResponse: ProgramLanguageResponse): ProgramLanguageObject =
        ProgramLanguageObject().apply {
            id = programLanguageResponse.id
            name = programLanguageResponse.name
            color = programLanguageResponse.color
            isVerified = programLanguageResponse.isVerified
        }

    fun toModel(programLanguagesObject: ProgramLanguagesObject): ProgramLanguagesModel =
        ProgramLanguagesModel(
            data = programLanguagesObject.data.map { it.toModel() },
            total = programLanguagesObject.total,
            totalPages = programLanguagesObject.totalPages,
        )

    private fun ProgramLanguageObject.toModel(): ProgramLanguage =
        ProgramLanguage(
            id = id,
            name = name,
            color = color,
            isVerified = isVerified,
        )
}
