//
//  IosLinkHandler.swift
//  canoe
//
//  Created by nVoxel on 29.05.2024.
//

import CanoeKit
import UIKit

class IosLinkHandler : LinkHandler {
    
    func openLink(url: String) {
        UIApplication.shared.open(URL(string: url)!)
    }
}
