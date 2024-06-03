package com.voxeldev.canoe.compose.ui.components.charts

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.voxeldev.canoe.dashboard.api.sumaries.DailyChartData
import com.voxeldev.canoe.utils.platform.Platform
import com.voxeldev.canoe.utils.platform.currentPlatform
import io.github.koalaplot.core.ChartLayout
import io.github.koalaplot.core.line.AreaBaseline
import io.github.koalaplot.core.line.AreaPlot
import io.github.koalaplot.core.style.AreaStyle
import io.github.koalaplot.core.style.LineStyle
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import io.github.koalaplot.core.xygraph.CategoryAxisModel
import io.github.koalaplot.core.xygraph.FloatLinearAxisModel
import io.github.koalaplot.core.xygraph.Point
import io.github.koalaplot.core.xygraph.XYGraph

/**
 * @author nvoxel
 */
@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
internal fun ProjectActivityChart(data: DailyChartData) {
    val max = remember(data) {
        data.totalLabels.maxOf { it.first / SECONDS_IN_HOUR }
    }

    val mappedData = remember(data) { mapData(data = data) }

    ChartLayout(
        modifier = Modifier
            .padding(start = 20.dp, bottom = 20.dp)
            .height(height = 300.dp),
        content = {
            XYGraph(
                xAxisModel = CategoryAxisModel(categories = data.horizontalLabels),
                yAxisModel = FloatLinearAxisModel(0f..max),
                yAxisTitle = if (currentPlatform == Platform.DESKTOP) "Hours" else null,
            ) {
                AreaPlot(
                    data = mappedData,
                    areaBaseline = AreaBaseline.ConstantLine(0f),
                    areaStyle = AreaStyle(
                        brush = SolidColor(value = MaterialTheme.colorScheme.primary),
                        alpha = .5f,
                    ),
                    lineStyle = LineStyle(
                        brush = SolidColor(value = MaterialTheme.colorScheme.primary),
                        strokeWidth = 2.dp,
                    ),
                )
            }
        },
    )
}

private fun mapData(data: DailyChartData): List<Point<String, Float>> =
    data.horizontalLabels.mapIndexed { index, day ->
        Point(x = day, y = data.totalLabels[index].first / SECONDS_IN_HOUR)
    }
