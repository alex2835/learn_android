package com.example.finalwork

import android.media.audiofx.AudioEffect
import java.time.LocalDateTime
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.internal.*
import java.util.*

data class Note(var date: LocalDateTime,
                var systolicPressure: Int,
                var diastolicPressure : Int,
                var pulse : Int)

@Serializable
data class NoteSerializable(var date: String,
                            var systolicPressure: Int,
                            var diastolicPressure : Int,
                            var pulse : Int)

fun ToNote(ser_note: NoteSerializable) : Note
{
    var note: Note = Note(LocalDateTime.now(), 0, 0, 0)
    note.date = LocalDateTime.parse(ser_note.date)
    note.systolicPressure = ser_note.systolicPressure
    note.diastolicPressure = ser_note.diastolicPressure
    note.pulse = ser_note.pulse
    return note
}


fun ToNoteSerializable(note: Note) : NoteSerializable
{
    var ser_note: NoteSerializable
            = NoteSerializable("", 0, 0, 0)
    ser_note.date = note.date.toString()
    ser_note.systolicPressure = note.systolicPressure
    ser_note.diastolicPressure = note.diastolicPressure
    ser_note.pulse = note.pulse
    return ser_note
}
