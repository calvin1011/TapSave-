package com.tapsave.app.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tapsave.app.domain.model.AppInfo
import com.tapsave.app.domain.usecase.GetInstalledAppsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AppSelectionState(
    val apps: List<AppInfo> = emptyList(),
    val selectedApps: Set<String> = emptySet(),
    val isLoading: Boolean = true,
    val searchQuery: String = ""
)

@HiltViewModel
class AppSelectionViewModel @Inject constructor(
    private val getInstalledAppsUseCase: GetInstalledAppsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AppSelectionState())
    val state: StateFlow<AppSelectionState> = _state.asStateFlow()

    init {
        loadApps()
    }

    private fun loadApps() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val apps = getInstalledAppsUseCase()

            _state.update {
                it.copy(
                    apps = apps,
                    isLoading = false
                )
            }
        }
    }

    fun toggleAppSelection(packageName: String) {
        _state.update { currentState ->
            val newSelected = if (packageName in currentState.selectedApps) {
                currentState.selectedApps - packageName
            } else {
                // Limit to 3 apps
                if (currentState.selectedApps.size >= 3) {
                    currentState.selectedApps
                } else {
                    currentState.selectedApps + packageName
                }
            }
            currentState.copy(selectedApps = newSelected)
        }
    }

    fun updateSearchQuery(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }

    fun getFilteredApps(): List<AppInfo> {
        val query = _state.value.searchQuery.lowercase()
        val selectedPackages = _state.value.selectedApps

        return _state.value.apps
            .filter { app ->
                query.isEmpty() || app.appName.lowercase().contains(query)
            }
            .map { app ->
                app.copy(isSelected = app.packageName in selectedPackages)
            }
    }

    fun canContinue(): Boolean {
        return _state.value.selectedApps.isNotEmpty()
    }
}