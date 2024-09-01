package ir.badesaba.taskmanaer.presentation.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import ir.badesaba.taskmanaer.R
import ir.badesaba.taskmanaer.REQUEST_NOTIFICATION_PERMISSIONS
import ir.badesaba.taskmanaer.data.TasksDto
import ir.badesaba.taskmanaer.databinding.ActivityMainBinding
import ir.badesaba.taskmanaer.di.ApiError
import ir.badesaba.taskmanaer.presentation.viewmodel.TasksViewModel
import ir.badesaba.taskmanaer.utils.Resource
import ir.badesaba.taskmanaer.utils.Utils.initRecyclerView
import ir.badesaba.taskmanaer.utils.enums.UiFlag
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding

    private val viewModel by viewModels<TasksViewModel>()

    private lateinit var tasksAdapter: TasksListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        grantNotificationPermission()

        tasksAdapter = TasksListAdapter()
        binding?.rvTasks?.initRecyclerView(this, tasksAdapter, false)

        viewModel.getTasks()

        observeTasks()

        binding?.apply {
            fabAddTask.setOnClickListener {
                UpsertTaskBottomSheet.createInstance().show(supportFragmentManager, "")
            }
        }

        binding?.btnRetry?.setOnClickListener {
            viewModel.getTasks()
        }

        tasksAdapter.setOnItemClickListener(object :
            TasksListAdapter.OnClickItemListener {
            override fun onClickDeleteItem(tasksModel: TasksDto) {
                viewModel.deleteTask(tasksModel)
            }

            override fun onClickEditItem(tasksModel: TasksDto) {
                UpsertTaskBottomSheet.createInstance(tasksModel).show(supportFragmentManager, "")
            }

        })

    }

    private fun observeTasks() {
        lifecycleScope.launch {
            viewModel.messageId.collect {
                Toast.makeText(
                    this@MainActivity,
                    it?.let { it1 -> resources.getString(it1) },
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }

        viewModel.taskState.observe(this) {
            when (it) {
                is Resource.Error<*> -> {
                    Log.e("TAGGGGG", "observeTasks: Error")
                    binding?.apply {
                        errorMessage.text = (it.errorType as ApiError).let {
                            it.message.plus(getString(R.string.text_error)).plus(it.statusCode)
                        }
                        vsTaskList.displayedChild = UiFlag.RETRY.value
                    }
                }

                is Resource.Loading -> {
                    Log.e("TAGGGGG", "observeTasks: LOADING")
                    binding?.vsTaskList?.displayedChild = UiFlag.LOADING.value
                }

                is Resource.Success -> {
                    binding?.vsTaskList?.displayedChild = UiFlag.DATA.value
                    setTasksList(it.data)
                }
            }
        }
    }

    private fun setTasksList(tasks: List<TasksDto>) {
        tasksAdapter.tasksList = tasks
    }


    private fun grantNotificationPermission() {
        if (Build.VERSION.SDK_INT < 33) return
        val notification = ContextCompat.checkSelfPermission(
            this, Manifest.permission.POST_NOTIFICATIONS
        )
        if (notification != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                REQUEST_NOTIFICATION_PERMISSIONS
            )
        }
    }
}