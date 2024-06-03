//
//  ProjectsView.swift
//  canoe
//
//  Created by nVoxel on 29.05.2024.
//

import SwiftUI
import CanoeKit

struct ProjectsView: View {
    
    private let component: Projects
    
    @ObservedObject
    private var childStack: ObservableValue<ChildStack<AnyObject, ProjectsChild>>
    
    init(component: Projects) {
        self.component = component
        self.childStack = ObservableValue(component.childStack)
    }
    
    var body: some View {
        ChildView(child: self.childStack.value.active.instance)
    }
}

private struct ChildView : View {
    let child: ProjectsChild
    
    var body: some View {
        switch child {
        case let child as ProjectsChild.ProjectsListChild:
            ProjectsListView(component: child.component)
        case let child as ProjectsChild.ProjectsDetailsChild:
            DashboardView(component: child.component)
        default: EmptyView()
        }
    }
}
