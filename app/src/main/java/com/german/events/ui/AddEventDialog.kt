package com.german.events.ui

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
import com.german.events.databinding.DialogAddEventBinding
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
class AddEventDialog : DialogFragment() {

    private lateinit var binding : DialogAddEventBinding

    var firebaseUser: FirebaseUser? = null
        @Inject set

    private var hourSelected : Long? = null
    private var dateSelected : Long? = null

    var onCreateEventListener : OnCreateEventListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = DialogAddEventBinding.inflate(layoutInflater)

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

        binding.create.setOnClickListener {

            val name = binding.name.text.toString()
            val address = binding.address.text.toString()

            if(name.isEmpty() || address.isEmpty() || hourSelected == null || dateSelected == null){
                Toast.makeText(requireActivity(), resources.getString(R.string.fill_form), Toast.LENGTH_LONG).show()
            }
            else{
                val event = Event(
                    createdBy = firebaseUser?.uid,
                    name = name,
                    address = address,
                    timestamp = Timestamp(Date(hourSelected!! + dateSelected!!))
                )
                onCreateEventListener?.onCreateEvent(event)
                dismiss()
            }

        }

        disableInput(binding.date)
        disableInput(binding.hour)

        val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
        builder.context.theme.applyStyle(R.style.CustomAlertDialog, true)
        builder.setView(binding.root)
        return builder.create()
    }

    private fun calendarConstraints(): CalendarConstraints {
        val dateValidatorMin: CalendarConstraints.DateValidator = DateValidatorPointForward.from(MaterialDatePicker.todayInUtcMilliseconds())
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

    interface OnCreateEventListener{
        fun onCreateEvent(event: Event)
    }

}