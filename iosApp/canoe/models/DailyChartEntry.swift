//
//  DailyChartEntry.swift
//  canoe
//
//  Created by nVoxel on 02.06.2024.
//

import Foundation

struct DailyChartEntry: Identifiable {
    let id: String = UUID().uuidString
    
    let index: Int
    let day: String
    
    init(index: Int, day: String) {
        self.index = index
        self.day = day
    }
}

struct DailyChartTotalEntry: Identifiable {
    let id: String = UUID().uuidString
    
    let index: Int
    let totalTimeValue: Float
    let totalTimeString: String
    
    init(index: Int, totalTimeValue: Float, totalTimeString: String) {
        self.index = index
        self.totalTimeValue = totalTimeValue
        self.totalTimeString = totalTimeString
    }
}

struct DailyChartDataEntry: Identifiable {
    let id: String = UUID().uuidString
    
    let index: Int
    let projectName: String
    let timeValue: Float
    let timeString: String
    
    init(index: Int, projectName: String, timeValue: Float, timeString: String) {
        self.index = index
        self.projectName = projectName
        self.timeValue = timeValue
        self.timeString = timeString
    }
}
