package com.example.akrapapp.fragment

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.akrapapp.R
import com.example.akrapapp.activity.AddInformationActivity
import com.example.akrapapp.adapter.InformationAdapter
import com.example.akrapapp.api.RetrofitClient
import com.example.akrapapp.model.DataItemInformation
import com.example.akrapapp.model.GetAllInformationResponse
import com.example.akrapapp.shared_preferences.PrefManager
import kotlinx.android.synthetic.main.fragment_information.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InformationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_information, container, false)
    }

    private lateinit var prefManager: PrefManager
    private var informationList = ArrayList<DataItemInformation>()
    private var call: Call<GetAllInformationResponse>? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefManager = PrefManager(requireActivity())
        val role = prefManager.getUserData().role

        informationRefreshLayout.setOnRefreshListener {
            informationList.clear()
            showAllInformation()
            informationRefreshLayout.isRefreshing = false
        }

        if (role == "member") {
            addInfoFloatingButton.visibility = View.GONE
        }
//        change icon and background color
        addInfoFloatingButton.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.orange))
        addInfoFloatingButton.setOnClickListener {
            val intent = Intent(requireActivity(), AddInformationActivity::class.java)
            startActivity(intent)
        }

        showAllInformation()
    }

    private fun showAllInformation() {
        val token = "Bearer ${prefManager.getToken()}"
        call = RetrofitClient.instance.informationAll(token)

        call?.enqueue(object : Callback<GetAllInformationResponse> {
            override fun onResponse(
                call: Call<GetAllInformationResponse>,
                response: Response<GetAllInformationResponse>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()!!.data
                    for (i in 0 until data.size){
                        val id = data[i].informationId
                        val title = data[i].title
                        val content = data[i].content
                        val category = data[i].category
                        val date = data[i].created_at
                        val updatedDate = data[i].updated_at

                        val information = DataItemInformation(id, title, content, category, date, updatedDate)
                        informationList.add(information)
                    }
                    shrimmerInformation.stopShimmer()
                    shrimmerInformation.visibility = View.GONE
                    informationRecyclerView.adapter = InformationAdapter(requireContext(), informationList, "information")
                    val layoutManager = LinearLayoutManager(activity)
                    informationRecyclerView.layoutManager = layoutManager
                }
            }

            override fun onFailure(call: Call<GetAllInformationResponse>, t: Throwable) {
//                Toast.makeText(requireContext(), "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show()
                Log.e("API Error", t.message.toString())
            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        call?.cancel()
    }
}