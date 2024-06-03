//
//  SettingsView.swift
//  canoe
//
//  Created by nVoxel on 29.05.2024.
//

import SwiftUI
import CanoeKit

struct SettingsView: View {
    
    private let component: Settings
    
    @StateValue
    private var model: SettingsModel
    
    init(component: Settings) {
        self.component = component
        _model = StateValue(component.model)
    }
    
    var body: some View {
        if (model.errorText != nil) {
            ErrorView(message: model.errorText!, shouldShowRetry: true, retryCallback: component.onReloadButtonClicked)
        }
        else {
            if (model.isLoading) { LoaderView() }
            else {
                NavigationView {
                    VStack(alignment: .center) {
                        Text("Account status: \(model.isConnected ? "Connected" : "Not connected")")
                        
                        Spacer()
                            .frame(height: 20)
                        
                        if (model.isConnected) {
                            Button(action: component.onDisconnectButtonClicked) {
                                Label("Disconnect", systemImage: "person.badge.minus")
                            }
                        }
                        else {
                            Button(action: component.onConnectButtonClicked) {
                                Label("Connect", systemImage: "person.badge.plus")
                            }
                        }
                    }
                    .onOpenURL(perform: { url in
                        component.onCodeDialogTextChanged(text: url.absoluteString)
                        component.onCodeDialogConfirmClicked()
                    })
                }
            }
        }
    }
}
