//
//  RootView.swift
//  canoe
//
//  Created by nVoxel on 29.05.2024.
//

import SwiftUI
import CanoeKit

struct RootView: View {
    
    private let component: Root
    
    @ObservedObject
    private var childStack: ObservableValue<ChildStack<AnyObject, RootChild>>
    
    init(component: Root) {
        self.component = component
        self.childStack = ObservableValue(component.childStack)
    }
    
    var body: some View {
        VStack {
                    ChildView(child: self.childStack.value.active.instance)
                        .frame(maxHeight: .infinity)
                    
                    HStack(alignment: .bottom, spacing: 32) {
                        Button(action: component.onDashboardTabClicked) {
                            Label("Dashboard", systemImage: "square.grid.2x2.fill")
                                .labelStyle(VerticalLabelStyle())
                                .opacity(self.childStack.value.active.instance is RootChild.DashboardChild ? 1 : 0.5)
                        }
                        
                        Button(action: component.onLeaderboardsTabClicked) {
                            Label("Leaders", systemImage: "list.number")
                                .labelStyle(VerticalLabelStyle())
                                .opacity(self.childStack.value.active.instance is RootChild.LeaderboardsChild ? 1 : 0.5)
                        }
                        
                        Button(action: component.onProjectsTabClicked) {
                            Label("Projects", systemImage: "list.dash")
                                .labelStyle(VerticalLabelStyle())
                                .opacity(self.childStack.value.active.instance is RootChild.ProjectsChild ? 1 : 0.5)
                        }
                        
                        Button(action: component.onSettingsTabClicked) {
                            Label("Settings", systemImage: "gear")
                                .labelStyle(VerticalLabelStyle())
                                .opacity(self.childStack.value.active.instance is RootChild.SettingsChild ? 1 : 0.5)
                        }
                    }
                }
    }
}

private struct ChildView: View {
    let child: RootChild
    
    var body: some View {
        switch child {
        case let child as RootChild.DashboardChild: DashboardView(component: child.component)
        case let child as RootChild.LeaderboardsChild: LeaderboardsView(component: child.component)
        case let child as RootChild.ProjectsChild: ProjectsView(component: child.component)
        case let child as RootChild.SettingsChild: SettingsView(component: child.component)
        default: EmptyView()
        }
    }
}

private struct VerticalLabelStyle: LabelStyle {
    func makeBody(configuration: Configuration) -> some View {
        VStack(alignment: .center, spacing: 8) {
            configuration.icon
            configuration.title
        }
    }
}
