package com.example.justdoam

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.justdoam.databinding.FragmentTaskBinding

/**
 * A simple [Fragment] subclass.
 */
private const val TAG = "TaskFragment"
class TaskFragment : Fragment() {
    private val taskViewModel: TaskViewModel by lazy {
        ViewModelProvider(this).get(TaskViewModel::class.java)
    }
    private lateinit var binding: FragmentTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = arguments?.let { TaskFragmentArgs.fromBundle(it) }
        if (args?.taskID != null) taskViewModel.loadTask(args.taskID)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_task, container, false)
        binding.save.setOnClickListener {
            if (!(taskViewModel.charSq.isBlank() || taskViewModel.charSq.isEmpty())) {
                if (taskViewModel.isNewTask) {
                    taskViewModel.apply {
                        this.task.taskString = this.charSq.trim()
                        addTask(this.task)
                    }
                } else {
                    taskViewModel.apply {
                        this.task.taskString = this.charSq.trim()
                        saveTask(this.task)
                    }
                }
            }
            it.findNavController().navigate(TaskFragmentDirections.actionTaskFragmentToTodayFragment())
        }
        Log.i(TAG, "onCreateView: Set to value: ${taskViewModel.charSq}")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        taskViewModel.taskLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                taskViewModel.task = it
                if (taskViewModel.isNewTask) {
                    taskViewModel.charSq = taskViewModel.task.taskString
                    taskViewModel.isNewTask = false
                }
                Log.i(TAG, "onViewCreated")
                updateUI()
            }
        })
    }

    override fun onStart() {
        super.onStart()

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // not needed for now
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val newString = s.toString()
                if (!(newString.isEmpty() || newString.isBlank())) taskViewModel.charSq = newString
                binding.taskString.placeCursorToEnd()
                Log.i(TAG, "Set to value: ${taskViewModel.charSq}")
            }

            override fun afterTextChanged(s: Editable?) {
                // not needed for now
            }

        }
        Log.i(TAG, "onStart")
        binding.taskString.addTextChangedListener(textWatcher)

    }

    fun EditText.placeCursorToEnd() {
        this.setSelection(this.text.length)
    }

    private fun updateUI() {
        binding.taskString.apply {
            if (taskViewModel.justStarted) {
                setText(taskViewModel.task.taskString)
                taskViewModel.justStarted = false
            } else {
                setText(taskViewModel.charSq)
            }
        }
    }

}