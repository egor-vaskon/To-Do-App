package com.egorvaskon.todoapp.ui

import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.egorvaskon.todoapp.App
import com.egorvaskon.todoapp.R
import com.egorvaskon.todoapp.Task
import com.egorvaskon.todoapp.databinding.FragmentTodoListBinding
import com.egorvaskon.todoapp.getDrawableCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ToDoListFragment : Fragment(){

    companion object{
        @JvmStatic
        fun newInstance(): ToDoListFragment {
            return ToDoListFragment()
        }
    }

    private val viewModel: ToDoListViewModel by activityViewModels {
        ToDoListViewModel.Factory(App.taskRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_todo_list,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewBinding = FragmentTodoListBinding.bind(view)
        val todoListAdapter = ToDoListAdapter(requireContext()){
            viewModel.addTask(it)
        }

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if(viewHolder is ToDoListAdapter.TaskViewHolder){
                    viewModel.deleteTask(todoListAdapter.tasks[viewHolder.adapterPosition])
                }
            }
        })

        itemTouchHelper.attachToRecyclerView(viewBinding.todoList)

        viewBinding.todoList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(DividerItemDecoration(requireContext(),LinearLayoutManager.VERTICAL).apply {
                //setDrawable(requireContext().getDrawableCompat(R.drawable.to_do_list_divider))
            })
            adapter = todoListAdapter
        }

        viewBinding.newTask.isEndIconVisible = !viewBinding.newTaskText.text.isNullOrBlank()

        viewBinding.newTaskText.addTextChangedListener {
            viewBinding.newTask.isEndIconVisible = !it.isNullOrBlank()
        }

        viewBinding.newTask.setEndIconOnClickListener {
            viewModel.addTask(Task(System.currentTimeMillis(),viewBinding.newTaskText.text!!.toString()))
            viewBinding.newTaskText.setText("")
        }

        lifecycleScope.launch {
            viewModel.tasks.collect {
                withContext(Dispatchers.Main){
                    todoListAdapter.updateList(it)
                }
            }
        }
    }

}