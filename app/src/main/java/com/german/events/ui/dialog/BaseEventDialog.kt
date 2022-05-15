package com.german.events.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.text.format.DateFormat
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.german.events.R
import com.german.events.databinding.EventFormBinding
import com.german.events.model.Event
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@AndroidEntryPoint
abstract class BaseEventDialog : DialogFragment() {

    lateinit var binding : EventFormBinding

    var firebaseUser: FirebaseUser? = null
        @Inject set

    var hourSelected : Long? = null
    var dateSelected : Long? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = EventFormBinding.inflate(layoutInflater)

        binding.title.text = getTitle()
        binding.actionEvent.text = getActionText()

        binding.icEditDate.setOnClickListener {

            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText(resources.getString(R.string.date))
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .setCalendarConstraints(calendarConstraints())
                    .build()

            datePicker.addOnPositiveButtonClickListener {
                dateSelected = it
                binding.date.setText(DateFormat.format("MM/dd/yyyy", Date(dateSelected!!)).toString())
            }

            datePicker.show(childFragmentManager, "DatePicker")
        }

        binding.icEditTime.setOnClickListener {
            val materialTimePicker: MaterialTimePicker = MaterialTimePicker.Builder()
                .setTitleText(resources.getString(R.string.hour))
                .setHour(12)
                .setMinute(10)
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .build()

            materialTimePicker.addOnPositiveButtonClickListener {
                hourSelected = TimeUnit.HOURS.toMillis(materialTimePicker.hour.toLong()) + TimeUnit.MINUTES.toMillis(materialTimePicker.minute.toLong())
                binding.hour.setText(DateFormat.format("hh:mm aa", Date(hourSelected!!)).toString())
            }
            materialTimePicker.show(childFragmentManager, "TimePicker")
        }

        binding.actionEvent.setOnClickListener {

            val name = binding.name.text.toString()
            val address = binding.address.text.toString()
            val date = binding.date.text.toString()
            val hour = binding.hour.text.toString()

            if(name.isEmpty() || address.isEmpty() || date.isEmpty() || hour.isEmpty()){
                Toast.makeText(requireActivity(), resources.getString(R.string.fill_form), Toast.LENGTH_LONG).show()
            }
            else{
                val event = Event(
                    createdBy = firebaseUser?.uid,
                    name = name,
                    address = address,
                    timestamp = Timestamp(Date((if(hourSelected!! != dateSelected!!) hourSelected!! + dateSelected!! else dateSelected)!!))
                )
                handleEvent(event)
                dismiss()
            }

        }

        disableInput(binding.date)
        disableInput(binding.hour)

        val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
        builder.context.theme.applyStyle(R.style.CustomAlertDialog, true)
        onViewPrepared(binding)
        builder.setView(binding.root)
        return builder.create()
    }

    private fun calendarConstraints(): CalendarConstraints {
        val dateValidatorMin: CalendarConstraints.DateValidator = DateValidatorPointForward.from(
            MaterialDatePicker.todayInUtcMilliseconds())
        return CalendarConstraints.Builder()
            .setValidator(dateValidatorMin)
            .build()
    }

    private fun disableInput(editText: EditText){

        editText.keyListener = null
        editText.showSoftInputOnFocus = false
        editText.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus) {
                val imm = editText.context.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(editText.windowToken, 0)
            }
        }

    }

    abstract fun getTitle(): String
    abstract fun getActionText(): String
    abstract fun handleEvent(event: Event)
    abstract fun onViewPrepared(binding: EventFormBinding)

}