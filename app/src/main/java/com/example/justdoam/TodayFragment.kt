package com.example.justdoam

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.CheckBox
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.justdoam.databinding.FragmentTodayBinding

/**
 * A simple [Fragment] subclass.
 */
class TodayFragment : Fragment() {

    // Associate this fragment with its viewModel
    private val todayViewModel : TodayViewModel by lazy {
        ViewModelProvider(this).get(TodayViewModel::class.java)
    }
    // adapter for the recyclerView
    private var adapter : TaskAdapter = TaskAdapter(emptyList<Task>())
    private lateinit var binding : FragmentTodayBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_today,
            container,
            false)
        // Initializes the layoutManager for the for the recyclerView
        binding.todayList.layoutManager = LinearLayoutManager(context)
        // Associates the recyclerView with its adapter
        binding.todayList.adapter = adapter
        // Enables optionsMenu in this fragment
        setHasOptionsMenu(true)
        return binding.root
    }

    // Must be overridden to inflate the menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.overflow_menu, menu)
    }

    // override to specify behaviour when a particular item is picked in the menu. In the default case the super method should be
    // called
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.taskFragment -> NavigationUI.onNavDestinationSelected(item, requireView().findNavController())
            R.id.delete -> {
                todayViewModel.deleteAll()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Observes taskListLiveData, from which it updates the UI
        todayViewModel.taskListLiveData.observe(viewLifecycleOwner, Observer { tasks ->
            // reset today's list adapter when there is a change in the database.
            // How does it know when there is a change in the database? By fucking observing taskListLiveData
            updateUI(tasks)
        })
    }

    // Used to reset today's list adapter
    fun updateUI(tasks: List<Task>) {
        adapter = TaskAdapter(tasks)
        binding.todayList.adapter = adapter
    }



    /* Class represents the ViewHolder through which the recyclerView will hold views
    It implements the OnClickListener interface
    Each instance of it holds properties unique to it
     */
    private inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        private lateinit var task: Task
        val isDone : CheckBox = view.findViewById(R.id.is_done)
        val taskString : TextView = view.findViewById(R.id.task_string)

        // Enable clickListeners on a viewHolder on initialization
        init {
            itemView.setOnClickListener(this)
        }

        // Used to bind data to the ViewHolder
        fun bind(task : Task) {
            this.task = task
            isDone.isChecked = task.isDone
            taskString.text = task.taskString
            // Notifies the database when a task is done(or undone)
            isDone.setOnCheckedChangeListener { _, isChecked ->
                task.isDone = isChecked
                todayViewModel.updateTask(task)
            }
        }

        // navigates to task fragment when an item is clicked
        override fun onClick(v: View?) {
            v?.findNavController()?.navigate(TodayFragmentDirections.actionTodayFragmentToTaskFragment(task.id))
        }
    }

    // Class represents the adapter for the recyclerView
    private inner class TaskAdapter(var tasks : List<Task>) : RecyclerView.Adapter<TaskViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
            val view = layoutInflater.inflate(R.layout.list_item, parent, false)
            return TaskViewHolder(view)
        }

        override fun getItemCount() = tasks.size

        override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
            val task = tasks[position]
            holder.bind(task)
        }

    }


}
