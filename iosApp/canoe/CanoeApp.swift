//
//  canoeApp.swift
//  canoe
//
//  Created by nVoxel on 28.05.2024.
//

import SwiftUI
import CanoeKit
import FirebaseCore
import FirebaseAnalytics

@main
struct canoeApp: App {
    
    @UIApplicationDelegateAdaptor(AppDelegate.self)
    var appDelegate: AppDelegate
    
    init() {
        let iosDependencies = IosDependencies(commonAnalytics: IosAnalytics())
        
        KoinHelperKt.doInitKoin(dependencies: iosDependencies)
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}

class AppDelegate: NSObject, UIApplicationDelegate {
    
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
        FirebaseApp.configure()
        return true
    }
}
