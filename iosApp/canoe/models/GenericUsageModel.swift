//
//  GenericChartModel.swift
//  canoe
//
//  Created by nVoxel on 03.06.2024.
//

import Foundation
import CanoeKit

struct GenericUsageModel {
    
    let entries: [GenericUsageEntry]
    
    init(data: [KotlinPair<NSString, KotlinTriple<KotlinFloat, NSString, KotlinInt>>]) {
        self.entries = data.map { entry in
            return GenericUsageEntry(name: entry.first! as String, totalTimeValue: Float(truncating: entry.second!.first!), totalTimeString: entry.second!.second! as String)
        }
    }
}
