package com.voxeldev.canoe.projects.list

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.voxeldev.canoe.projects.list.ProjectsList.Model
import com.voxeldev.canoe.projects.list.ProjectsList.Output
import com.voxeldev.canoe.utils.extensions.asValue
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

/**
 * @author nvoxel
 */
class ProjectsListComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val output: (Output) -> Unit,
) : ProjectsList, KoinComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore {
        ProjectsListStoreProvider(storeFactory = storeFactory, commonAnalytics = get()).provide()
    }

    override val model: Value<Model> = store.asValue().map { state ->
        Model(
            projectsModel = state.projectsModel,
            searchText = state.searchText,
            errorText = state.errorText,
            searchActive = state.searchActive,
            isLoading = state.isLoading,
        )
    }

    override fun onItemClicked(projectName: String) = output(Output.Selected(projectName = projectName))

    override fun onSearchTextChanged(text: String) = store.accept(
        intent = ProjectsListStore.Intent.SetSearchText(text = text),
    )

    override fun onToggleSearchClicked() = store.accept(intent = ProjectsListStore.Intent.ToggleSearch)

    override fun onSearchClicked() = store.accept(intent = ProjectsListStore.Intent.Search)
}
