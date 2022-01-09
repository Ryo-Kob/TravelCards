package com.rkb.travelcards.newCard

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import com.rkb.travelcards.R
import com.rkb.travelcards.reusable.TimerPickerFragment
import java.util.*

class NewCardActivity : AppCompatActivity() {

    private val REQUEST_INPUT_NAME : Int = 1234

    lateinit var startDateTime : Calendar // TODO: livedata的なのを使う

    lateinit var tvDate : TextView
    lateinit var ibDate : ImageButton
    lateinit var tvTime : TextView
    lateinit var ibTime : ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_card)

        title = getString(R.string.activity_new_card_title_text)

        // lateinit
        tvDate = findViewById(R.id.text_view_date)
        ibDate = findViewById(R.id.image_button_date)
        tvTime = findViewById(R.id.text_view_time)
        ibTime = findViewById(R.id.image_button_time)

        // ボタンクリックイベント
        val btn = findViewById<Button>(R.id.button_date)
        btn.setOnClickListener {
            val dateTimeDialog = DateTimeDialogFragment()
            dateTimeDialog.show(supportFragmentManager, "String")
        }

        val button = findViewById<Button>(R.id.activity_new_card_button_submit)
        button.setOnClickListener {
            val replyIntent = Intent()
            val editWordView = findViewById<EditText>(R.id.editText_title)

            if (TextUtils.isEmpty(editWordView.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val word = editWordView.text.toString()
                replyIntent.putExtra(EXTRA_REPLY, word)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }

        // 子Fragment実行結果
        supportFragmentManager.setFragmentResultListener("setStartDateTime", this) { key, data ->
            val year = data.getInt("year", 0)
            val month = data.getInt("month", 0)
            val date = data.getInt("date", 0)
            val hourOfDay = data.getInt("hourOfDay", 0)
            val minute = data.getInt("minute", 0)

            // 結果を使った処理
            Log.v("", "Good: $year $month $date - $hourOfDay $minute")
            setStartDateTime(year, month, date, hourOfDay, minute)
        }

        startDateTime = Calendar.getInstance()
    }

    private fun setStartDateTime(year: Int, month: Int, date: Int, hourOfDay: Int, minute: Int) {
        startDateTime.set(Calendar.YEAR, year)
        startDateTime.set(Calendar.MONTH, month)
        startDateTime.set(Calendar.DATE, date)
        startDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
        startDateTime.set(Calendar.MINUTE, minute)
        tvDate.setText("$month/$date $hourOfDay:$minute")
        ibDate.visibility=View.VISIBLE

    }

    companion object {
        const val EXTRA_REPLY = "com.example.android.wordlistsql.REPLY"
    }

    fun showTimerPickerDialog(v: View) {
        TimerPickerFragment().show(supportFragmentManager, "timerPicker")
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