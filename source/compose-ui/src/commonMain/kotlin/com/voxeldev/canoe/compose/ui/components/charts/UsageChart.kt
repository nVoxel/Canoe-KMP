package com.voxeldev.canoe.compose.ui.components.charts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.voxeldev.canoe.compose.ui.components.charts.piechart.charts.DonutPieChart
import com.voxeldev.canoe.compose.ui.components.charts.piechart.charts.PieChart
import com.voxeldev.canoe.compose.ui.components.charts.piechart.models.PieChartConfig
import com.voxeldev.canoe.compose.ui.components.charts.piechart.models.PieChartData
import com.voxeldev.canoe.compose.ui.components.charts.piechart.models.PlotType
import com.voxeldev.canoe.compose.ui.components.conditional
import com.voxeldev.canoe.dashboard.api.languages.ProgramLanguagesModel
import com.voxeldev.canoe.utils.extensions.toColorInt
import com.voxeldev.canoe.utils.platform.Platform
import com.voxeldev.canoe.utils.platform.currentPlatform
import androidx.compose.ui.graphics.Color as ComposeColor

/**
 * @author nvoxel
 */
@Composable
internal fun LanguagesChart(
    modifier: Modifier = Modifier,
    data: List<Pair<String, Pair<Float, String>>>,
    programLanguagesModel: ProgramLanguagesModel,
) {
    UsageChart(
        modifier = modifier,
        data = data.applyColors(programLanguagesModel),
        isDonutChart = false,
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun UsageChart(
    modifier: Modifier = Modifier,
    data: List<Pair<String, Triple<Float, String, Int>>>,
    isDonutChart: Boolean = true,
) {
    val pieChartData = remember(data, isDonutChart) {
        PieChartData(
            slices = data.map {
                PieChartData.Slice(
                    label = it.first,
                    value = it.second.first,
                    color = ComposeColor(color = it.second.third),
                )
            },
            plotType = if (isDonutChart) PlotType.Donut else PlotType.Pie,
        )
    }

    val pieChartConfig = remember {
        PieChartConfig(
            isAnimationEnable = true,
            chartPadding = 25,
            // backgroundColor = ComposeColor.Transparent,
        )
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (isDonutChart) {
            DonutPieChart(
                modifier = modifier
                    .conditional(
                        condition = currentPlatform == Platform.DESKTOP,
                        conditionMetModifier = Modifier.size(size = 300.dp),
                    ),
                pieChartData = pieChartData,
                pieChartConfig = pieChartConfig,
            )
        } else {
            PieChart(
                modifier = modifier
                    .conditional(
                        condition = currentPlatform == Platform.DESKTOP,
                        conditionMetModifier = Modifier.size(size = 300.dp),
                    ),
                pieChartData = pieChartData,
                pieChartConfig = pieChartConfig,
            )
        }

        FlowRow(
            modifier = Modifier
                .padding(all = 16.dp)
                .fillMaxWidth(),
        ) {
            data.forEach {
                SuggestionChip(
                    modifier = Modifier
                        .padding(horizontal = 4.dp),
                    onClick = {},
                    label = { Text(text = "${it.first}: ${it.second.second}") },
                    icon = {
                        Box(
                            modifier = Modifier
                                .size(size = 16.dp)
                                .background(color = ComposeColor(color = it.second.third)),
                        )
                    },
                )
            }
        }
    }
}

private fun List<Pair<String, Pair<Float, String>>>.applyColors(programLanguagesModel: ProgramLanguagesModel) =
    map { entry ->
        val color = programLanguagesModel.data.firstOrNull { programLanguage -> programLanguage.name == entry.first }?.color
        val colorInt = color?.let { (color.removePrefix("#").toLong(16) or 0x00000000FF000000).toInt() }
        entry.first to Triple(entry.second.first, entry.second.second, colorInt ?: entry.first.toColorInt())
    }
