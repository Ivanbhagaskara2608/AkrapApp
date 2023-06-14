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
import kotlinx.android.synthetic.main.activity_add_information.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddInformationActivity : AppCompatActivity() {

    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_information)

        prefManager = PrefManager(this)

        backAddInfoImageButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("fragmentId", "information")
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        val categoryList = listOf("Acara", "Berita", "Pembaruan", "Pengumuman")
        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, categoryList)

        categoryAutoComplete.setAdapter(categoryAdapter)

        addInformationButton.setOnClickListener {
            if (titleInfoEditText.text.isNullOrEmpty()) {
                titleInfoEditText.error = "Harap isi Judul"
                titleInfoEditText.requestFocus()
            } else if (categoryAutoComplete.text.toString() == "Pilih Kategori") {
                categoryAutoComplete.error = "Harap pilih Kategori"
                categoryAutoComplete.requestFocus()
                clearError(categoryAutoComplete)
            } else if (descInfoEditText.text.isNullOrEmpty()) {
                descInfoEditText.error = "Harap isi Deskripsi"
                descInfoEditText.requestFocus()
            } else {
                addInformation()
            }
        }
    }

    private fun addInformation() {
        val token = "Bearer ${prefManager.getToken()}"
        val title = titleInfoEditText.text.toString()
        val content = descInfoEditText.text.toString()
        val category = categoryAutoComplete.text.toString()
        val jobj = JsonObject()

        jobj.addProperty("title", title)
        jobj.addProperty("content", content)
        jobj.addProperty("category", category)

        RetrofitClient.instance.addInformation(token, jobj).enqueue(object : Callback<GetInformationData> {
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
                    Toast.makeText(this@AddInformationActivity, response.body()!!.message, Toast.LENGTH_SHORT).show()

                    val intent = Intent(this@AddInformationActivity, InformationDetailActivity::class.java)
                    intent.putExtra("fragmentId", "information")
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