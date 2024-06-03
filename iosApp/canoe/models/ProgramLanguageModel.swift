//
//  ProgramLanguageModel.swift
//  canoe
//
//  Created by nVoxel on 02.06.2024.
//

import SwiftUI
import CanoeKit

struct ProgramLanguageModel {
    
    let entries: [ProgramLanguageEntry]
    
    init(languages: [ProgramLanguage], languagesChartData: [KotlinPair<NSString, KotlinPair<KotlinFloat, NSString>>]) {
        self.entries = languagesChartData.map { language in
            let hexColor = languages.first { $0.name == language.first! as String }?.color
            let color = hexColor == nil ? (language.first! as String).toColor() : Color.init(hex: hexColor!)!
            
            return ProgramLanguageEntry(name: language.first! as String, color: color, timeValue: Float(truncating: language.second!.first!), timeString: language.second!.second! as String)
        }
    }
}
