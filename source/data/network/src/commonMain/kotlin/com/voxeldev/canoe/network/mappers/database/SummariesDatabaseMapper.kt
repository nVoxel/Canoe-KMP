package com.voxeldev.canoe.network.mappers.database

import com.voxeldev.canoe.dashboard.api.sumaries.CumulativeTotal
import com.voxeldev.canoe.dashboard.api.sumaries.DEFAULT_EMPTY_VALUE
import com.voxeldev.canoe.dashboard.api.sumaries.DailyAverage
import com.voxeldev.canoe.dashboard.api.sumaries.DailyChartData
import com.voxeldev.canoe.dashboard.api.sumaries.GrandTotal
import com.voxeldev.canoe.dashboard.api.sumaries.Range
import com.voxeldev.canoe.dashboard.api.sumaries.SummariesModel
import com.voxeldev.canoe.database.objects.CategoryObject
import com.voxeldev.canoe.database.objects.CumulativeTotalObject
import com.voxeldev.canoe.database.objects.DailyAverageObject
import com.voxeldev.canoe.database.objects.DependencyObject
import com.voxeldev.canoe.database.objects.EditorObject
import com.voxeldev.canoe.database.objects.GeneralizedEntityObject
import com.voxeldev.canoe.database.objects.GrandTotalObject
import com.voxeldev.canoe.database.objects.LanguageObject
import com.voxeldev.canoe.database.objects.MachineObject
import com.voxeldev.canoe.database.objects.OperatingSystemObject
import com.voxeldev.canoe.database.objects.RangeObject
import com.voxeldev.canoe.database.objects.SummariesDayObject
import com.voxeldev.canoe.database.objects.SummariesObject
import com.voxeldev.canoe.database.objects.SummaryProjectObject
import com.voxeldev.canoe.network.mappers.network.SummariesMapper.Companion.applyColors
import com.voxeldev.canoe.network.mappers.network.SummariesMapper.Companion.sumOf
import com.voxeldev.canoe.network.mappers.network.SummariesMapper.Companion.toHMS
import com.voxeldev.canoe.network.wakatime.datasource.response.CategoryResponse
import com.voxeldev.canoe.network.wakatime.datasource.response.CumulativeTotalResponse
import com.voxeldev.canoe.network.wakatime.datasource.response.DailyAverageResponse
import com.voxeldev.canoe.network.wakatime.datasource.response.DependencyResponse
import com.voxeldev.canoe.network.wakatime.datasource.response.EditorResponse
import com.voxeldev.canoe.network.wakatime.datasource.response.GrandTotalResponse
import com.voxeldev.canoe.network.wakatime.datasource.response.LanguageResponse
import com.voxeldev.canoe.network.wakatime.datasource.response.MachineResponse
import com.voxeldev.canoe.network.wakatime.datasource.response.OperatingSystemResponse
import com.voxeldev.canoe.network.wakatime.datasource.response.RangeResponse
import com.voxeldev.canoe.network.wakatime.datasource.response.SummariesDayResponse
import com.voxeldev.canoe.network.wakatime.datasource.response.SummariesResponse
import com.voxeldev.canoe.network.wakatime.datasource.response.SummaryProjectResponse
import io.realm.kotlin.ext.toRealmList
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlinx.datetime.toInstant

/**
 * @author nvoxel
 */
internal class SummariesDatabaseMapper {

    private val fromDateTimeFormat = DateTimeComponents.Format {
        year()
        char('-')
        monthNumber()
        char('-')
        dayOfMonth()
    }
    private val toDateTimeFormat = DateTimeComponents.Format {
        monthName(MonthNames.ENGLISH_ABBREVIATED)
        char(' ')
        dayOfMonth()
    }

    fun toObject(summariesResponse: SummariesResponse, projectName: String?): SummariesObject =
        SummariesObject().apply {
            timestamp = Clock.System.now().epochSeconds
            project = projectName
            data = summariesResponse.data.map { toObject(it) }.toRealmList()
            cumulativeTotal = toObject(summariesResponse.cumulativeTotal)
            dailyAverage = toObject(summariesResponse.dailyAverage)
            start = summariesResponse.start.toUnixTimestamp()
            end = summariesResponse.end.toUnixTimestamp()
        }

