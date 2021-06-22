package com.egorvaskon.todoapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.egorvaskon.todoapp.Task
import com.egorvaskon.todoapp.db.TaskRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class ToDoListViewModel(private val taskRepository: TaskRepository) : ViewModel(){

    companion object{
        const val SHOW_ALL = 0
        const val SHOW_ONLY_FINISHED = 1
        const val ORDER_BY_DATE = 0
        const val ORDER_BY_CONTENT = 1
    }

    private val taskSource = MutableStateFlow(taskRepository.tasksSortedByDate)

    private val sortedTasks = taskSource
        .flatMapLatest { it  -> it.map { if(reversed.value) it.asReversed() else it}}

    val tasks = sortedTasks
        .map { it -> it.filter {
            it.done || filter.value == SHOW_ALL
        }}
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            emptyList())

    private val _tasksLeft = MutableStateFlow(0)
    val tasksLeft = _tasksLeft.asStateFlow()

    private val filter = MutableStateFlow(SHOW_ALL)

    val order = MutableStateFlow(ORDER_BY_DATE)
    val reversed = MutableStateFlow(false)

    init{
        viewModelScope.launch {
            launch {
                sortedTasks.map{it -> it.filter { !it.done }}.collect {
                    _tasksLeft.value = it.size
                }
            }

            launch {
                order.collect {
                    if(order.value == ORDER_BY_DATE)
                        taskSource.value = taskRepository.tasksSortedByDate
                    else if(order.value == ORDER_BY_CONTENT)
                        taskSource.value = taskRepository.tasksSortedByText
                }
            }
        }
    }

    fun addTask(task: Task){
        viewModelScope.launch {
            taskRepository.addTask(task)
        }
    }

    fun deleteTask(task: Task){
        viewModelScope.launch {
            taskRepository.deleteTask(task)
        }
    }

    fun setReversed(contentReversed: Boolean) {
        reversed.value = contentReversed
        updateToDoList()
    }

    fun setFilter(newFilter: Int){
        filter.value = newFilter
        updateToDoList()
    }

    private fun updateToDoList(){
        val taskList =
            if(order.value == ORDER_BY_DATE)
                taskRepository.tasksSortedByDate
            else
                taskRepository.tasksSortedByText

        taskSource.value = taskList.map { it }
    }

    class Factory(private val taskRepository: TaskRepository) : ViewModelProvider.Factory{
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(ToDoListViewModel::class.java))
                return ToDoListViewModel(taskRepository) as T

            throw IllegalArgumentException()
        }
    }

}