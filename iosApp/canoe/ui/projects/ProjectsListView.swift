//
//  ProjectsListView.swift
//  canoe
//
//  Created by nVoxel on 30.05.2024.
//

import UIKit
import SwiftUI
import CanoeKit

struct ProjectsListView: View {
    
    private let component: ProjectsList
    
    @StateValue
    private var model: ProjectsListModel
    
    init(component: ProjectsList) {
        self.component = component
        _model = StateValue(component.model)
    }
    
    var body: some View {
        if (model.errorText != nil) {
            ErrorView(message: model.errorText!, shouldShowRetry: true, retryCallback: component.onSearchClicked)
        }
        else {
            if (model.isLoading) { LoaderView() }
            else {
                NavigationWrapper {
                    VStack {
                        if (model.searchActive) {
                            HStack {
                                TextField("Project name", text: Binding(get: { model.searchText }, set: { component.onSearchTextChanged(text: $0) }))
                                    .textFieldStyle(RoundedBorderTextFieldStyle())
                                
                                Button(action: component.onSearchClicked) {
                                    Label("Search", systemImage: "magnifyingglass")
                                }
                            }
                            .padding(.horizontal, 10)
                        }
                        
                        List(model.projectsModel.data, id: \.id) { project in
                            Button(action: { component.onItemClicked(projectName: project.name) }) {
                                VStack(alignment: .leading, spacing: 10) {
                                    Text(project.name)
                                        .fontWeight(.bold)
                                    Text("Created at: \(project.createdAt)")
                                    if (project.lastHeartBeatAt != nil) {
                                        Text("Last update at: \(project.lastHeartBeatAt!)")
                                    }
                                }
                            }
                        }
                    }
                } toolbar: {
                    Button(action: component.onToggleSearchClicked) {
                        Label("Search", systemImage: "magnifyingglass")
                    }
                }
                .refreshable { component.onSearchClicked() }
            }
        }
    }
}
