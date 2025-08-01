package com.openclassrooms.arista.ui.exercise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.arista.data.repository.DataResult
import com.openclassrooms.arista.domain.model.Exercise
import com.openclassrooms.arista.domain.usecase.AddNewExerciseUseCase
import com.openclassrooms.arista.domain.usecase.DeleteExerciseUseCase
import com.openclassrooms.arista.domain.usecase.GetAllExercisesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExerciseViewModel @Inject constructor(
    private val getAllExercisesUseCase: GetAllExercisesUseCase,
    private val addNewExerciseUseCase: AddNewExerciseUseCase,
    private val deleteExerciseUseCase: DeleteExerciseUseCase
) : ViewModel() {
    private val _exercisesFlow = MutableStateFlow<List<Exercise>>(emptyList())
    val exercisesFlow: StateFlow<List<Exercise>> = _exercisesFlow.asStateFlow()

    private val _operationMessage = MutableStateFlow<String?>(null)
    val operationMessage: StateFlow<String?> = _operationMessage.asStateFlow()

    init {
        loadAllExercises()
    }

    fun deleteExercise(exercise: Exercise) {
        viewModelScope.launch {
            when (val result = deleteExerciseUseCase.execute(exercise)) {
                is DataResult.Success -> {
                    _operationMessage.value = "Exercise deleted successfully!"
                }
                is DataResult.Error -> {
                    _operationMessage.value = "Failed to delete exercise: ${result.exception.message}"
                }
            }
        }
    }

    private fun loadAllExercises() {
        viewModelScope.launch {
            getAllExercisesUseCase.execute()
                .catch { e ->
                    _operationMessage.value = "Unexpected error in exercise data flow: ${e.message}"
                    _exercisesFlow.value = emptyList()
                }
                .collect { result ->
                    when (result) {
                        is DataResult.Success -> {
                            _exercisesFlow.value = result.data
                            _operationMessage.value = null
                        }
                        is DataResult.Error -> {
                            _exercisesFlow.value = emptyList()
                            _operationMessage.value = "Failed to load exercises: ${result.exception.message}"
                        }
                    }
                }
        }
    }

    fun addNewExercise(exercise: Exercise) {
        viewModelScope.launch {
            when (val result = addNewExerciseUseCase.execute(exercise)) {
                is DataResult.Success -> {
                    _operationMessage.value = "Exercise added successfully!"
                }
                is DataResult.Error -> {
                    _operationMessage.value = "Failed to add exercise: ${result.exception.message}"
                }
            }
        }
    }
}
