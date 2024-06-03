package com.voxeldev.canoe.compose.ui.content.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.voxeldev.canoe.compose.ui.components.ConfirmationDialog
import com.voxeldev.canoe.compose.ui.components.Error
import com.voxeldev.canoe.compose.ui.components.Loader
import com.voxeldev.canoe.compose.ui.components.TextFieldDialog
import com.voxeldev.canoe.settings.Settings
import com.voxeldev.canoe.settings.integration.SettingsComponent

/**
 * @author nvoxel
 */
@Composable
internal fun SettingsContent(component: SettingsComponent) {
    with(component) {
        val model by model.subscribeAsState()

        SettingsContent(
            model = model,
            onReloadButtonClicked = ::onReloadButtonClicked,
            onConnectButtonClicked = ::onConnectButtonClicked,
            onDisconnectButtonClicked = ::onDisconnectButtonClicked,
            loginConfirm = ::onLoginDialogConfirmClicked,
            loginDismiss = ::onLoginDialogDismissClicked,
            codeConfirm = ::onCodeDialogConfirmClicked,
            codeDismiss = ::onCodeDialogDismissClicked,
            codeTextChanged = ::onCodeDialogTextChanged,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SettingsContent(
    model: Settings.Model,
    onReloadButtonClicked: () -> Unit,
    onConnectButtonClicked: () -> Unit,
    onDisconnectButtonClicked: () -> Unit,
    loginConfirm: () -> Unit,
    loginDismiss: () -> Unit,
    codeConfirm: () -> Unit,
    codeDismiss: () -> Unit,
    codeTextChanged: (String) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Settings") },
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(top = paddingValues.calculateTopPadding())
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (model.isLoading) {
                    Loader()
                } else {
                    model.errorText?.let {
                        Error(
                            message = it,
                            shouldShowRetry = true,
                            retryCallback = onReloadButtonClicked,
                        )
                    } ?: run {
                        Text(
                            modifier = Modifier.padding(all = 16.dp),
                            text = "Account Status: ${if (model.isConnected) "Connected" else "Not connected"}",
                            style = MaterialTheme.typography.titleLarge,
                        )

                        if (model.isConnected) {
                            OutlinedButton(onClick = onDisconnectButtonClicked) {
                                Text(text = "Disconnect")
                            }
                        } else {
                            Button(onClick = onConnectButtonClicked) {
                                Text(text = "Connect")
                            }
                        }

                        Dialogs(
                            loginVisible = model.isLoginDialogVisible,
                            loginConfirm = loginConfirm,
                            loginDismiss = loginDismiss,
                            codeVisible = model.isCodeDialogVisible,
                            codeConfirm = codeConfirm,
                            codeDismiss = codeDismiss,
                            codeText = model.codeText,
                            codeTextChanged = codeTextChanged,
                        )
                    }
                }
            }
        },
    )
}

@Composable
private fun Dialogs(
    loginVisible: Boolean,
    loginConfirm: () -> Unit,
    loginDismiss: () -> Unit,
    codeVisible: Boolean,
    codeConfirm: () -> Unit,
    codeDismiss: () -> Unit,
    codeText: String,
    codeTextChanged: (String) -> Unit,
) {
    ConfirmationDialog(
        isVisible = loginVisible,
        title = { Text(text = "Connect account") },
        text = { Text(text = "Enter the code into the next dialog after completing authentication in browser") },
        onConfirm = loginConfirm,
        onDismiss = loginDismiss,
    )

    TextFieldDialog(
        isVisible = codeVisible,
        onConfirmClicked = codeConfirm,
        onDismiss = codeDismiss,
        text = codeText,
        onTextChange = codeTextChanged,
        textLabel = { Text(text = "Enter the code from the website") },
        confirmButtonContent = { Text(text = "Connect") },
    )
}