    private fun String.toUnixTimestamp(): Long =
        LocalDateTime
            .parse(this.dropLast(1))
            .toInstant(TimeZone.UTC)
            .epochSeconds

    private fun toObject(summariesDayResponse: SummariesDayResponse): SummariesDayObject =
        SummariesDayObject().apply {
            grandTotal = toObject(summariesDayResponse.grandTotal)
            categories = summariesDayResponse.categories.map { toObject(it) }.toRealmList()
            summariesDayResponse.projects?.let { projects = it.map { toObject(it) }.toRealmList() }
            languages = summariesDayResponse.languages.map { toObject(it) }.toRealmList()
            editors = summariesDayResponse.editors.map { toObject(it) }.toRealmList()
            operatingSystems = summariesDayResponse.operatingSystems.map { toObject(it) }.toRealmList()
            dependencies = summariesDayResponse.dependencies.map { toObject(it) }.toRealmList()
            machines = summariesDayResponse.machines.map { toObject(it) }.toRealmList()
            range = toObject(summariesDayResponse.range)
        }

    private fun toObject(grandTotalResponse: GrandTotalResponse): GrandTotalObject =
        GrandTotalObject().apply {
            digital = grandTotalResponse.digital
            hours = grandTotalResponse.hours
            minutes = grandTotalResponse.minutes
            text = grandTotalResponse.text
            totalSeconds = grandTotalResponse.totalSeconds
        }

    private fun toObject(categoryResponse: CategoryResponse): CategoryObject =
        CategoryObject().apply {
            name = categoryResponse.name
            totalSeconds = categoryResponse.totalSeconds
            percent = categoryResponse.percent
            digital = categoryResponse.digital
            text = categoryResponse.text
            hours = categoryResponse.hours
            minutes = categoryResponse.minutes
        }

    private fun toObject(summaryProjectResponse: SummaryProjectResponse): SummaryProjectObject =
        SummaryProjectObject().apply {
            name = summaryProjectResponse.name
            totalSeconds = summaryProjectResponse.totalSeconds
            percent = summaryProjectResponse.percent
            digital = summaryProjectResponse.digital
            decimal = summaryProjectResponse.decimal
            text = summaryProjectResponse.text
            hours = summaryProjectResponse.hours
            minutes = summaryProjectResponse.minutes
        }

    private fun toObject(languageResponse: LanguageResponse): LanguageObject =
        LanguageObject().apply {
            name = languageResponse.name
            totalSeconds = languageResponse.totalSeconds
            percent = languageResponse.percent
            digital = languageResponse.digital
            text = languageResponse.text
            hours = languageResponse.hours
            minutes = languageResponse.minutes
        }

    private fun toObject(editorResponse: EditorResponse): EditorObject =
        EditorObject().apply {
            name = editorResponse.name
            totalSeconds = editorResponse.totalSeconds
            percent = editorResponse.percent
            digital = editorResponse.digital
            text = editorResponse.text
            hours = editorResponse.hours
            minutes = editorResponse.minutes
            seconds = editorResponse.seconds
        }

    private fun toObject(operatingSystemResponse: OperatingSystemResponse): OperatingSystemObject =
        OperatingSystemObject().apply {
            name = operatingSystemResponse.name
            totalSeconds = operatingSystemResponse.totalSeconds
            percent = operatingSystemResponse.percent
            digital = operatingSystemResponse.digital
            text = operatingSystemResponse.text
            hours = operatingSystemResponse.hours
            minutes = operatingSystemResponse.minutes
            seconds = operatingSystemResponse.seconds
        }

    private fun toObject(dependencyResponse: DependencyResponse): DependencyObject =
        DependencyObject().apply {
            name = dependencyResponse.name
            totalSeconds = dependencyResponse.totalSeconds
            percent = dependencyResponse.percent
            digital = dependencyResponse.digital
            text = dependencyResponse.text
            hours = dependencyResponse.hours
            minutes = dependencyResponse.minutes
            seconds = dependencyResponse.seconds
        }

    private fun toObject(machineResponse: MachineResponse): MachineObject =
        MachineObject().apply {
            name = machineResponse.name
            machineNameId = machineResponse.machineNameId
            totalSeconds = machineResponse.totalSeconds
            percent = machineResponse.percent
            digital = machineResponse.digital
            text = machineResponse.text
            hours = machineResponse.hours
            minutes = machineResponse.minutes
            seconds = machineResponse.seconds
        }

