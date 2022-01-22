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
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class NewCardActivity : AppCompatActivity() {

    lateinit var startDate : LocalDate // TODO: viewmodel的なのを使う
    lateinit var startTime : LocalTime
    var isSetDate : Boolean = false
    var isSetTime : Boolean = false
    var timeHour : Int = 0
    var timeMinute : Int = 0
    var strStartDateTime : String = ""
    var strTime : String = ""
    var strDateTime : String = ""

    lateinit var tvDate : TextView
    lateinit var ibDate : ImageButton
    lateinit var tvTime : TextView
    lateinit var ibTime : ImageButton

    companion object {
        const val card_name = "カードの名前"
//        const val card_startDateTime = "カードの開始日時"
        const val card_startDate = "カードの開始日付"
        const val card_startTime = "カードの開始時刻"
        const val card_isStartDateSet = "カードの開始日付を使うか"
        const val card_isStartTimeSet = "カードの開始時刻を使うか"
        const val card_strStartDateTime = "カードの開始日時を表す文字列"
        const val card_strDateTime = "カードの開始日時・時間を表す文字列"
        const val card_timeHour = "カードの所要時間:時"
        const val card_timeMinute = "カードの所要時間:分"
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
                strDateTime = (
                    if (ibDate.visibility == View.VISIBLE && ibTime.visibility == View.VISIBLE) "$strStartDateTime から $strTime"
                    else if (ibDate.visibility == View.VISIBLE && ibTime.visibility != View.VISIBLE) strStartDateTime
                    else if (ibDate.visibility != View.VISIBLE && ibTime.visibility == View.VISIBLE) strTime
                    else ""
                )

                replyIntent.putExtra(card_name, editWordView.text.toString())
                replyIntent.putExtra(card_startDate, startDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")))
                replyIntent.putExtra(card_startTime, startTime.format(DateTimeFormatter.ofPattern("HH:mm")))
                replyIntent.putExtra(card_isStartDateSet, isSetDate)
                replyIntent.putExtra(card_isStartTimeSet, isSetTime)
                replyIntent.putExtra(card_strStartDateTime, strStartDateTime)
                replyIntent.putExtra(card_strDateTime, strDateTime)
                replyIntent.putExtra(card_timeHour, timeHour.toString())
                replyIntent.putExtra(card_timeMinute, timeMinute.toString())
                replyIntent.putExtra(card_comment, editCommentView.text.toString())
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }

        // 子Fragment実行結果
        supportFragmentManager.setFragmentResultListener("setStartDateTime", this) { key, data ->
            startDate = LocalDate.parse(data.getString("startDate"), DateTimeFormatter.ofPattern("yyyy/MM/dd"))
            startTime = LocalTime.parse(data.getString("startTime"), DateTimeFormatter.ofPattern("HH:mm"))
            isSetDate = data.getBoolean("isSetDate")
            isSetTime = data.getBoolean("isSetTime")

            // 結果を使った処理
//            Log.v("", "Good: $year $month $date - $hourOfDay $minute")
            setStartDateTime(startDate, startTime, isSetDate, isSetTime)
        }

        supportFragmentManager.setFragmentResultListener("setTimer", this) { key, data ->
            val hour = data.getInt("hour", 0)
            val minute = data.getInt("minute", 0)

            // 結果を使った処理
            Log.v("", "Good: $hour $minute")
            setTimer(hour, minute)
        }

        startDate = LocalDate.now()
        startTime = LocalTime.now()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setStartDateTime(sd: LocalDate, st: LocalTime, isSetDate: Boolean, isSetTime: Boolean) {
        var strDate : String = ""
        var strTime : String = ""
        if (isSetDate) {
            startDate = LocalDate.of(sd.year, sd.monthValue, sd.dayOfMonth)
            strDate = startDate.format(DateTimeFormatter.ofPattern("M/d"))
        }
        if (isSetTime) {
            startTime = LocalTime.of(st.hour, st.minute, 0)
            strTime = startTime.format(DateTimeFormatter.ofPattern("H:mm"))
        }
        Log.v("", "isSetDate, isSetTime: $isSetDate, $isSetTime. $strDate, $strTime")
        if (isSetDate && isSetTime) strStartDateTime = "$strDate $strTime"
        if (isSetDate && !isSetTime) strStartDateTime = strDate
        if (!isSetDate && isSetTime) strStartDateTime = strTime
        if (isSetDate || isSetTime) {
            tvDate.setText(strStartDateTime)
            ibDate.visibility = View.VISIBLE
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setTimer(hour: Int, minute: Int) {
        timeHour = hour
        timeMinute = minute
        strTime = "$hour 時間 $minute 分"
        tvTime.setText(strTime)
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