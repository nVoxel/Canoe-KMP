//
//  LeaderboardsView.swift
//  canoe
//
//  Created by nVoxel on 29.05.2024.
//

import SwiftUI
import CanoeKit
import CachedAsyncImage

struct LeaderboardsView: View {
    
    private let component: Leaderboards
    
    @StateValue
    private var model: LeaderboardsModel
    
    private let photoUrl = "https://wakatime.com/photo/"
    private let profileUrl = "https://wakatime.com/@"
    private let languageLimit = 5
    
    init(component: Leaderboards) {
        self.component = component
        _model = StateValue(component.model)
    }
    
    var body: some View {
        if (model.errorText != nil) {
            ErrorView(message: model.errorText!, shouldShowRetry: true, retryCallback: component.onReloadClicked)
        }
        else {
            if (model.isLoading) { LoaderView() }
            else {
                NavigationWrapper {
                    if (model.leaderboardsModel != nil) {
                        let currentUser = model.leaderboardsModel!.currentUser
                        if (currentUser.rank != nil) {
                            HStack() {
                                CachedAsyncImage(url: URL(string: photoUrl + currentUser.user.id)) { image in
                                    image.resizable().scaledToFill()
                                } placeholder: {
                                    ProgressView().progressViewStyle(.circular)
                                }
                                .frame(width: 64, height: 64)
                                .clipShape(Circle())
                                
                                Spacer()
                                    .frame(width: 20)
                                
                                VStack(alignment: .leading) {
                                    if (currentUser.rank != nil) {
                                        Text("#\(currentUser.rank!)")
                                            .font(.title)
                                    }
                                    
                                    let name = currentUser.user.displayName ?? currentUser.user.username ?? currentUser.user.id
                                    Text(name)
                                        .font(.title2)
                                }
                            }
                            .padding(.vertical, 10)
                        }
                        
                        List {
                            ForEach(model.leaderboardsModel!.data, id: \.user.id) { entry in
                                Button(action: { component.onItemClicked(profileUrl: profileUrl + String(entry.user.id)) }) {
                                    HStack() {
                                        CachedAsyncImage(url: URL(string: photoUrl + entry.user.id)) { image in
                                            image.resizable().scaledToFill()
                                        } placeholder: {
                                            ProgressView().progressViewStyle(.circular)
                                        }
                                        .frame(width: 64, height: 64)
                                        .clipShape(Circle())
                                        
                                        Spacer()
                                            .frame(width: 20)
                                        
                                        VStack(alignment: .leading) {
                                            if (entry.rank != nil) {
                                                Text("#\(entry.rank!)")
                                                    .font(.title)
                                                
                                                Spacer(minLength: 5)
                                            }
                                            
                                            let name = entry.user.displayName ?? entry.user.username ?? entry.user.id
                                            Text(name)
                                                .font(.title2)
                                            
                                            Spacer(minLength: 5)
                                            
                                            if (entry.user.city?.title != nil) {
                                                Text(entry.user.city!.title!)
                                                    .font(.title3)
                                                
                                                Spacer(minLength: 5)
                                            }
                                            
                                            if (entry.runningTotal?.languages != nil) {
                                                let languages = entry
                                                    .runningTotal!
                                                    .languages!
                                                    .prefix(languageLimit)
                                                    .map { $0.name }
                                                    .joined(separator: ", ")
                                                
                                                Text(languages)
                                                    .font(.title3)
                                                
                                                Spacer(minLength: 5)
                                            }
                                            
                                            if (entry.runningTotal?.humanReadableDailyAverage != nil) {
                                                Text("Daily average: \(entry.runningTotal!.humanReadableDailyAverage!)")
                                                    .font(.title3)
                                                
                                                Spacer(minLength: 5)
                                            }
                                            
                                            if (entry.runningTotal?.humanReadableTotal != nil) {
                                                Text("Total: \(entry.runningTotal!.humanReadableTotal!)")
                                                    .font(.title3)
                                                
                                                Spacer(minLength: 5)
                                            }
                                        }
                                    }
                                }
                                .foregroundStyle(.black)
                            }
                            
                            if (model.leaderboardsModel!.page < model.leaderboardsModel!.totalPages) {
                                Text("Loading leaderboards...")
                                    .onAppear(perform: {
                                        component.onScrolledToBottom()
                                    })
                            }
                        }
                        .refreshable { component.onReloadClicked() }
                    }
                } toolbar: {
                    Button(action: component.onToggleFilterBottomSheet) {
                        Label("Filter", systemImage: "line.horizontal.3.decrease")
                    }.sheet(
                        isPresented: Binding(
                            get: { model.filterBottomSheetModel.active },
                            set: { val in }
                        ),
                        onDismiss: component.onToggleFilterBottomSheet
                    ) {
                        VStack(alignment: .leading) {
                            Spacer()
                                .frame(height: 40)
                            
                            Text("Filters")
                                .font(.title)
                                .padding(.horizontal, 10)
                            
                            Spacer()
                                .frame(height: 40)
                            
                            Text("Select Programming Language")
                                .font(.title2)
                                .padding(.horizontal, 10)
                            
                            ScrollView(.horizontal) {
                                LazyHStack(spacing: 10) {
                                    ForEach(model.filterBottomSheetModel.languages, id: \.name) { language in
                                        Button(action: { component.onSelectLanguage(language: language.value) }) {
                                            Text(language.name)
                                                .foregroundStyle(language.value == model.filterBottomSheetModel.selectedLanguage ? .blue : .black)
                                        }
                                    }
                                }
                                .padding(.horizontal, 10)
                                .frame(height: 20)
                            }
                            
                            Spacer()
                                .frame(height: 10)
                            
                            TextField("Programming Language Name", text: Binding(get: { model.filterBottomSheetModel.selectedLanguage ?? "" }, set: { component.onSelectLanguage(language: $0) }))
                                .padding(.horizontal, 10)
                            
                            Spacer()
                                .frame(height: 40)
                            
                            Text("Select Country")
                                .font(.title2)
                                .padding(.horizontal, 10)
                            
                            ScrollView(.horizontal) {
                                LazyHStack(spacing: 10) {
                                    ForEach(model.filterBottomSheetModel.countryCodes, id: \.name) { countryCode in
                                        Button(action: { component.onSelectCountryCode(countryCode: countryCode.value) }) {
                                            Text(countryCode.name)
                                                .foregroundStyle(countryCode.value == model.filterBottomSheetModel.selectedCountryCode ? .blue : .black)
                                        }
                                    }
                                }
                                .padding(.horizontal, 10)
                                .frame(height: 20)
                            }
                            
                            Spacer()
                                .frame(height: 10)
                            
                            TextField("2-letter country code", text: Binding(get: { model.filterBottomSheetModel.selectedCountryCode ?? "" }, set: { component.onSelectCountryCode(countryCode: $0) }))
                                .padding(.horizontal, 10)
                            
                            Spacer()
                                .frame(height: 40)
                            
                            Text("Is hireable?")
                                .font(.title2)
                                .padding(.horizontal, 10)
                            
                            Toggle(isOn: Binding(get: { model.filterBottomSheetModel.hireable?.boolValue ?? false }, set: { component.onSelectHireable(hireable: $0) }), label: {})
                                .labelsHidden()
                                .padding(.horizontal, 10)
                            
                            Spacer()
                                .frame(height: 40)
                            
                            Button(action: component.onResetFilters) {
                                Label("Reset filters", systemImage: "xmark")
                            }
                            .padding(.horizontal, 10)
                            
                            Spacer()
                        }
                    }
                }
            }
        }
    }
}
