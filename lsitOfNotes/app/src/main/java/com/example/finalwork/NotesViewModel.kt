package com.example.finalwork

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.*
import java.time.LocalDate
import java.time.LocalTime

class NotesViewModel(application: Application): AndroidViewModel(application)
{
    private val mFile: File = File(application.applicationContext.filesDir, "notes.txt")
    var mNotes: MutableLiveData<MutableList<Note>> = MutableLiveData(mutableListOf())
    lateinit var mAdapter: MainAdapter
    var mPosition: Int = -1

    var min_pulse = 0
    var min_sp    = 0
    var min_dp    = 0
    var max_pulse = 0
    var max_sp    = 0
    var max_dp    = 0
    var avg_pulse = 0
    var avg_sp    = 0
    var avg_dp    = 0

    init
    {
        Deserialize()
    }

    fun Add(note: Note)
    {
        mNotes.value?.add(note)
        Sort()
    }

    fun Remove(i: Int)
    {
        mNotes.value?.removeAt(i)
    }

    fun GetNote(i: Int) : Note
    {
        return mNotes.value!!.get(i)
    }

    fun Size() : Int
    {
        return mNotes.value!!.size
    }

    fun Sort()
    {
        mNotes.value?.sortBy { it.date }
        mNotes.value?.reverse()
    }

    fun Serialize()
    {
        val outArray = mutableListOf<NoteSerializable>()
        for (note in mNotes.value!!)
        {
            outArray.add(ToNoteSerializable(note))
        }
        val output = Json.encodeToString(outArray)

        if (!mFile.exists())
            mFile.createNewFile()

        mFile.writeText(output)
    }

    fun Deserialize()
    {
        if (mFile.exists())
        {
            var json_str: String = mFile.readText()
            var notes_ser = Json.decodeFromString<MutableList<NoteSerializable>>(json_str)
            for (note_ser in notes_ser)
            {
                mNotes.value?.add(ToNote(note_ser))
            }
        }
    }

    fun CalculateStatistic(start_date: LocalDate,
                           end_date: LocalDate,
                           start_time: LocalTime,
                           end_time: LocalTime)
    {
        min_pulse = 1000
        min_sp    = 1000
        min_dp    = 1000
        max_pulse = 0
        max_sp    = 0
        max_dp    = 0
        avg_pulse = 0
        avg_sp    = 0
        avg_dp    = 0

        var count = 0
        for (note in mNotes.value!!)
        {
            val date = LocalDate.now()
                            .withYear(note.date.year)
                            .withMonth(note.date.monthValue)
                            .withDayOfMonth(note.date.dayOfMonth)

            val time = LocalTime.now()
                            .withHour(note.date.hour)
                            .withMinute(note.date.minute)

            if ( (date.isAfter(start_date) || date.isEqual(start_date)) &&
                 (date.isBefore(end_date)  || date.isEqual(end_date))   &&
                  time.isAfter(start_time) &&
                  time.isBefore(end_time))
            {
                if (min_pulse > note.pulse)             min_pulse = note.pulse
                if (min_sp    > note.systolicPressure)  min_sp = note.systolicPressure
                if (min_dp    > note.diastolicPressure) min_dp = note.diastolicPressure

                if (max_pulse < note.pulse)             max_pulse = note.pulse
                if (max_sp    < note.systolicPressure)  max_sp = note.systolicPressure
                if (max_dp    < note.diastolicPressure) max_dp = note.diastolicPressure

                avg_pulse += note.pulse
                avg_sp    += note.systolicPressure
                avg_dp    += note.diastolicPressure
                count++
            }
        }
        if (count != 0)
        {
            avg_pulse /= count
            avg_sp    /= count
            avg_dp    /= count
        }

        if (min_dp == 1000 || min_sp == 1000 || min_pulse == 1000)
        {
            min_dp    = 0
            min_sp    = 0
            min_pulse = 0
        }
    }

}