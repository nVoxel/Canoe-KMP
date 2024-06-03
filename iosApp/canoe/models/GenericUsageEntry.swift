//
//  GenericChartEntry.swift
//  canoe
//
//  Created by nVoxel on 03.06.2024.
//

import Foundation

struct GenericUsageEntry : Identifiable {
    let id: String = UUID().uuidString
    
    let name: String
    let totalTimeValue: Float
    let totalTimeString: String
}
