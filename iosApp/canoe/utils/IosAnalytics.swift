//
//  IosAnalytics.swift
//  canoe
//
//  Created by nVoxel on 04.06.2024.
//

import Foundation
import CanoeKit
import FirebaseAnalytics

class IosAnalytics : CommonAnalytics {
    
    func logEvent(event: CustomEvent) {
        Analytics.logEvent(event.name, parameters: nil)
    }
}
