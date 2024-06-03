//
//  StateValue.swift
//  canoe
//
//  Created by nVoxel on 30.05.2024.
//

import SwiftUI
import CanoeKit

@propertyWrapper struct StateValue<T : AnyObject>: DynamicProperty {
    @ObservedObject
    private var obj: ObservableValue<T>

    var wrappedValue: T { obj.value }

    init(_ value: Value<T>) {
        obj = ObservableValue(value)
    }
}
