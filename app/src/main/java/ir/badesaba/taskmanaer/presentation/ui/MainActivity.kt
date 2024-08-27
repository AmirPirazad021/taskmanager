package ir.badesaba.taskmanaer.presentation.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dagger.hilt.android.AndroidEntryPoint
import ir.badesaba.taskmanaer.R
import ir.badesaba.taskmanaer.databinding.ActivityMainBinding
import ir.badesaba.taskmanaer.domain.tasks.TasksModel
import ir.badesaba.taskmanaer.presentation.viewmodel.TasksViewModel
import ir.badesaba.taskmanaer.utils.Resource
import ir.badesaba.taskmanaer.utils.Utils.initRecyclerView

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

        tasksAdapter = TasksListAdapter()
        binding?.rvTasks?.initRecyclerView(this, tasksAdapter, false)

        viewModel.getTasks()

        observeTasks()

        binding?.apply {
            fabAddTask.setOnClickListener {
                UpsertTaskBottomSheet.createInstance().show(supportFragmentManager, "")
            }
        }

        tasksAdapter.setOnItemClickListener(object :
            TasksListAdapter.OnClickItemListener {
            override fun onClickDeleteItem(tasksModel: TasksModel) {
                viewModel.deleteTask(tasksModel)
            }

            override fun onClickEditItem(tasksModel: TasksModel) {
                UpsertTaskBottomSheet.createInstance(tasksModel).show(supportFragmentManager, "")
            }

        })

    }

    private fun observeTasks() {
        viewModel.tasksList.observe(this) {
            when (it) {
                is Resource.Error -> {

                }

                is Resource.Loading -> {

                }

                is Resource.Success -> {
                    it.data?.let { it1 -> setTasksList(it1) }
                }
            }
        }
    }

    private fun setTasksList(tasks: List<TasksModel>) {
        tasksAdapter.tasksList = tasks
    }

}