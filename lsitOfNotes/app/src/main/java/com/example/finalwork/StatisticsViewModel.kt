package com.example.finalwork

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class StatisticsViewModel : ViewModel()
{
    var mStartDate = MutableLiveData<LocalDate>(LocalDate.now())
    var mEndDate   = MutableLiveData<LocalDate>(LocalDate.now())
    var mStartTime = MutableLiveData<LocalTime>(LocalTime.now())
    var mEndTime   = MutableLiveData<LocalTime>(LocalTime.now())
    var mActiveDateFiled = -1

    //var mStartDate = LocalDate.now()
    //var mEndDate   = LocalDate.now()
    //var mStartTime = LocalTime.now()
    //var mEndTime   = LocalTime.now()
}