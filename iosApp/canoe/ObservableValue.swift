//
//  ObservableValue.swift
//  canoe
//
//  Created by nVoxel on 29.05.2024.
//

import CanoeKit

public class ObservableValue<T : AnyObject> : ObservableObject {
    @Published
    var value: T

    private var cancellation: Cancellation?
    
    init(_ value: Value<T>) {
        self.value = value.value
        self.cancellation = value.subscribe { [weak self] value in self?.value = value }
    }

    deinit {
        cancellation?.cancel()
    }
}
