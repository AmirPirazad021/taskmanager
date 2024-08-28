package ir.badesaba.taskmanaer.presentation.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.badesaba.taskmanaer.R
import ir.badesaba.taskmanaer.data.TasksDto
import ir.badesaba.taskmanaer.di.ApiError
import ir.badesaba.taskmanaer.di.parseError
import ir.badesaba.taskmanaer.domain.tasks.use_case.DeleteTaskUseCase
import ir.badesaba.taskmanaer.domain.tasks.use_case.InsertAllTasksUseCase
import ir.badesaba.taskmanaer.domain.tasks.use_case.ListTasksUseCase
import ir.badesaba.taskmanaer.domain.tasks.use_case.UpsertTaskUseCase
import ir.badesaba.taskmanaer.utils.DataStoreManager
import ir.badesaba.taskmanaer.utils.Resource
import ir.badesaba.taskmanaer.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val upsertTaskUseCase: UpsertTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val listTasksUseCase: ListTasksUseCase,
    private val insertAllTasksUseCase: InsertAllTasksUseCase,
) : ViewModel() {

    private val _taskState = MutableLiveData<Resource<List<TasksDto>>>(Resource.Loading)
    val taskState: LiveData<Resource<List<TasksDto>>> = _taskState

    val submitNewTask: MutableLiveData<Resource<Unit>> = MutableLiveData()

    private val _title = MutableStateFlow<String?>(null)
    val title get() = _title.asStateFlow()

    private val _description = MutableStateFlow<String?>(null)
    val description get() = _description.asStateFlow()

    private val _deadline = MutableStateFlow<Long?>(null)
    val deadline get() = _deadline.asStateFlow()

    private val _messageId = MutableSharedFlow<Int?>()
    val messageId get() = _messageId.asSharedFlow()

    fun updateTitle(newTitle: String) {
        _title.value = newTitle
    }

    fun updateDesc(newDesc: String) {
        _description.value = newDesc
    }

    fun updateDeadline(newDeadline: String) {
        val arrDateTime = newDeadline.split(" ")
        val arrDate = arrDateTime[0].split("/")
        val arrTime = arrDateTime[1].split(":")
        _deadline.value = Utils.convertDateToTimestamp(
            arrDate[0].toInt(),
            arrDate[1].toInt(),
            arrDate[2].toInt(),
            arrTime[0].toInt(),
            arrTime[1].toInt()
        )
    }

    init {
        getTasks()
    }

    fun deleteTask(task: TasksDto) = viewModelScope.launch {
        deleteTaskUseCase(task)
    }

    fun upsertTask(taskModel: TasksDto) = viewModelScope.launch {
        title.value?.let { title ->
            description.value?.let { description ->
                deadline.value?.let { deadline ->
                    submitNewTask.postValue(Resource.Loading)
                    val response = upsertTaskUseCase(taskModel)
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
        listTasksUseCase().collect {
            _taskState.postValue(Resource.Loading)
            if (it.isEmpty() && !DataStoreManager.getValue(
                    context,
                    DataStoreManager.FETCHED_DATA_FROM_SERVER,
                    false
                ).first()
            )
                fetchFromServer()
            else
                _taskState.postValue(handleTaskListResponse(Response.success(it)))
        }
    }

    private fun fetchFromServer() = viewModelScope.launch {
        try {
            insertAllTasksUseCase()
            DataStoreManager.saveValue(
                context,
                DataStoreManager.FETCHED_DATA_FROM_SERVER,
                true
            )
        } catch (e: HttpException) {
            Log.e("API Error0", "${e.code()}")
            _taskState.postValue(
                Resource.Error(
                    e.response()?.let { parseError(it) }
                )
            )
        } catch (e: Exception) {
            _taskState.postValue(Resource.Error(ApiError(1000, e.message.toString())))
        }
    }

    private fun handleTaskListResponse(response: Response<List<TasksDto>>): Resource<List<TasksDto>> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

}