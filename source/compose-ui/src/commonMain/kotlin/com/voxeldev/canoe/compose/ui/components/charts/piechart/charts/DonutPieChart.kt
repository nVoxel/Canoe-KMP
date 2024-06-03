package com.voxeldev.canoe.compose.ui.components.charts.piechart.charts

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight.Companion.W700
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.voxeldev.canoe.compose.ui.components.charts.piechart.models.PieChartConfig
import com.voxeldev.canoe.compose.ui.components.charts.piechart.models.PieChartData
import com.voxeldev.canoe.compose.ui.components.charts.piechart.models.PlotType
import com.voxeldev.canoe.compose.ui.components.charts.piechart.utils.convertTouchEventPointToAngle
import com.voxeldev.canoe.compose.ui.components.charts.piechart.utils.proportion
import com.voxeldev.canoe.compose.ui.components.charts.piechart.utils.sweepAngles
import kotlin.math.roundToInt

/**
 * Compose function for Drawing Donut chart
 * @param modifier : All modifier related property
 * @param pieChartData: data list for the pie chart
 * @param pieChartConfig: configuration for the pie chart
 * @param onSliceClick(pieChartData.Slice)->Unit: The event that captures the click
 */
@Composable
internal fun DonutPieChart(
    modifier: Modifier,
    pieChartData: PieChartData,
    pieChartConfig: PieChartConfig,
    onSliceClick: (PieChartData.Slice) -> Unit = {},
) {
    // Sum of all the values
    val sumOfValues = pieChartData.totalLength

    // Calculate each proportion value
    val proportions = pieChartData.slices.proportion(sumOfValues)

    // Convert each proportions to angle
    val sweepAngles = proportions.sweepAngles()

    val progressSize = mutableListOf<Float>()
    progressSize.add(sweepAngles.first())

    for (x in 1 until sweepAngles.size) {
        progressSize.add(sweepAngles[x] + progressSize[x - 1])
    }

    var activePie by rememberSaveable {
        mutableIntStateOf(-1)
    }
    val textMeasurer = rememberTextMeasurer()

    Surface(
        modifier = modifier,
    ) {
        BoxWithConstraints(
            modifier = modifier
                .aspectRatio(1f)
                .semantics {
                    contentDescription = pieChartConfig.accessibilityConfig.chartDescription
                },
        ) {
            val sideSize = if (constraints.maxWidth < constraints.maxHeight) constraints.maxWidth else constraints.maxHeight
            val padding = (sideSize * pieChartConfig.chartPadding) / 100f
            val size = Size(sideSize.toFloat() - padding, sideSize.toFloat() - padding)

            val pathPortion = remember {
                Animatable(initialValue = 0f)
            }

            if (pieChartConfig.isAnimationEnable) {
                LaunchedEffect(key1 = Unit) {
                    pathPortion.animateTo(
                        1f,
                        animationSpec = tween(pieChartConfig.animationDuration),
                    )
                }
            }

            Canvas(
                modifier = Modifier
                    .width(sideSize.dp)
                    .height(sideSize.dp)
                    .pointerInput(true) {
                        detectTapGestures {
                            val clickedAngle = convertTouchEventPointToAngle(
                                sideSize.toFloat(),
                                sideSize.toFloat(),
                                it.x,
                                it.y,
                            )
                            progressSize.forEachIndexed { index, item ->
                                if (clickedAngle <= item) {
                                    if (activePie != index) {
                                        activePie = index
                                    }
                                    onSliceClick(pieChartData.slices[index])
                                    return@detectTapGestures
                                }
                            }
                        }
                    },

            ) {
                var sAngle = pieChartConfig.startAngle

                sweepAngles.forEachIndexed { index, arcProgress ->
                    drawPie(
                        color = pieChartData.slices[index].color,
                        startAngle = sAngle,
                        arcProgress = if (pieChartConfig.isAnimationEnable) {
                            arcProgress * pathPortion.value
                        } else {
                            arcProgress
                        },
                        size = size,
                        padding = padding,
                        isDonut = pieChartData.plotType == PlotType.Donut,
                        strokeWidth = pieChartConfig.strokeWidth,
                        isActive = activePie == index,
                        pieChartConfig = pieChartConfig,
                    )
                    sAngle += arcProgress
                }

                if (activePie != -1 && pieChartConfig.percentVisible) {
                    drawContext.canvas.nativeCanvas.apply {
                        val percentageText = AnnotatedString("${proportions[activePie].roundToInt()}%")
                        val textHeight = textMeasurer.measure(percentageText).size.height
                        val textWidth = textMeasurer.measure(percentageText).size.width

                        drawText(
                            textMeasurer = textMeasurer,
                            text = percentageText,
                            topLeft = Offset(
                                ((sideSize / 2) - textWidth).toFloat(),
                                ((sideSize / 2) - textHeight).toFloat(),
                            ),
                            style = TextStyle(
                                color = pieChartConfig.percentColor,
                                fontSize = pieChartConfig.percentageFontSize,
                                textAlign = TextAlign.Center,
                                fontWeight = W700,
                            ),
                        )
                    }
                }
            }
        }
    }
}
