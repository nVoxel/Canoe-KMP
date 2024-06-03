package com.voxeldev.canoe.compose.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

/**
 * @author nvoxel
 */
@Composable
internal fun ConfirmationDialog(
    isVisible: Boolean,
    title: @Composable () -> Unit,
    text: @Composable () -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    if (isVisible) {
        AlertDialog(
            title = title,
            text = text,
            onDismissRequest = onDismiss,
            confirmButton = {
                Button(
                    onClick = onConfirm,
                ) { Text(text = "Continue") }
            },
            dismissButton = {
                Button(
                    onClick = onDismiss,
                ) { Text(text = "Cancel") }
            },
        )
    }
}
