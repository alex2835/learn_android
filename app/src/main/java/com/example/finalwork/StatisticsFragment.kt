package com.example.finalwork

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.finalwork.databinding.FragmentStatisticsBinding
import java.time.DateTimeException
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class StatisticsFragment : Fragment()
{
    private val mNotesViewModel: NotesViewModel by activityViewModels()
    private lateinit var binding: FragmentStatisticsBinding
    private var mStartDate = LocalDate.now()
    private var mEndDate   = LocalDate.now()
    private var mStartTime = LocalTime.now()
    private var mEndTime   = LocalTime.now()
    private var mActiveDateFiled = -1


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View?
    {
        binding = DataBindingUtil.inflate<FragmentStatisticsBinding>(
            inflater, R.layout.fragment_statistics, container, false)

        SetOnCreateData()
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun SetOnCreateData()
    {
        // Select range
        val format = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        binding.textViewStartDate.setText("Start date: " + mStartDate.format(format))
        binding.textViewEndDate.setText("End  date: "    + mEndDate.format(format))

        var hours   = if (mStartTime.hour >= 10) "${mStartTime.hour}" else "0${mStartTime.hour}"
        var minutes = if (mStartTime.minute >= 10) ":${mStartTime.minute}" else ":0${mStartTime.minute}"
        binding.editTextStartTime.setText(hours + minutes)

        hours   = if (mEndTime.hour >= 10) "${mEndTime.hour}" else "0${mEndTime.hour}"
        minutes = if (mEndTime.minute >= 10) ":${mEndTime.minute}" else ":0${mEndTime.minute}"
        binding.editTextEndTime.setText(hours + minutes)

        binding.textViewStartDate.setOnClickListener {
            if (mActiveDateFiled == 1)
            {
                mActiveDateFiled = -1
                binding.datePicker.visibility = View.GONE
                return@setOnClickListener
            }
            mActiveDateFiled = 1
            binding.datePicker.visibility = View.VISIBLE
            binding.datePicker.updateDate(mStartDate.year, mStartDate.monthValue - 1, mStartDate.dayOfMonth)
        }

        binding.textViewEndDate.setOnClickListener {
            if (mActiveDateFiled == 2)
            {
                mActiveDateFiled = -1
                binding.datePicker.visibility = View.GONE
                return@setOnClickListener
            }
            mActiveDateFiled = 2
            binding.datePicker.visibility = View.VISIBLE
            binding.datePicker.updateDate(mEndDate.year, mEndDate.monthValue - 1, mEndDate.dayOfMonth)
        }

        binding.datePicker.setOnDateChangedListener { view, year, monthOfYear, dayOfMonth ->
            if (mActiveDateFiled == 1)
            {
                ExtractDate()
                binding.textViewStartDate.setText(getString(R.string.start_date_tv) + mStartDate.format(format))
            }
            else if (mActiveDateFiled == 2)
            {
                ExtractDate()
                binding.textViewEndDate.setText(getString(R.string.end_date_tv)    + mEndDate.format(format))
            }
            else {
                assert(false)
            }
        }

        // Show statistic
        binding.buttonCalculate.setOnClickListener {
            binding.datePicker.visibility = View.GONE

            try {
                ExtractTime()
                mNotesViewModel.CalculateStatistic(mStartDate, mEndDate, mStartTime, mEndTime)

                binding.spDataTv.setText("Min: ${mNotesViewModel.min_sp}     Max: ${mNotesViewModel.max_sp}     Avg: ${mNotesViewModel.avg_sp} ")
                binding.dpDataTv.setText("Min: ${mNotesViewModel.min_dp}     Max: ${mNotesViewModel.max_dp}     Avg: ${mNotesViewModel.avg_dp} ")
                binding.pulseDataTv.setText("Min: ${mNotesViewModel.min_pulse}     Max: ${mNotesViewModel.max_pulse}     Avg: ${mNotesViewModel.avg_pulse}")
            }
            catch (e: DateTimeException)
            {
                Toast.makeText(context, getString(R.string.invalid_time_toast) , Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun ExtractDate()
    {
        val year  = binding.datePicker.year
        val month = binding.datePicker.month
        val day   = binding.datePicker.dayOfMonth

        if (mActiveDateFiled == 1)
        {
            mStartTime = LocalTime.parse(binding.editTextStartTime.text)
            mStartDate = LocalDate.now()
                .withYear(year)
                .withMonth(month + 1)
                .withDayOfMonth(day)
        }
        else if (mActiveDateFiled == 2)
        {
            mEndTime = LocalTime.parse(binding.editTextEndTime.text)
            mEndDate = LocalDate.now()
                .withYear(year)
                .withMonth(month + 1)
                .withDayOfMonth(day)
        }
        else {
            assert(false)
        }
    }

    fun ExtractTime() {
        mStartTime = LocalTime.parse(binding.editTextStartTime.text)
        mEndTime   = LocalTime.parse(binding.editTextEndTime.text)
    }

    override fun onSaveInstanceState(outState: Bundle)
    {
        super.onSaveInstanceState(outState)
        outState?.run {
            putString("TextView_sp",    binding.textViewSp.text.toString())
            putString("TextView_dp",    binding.textViewDp.text.toString())
            putString("TextView_pulse", binding.textViewPulse.text.toString())
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?)
    {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState?.isEmpty == false)
        {
            binding.textViewSp.setText(savedInstanceState!!.getString("TextView_sp"))
            binding.textViewDp.setText(savedInstanceState!!.getString("TextView_dp"))
            binding.textViewPulse.setText(savedInstanceState!!.getString("TextView_pulse"))
        }
    }
}