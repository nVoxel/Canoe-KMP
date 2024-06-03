//
//  DashboardView.swift
//  canoe
//
//  Created by nVoxel on 29.05.2024.
//

import SwiftUI
import Charts
import CanoeKit

struct DashboardView: View {
    
    private let component: Dashboard
    
    @StateValue
    private var model: DashboardModel
    
    @State private var startDate = Date()
    @State private var endDate = Date()
    
    private let SECOUNDS_IN_HOUR: Float = 3600
    
    init(component: Dashboard) {
        self.component = component
        _model = StateValue(component.model)
    }
    
    
    var body: some View {
        if (model.errorText != nil) {
            ErrorView(message: model.errorText!, shouldShowRetry: true, retryCallback: component.onReloadClicked)
        }
        else {
            if (model.isSummariesLoading || model.isProgramLanguagesLoading) { LoaderView() }
            else {
                NavigationView {
                    ScrollView(.vertical) {
                        Spacer()
                            .frame(height: 20)
                        
                        Text("**\(model.summariesModel!.cumulativeTotal.text)** from \(model.datePickerBottomSheetModel.startDate) to \(model.datePickerBottomSheetModel.endDate)")
                        
                        Text("Daily average time is: **\(model.summariesModel!.dailyAverage.text)**")
                        
                        if (model.allTimeModel != nil) {
                            Text("Total time spent: \(model.projectName != nil ? "for \(model.projectName!) " : "")is: **\(model.allTimeModel!.data.text)**")
                        }
                        
                        Spacer()
                            .frame(height: 40)
                        
                        Text(model.projectName == nil ? "Daily Stats" : "Project Stats")
                            .font(.title2)
                        
                        Spacer()
                            .frame(height: 20)
                        
                        let dailyChartModel = DailyChartModel(entries: model.summariesModel!.dailyChartData.horizontalLabels, totalEntries: model.summariesModel!.dailyChartData.totalLabels, dataEntries: model.summariesModel!.dailyChartData.projectsSeries)
                        
                        if (model.projectName == nil) {
                            Chart(dailyChartModel.entries) { entry in
                                ForEach(dailyChartModel.dataEntries, id: \.id) { projectSeriesEntry in
                                    if (projectSeriesEntry.index == entry.index) {
                                        BarMark(
                                            x: .value("Day", entry.day),
                                            y: .value("Time", projectSeriesEntry.timeValue),
                                            stacking: .standard
                                        )
                                        .foregroundStyle(by: .value("Project", projectSeriesEntry.projectName))
                                    }
                                }
                            }
                            .chartYAxis {
                                AxisMarks(position: .leading)
                            }
                            .frame(height: 250)
                            .padding()
                        }
                        else {
                            Chart(dailyChartModel.totalEntries) { entry in
                                LineMark(
                                    x: .value("Day", dailyChartModel.entries.first { $0.index == entry.index}!.day),
                                    y: .value("Time spent", entry.totalTimeValue / SECOUNDS_IN_HOUR)
                                )
                            }
                            .chartYAxis {
                                AxisMarks(position: .leading)
                            }
                            .frame(height: 250)
                            .padding()
                        }
                        
                        if #available(iOS 17.0, *) {
                            Spacer()
                                .frame(height: 40)
                            
                            Text("Languages Usage")
                                .font(.title2)
                            
                            Spacer()
                                .frame(height: 20)
                            
                            let programLanguagesModel = ProgramLanguageModel(languages: model.programLanguagesModel!.data, languagesChartData: model.summariesModel!.languagesChartData)
                            
                            Chart(programLanguagesModel.entries) { language in
                                SectorMark(
                                    angle: .value(Text(language.name), language.timeValue)
                                )
                                .foregroundStyle(by: .value("Language", language.name))
                            }
                            .chartForegroundStyleScale(range: languageColors(languages: programLanguagesModel.entries))
                            .frame(height: 350)
                            .padding()
                            
                            Spacer()
                                .frame(height: 40)
                            
                            Text("Editors Usage")
                                .font(.title2)
                            
                            Spacer()
                                .frame(height: 20)
                            
                            let editors = GenericUsageModel(data: model.summariesModel!.editorsChartData)
                            
                            Chart(editors.entries) { editor in
                                SectorMark(
                                    angle: .value(Text(editor.name), editor.totalTimeValue),
                                    innerRadius: .ratio(0.6)
                                )
                                .foregroundStyle(by: .value("Editor", editor.name))
                            }
                            .frame(height: 280)
                            .padding()
                            
                            Spacer()
                                .frame(height: 40)
                            
                            Text("OS Usage")
                                .font(.title2)
                            
                            Spacer()
                                .frame(height: 20)
                            
                            let operatingSystems = GenericUsageModel(data: model.summariesModel!.operatingSystemsChartData)
                            
                            Chart(operatingSystems.entries) { os in
                                SectorMark(
                                    angle: .value(Text(os.name), os.totalTimeValue),
                                    innerRadius: .ratio(0.6)
                                )
                                .foregroundStyle(by: .value("OS", os.name))
                            }
                            .frame(height: 280)
                            .padding()
                            
                            Spacer()
                                .frame(height: 40)
                            
                            Text("Machines Usage")
                                .font(.title2)
                            
                            Spacer()
                                .frame(height: 20)
                            
                            let machines = GenericUsageModel(data: model.summariesModel!.machinesChartData)
                            
                            Chart(machines.entries) { machine in
                                SectorMark(
                                    angle: .value(Text(machine.name), machine.totalTimeValue),
                                    innerRadius: .ratio(0.6)
                                )
                                .foregroundStyle(by: .value("Editor", machine.name))
                            }
                            .frame(height: 280)
                            .padding()
                        }
                    }
                    .toolbar {
                        if (model.projectName != nil) {
                            ToolbarItem(placement: .topBarLeading) {
                                Button(action: component.onCloseClicked) {
                                    Label("Back", systemImage: "chevron.left")
                                }
                            }
                            
                            ToolbarItem(placement: .topBarLeading) {
                                Text(model.projectName!)
                            }
                        }
                        
                        ToolbarItem {
                            Button(action: component.onShowDatePickerBottomSheet) {
                                Label("Set date", systemImage: "calendar")
                            }
                            .sheet(
                                isPresented: Binding(
                                    get: { model.datePickerBottomSheetModel.active },
                                    set: { val in }
                                ),
                                onDismiss: { component.onDismissDatePickerBottomSheet(startMillis: KotlinLong(value: Int64(startDate.timeIntervalSince1970 * 1000)), endMillis: KotlinLong(value: Int64(endDate.timeIntervalSince1970 * 1000))) }
                            ) {
                                DatePicker(
                                    "Pick range start date",
                                    selection: $startDate,
                                    displayedComponents: [.date])
                                .padding()
                                
                                DatePicker(
                                    "Pick range end date",
                                    selection: $endDate,
                                    displayedComponents: [.date])
                                .padding()
                            }
                        }
                    }
                    .refreshable { component.onReloadClicked() }
                }
            }
        }
    }
    
    private func languageColors(languages: [ProgramLanguageEntry]) -> [Color] {
        var returnColors = [Color]()
        for language in languages {
            returnColors.append(language.color)
        }
        return returnColors
    }
}
