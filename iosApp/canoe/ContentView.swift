//
//  ContentView.swift
//  canoe
//
//  Created by nVoxel on 28.05.2024.
//

import SwiftUI
import CanoeKit

struct ContentView: View {
    
    @State
    private var componentHolder =
    ComponentHolder {
        RootComponent(
            componentContext: $0,
            storeFactory: DefaultStoreFactory(),
            linkHandler: IosLinkHandler(),
            deepLink: nil // TODO
        )
    }
    
    var body: some View {
        RootView(component: componentHolder.component)
            .onAppear { LifecycleRegistryExtKt.resume(self.componentHolder.lifecycle) }
            .onDisappear { LifecycleRegistryExtKt.stop(self.componentHolder.lifecycle) }
    }
}
