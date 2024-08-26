package ir.badesaba.taskmanaer.presentation.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ir.badesaba.taskmanaer.R
import ir.badesaba.taskmanaer.databinding.UpsertTaskBinding
import ir.badesaba.taskmanaer.domain.tasks.TasksModel
import ir.badesaba.taskmanaer.presentation.setUpAlarm
import ir.badesaba.taskmanaer.presentation.viewmodel.TasksViewModel

@SuppressLint("SetTextI18n")
class UpsertTaskBottomSheet : BottomSheetDialogFragment() {

    private val taskViewModel: TasksViewModel by activityViewModels()

    private var _binding: UpsertTaskBinding? = null
    private val binding get() = _binding!!


    @Suppress("DEPRECATION")
    private val tasksModel by lazy<TasksModel?> {
        requireArguments().getParcelable(
            TASK_ITEM
        )
    }

    companion object {
        private const val TASK_ITEM = "TASK_ITEM"
        fun createInstance(
            taskModelItem: TasksModel? = null,
        ): UpsertTaskBottomSheet {
            val bundle = bundleOf(
                TASK_ITEM to taskModelItem,
            )
            return UpsertTaskBottomSheet().also {
                it.arguments = bundle
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogLightOption)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = UpsertTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            tasksModel?.let { tasksModel ->
                edTitle.setText(tasksModel.title)
                etDescription.setText(tasksModel.description)
            }
            saveTask.setOnClickListener {
                taskViewModel.apply {
                    val nowTime = (System.currentTimeMillis() + 1000 * 30).toString()
                    taskViewModel.updateTitle(edTitle.text.toString())
                    taskViewModel.updateDesc(etDescription.text.toString())
                    taskViewModel.updateDeadline(nowTime)
                    Log.e("TAGGGGG", "onViewCreated: $nowTime")
                    val model = TasksModel(
                        id = tasksModel?.id ?: 0,
                        title = title.value!!,
                        description = description.value!!,
                        deadLine = deadline.value?.toLong()!!
                    )
                    taskViewModel.upsertTask(model, false)
                    setUpAlarm(requireActivity(), model)
                    dismiss()
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}