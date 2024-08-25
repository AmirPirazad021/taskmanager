package ir.badesaba.taskmanaer.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.badesaba.taskmanaer.R
import ir.badesaba.taskmanaer.domain.tasks.TasksModel
import ir.badesaba.taskmanaer.domain.tasks.use_case.DeleteTaskUseCase
import ir.badesaba.taskmanaer.domain.tasks.use_case.ListTasksUseCase
import ir.badesaba.taskmanaer.domain.tasks.use_case.UpsertTaskUseCase
import ir.badesaba.taskmanaer.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val upsertTaskUseCase: UpsertTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val listTasksUseCase: ListTasksUseCase,
) : ViewModel() {

    val tasksList: MutableLiveData<Resource<List<TasksModel>>> = MutableLiveData()

    val submitNewTask: MutableLiveData<Resource<Unit>> = MutableLiveData()

    private val title = MutableStateFlow<String?>(null)

    private val description = MutableStateFlow<String?>(null)

    private val deadline = MutableStateFlow<String?>(null)

    private val _messageId = MutableSharedFlow<Int?>()
    val messageId get() = _messageId.asSharedFlow()

    fun updateTitle(newTitle: String) {
        title.value = newTitle
    }

    fun updateDesc(newDesc: String) {
        description.value = newDesc
    }

    fun updateDeadline(newDeadline: String) {
        deadline.value = newDeadline
    }

    init {
        getTasks()
    }

    fun deleteTask(task: TasksModel) = viewModelScope.launch {
        deleteTaskUseCase(task)
    }

    fun upsertTask() = viewModelScope.launch {
        title.value?.let { title ->
            description.value?.let { description ->
                deadline.value?.let { deadline ->
                    submitNewTask.postValue(Resource.Loading())
                    val response = upsertTaskUseCase(
                        TasksModel(
                            title = title, description = description, deadLine = deadline.toLong()
                        )
                    )
                    submitNewTask.postValue(Resource.Success(response))
                } ?: kotlin.run {
                    _messageId.emit(R.string.error_deadline)
                }
            } ?: kotlin.run {
                _messageId.emit(R.string.error_desc)
            }
        } ?: kotlin.run {
            _messageId.emit(R.string.error_title)
        }
    }

    fun getTasks() = viewModelScope.launch(Dispatchers.IO) {
        tasksList.postValue(Resource.Loading())
        listTasksUseCase().collect {
            tasksList.postValue(handleTaskListResponse(Response.success(it)))
        }
    }

    private fun handleTaskListResponse(response: Response<List<TasksModel>>): Resource<List<TasksModel>> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

}