package com.egorvaskon.todoapp.ui

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.egorvaskon.todoapp.R
import com.egorvaskon.todoapp.Task
import com.egorvaskon.todoapp.databinding.TaskBinding
import com.egorvaskon.todoapp.layoutInflater
import java.text.DateFormat
import java.util.*

class ToDoListAdapter(private val context: Context,
                      private val onItemChanged: (item: Task) -> Unit)
    : RecyclerView.Adapter<ToDoListAdapter.TaskViewHolder>() {

    companion object{
        private const val TAG = "ToDoListAdapter"
    }

    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view){
        private val viewBinding = TaskBinding.bind(view)

        init {
            viewBinding.todoItem.setOnCheckedChangeListener { _, isChecked ->
                tasks[adapterPosition].done = isChecked
                onItemChanged(tasks[adapterPosition])
            }
        }

        fun bind(task: Task){
            viewBinding.todoItem.text = task.text
            viewBinding.todoItem.isChecked = task.done

            val creationDate = DateFormat
                .getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT)
                .format(Date(task.date))

            viewBinding.date.text = context.getString(R.string.creation_date,creationDate)
        }
    }

    private class TodoListItemDiffCallback(private val oldList: List<Task>,
                                           private val newList: List<Task>) : DiffUtil.Callback(){
        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].text == newList[newItemPosition].text
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].text == newList[newItemPosition].text
                    && oldList[oldItemPosition].done == newList[newItemPosition].done
        }
    }

    var tasks = emptyList<Task>()
        private set

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(context.layoutInflater.inflate(R.layout.task,parent,false))
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(tasks[position])
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    fun updateList(newList: List<Task>){
        Log.d(TAG,"newList: $newList")
        val diffResult = DiffUtil.calculateDiff(TodoListItemDiffCallback(tasks,newList),false)
        diffResult.dispatchUpdatesTo(this)
        tasks = newList
    }
}