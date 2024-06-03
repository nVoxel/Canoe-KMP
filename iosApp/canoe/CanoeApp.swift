//
//  canoeApp.swift
//  canoe
//
//  Created by nVoxel on 28.05.2024.
//

import SwiftUI
import CanoeKit

@main
struct canoeApp: App {
    
    init() {
        KoinHelperKt.doInitKoin()
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
