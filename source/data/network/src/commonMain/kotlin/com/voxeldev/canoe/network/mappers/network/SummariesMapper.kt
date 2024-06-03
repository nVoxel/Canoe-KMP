package com.voxeldev.canoe.network.mappers.network

import com.voxeldev.canoe.dashboard.api.sumaries.CumulativeTotal
import com.voxeldev.canoe.dashboard.api.sumaries.DEFAULT_EMPTY_VALUE
import com.voxeldev.canoe.dashboard.api.sumaries.DailyAverage
import com.voxeldev.canoe.dashboard.api.sumaries.DailyChartData
import com.voxeldev.canoe.dashboard.api.sumaries.SummariesModel
import com.voxeldev.canoe.network.wakatime.datasource.response.CumulativeTotalResponse
import com.voxeldev.canoe.network.wakatime.datasource.response.DailyAverageResponse
import com.voxeldev.canoe.network.wakatime.datasource.response.GeneralizedEntityResponse
import com.voxeldev.canoe.network.wakatime.datasource.response.SummariesResponse
import com.voxeldev.canoe.utils.extensions.toColorInt
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char

/**
 * @author nvoxel
 */
internal class SummariesMapper {

    fun toModel(summariesResponse: SummariesResponse): SummariesModel =
        SummariesModel(
            dailyChartData = getDailyChartData(summariesResponse),
            languagesChartData = summariesResponse.data.flatMap { it.languages }.toGeneralizedList(),
            editorsChartData = summariesResponse.data.flatMap { it.editors }.toGeneralizedList().applyColors(),
            operatingSystemsChartData = summariesResponse.data.flatMap { it.operatingSystems }.toGeneralizedList().applyColors(),
            machinesChartData = summariesResponse.data.flatMap { it.machines }.toGeneralizedList().applyColors(),
            cumulativeTotal = summariesResponse.cumulativeTotal.toModel(),
            dailyAverage = summariesResponse.dailyAverage.toModel(),
        )

    private fun <T : GeneralizedEntityResponse> List<T>.toGeneralizedList() = this
        .groupBy { it.name }
        .mapValues { pair -> pair.value.sumOf { it.totalSeconds }.let { it to it.toLong().toHMS() } }
        .toList()
        .sortedByDescending { it.second.first }

    private fun getDailyChartData(summariesResponse: SummariesResponse): DailyChartData {
        val fromDateTimeFormat = DateTimeComponents.Format {
            year()
            char('-')
            monthNumber()
            char('-')
            dayOfMonth()
        }
        val toDateTimeFormat = DateTimeComponents.Format {
            monthName(MonthNames.ENGLISH_ABBREVIATED)
            char(' ')
            dayOfMonth()
        }

        val projectsSeries = hashMapOf<String, MutableList<Pair<Float, String>>>()
        val totalLabels = mutableListOf<Pair<Float, String>>()
        val horizontalLabels = mutableListOf<String>()
        var currentDay = 1

        summariesResponse.data.forEach { day ->
            day.projects?.forEach { project ->
                val time = project.decimal
                val pair = (if (time > 0f) time else DEFAULT_EMPTY_VALUE) to "${project.name}: ${project.text}"

                projectsSeries
                    .getOrPut(key = project.name) {
                        if (currentDay != 1) {
                            MutableList(currentDay - 1) { DEFAULT_EMPTY_VALUE to "${project.name}: 0s" }
                        } else {
                            mutableListOf()
                        }
                    }
                    .add(pair)
            }

            totalLabels.add(day.grandTotal.totalSeconds to day.grandTotal.text)

            val date = fromDateTimeFormat.parse(day.range.date)
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

    private fun CumulativeTotalResponse.toModel(): CumulativeTotal =
        CumulativeTotal(
            seconds = seconds,
            text = text,
            digital = digital,
        )

    private fun DailyAverageResponse.toModel(): DailyAverage =
        DailyAverage(
            seconds = seconds,
            text = text,
        )

    internal companion object {
        const val ISO_8601_DATE_FORMAT = "yyyy-MM-dd"
        const val AXIS_FORMAT = "MMM dd"

        private const val SECONDS_IN_HOUR = 3600
        private const val SECONDS_IN_MINUTE = 60
        private const val MAX_ONE_DIGIT_NUMBER = 9

        fun List<Pair<String, Pair<Float, String>>>.applyColors() =
            map { entry -> entry.first to Triple(entry.second.first, entry.second.second, entry.first.toColorInt()) }

        inline fun <T> Iterable<T>.sumOf(selector: (T) -> Float): Float {
            var sum = 0f
            for (element in this) {
                sum += selector(element)
            }
            return sum
        }

        fun Long.toHMS(): String {
            val hours = this / SECONDS_IN_HOUR
            val minutes = (this % SECONDS_IN_HOUR) / SECONDS_IN_MINUTE
            val remainingSeconds = this % SECONDS_IN_MINUTE

            val timeComponents = mutableListOf<String>()

            if (hours > 0) {
                timeComponents.add(
                    "${if (hours > MAX_ONE_DIGIT_NUMBER) hours.toString() else "0$hours"}h",
                )
            }

            if (minutes > 0 || hours > 0) {
                timeComponents.add(
                    "${if (minutes > MAX_ONE_DIGIT_NUMBER) minutes.toString() else "0$minutes"}m",
                )
            }

            timeComponents.add(
                "${if (remainingSeconds > MAX_ONE_DIGIT_NUMBER) remainingSeconds.toString() else "0$remainingSeconds"}s",
            )

            return timeComponents.joinToString(" ")
        }
    }
}
