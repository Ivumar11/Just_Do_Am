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

    private val todayViewModel : TodayViewModel by lazy {
        ViewModelProvider(this).get(TodayViewModel::class.java)
    }
    private var adapter : TaskAdapter? = TaskAdapter(emptyList<Task>())
    private lateinit var binding : FragmentTodayBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_today,
            container,
            false)
        binding.todayList.layoutManager = LinearLayoutManager(context)
        binding.todayList.adapter = adapter
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.overflow_menu, menu)
    }

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
        todayViewModel.taskListLiveData.observe(viewLifecycleOwner, Observer { tasks ->
            updateUI(tasks)
        })
    }

    fun updateUI(tasks: List<Task>) {
        adapter = TaskAdapter(tasks)
        binding.todayList.adapter = adapter
    }



    private inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        private lateinit var task: Task
        val isDone : CheckBox = view.findViewById(R.id.is_done)
        val taskString : TextView = view.findViewById(R.id.task_string)

        init {
            itemView.setOnClickListener(this)
        }


        fun bind(task : Task) {
            this.task = task
            isDone.isChecked = task.isDone
            taskString.text = task.taskString
            isDone.setOnCheckedChangeListener { _, isChecked ->
                task.isDone = isChecked
                todayViewModel.updateTask(task)
            }
        }

        override fun onClick(v: View?) {
            v?.findNavController()?.navigate(TodayFragmentDirections.actionTodayFragmentToTaskFragment(task.id))
        }
    }

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
