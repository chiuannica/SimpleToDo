package com.example.simpletodo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class EditTaskActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_task)

        val task: String = intent.getStringExtra("task").toString()
        val position: Int = intent.getIntExtra("position", 0)

        val data = Intent()
        data.putExtra("position", position)

        if (task != null) {
            findViewById<EditText>(R.id.taskTextView).setText(task)
        }

        findViewById<Button>(R.id.deleteButton).setOnClickListener {
            data.putExtra("action", "delete")
            data.putExtra("task", task)

            setResult(RESULT_OK, data)
            finish()
        }

        findViewById<Button>(R.id.updateButton).setOnClickListener {
            val newTaskName = findViewById<EditText>(R.id.taskTextView).text.toString()

            data.putExtra("action", "update")
            data.putExtra("task", newTaskName)

            setResult(RESULT_OK, data)
            finish()
        }

        findViewById<Button>(R.id.cancelButton).setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    }

    fun onSubmit(v: View) {
        this.finish()
    }
}