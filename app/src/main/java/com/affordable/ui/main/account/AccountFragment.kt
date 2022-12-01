package com.affordable.ui.main.account

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.affordable.base.BaseFragment
import com.affordable.databinding.FragmentAccountBinding
import java.util.*


class AccountFragment : BaseFragment<FragmentAccountBinding>() {

    private val TAG = AccountFragment::class.java.name

    lateinit var activity: AppCompatActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun initBindingRef(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentAccountBinding = FragmentAccountBinding.inflate(inflater, container, false)

    override fun initRoot(): View? = binding?.root

    override fun init() {

        activity = requireActivity() as AppCompatActivity


    }


    override fun initListener() {

        with(binding!!) {

            fetchAndSetdata()

            saveUpdate.setOnClickListener {
                if (checkAndValidate(this)) {
                    updateUserProfile(
                        dob.text.toString().trim(),
                        firstname.text.toString().trim(),
                        lastname.text.toString().trim()
                    ) {
                        showToastShort(it)
                        fetchAndSetdata()
                    }
                }
            }

            dob.setOnClickListener {
                showDatePickerDialog()
            }

        }
    }

    private fun FragmentAccountBinding.fetchAndSetdata() {
        getUserProfile { userModel ->
            firstname.setText(userModel.firstName)
            lastname.setText(userModel.lastName)
            dob.setText(userModel.dob)
            username.setText(userModel.username)
            if (!userModel.userEmail.isEmpty() && !userModel.userEmail.equals("null")) {
                email.setText(userModel.userEmail)
            } else {
                emailOrPhone.text = "Phone"
                email.setText(auth.currentUser?.phoneNumber.toString())
            }
        }
    }

    private fun showDatePickerDialog() {
        val c: Calendar = Calendar.getInstance()
        val mYear = c.get(Calendar.YEAR)
        val mMonth = c.get(Calendar.MONTH)
        val mDay = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            activity,
            OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                binding?.dob?.setText(dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year)
            },
            mYear,
            mMonth,
            mDay
        )
        datePickerDialog.datePicker.maxDate = Date().time
        datePickerDialog.show()
    }

    private fun checkAndValidate(binding: FragmentAccountBinding): Boolean {
        if (binding.firstname.text.toString().trim().isEmpty()) {
            showToastShort("Firstname is missing")
            return false
        } else if (binding.lastname.text.toString().trim().isEmpty()) {
            showToastShort("Lastname is Empty")
            return false
        } else if (binding.dob.text.toString().trim().isEmpty()) {
            showToastShort("Date of birth is Empty")
            return false
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}