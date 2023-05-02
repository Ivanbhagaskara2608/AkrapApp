package com.example.akrapapp.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.akrapapp.R
import kotlinx.android.synthetic.main.activity_register1.*
import java.util.*

class Register1Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register1)

        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)
        val genderList = listOf("Laki - laki", "Perempuan")
        val genderAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, genderList)
        var getGender: String? = null

        genderAutoComplete.setAdapter(genderAdapter)
        genderAutoComplete.setOnItemClickListener { adapterView, view, i, l ->
            genderInputLayout.isHintEnabled = false
            when (i) {
                0 -> getGender = "male"
                1 -> getGender = "female"
            }
        }

        birthdateEditText.setOnClickListener {
            birthdateInputLayout.isHintEnabled = false
            val dpd = DatePickerDialog(this, android.R.style.Theme_Material_Dialog,
                DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                val mMonth1 = mMonth + 1
                birthdateEditText.setText("" + mYear + "/" + mMonth1 + "/" + mDay)
            }, year, month, day)
            dpd.show()
        }

        register1Button.setOnClickListener {
            if (fullnameEditText.text!!.isEmpty()) {
                fullnameEditText.error = "Harap isi Nama Lengkap"
                fullnameEditText.requestFocus()
            } else if (phoneNumberEditText.text!!.isEmpty()) {
                phoneNumberEditText.error = "Harap isi Nomor Telepon"
                phoneNumberEditText.requestFocus()
            } else if (birthdateEditText.text.isNullOrEmpty()) {
                birthdateEditText.error = "Harap isi Tanggal Lahir"
                birthdateEditText.requestFocus()
            } else if (genderAutoComplete.text.isEmpty()) {
                genderAutoComplete.error = "Harap isi Jenis Kelamin"
                genderAutoComplete.requestFocus()
            } else {
                val intent = Intent(this, Register2Activity::class.java)
                val bundle = Bundle()
                bundle.putString("fullName", fullnameEditText.text.toString())
                bundle.putString("birthdate", birthdateEditText.text.toString())
                bundle.putString("phoneNumber", phoneNumberEditText.text.toString())
                bundle.putString("gender", getGender.toString())

                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
    }
}