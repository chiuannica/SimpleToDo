package com.example.simpletodo

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    val REQUEST_CODE: Int = 90
    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadItems()

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        val longClickListener = object : TaskItemAdapter.OnLongClickListener {
            override fun onItemLongClicked(position: Int) {
                listOfTasks.removeAt(position)
                adapter.notifyDataSetChanged()
                saveItems()
            }
        }
        val clickListener = object : TaskItemAdapter.OnClickListener {
            override fun onItemClicked(position: Int) {
                launchTaskView(position)
            }
        }

        adapter = TaskItemAdapter(listOfTasks, longClickListener, clickListener)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        findViewById<Button>(R.id.addTaskButton).setOnClickListener() {
            val addTaskField = findViewById<EditText>(R.id.addTaskField);
            val newTaskName = addTaskField.text.toString();
            if (newTaskName != "") {
                listOfTasks.add(newTaskName);
                adapter.notifyItemInserted(listOfTasks.size - 1);
                addTaskField.setText("")
            }
            saveItems()
        }
    }

    private fun launchTaskView(position: Int) {
        val i = Intent(this, EditTaskActivity::class.java).apply {
            putExtra("task", listOfTasks[position])
            putExtra("position", position)
        }
        startActivityForResult(i, REQUEST_CODE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            val position: Int? = data?.extras?.getInt("position")
            val action: String? = data?.extras?.getString("action")
            val task: String? = data?.extras?.getString("task")

            if (action == "update" && position != null && task != null) {
                listOfTasks[position] = task
                Toast.makeText(this, "Updated to $task", Toast.LENGTH_SHORT).show()
            } else if (action == "delete" && position != null) {
                listOfTasks.removeAt(position)
                Toast.makeText(this, "Removed $task", Toast.LENGTH_SHORT).show()
            }
            adapter.notifyDataSetChanged()
            saveItems()

        }
    }

    private fun getDataFile() : File {
        return File(filesDir, "data.txt")
    }

    private fun loadItems() {
        try {
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }

    private fun saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }
}