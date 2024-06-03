package com.voxeldev.canoe.compose.ui.components.charts.piechart

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.voxeldev.canoe.compose.ui.components.charts.piechart.charts.DonutPieChart
import com.voxeldev.canoe.compose.ui.components.charts.piechart.charts.PieChart
import com.voxeldev.canoe.compose.ui.components.charts.piechart.models.PieChartConfig
import com.voxeldev.canoe.compose.ui.components.charts.piechart.models.PieChartData
import com.voxeldev.canoe.compose.ui.components.charts.piechart.models.PlotType

object ChartWrapper {

    /**
     * Wrapper compose method for drawing Pie Chart and Donut chart.
     * @param modifier : All modifier related property
     * @param plotType: Type of the chart (Pie or Donut)
     * @param pieChartData: data list for the pie chart
     * @param onSliceClick: Callback when any slice is clicked.
     */

    @Composable
    internal fun DrawChart(
        modifier: Modifier,
        plotType: PlotType,
        pieChartData: PieChartData,
        pieChartConfig: PieChartConfig,
        onSliceClick: (PieChartData.Slice) -> Unit = {},
    ) {
        when (plotType) {
            is PlotType.Pie -> {
                PieChart(
                    modifier = modifier,
                    pieChartData = pieChartData,
                    pieChartConfig = pieChartConfig,
                    onSliceClick = onSliceClick,
                )
            }

            is PlotType.Donut -> {
                DonutPieChart(
                    modifier = modifier,
                    pieChartData = pieChartData,
                    pieChartConfig = pieChartConfig,
                    onSliceClick = onSliceClick,
                )
            }

            else -> { // T0DO Handle if required for other types
            }
        }
    }
}
