package com.voxeldev.canoe.dashboard.store

import com.arkivanov.mvikotlin.core.store.Store
import com.voxeldev.canoe.dashboard.api.alltime.AllTimeModel
import com.voxeldev.canoe.dashboard.api.languages.ProgramLanguagesModel
import com.voxeldev.canoe.dashboard.api.sumaries.SummariesModel
import com.voxeldev.canoe.dashboard.store.DashboardStore.Intent
import com.voxeldev.canoe.dashboard.store.DashboardStore.State
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.char
import kotlinx.datetime.plus

/**
 * @author nvoxel
 */
internal interface DashboardStore : Store<Intent, State, Nothing> {

    sealed class Intent {
        data object ShowDatePickerBottomSheet : Intent()
        data class DismissDatePickerBottomSheet(val startMillis: Long?, val endMillis: Long?) : Intent()
        data object ReloadDashboard : Intent()
        data object DatesReset : Intent()
    }

    data class State(
        val projectName: String? = null,
        val summariesModel: SummariesModel? = null,
        val programLanguagesModel: ProgramLanguagesModel? = null,
        val allTimeModel: AllTimeModel? = null,
        val errorText: String? = null,
        val isSummariesLoading: Boolean = true,
        val isProgramLanguagesLoading: Boolean = true,
        val isAllTimeLoading: Boolean = true,
        val datePickerBottomSheetState: DatePickerBottomSheetState = DatePickerBottomSheetState(),
    )

    data class DatePickerBottomSheetState(
        val active: Boolean,
        val startDate: String,
        val endDate: String,
    ) {

        constructor(
            active: Boolean = false,
            localDateTimeFormat: DateTimeFormat<DateTimeComponents> = DateTimeComponents.Format {
                year()
                char('-')
                monthNumber()
                char('-')
                dayOfMonth()
            },
        ) : this(
            active = active,
            startDate = getCurrentDate(
                localDateTimeFormat = localDateTimeFormat,
                daysOffset = DEFAULT_DAYS_OFFSET,
            ),
            endDate = getCurrentDate(localDateTimeFormat = localDateTimeFormat),
        )

        private companion object {
            fun getCurrentDate(localDateTimeFormat: DateTimeFormat<DateTimeComponents>, daysOffset: Int = 0): String {
                val dateTime = Clock.System.now().plus(
                    period = DateTimePeriod(days = daysOffset),
                    timeZone = TimeZone.UTC,
                )
                return dateTime.format(format = localDateTimeFormat)
            }
        }
    }

    private companion object {
        const val DEFAULT_DAYS_OFFSET = -6
    }
}
