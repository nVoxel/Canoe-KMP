//
//  ProgramLanguageEntry.swift
//  canoe
//
//  Created by nVoxel on 02.06.2024.
//

import Foundation
import SwiftUI

struct ProgramLanguageEntry: Identifiable {
    
    let id: String = UUID().uuidString
    
    let name: String
    let color: Color
    let timeValue: Float
    let timeString: String
    
    init(name: String, color: Color, timeValue: Float, timeString: String) {
        self.name = name
        self.color = color
        self.timeValue = timeValue
        self.timeString = timeString
    }
}
