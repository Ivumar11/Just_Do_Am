package com.example.justdoam

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
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
    // Associate this fragment with its viewmodel
    private val taskViewModel: TaskViewModel by lazy {
        ViewModelProvider(this).get(TaskViewModel::class.java)
    }

    private lateinit var binding: FragmentTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Retrieves its arguments if there is
        val args = arguments?.let { TaskFragmentArgs.fromBundle(it) }

        /* If the taskId argument is not null, i.e, if the task is an old task, it indirectly updates [taskLiveData] from TaskViewModel.
        It first updates the taskIdLiveData, which in turn updates the taskLiveData. All accomplished through loadTask(UUID).
        The TaskLiveData is then observed by this fragment.
         */
        if (args?.taskID != null) taskViewModel.loadTask(args.taskID)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_task, container, false)

        binding.save.setOnClickListener {
            // Checks that the taskString to-be is a valid string
            if (!(taskViewModel.taskStringToBe.isBlank() || taskViewModel.taskStringToBe.isEmpty())) {
                // For task just being added newly
                if (taskViewModel.isNewTask) {
                    taskViewModel.apply {
                        this.task.taskString = this.taskStringToBe.trim()
                        // Adds the new task to the database
                        addTask(this.task)
                    }
                 // For already existing tasks
                } else {
                    taskViewModel.apply {
                        this.task.taskString = this.taskStringToBe.trim()
                        // Updates this task in the database
                        saveTask(this.task)
                    }
                }
            }
            // Navigates back to TodayFragment
            it.findNavController().navigate(TaskFragmentDirections.actionTaskFragmentToTodayFragment())
        }
        Log.i(TAG, "onCreateView: Set to value: ${taskViewModel.taskStringToBe}")

        // Specifies what happens when the focus on the taskString editText view changes
        binding.taskString.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                // Hides keyboard when the editText loses focus
                val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Observes the taskLiveData from TaskViewModel
        /* Only old tasks can execute this block of code beacause new tasks has nothing to observe as they don't call  loadTask(UUID)
         from TaskViewModel in onCreate
         */
        taskViewModel.taskLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                // Updates task to assume the value of the already existing task gotten from the database
                taskViewModel.task = it

                /* This block corrects the notion that this is a new task and updates our taskStringToBe to assume the value of
                task.taskString
                 */
                if (taskViewModel.isNewTask) {
                    taskViewModel.taskStringToBe = taskViewModel.task.taskString
                    taskViewModel.isNewTask = false
                }
                Log.i(TAG, "onViewCreated")
                // As the name suggests
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

            // Specifies what to do when text changes
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val newString = s.toString()
                // Updates the taskStringTobe if [newString] is a valid string (based on our context)
                if (!(newString.isEmpty() || newString.isBlank())) taskViewModel.taskStringToBe = newString
                // Ensures that cursor is at the end of the string all of the time, except the user touches another point in editText
                binding.taskString.placeCursorToEnd()
                Log.i(TAG, "Set to value: ${taskViewModel.taskStringToBe}")
            }

            override fun afterTextChanged(s: Editable?) {
                // not needed for now
            }

        }
        Log.i(TAG, "onStart")
        // Associates the taskString editText with the textWatcher
        binding.taskString.addTextChangedListener(textWatcher)

    }

    // Used to place cursor at the end of text in an EditText
    fun EditText.placeCursorToEnd() {
        this.setSelection(this.text.length)
    }

    // Used to put the relevant data on screen
    private fun updateUI() {
        binding.taskString.apply {
            // For the fragment when it is newly launched
            if (taskViewModel.justStarted) {
                setText(taskViewModel.task.taskString)
                taskViewModel.justStarted = false
             // For previously launched fragment
            } else {
                setText(taskViewModel.taskStringToBe)
            }
        }
    }

}