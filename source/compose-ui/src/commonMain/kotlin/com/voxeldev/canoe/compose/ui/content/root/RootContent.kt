package com.voxeldev.canoe.compose.ui.content.root

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.voxeldev.canoe.compose.ui.components.CustomNavigationRail
import com.voxeldev.canoe.compose.ui.components.PlatformInit
import com.voxeldev.canoe.compose.ui.components.decompose.slideTabAnimation
import com.voxeldev.canoe.compose.ui.components.getOrientation
import com.voxeldev.canoe.compose.ui.content.dashboard.DashboardContent
import com.voxeldev.canoe.compose.ui.content.leaderboards.LeaderboardsContent
import com.voxeldev.canoe.compose.ui.content.projects.ProjectsContent
import com.voxeldev.canoe.compose.ui.content.settings.SettingsContent
import com.voxeldev.canoe.compose.ui.theme.AdditionalIcons
import com.voxeldev.canoe.compose.ui.theme.CanoeTheme
import com.voxeldev.canoe.root.Root
import com.voxeldev.canoe.root.integration.RootComponent
import com.voxeldev.canoe.compose.ui.components.Orientation as ScreenOrientation

internal val navigationTonalElevation = 3.0.dp

private const val DASHBOARD = "Dashboard"
private const val LEADERBOARDS = "Leaders"
private const val PROJECTS = "Projects"
private const val SETTINGS = "Settings"

/**
 * @author nvoxel
 */
@Composable
fun RootContent(component: RootComponent) {
    CanoeTheme {
        PlatformInit()

        when (getOrientation()) {
            ScreenOrientation.PORTRAIT -> VerticalLayout(component = component)
            else -> HorizontalLayout(component = component)
        }
    }
}

@Composable
private fun VerticalLayout(component: RootComponent) {
    Scaffold(
        bottomBar = {
            BottomNavigation(component = component)
        },
    ) { paddingValues ->
        Children(
            component = component,
            modifier = Modifier.padding(
                bottom = paddingValues.calculateBottomPadding(),
            ),
            isHorizontalLayout = false,
        )
    }
}

@Composable
private fun HorizontalLayout(component: RootComponent) {
    val layoutDirection = LocalLayoutDirection.current

    Scaffold { paddingValues ->
        Row(
            modifier = Modifier
                .padding(
                    start = paddingValues.calculateStartPadding(layoutDirection = layoutDirection),
                    end = paddingValues.calculateEndPadding(layoutDirection = layoutDirection),
                    bottom = paddingValues.calculateBottomPadding(),
                ),
        ) {
            SideNavigation(component = component)
            Children(
                component = component,
                isHorizontalLayout = true,
            )
        }
    }
}

@Composable
private fun Children(component: RootComponent, modifier: Modifier = Modifier, isHorizontalLayout: Boolean) {
    Children(
        stack = component.childStack,
        modifier = modifier,
        animation = slideTabAnimation(
            orientation = if (isHorizontalLayout) Orientation.Vertical else Orientation.Horizontal,
        ) { index() },
    ) {
        when (val child = it.instance) {
            is Root.Child.DashboardChild -> DashboardContent(component = child.component)
            is Root.Child.ProjectsChild -> ProjectsContent(component = child.component)
            is Root.Child.SettingsChild -> SettingsContent(component = child.component)
            is Root.Child.LeaderboardsChild -> LeaderboardsContent(component = child.component)
        }
    }
}

@Composable
private fun BottomNavigation(component: RootComponent, modifier: Modifier = Modifier) {
    val childStack by component.childStack.subscribeAsState()
    val activeComponent = childStack.active.instance

    NavigationBar(
        modifier = modifier,
        tonalElevation = navigationTonalElevation,
    ) {
        NavigationBarItem(
            selected = activeComponent is Root.Child.DashboardChild,
            onClick = component::onDashboardTabClicked,
            icon = {
                Icon(
                    imageVector = AdditionalIcons.Dashboard,
                    contentDescription = DASHBOARD,
                )
            },
            label = {
                Text(text = DASHBOARD)
            },
            alwaysShowLabel = false,
        )

        NavigationBarItem(
            selected = activeComponent is Root.Child.LeaderboardsChild,
            onClick = component::onLeaderboardsTabClicked,
            icon = {
                Icon(
                    imageVector = AdditionalIcons.Leaderboard,
                    contentDescription = LEADERBOARDS,
                )
            },
            label = {
                Text(text = LEADERBOARDS)
            },
            alwaysShowLabel = false,
        )

        NavigationBarItem(
            selected = activeComponent is Root.Child.ProjectsChild,
            onClick = component::onProjectsTabClicked,
            icon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.List,
                    contentDescription = PROJECTS,
                )
            },
            label = {
                Text(text = PROJECTS)
            },
            alwaysShowLabel = false,
        )

        NavigationBarItem(
            selected = activeComponent is Root.Child.SettingsChild,
            onClick = component::onSettingsTabClicked,
            icon = {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = SETTINGS,
                )
            },
            label = {
                Text(text = SETTINGS)
            },
            alwaysShowLabel = false,
        )
    }
}

@Composable
private fun SideNavigation(component: RootComponent, modifier: Modifier = Modifier) {
    val childStack by component.childStack.subscribeAsState()
    val activeComponent = childStack.active.instance

    CustomNavigationRail(
        modifier = modifier
            .zIndex(1f),
        containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(elevation = navigationTonalElevation),
    ) {
        NavigationRailItem(
            selected = activeComponent is Root.Child.DashboardChild,
            onClick = component::onDashboardTabClicked,
            icon = {
                Icon(
                    imageVector = AdditionalIcons.Dashboard,
                    contentDescription = DASHBOARD,
                )
            },
            label = {
                Text(text = DASHBOARD)
            },
            alwaysShowLabel = false,
        )

        NavigationRailItem(
            selected = activeComponent is Root.Child.LeaderboardsChild,
            onClick = component::onLeaderboardsTabClicked,
            icon = {
                Icon(
                    imageVector = AdditionalIcons.Leaderboard,
                    contentDescription = LEADERBOARDS,
                )
            },
            label = {
                Text(text = LEADERBOARDS)
            },
            alwaysShowLabel = false,
        )

        NavigationRailItem(
            selected = activeComponent is Root.Child.ProjectsChild,
            onClick = component::onProjectsTabClicked,
            icon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.List,
                    contentDescription = PROJECTS,
                )
            },
            label = {
                Text(text = PROJECTS)
            },
            alwaysShowLabel = false,
        )

        NavigationRailItem(
            selected = activeComponent is Root.Child.SettingsChild,
            onClick = component::onSettingsTabClicked,
            icon = {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = SETTINGS,
                )
            },
            label = {
                Text(text = SETTINGS)
            },
            alwaysShowLabel = false,
        )
    }
}

private fun Root.Child.index(): Int =
    when (this) {
        is Root.Child.DashboardChild -> 0
        is Root.Child.LeaderboardsChild -> 1
        is Root.Child.ProjectsChild -> 2
        is Root.Child.SettingsChild -> 3
    }
