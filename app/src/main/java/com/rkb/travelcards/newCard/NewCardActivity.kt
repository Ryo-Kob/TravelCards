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

class NewCardActivity : AppCompatActivity() {

    lateinit var startDateTime : Calendar // TODO: viewmodel的なのを使う
    var isSetDate : Boolean = false
    var isSetTime : Boolean = false
    lateinit var timer : Duration

    lateinit var tvDate : TextView
    lateinit var ibDate : ImageButton
    lateinit var tvTime : TextView
    lateinit var ibTime : ImageButton

    companion object {
        const val card_name = "カードの名前"
        const val card_startDateTime = "カードの開始日時"
        const val card_isStartDateSet = "カードの開始日付を使うか"
        const val card_isStartTimeSet = "カードの開始時刻を使うか"
        const val card_comment = "カードのコメント"
    }

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
            val editCommentView = findViewById<EditText>(R.id.editText_comment)

            if (TextUtils.isEmpty(editWordView.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                replyIntent.putExtra(card_name, editWordView.text.toString())
                replyIntent.putExtra(card_startDateTime, startDateTime)
                replyIntent.putExtra(card_isStartDateSet, isSetDate)
                replyIntent.putExtra(card_isStartTimeSet, isSetTime)
                replyIntent.putExtra(card_comment, editCommentView.text.toString())
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
            isSetDate = data.getBoolean("isSetDate")
            isSetTime = data.getBoolean("isSetTime")

            // 結果を使った処理
            Log.v("", "Good: $year $month $date - $hourOfDay $minute")
            setStartDateTime(year, month, date, hourOfDay, minute, isSetDate, isSetTime)
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

    private fun setStartDateTime(year: Int, month: Int, date: Int, hourOfDay: Int, minute: Int, isSetDate: Boolean, isSetTime: Boolean) {
        if (isSetDate) {
            startDateTime.set(Calendar.YEAR, year)
            startDateTime.set(Calendar.MONTH, month)
            startDateTime.set(Calendar.DATE, date)
        }
        if (isSetTime) {
            startDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
            startDateTime.set(Calendar.MINUTE, minute)
        }
        if (isSetDate && isSetTime) tvDate.setText("$month/$date $hourOfDay:$minute")
        if (isSetDate && !isSetTime) tvDate.setText("$month/$date")
        if (!isSetDate && isSetTime) tvDate.setText("$hourOfDay:$minute")
        if (!isSetDate && !isSetTime) return
        ibDate.visibility=View.VISIBLE
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setTimer(hour: Int, minute: Int) {
        timer.withSeconds((minute*60 + hour*3600).toLong())
        tvTime.setText("$hour 時間 $minute 分")
        ibTime.visibility=View.VISIBLE
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        Log.v("", "Code: $requestCode, $resultCode")
//
//        if (resultCode != RESULT_OK) {
//            return
//        }
//        val name = data?.getStringExtra(Intent.EXTRA_TEXT)
//        Log.v("", "FirstFragment: 入力された名前は$name")
//        return
//    }
}