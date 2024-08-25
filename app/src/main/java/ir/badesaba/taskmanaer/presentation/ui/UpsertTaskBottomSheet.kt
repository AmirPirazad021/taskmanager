package ir.badesaba.taskmanaer.presentation.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ir.badesaba.taskmanaer.R
import ir.badesaba.taskmanaer.databinding.UpsertTaskBinding
import ir.badesaba.taskmanaer.presentation.viewmodel.TasksViewModel

@SuppressLint("SetTextI18n")
class UpsertTaskBottomSheet : BottomSheetDialogFragment() {

    private val taskViewModel: TasksViewModel by activityViewModels()

    private var _binding: UpsertTaskBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogLightOption)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = UpsertTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            saveTask.setOnClickListener {
                taskViewModel.updateTitle(edTitle.text.toString())
                taskViewModel.updateDesc(etDescription.text.toString())
                taskViewModel.updateDeadline("1724590545")
                taskViewModel.upsertTask()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}