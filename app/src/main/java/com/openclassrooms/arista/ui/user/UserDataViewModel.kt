package com.openclassrooms.arista.ui.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.arista.data.repository.DataResult
import com.openclassrooms.arista.domain.model.User
import com.openclassrooms.arista.domain.usecase.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDataViewModel @Inject constructor(private val getUserUseCase: GetUserUseCase) :
    ViewModel() {
    private val _userFlow = MutableStateFlow<User?>(null)
    val userFlow: StateFlow<User?> = _userFlow.asStateFlow()

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState.asStateFlow()
    init {
        loadUserData()
    }

    /**
     * Loads user data from the use case and updates the UI accordingly.
     * If an error occurs, it will be displayed in the UI.
     */
    private fun loadUserData() {
        viewModelScope.launch(Dispatchers.IO) {
            getUserUseCase.execute()
                .catch { e ->
                    _errorState.value = "An unexpected error occurred: ${e.message}"
                    _userFlow.value = null
                }
                .collect { result: DataResult<User?> ->
                    when (result) {
                        is DataResult.Success -> {
                            _userFlow.value = result.data
                            _errorState.value = null
                        }
                        is DataResult.Error -> {
                            _userFlow.value = null // Clear user data on error
                            _errorState.value = "Failed to load user: ${result.exception.message}"
                        }
                    }
                }
        }}
    }