    private fun toObject(rangeResponse: RangeResponse): RangeObject =
        RangeObject().apply {
            date = rangeResponse.date
            text = rangeResponse.text
        }

    private fun toObject(cumulativeTotalResponse: CumulativeTotalResponse): CumulativeTotalObject =
        CumulativeTotalObject().apply {
            seconds = cumulativeTotalResponse.seconds
            text = cumulativeTotalResponse.text
            digital = cumulativeTotalResponse.digital
        }

    private fun toObject(dailyAverageResponse: DailyAverageResponse): DailyAverageObject =
        DailyAverageObject().apply {
            seconds = dailyAverageResponse.seconds
            text = dailyAverageResponse.text
        }

    fun toModel(summariesObject: SummariesObject): SummariesModel =
        SummariesModel(
            dailyChartData = getDailyChartData(summariesObject),
            languagesChartData = summariesObject.data.flatMap { it.languages }.toGeneralizedList(),
            editorsChartData = summariesObject.data.flatMap { it.editors }.toGeneralizedList().applyColors(),
            operatingSystemsChartData = summariesObject.data.flatMap { it.operatingSystems }.toGeneralizedList().applyColors(),
            machinesChartData = summariesObject.data.flatMap { it.machines }.toGeneralizedList().applyColors(),
            cumulativeTotal = summariesObject.cumulativeTotal?.toModel() ?: defaultCumulativeTotal,
            dailyAverage = summariesObject.dailyAverage?.toModel() ?: defaultDailyAverage,
        )

    private fun <T : GeneralizedEntityObject> List<T>.toGeneralizedList() = this
        .groupBy { it.name }
        .mapValues { pair -> pair.value.sumOf { it.totalSeconds }.let { it to it.toLong().toHMS() } }
        .toList()
        .sortedByDescending { it.second.first }

    private fun getDailyChartData(summariesObject: SummariesObject): DailyChartData {
        val projectsSeries = hashMapOf<String, MutableList<Pair<Float, String>>>()
        val totalLabels = mutableListOf<Pair<Float, String>>()
        val horizontalLabels = mutableListOf<String>()
        var currentDay = 1

        summariesObject.data.forEach { day ->
            day.projects.forEach { project ->
                val time = project.decimal
                val pair = (if (time > 0f) time else DEFAULT_EMPTY_VALUE) to "${project.name}: ${project.text}"

                projectsSeries
                    .getOrPut(key = project.name) {
                        if (currentDay != 1) {
                            MutableList(
                                currentDay - 1,
                            ) { DEFAULT_EMPTY_VALUE to "${project.name}: 0s" }
                        } else {
                            mutableListOf()
                        }
                    }
                    .add(pair)
            }

            totalLabels.add(
                (day.grandTotal?.totalSeconds ?: defaultGrandTotal.totalSeconds) to (day.grandTotal?.text ?: defaultGrandTotal.text),
            )

            val date = fromDateTimeFormat.parse(day.range?.date ?: defaultRange.date)
            horizontalLabels.add(toDateTimeFormat.format(date))

            projectsSeries.keys.forEach { key ->
                val value = projectsSeries.getValue(key)
                if (value.size < currentDay) value.add(DEFAULT_EMPTY_VALUE to "$key: 0s")
            }
            currentDay++
        }

        return DailyChartData(
            projectsSeries = projectsSeries,
            totalLabels = totalLabels,
            horizontalLabels = horizontalLabels,
        )
    }

    private fun CumulativeTotalObject.toModel(): CumulativeTotal =
        CumulativeTotal(
            seconds = seconds,
            text = text,
            digital = digital,
        )

    private fun DailyAverageObject.toModel(): DailyAverage =
        DailyAverage(
            seconds = seconds,
            text = text,
        )

    private companion object {
        val defaultCumulativeTotal: CumulativeTotal = CumulativeTotal(seconds = 0f, text = "", digital = "")
        val defaultDailyAverage: DailyAverage = DailyAverage(seconds = 0f, text = "")
        val defaultGrandTotal: GrandTotal = GrandTotal(
            digital = "",
            hours = 0,
            minutes = 0,
            text = "",
            totalSeconds = 0f,
        )
        val defaultRange: Range = Range(date = "", text = "")
    }
}
