package com.egorvaskon.todoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.egorvaskon.todoapp.databinding.ActivityMainBinding
import com.egorvaskon.todoapp.ui.ToDoListFragment
import com.egorvaskon.todoapp.ui.ToDoListViewModel
import kotlinx.coroutines.flow.collect

class MainActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityMainBinding

    private val toDoListviewModel: ToDoListViewModel by viewModels {
        ToDoListViewModel.Factory(App.taskRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        setSupportActionBar(viewBinding.toolbar)

        if(savedInstanceState == null){
            supportFragmentManager
                .beginTransaction()
                .add(R.id.main_fragment_container,
                    ToDoListFragment.newInstance())
                .commit()
        }

        lifecycleScope.launchWhenCreated {
            toDoListviewModel.tasksLeft.collect {
                viewBinding.toolbar.subtitle = getString(R.string.tasks_left,it)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {

        menu?.let {
            it.findItem(R.id.reversed)?.let {  item ->
                item.isChecked = false
                return true
            }
        }

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.filter_show_all ->
                toDoListviewModel.setFilter(ToDoListViewModel.SHOW_ALL)
            R.id.filter_show_finished_only ->
                toDoListviewModel.setFilter(ToDoListViewModel.SHOW_ONLY_FINISHED)
            R.id.order_by_creation_date ->
                toDoListviewModel.order.value = ToDoListViewModel.ORDER_BY_DATE
            R.id.order_by_text ->
                toDoListviewModel.order.value = ToDoListViewModel.ORDER_BY_CONTENT
            R.id.reversed -> {
                item.isChecked = !item.isChecked
                toDoListviewModel.setReversed(item.isChecked)
            }
            else -> return false
        }

        //invalidateOptionsMenu()
        return true
    }
}