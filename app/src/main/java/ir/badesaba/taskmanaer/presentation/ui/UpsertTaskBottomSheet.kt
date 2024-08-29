package ir.badesaba.taskmanaer.presentation.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ir.badesaba.taskmanaer.R
import ir.badesaba.taskmanaer.data.TasksDto
import ir.badesaba.taskmanaer.databinding.UpsertTaskBinding
import ir.badesaba.taskmanaer.presentation.setUpAlarm
import ir.badesaba.taskmanaer.presentation.viewmodel.TasksViewModel
import ir.badesaba.taskmanaer.utils.Resource
import ir.badesaba.taskmanaer.utils.Utils

@SuppressLint("SetTextI18n")
class UpsertTaskBottomSheet : BottomSheetDialogFragment(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    private val taskViewModel: TasksViewModel by activityViewModels()

    private var _binding: UpsertTaskBinding? = null
    private val binding get() = _binding!!
    private var time = ""
    private var date = ""
    private var dateTime = ""


    @Suppress("DEPRECATION")
    private val tasksModel by lazy<TasksDto?> {
        requireArguments().getParcelable(
            TASK_ITEM
        )
    }

    companion object {
        private const val TASK_ITEM = "TASK_ITEM"
        fun createInstance(
            taskModelItem: TasksDto? = null,
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
                datePicker.setText(Utils.convertToDate(tasksModel.deadLine))
            }
            datePicker.setOnClickListener {
                val now = Calendar.getInstance()
                val dpd = DatePickerDialog(
                    requireActivity(),
                    this@UpsertTaskBottomSheet,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
                )
                dpd.show()
            }

            taskViewModel.submitNewTask.observe(viewLifecycleOwner) {
                when(it) {
                    is Resource.Error<*> -> {}
                    Resource.Loading -> {}
                    is Resource.Success -> {
                        setUpAlarm(requireActivity(), it.data)
                        dismiss()
                    }
                }
            }

            saveTask.setOnClickListener {
                taskViewModel.apply {
                    taskViewModel.updateTitle(edTitle.text.toString())
                    taskViewModel.updateDesc(etDescription.text.toString())
                    taskViewModel.updateDeadline(dateTime)
                    val model = TasksDto(
                        id = tasksModel?.id ?: 0,
                        title = title.value ?: "",
                        description = description.value ?: "",
                        deadLine = deadline.value ?: 0L
                    )
                    taskViewModel.upsertTask(model)
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDateSet(p0: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        date = "$year/${monthOfYear + 1}/$dayOfMonth"

        val now = Calendar.getInstance()
        val dpd = TimePickerDialog(
            requireActivity(),
            this,  // Pass your TimePickerDialog.OnTimeSetListener implementation here
            now.get(Calendar.HOUR_OF_DAY),  // Correct way to get the current hour
            now.get(Calendar.MINUTE),  // Correct way to get the current minute
            true  // Whether to use 24-hour mode
        )
        dpd.show()

    }

    override fun onTimeSet(p0: TimePicker?, hourOfDay: Int, minute: Int) {
        time = "$hourOfDay:$minute"
        dateTime = date.plus(" ").plus(time)
        binding.datePicker.text = dateTime.replace(" ", "  ")
    }

}