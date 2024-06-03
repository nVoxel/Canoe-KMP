//
//  DailyChartModel.swift
//  canoe
//
//  Created by nVoxel on 02.06.2024.
//

import Foundation
import CanoeKit

struct DailyChartModel {
    
    let entries: [DailyChartEntry]
    let totalEntries: [DailyChartTotalEntry]
    let dataEntries: [DailyChartDataEntry]
    
    init(entries: NSMutableArray, totalEntries: NSMutableArray, dataEntries: KotlinMutableDictionary<NSString, NSMutableArray>) {
        let horizontalLabels = entries as! [NSString]
        self.entries = horizontalLabels.enumerated().map { index, label in DailyChartEntry(index: index, day: label as String) }
        
        let totalLabels = totalEntries as! [KotlinPair<KotlinFloat, NSString>]
        self.totalEntries = totalLabels.enumerated().map { index, total in DailyChartTotalEntry(index: index, totalTimeValue: Float(truncating: total.first!), totalTimeString: total.second! as String) }
        
        let projectsSeries = dataEntries as! [NSString : [KotlinPair<KotlinFloat, NSString>]]
        self.dataEntries = projectsSeries.flatMap { entry in
            entry.value.enumerated().map { index, val in
                DailyChartDataEntry(index: index, projectName: entry.key as String, timeValue: Float(truncating: val.first!), timeString: val.second! as String)
            }
        }
    }
}
