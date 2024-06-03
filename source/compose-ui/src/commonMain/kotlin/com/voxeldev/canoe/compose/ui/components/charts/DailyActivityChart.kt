package com.voxeldev.canoe.compose.ui.components.charts

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.voxeldev.canoe.compose.ui.theme.LocalChartColorsPalette
import com.voxeldev.canoe.dashboard.api.sumaries.DailyChartData
import com.voxeldev.canoe.utils.platform.Platform
import com.voxeldev.canoe.utils.platform.currentPlatform
import io.github.koalaplot.core.ChartLayout
import io.github.koalaplot.core.bar.DefaultVerticalBar
import io.github.koalaplot.core.bar.StackedVerticalBarPlot
import io.github.koalaplot.core.bar.VerticalBarPlotStackedPointEntry
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import io.github.koalaplot.core.xygraph.CategoryAxisModel
import io.github.koalaplot.core.xygraph.DoubleLinearAxisModel
import io.github.koalaplot.core.xygraph.XYGraph

internal const val SECONDS_IN_HOUR = 3600
private const val TIME_VALUE_THRESHOLD = 0.1e-3

/**
 * @author nvoxel
 */
@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
internal fun DailyActivityChart(data: DailyChartData) {
    val max = remember(data) {
        data.totalLabels.maxOf { it.first / SECONDS_IN_HOUR }.toDouble()
    }

    val defaultColor = Color.Red
    val colors = listOf(
        LocalChartColorsPalette.current.red,
        LocalChartColorsPalette.current.green,
        LocalChartColorsPalette.current.yellow,
        LocalChartColorsPalette.current.blue,
        LocalChartColorsPalette.current.cyan,
        LocalChartColorsPalette.current.orange,
        LocalChartColorsPalette.current.magenta,
        LocalChartColorsPalette.current.purple,
    )

    val mappedData = remember(data) { mapData(data = data) }
    val mappedColors = remember(data) { mapColors(data = data, colors = colors) }

    ChartLayout(
        modifier = Modifier
            .padding(start = 20.dp, bottom = 20.dp)
            .height(height = 300.dp),
        content = {
            XYGraph(
                xAxisModel = CategoryAxisModel(categories = data.horizontalLabels),
                yAxisModel = DoubleLinearAxisModel(range = 0.0..max),
                yAxisTitle = if (currentPlatform == Platform.DESKTOP) "Hours" else null,
                panZoomEnabled = false,
            ) {
                StackedVerticalBarPlot(
                    data = barChartEntries(bars = mappedData),
                    bar = { dayIndex, projectIndex ->
                        val bar = mappedData[dayIndex]
                        val projectName = bar.stackedBars[projectIndex].projectName

                        DefaultVerticalBar(
                            brush = SolidColor(value = mappedColors[projectName] ?: defaultColor),
                            modifier = Modifier.fillMaxWidth(),
                            hoverElement = { Marker(bar = bar) },
                        )
                    },
                    animationSpec = tween(durationMillis = 0), // temp solution to disable animation
                )
            }
        },
    )
}

@Composable
private fun Marker(bar: Bar) {
    OutlinedCard {
        Column(
            modifier = Modifier
                .padding(all = 8.dp),
        ) {
            Text(
                text = "Total: ${bar.totalTimeString}",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
            )

            bar.stackedBars.sortedByDescending { it.projectTimeValue }.forEach { stackedBar ->
                if (stackedBar.projectTimeValue > TIME_VALUE_THRESHOLD) {
                    Text(
                        text = stackedBar.projectTimeString,
                        fontSize = 12.sp,
                    )
                }
            }
        }
    }
}

private fun mapData(data: DailyChartData): List<Bar> =
    data.totalLabels.mapIndexed { index, total ->
        val stackedBars = data.projectsSeries.mapNotNull {
            if (it.value.size <= index) return@mapNotNull null
            return@mapNotNull StackedBar(
                projectName = it.key,
                projectTimeValue = it.value[index].first.toDouble(),
                projectTimeString = it.value[index].second,
            )
        }

        Bar(
            caption = data.horizontalLabels[index],
            totalTimeValue = total.first.toDouble(),
            totalTimeString = total.second,
            stackedBars = stackedBars,
        )
    }

private fun mapColors(data: DailyChartData, colors: List<Color>): Map<String, Color> =
    data.projectsSeries.keys.mapIndexed { index, projectName ->
        projectName to colors[index % colors.size]
    }.toMap()

private fun barChartEntries(bars: List<Bar>): List<VerticalBarPlotStackedPointEntry<String, Double>> {
    return bars.map { bar ->
        object : VerticalBarPlotStackedPointEntry<String, Double> {
            override val x: String = bar.caption
            override val yOrigin: Double = 0.0

            override val y: List<Double> = bar.stackedBars.map { it.projectTimeValue }
        }
    }
}

private data class Bar(
    val caption: String,
    val totalTimeValue: Double,
    val totalTimeString: String,
    val stackedBars: List<StackedBar>,
)

private data class StackedBar(
    val projectName: String,
    val projectTimeValue: Double,
    val projectTimeString: String,
)
