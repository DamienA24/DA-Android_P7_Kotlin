package com.openclassrooms.arista.ui.sleep

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.arista.data.repository.DataResult
import com.openclassrooms.arista.domain.model.Sleep
import com.openclassrooms.arista.domain.usecase.GetAllSleepsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SleepViewModel @Inject constructor(private val getAllSleepsUseCase: GetAllSleepsUseCase) :
    ViewModel() {
    private val _sleeps = MutableStateFlow<List<Sleep>>(emptyList())
    val sleeps: StateFlow<List<Sleep>> = _sleeps.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun fetchSleeps() {
        viewModelScope.launch {
          getAllSleepsUseCase.execute()
                .catch { e ->
                    _errorMessage.value = "Unexpected error in sleep data flow: ${e.message}"
                    _sleeps.value = emptyList()
                }
                .collect { result ->
                    when (result) {
                        is DataResult.Success -> {
                            _sleeps.value = result.data
                            _errorMessage.value = null
                        }
                        is DataResult.Error -> {
                            _sleeps.value = emptyList()
                            _errorMessage.value = "Failed to load sleeps: ${result.exception.message}"
                        }
                    }
                }
        }
    }
}
