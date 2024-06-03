package com.voxeldev.canoe.compose.ui.components

import androidx.compose.ui.Modifier

/**
 * @author nvoxel
 */
internal fun Modifier.conditional(condition: Boolean, conditionMetModifier: Modifier) =
    if (condition) then(other = conditionMetModifier) else this
