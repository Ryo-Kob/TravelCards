package com.rkb.travelcards.newCard

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.rkb.travelcards.R

class NewCardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_card)

        setTitle(getString(R.string.activity_new_card_title_text))

        // ボタンクリックイベント
//        val bDate = findViewById<Button>(R.id.activity_new_card_button_date)
//        bDate.setOnClickListener {
//            Toast.makeText(applicationContext, "Hello World!!!!!!!!!!!!!!", 1).show()
//        }

        val bTime = findViewById<Button>(R.id.activity_new_card_button_time)
        bTime.setOnClickListener {
            Toast.makeText(applicationContext, "Hello World!!!!!!!!!!!!!!", 1).show()
        }

        val button = findViewById<Button>(R.id.activity_new_card_button_submit)
        button.setOnClickListener {
            val replyIntent = Intent()
            val editWordView = findViewById<EditText>(R.id.activity_new_card_title)

            if (TextUtils.isEmpty(editWordView.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val word = editWordView.text.toString()
                replyIntent.putExtra(EXTRA_REPLY, word)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }

    companion object {
        const val EXTRA_REPLY = "com.example.android.wordlistsql.REPLY"
    }

    fun showTimePickerDialog(v: View) {
        TimePickerFragment().show(supportFragmentManager, "timePicker")
    }
}