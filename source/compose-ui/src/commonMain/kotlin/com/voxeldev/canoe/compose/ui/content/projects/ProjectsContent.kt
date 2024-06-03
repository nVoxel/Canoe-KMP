package com.voxeldev.canoe.compose.ui.content.projects

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.voxeldev.canoe.compose.ui.components.getOrientation
import com.voxeldev.canoe.compose.ui.content.dashboard.DashboardContent
import com.voxeldev.canoe.projects.Projects
import com.voxeldev.canoe.projects.ProjectsComponent
import com.voxeldev.canoe.compose.ui.components.Orientation as ScreenOrientation

/**
 * @author nvoxel
 */
@Composable
internal fun ProjectsContent(component: ProjectsComponent) {
    Children(
        component = component,
        modifier = Modifier
            .fillMaxSize(),
    )
}

@Composable
private fun Children(component: ProjectsComponent, modifier: Modifier = Modifier) {
    val orientation = getOrientation()

    Children(
        stack = component.childStack,
        modifier = modifier,
        animation = stackAnimation(
            slide(
                orientation = if (orientation == ScreenOrientation.PORTRAIT) Orientation.Vertical else Orientation.Horizontal,
            ),
        ),
    ) {
        when (val child = it.instance) {
            is Projects.Child.ProjectsListChild -> ProjectsListContent(component = child.component)
            is Projects.Child.ProjectsDetailsChild -> DashboardContent(component = child.component)
        }
    }
}
