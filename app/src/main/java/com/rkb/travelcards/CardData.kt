package com.rkb.travelcards

class CardData {
    var title : String = ""
    var description : String = ""
    var strStartDate : String = ""
    var strStartTime : String = ""
    var isStartDateSet : Boolean = false
    var isStartTimeSet : Boolean = false
    var strStartDateTime : String = "(スケジュール未設定)"
    var timerHour : Int = 0
    var timerMinute : Int = 0
}