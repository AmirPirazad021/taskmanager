package ir.badesaba.taskmanaer.presentation.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ir.badesaba.taskmanaer.R
import ir.badesaba.taskmanaer.databinding.ItemTaskBinding
import ir.badesaba.taskmanaer.domain.tasks.TasksModel

class TasksListAdapter : RecyclerView.Adapter<TasksListAdapter.TaskViewHolder>() {
    inner class TaskViewHolder(
        val binding: ItemTaskBinding
    ) : RecyclerView.ViewHolder(
        binding.root
    )

    private var onClickItemDeleteListener: OnClickItemDeleteListener? = null

    private val diffCallback = object : DiffUtil.ItemCallback<TasksModel>() {
        override fun areItemsTheSame(
            oldItem: TasksModel, newItem: TasksModel
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: TasksModel, newItem: TasksModel
        ): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var tasksList: List<TasksModel>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            ItemTaskBinding.bind(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_task, parent, false
                )
            )
        )
    }

    override fun getItemCount(): Int {
        return tasksList.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.binding.apply {
            val task = tasksList[position]
            tvTitle.text = task.title
            tvDescription.text = task.description
            tvDeadline.text = task.deadLine.toString()
            ivDelete.setOnClickListener {
                onClickItemDeleteListener?.onClickDeleteItem(task)
            }
        }
    }

    interface OnClickItemDeleteListener {
        fun onClickDeleteItem(tasksModel: TasksModel)
    }

    fun setOnClickDeleteItemListener(onClickListener: OnClickItemDeleteListener?) {
        this.onClickItemDeleteListener = onClickListener
    }

}