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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.rkb.travelcards.R
import com.rkb.travelcards.reusable.DatePickerFragment
import com.rkb.travelcards.reusable.TimePickerFragment

class NewCardActivity : AppCompatActivity() {

    private val REQUEST_INPUT_NAME : Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_card)

        setTitle(getString(R.string.activity_new_card_title_text))

        // ボタンクリックイベント
        val btn = findViewById<Button>(R.id.activity_new_card_button_date)
        btn.setOnClickListener {
            val intent = Intent(this, DateTimeDialogFragment::class.java)
//            startFragment(intent)
//            DateTimeDialogFragment().show(supportFragmentManager, "String")
//            Toast.makeText(this, answer, Toast.LENGTH_LONG).show()
            startActivityForResult(intent, REQUEST_INPUT_NAME)
//            Toast.makeText(getActivity(), "Hello World!!!!!!!!!!!!!!", 1).show()
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

    fun showDatePickerDialog(v: View) {
        DatePickerFragment().show(supportFragmentManager, "datePicker")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Toast.makeText(this, "Code: $requestCode, $resultCode", Toast.LENGTH_SHORT)

        when (requestCode) {
            REQUEST_INPUT_NAME -> {
                if (resultCode != RESULT_OK) {
                    return
                }
                val name = data?.getStringExtra(Intent.EXTRA_TEXT)
                Toast.makeText(this, "FirstFragment: 入力された名前は$name", Toast.LENGTH_SHORT)
                return
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}