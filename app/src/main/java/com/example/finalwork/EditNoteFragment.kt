package com.example.finalwork

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import java.lang.Integer.parseInt
import java.time.DateTimeException
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/**
 *  @param item_id Id of the item in array
 *  @note  item_id = -1 means create new elem
 * */
//class EditNoteFragment(var main_adapter: MainAdapter, val item_id: Int) : DialogFragment()
class EditNoteFragment() : DialogFragment()
{
    private val mNotesViewModel: NotesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View?
    {
        var root: View = inflater.inflate(R.layout.dialog_edit_note, container, false)

        SetFields(root)
        SetOnButtonClickListener(root)
        return root
    }

    fun SetFields(root: View)
    {
        if (mNotesViewModel.mPosition == -1)
        {
            val cur_date = LocalDateTime.now()
            val date_format = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG, FormatStyle.SHORT)
            root.findViewById<TextView>(R.id.DateView).setText(cur_date.format(date_format).toString())

            val hours   = if (cur_date.hour > 10) "${cur_date.hour}" else "0${cur_date.hour}"
            val minutes = if (cur_date.minute > 10) ":${cur_date.minute}" else ":0${cur_date.minute}"
            root.findViewById<EditText>(R.id.time).setText(hours + minutes)

            root.findViewById<DatePicker>(R.id.datePicker).updateDate(
                cur_date.year, cur_date.monthValue - 1, cur_date.dayOfMonth)

            root.findViewById<TextView>(R.id.DateView).setOnClickListener {
                root.findViewById<TextView>(R.id.DateView).visibility     = View.GONE
                root.findViewById<DatePicker>(R.id.datePicker).visibility = View.VISIBLE
                root.findViewById<EditText>(R.id.time).visibility         = View.VISIBLE

                val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view?.windowToken, 0)

                root.findViewById<Button>(R.id.add_note_button).text = getString(R.string.add_button)
            }
        }
        else if (mNotesViewModel.mPosition >= 0 && mNotesViewModel.mPosition < mNotesViewModel.Size())
        {
            var note = mNotesViewModel.GetNote(mNotesViewModel.mPosition)
            root.findViewById<EditText>(R.id.sp_et).setText(note.systolicPressure.toString())
            root.findViewById<EditText>(R.id.dp_et).setText(note.diastolicPressure.toString())
            root.findViewById<EditText>(R.id.pulse_et).setText(note.pulse.toString())

            root.findViewById<TextView>(R.id.DateView).setText(
                note.date.format(DateTimeFormatter.ofLocalizedDateTime(
                    FormatStyle.LONG, FormatStyle.SHORT)).toString())

            val hours   = if (note.date.hour >= 10) "${note.date.hour}" else "0${note.date.hour}"
            val minutes = if (note.date.minute >= 10) ":${note.date.minute}" else ":0${note.date.minute}"
            root.findViewById<EditText>(R.id.time).setText(hours + minutes)

            root.findViewById<DatePicker>(R.id.datePicker).updateDate(
                note.date.year, note.date.monthValue - 1, note.date.dayOfMonth)

            root.findViewById<TextView>(R.id.DateView).setOnClickListener {
                root.findViewById<TextView>(R.id.DateView).visibility = View.GONE
                root.findViewById<DatePicker>(R.id.datePicker).visibility = View.VISIBLE
                root.findViewById<EditText>(R.id.time).visibility = View.VISIBLE
            }
            root.findViewById<Button>(R.id.add_note_button).text = getString(R.string.change_button)
        }
        else {
            assert(false)
        }
    }

    fun SetOnButtonClickListener(root: View)
    {
        root.findViewById<Button>(R.id.add_note_button).setOnClickListener {
            if (mNotesViewModel.mPosition == -1)
            {
                try {
                    var note = ExtractNote(root)
                    if (note != null)
                    {
                        mNotesViewModel.Add(note)
                        mNotesViewModel.mAdapter.notifyDataSetChanged()
                        dismiss()
                    }
                }
                catch (e: DateTimeException)
                {
                    Toast.makeText(context, getString(R.string.invalid_time_toast), Toast.LENGTH_SHORT).show()
                }
                catch (e: Exception)
                {
                    Toast.makeText(context, getString(R.string.empty_fields_toast), Toast.LENGTH_SHORT).show()
                }
            }
            else if (mNotesViewModel.mPosition >= 0 && mNotesViewModel.mPosition < mNotesViewModel.Size())
            {
                try {
                    var input_note = ExtractNote(root)
                    if (input_note != null)
                    {
                        var note = mNotesViewModel.GetNote(mNotesViewModel.mPosition)
                        note.systolicPressure = input_note.systolicPressure
                        note.diastolicPressure = input_note.diastolicPressure
                        note.pulse = input_note.pulse
                        note.date = input_note.date
                        mNotesViewModel.mAdapter.notifyDataSetChanged()
                        dismiss()
                    }
                }
                catch (e: DateTimeException)
                {
                    Toast.makeText(context, getString(R.string.invalid_time_toast), Toast.LENGTH_SHORT).show()
                }
                catch (e: Exception)
                {
                    Toast.makeText(context, getString(R.string.empty_fields_toast), Toast.LENGTH_SHORT).show()
                }
            }
            else {
                assert(false)
            }
        }
    }

    fun ExtractDate(root: View) : LocalDateTime
    {
        val year  = root.findViewById<DatePicker>(R.id.datePicker).year
        val month = root.findViewById<DatePicker>(R.id.datePicker).month + 1
        val day   = root.findViewById<DatePicker>(R.id.datePicker).dayOfMonth
        val time  = LocalTime.parse(root.findViewById<EditText>(R.id.time).text)

        return LocalDateTime.now()
                    .withHour(time.hour)
                    .withMinute(time.minute)
                    .withYear(year)
                    .withMonth(month)
                    .withDayOfMonth(day)
    }

    fun ExtractNote(root: View) : Note?
    {
        val sp  = parseInt(root.findViewById<EditText>(R.id.sp_et).text.toString())
        val dp  = parseInt(root.findViewById<EditText>(R.id.dp_et).text.toString())
        val pulse = parseInt(root.findViewById<EditText>(R.id.pulse_et).text.toString())
        val date = ExtractDate(root)

        if (sp > 250 || dp > 250 || pulse > 250)
        {
            Toast.makeText(context, getString(R.string.to_big_values_toast), Toast.LENGTH_SHORT).show()
            return null
        }
        if (sp < 50 || dp < 50 || pulse < 50)
        {
            Toast.makeText(context, getString(R.string.to_small_values_toast), Toast.LENGTH_SHORT).show()
            return null
        }
        return Note(date, sp, dp, pulse)
    }

}