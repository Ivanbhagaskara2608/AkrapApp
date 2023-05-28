package com.example.akrapapp.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.akrapapp.R
import com.example.akrapapp.api.RetrofitClient
import com.example.akrapapp.model.GetInformationData
import com.example.akrapapp.shared_preferences.PrefManager
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_edit_information.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditInformationActivity : AppCompatActivity() {

    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_information)

        prefManager = PrefManager(this)
        val title = prefManager.getInformationData().title
        val category = prefManager.getInformationData().category
        val content = prefManager.getInformationData().content

        titleEditInfoEditText.setText(title)
        categoryEditAutoComplete.setText(category)
        descEditInfoEditText.setText(content)

        backEditInfoImageButton.setOnClickListener {
            val intent = Intent(this, InformationDetailActivity::class.java)
            startActivity(intent)
            finish()
        }

        val categoryList = listOf("Acara", "Berita", "Pembaruan", "Pengumuman")
        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categoryList)

        categoryEditAutoComplete.setAdapter(categoryAdapter)

        editInformationButton.setOnClickListener {
            if (titleEditInfoEditText.text.isNullOrEmpty()) {
                titleEditInfoEditText.error = "Harap isi Judul"
                titleEditInfoEditText.requestFocus()
            } else if (categoryEditAutoComplete.text.toString() == "Pilih Kategori") {
                categoryEditAutoComplete.error = "Harap pilih Kategori"
                categoryEditAutoComplete.requestFocus()
                clearError(categoryEditAutoComplete)
            } else if (descEditInfoEditText.text.isNullOrEmpty()) {
                descEditInfoEditText.error = "Harap isi Deskripsi"
                descEditInfoEditText.requestFocus()
            } else {
                editInformation()
            }
        }
    }

    private fun editInformation() {
        val token = "Bearer ${prefManager.getToken()}"
        val id = prefManager.getInformationData().informationId
        val title = titleEditInfoEditText.text.toString()
        val content = descEditInfoEditText.text.toString()
        val category = categoryEditAutoComplete.text.toString()
        val jobj = JsonObject()

        jobj.addProperty("informationId", id)
        jobj.addProperty("title", title)
        jobj.addProperty("content", content)
        jobj.addProperty("category", category)

        RetrofitClient.instance.updateInformation(token, jobj).enqueue(object : Callback<GetInformationData> {
            override fun onResponse(
                call: Call<GetInformationData>,
                response: Response<GetInformationData>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()!!.data
                    val id = data.informationId
                    val title = data.title
                    val content = data.content
                    val category = data.category
                    val date = data.created_at
                    val updated_at = data.updated_at

                    prefManager.setInformationData(id, title, content, category, date, updated_at)
                    Toast.makeText(this@EditInformationActivity, response.body()!!.message, Toast.LENGTH_SHORT).show()

                    val intent = Intent(this@EditInformationActivity, InformationDetailActivity::class.java)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<GetInformationData>, t: Throwable) {
                Log.e("API Error", t.message.toString())
            }

        })
    }

    private fun clearError(autoCompleteTextView: AutoCompleteTextView) {
        autoCompleteTextView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                        do nothing
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                        do nothing
            }

            override fun afterTextChanged(p0: Editable?) {
                autoCompleteTextView.error = null
            }

        })
    }
}