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
    private val mData: StatisticsViewModel by activityViewModels()
    private lateinit var binding: FragmentStatisticsBinding

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
        binding.textViewStartDate.setText(getString(R.string.start_date_tv) + mData.mStartDate.value?.format(format))
        binding.textViewEndDate.setText(getString(R.string.end_date_tv)  + mData.mEndDate.value?.format(format))

        var hours   = if (mData.mStartTime.value!!.hour >= 10) "${mData.mStartTime.value!!.hour}" else "0${mData.mStartTime.value!!.hour}"
        var minutes = if (mData.mStartTime.value!!.minute >= 10) ":${mData.mStartTime.value!!.minute}" else ":0${mData.mStartTime.value!!.minute}"
        binding.editTextStartTime.setText(hours + minutes)

        hours   = if (mData.mEndTime.value!!.hour >= 10) "${mData.mEndTime.value!!.hour}" else "0${mData.mEndTime.value!!.hour}"
        minutes = if (mData.mEndTime.value!!.minute >= 10) ":${mData.mEndTime.value!!.minute}" else ":0${mData.mEndTime.value!!.minute}"
        binding.editTextEndTime.setText(hours + minutes)

        binding.textViewStartDate.setOnClickListener {
            if (mData.mActiveDateFiled == 1)
            {
                mData.mActiveDateFiled = -1
                binding.datePicker.visibility = View.GONE
                return@setOnClickListener
            }
            mData.mActiveDateFiled = 1
            binding.datePicker.visibility = View.VISIBLE
            binding.datePicker.updateDate(mData.mStartDate.value!!.year, mData.mStartDate.value!!.monthValue - 1, mData.mStartDate.value!!.dayOfMonth)
        }

        binding.textViewEndDate.setOnClickListener {
            if (mData.mActiveDateFiled == 2)
            {
                mData.mActiveDateFiled = -1
                binding.datePicker.visibility = View.GONE
                return@setOnClickListener
            }
            mData.mActiveDateFiled = 2
            binding.datePicker.visibility = View.VISIBLE
            binding.datePicker.updateDate(mData.mEndDate.value!!.year, mData.mEndDate.value!!.monthValue - 1, mData.mEndDate.value!!.dayOfMonth)
        }

        binding.datePicker.setOnDateChangedListener { view, year, monthOfYear, dayOfMonth ->
            if (mData.mActiveDateFiled == 1)
            {
                ExtractDate()
                binding.textViewStartDate.setText(getString(R.string.start_date_tv) + mData.mStartDate.value?.format(format))
            }
            else if (mData.mActiveDateFiled == 2)
            {
                ExtractDate()
                binding.textViewEndDate.setText(getString(R.string.end_date_tv)    + mData.mEndDate.value?.format(format))
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
                mNotesViewModel.CalculateStatistic(mData.mStartDate.value!!, mData.mEndDate.value!!, mData.mStartTime.value!!, mData.mEndTime.value!!)

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

        if (mData.mActiveDateFiled == 1)
        {
            mData.mStartTime.value = LocalTime.parse(binding.editTextStartTime.text)
            mData.mStartDate.value = LocalDate.now()
                                        .withYear(year)
                                        .withMonth(month + 1)
                                        .withDayOfMonth(day)
        }
        else if (mData.mActiveDateFiled == 2)
        {
            mData.mEndTime.value = LocalTime.parse(binding.editTextEndTime.text)
            mData.mEndDate.value = LocalDate.now()
                                        .withYear(year)
                                        .withMonth(month + 1)
                                        .withDayOfMonth(day)
        }
        else {
            assert(false)
        }
    }

    fun ExtractTime()
    {
        mData.mStartTime.value = LocalTime.parse(binding.editTextStartTime.text)
        mData.mEndTime.value   = LocalTime.parse(binding.editTextEndTime.text)
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