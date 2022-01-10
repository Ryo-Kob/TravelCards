package com.rkb.travelcards.newCard

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import com.rkb.travelcards.R
import com.rkb.travelcards.reusable.TimerPickerFragment
import java.util.*
import java.time.Duration
import java.time.LocalDateTime
import java.time.temporal.TemporalAmount

class NewCardActivity : AppCompatActivity() {

    private val REQUEST_INPUT_NAME : Int = 1234

    lateinit var startDateTime : Calendar // TODO: viewmodel的なのを使う
    lateinit var timer : Duration

    lateinit var tvDate : TextView
    lateinit var ibDate : ImageButton
    lateinit var tvTime : TextView
    lateinit var ibTime : ImageButton

    @RequiresApi(Build.VERSION_CODES.O) // なぜこの宣言が必要かわからないが、Duration.ofHours() を使うときに要求された
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

        val btTimer = findViewById<Button>(R.id.button_time)
        btTimer.setOnClickListener {
            TimerPickerFragment().show(supportFragmentManager, "timerPicker")
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

        supportFragmentManager.setFragmentResultListener("setTimer", this) { key, data ->
            val hour = data.getInt("hour", 0)
            val minute = data.getInt("minute", 0)

            // 結果を使った処理
            Log.v("", "Good: $hour $minute")
            setTimer(hour, minute)
        }

        startDateTime = Calendar.getInstance()
        timer = Duration.ofHours(0)
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setTimer(hour: Int, minute: Int) {
        timer.withSeconds((minute*60 + hour*3600).toLong())
        tvTime.setText("$hour 時間 $minute 分")
        ibTime.visibility=View.VISIBLE
    }

    companion object {
        const val EXTRA_REPLY = "com.example.android.wordlistsql.REPLY"
    }

    fun showTimerPickerDialog(v: View) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.v("", "Code: $requestCode, $resultCode")

        when (requestCode) {
            REQUEST_INPUT_NAME -> {
                if (resultCode != RESULT_OK) {
                    return
                }
                val name = data?.getStringExtra(Intent.EXTRA_TEXT)
                Log.v("", "FirstFragment: 入力された名前は$name")
                return
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}