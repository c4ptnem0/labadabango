package com.example.labadabango

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import com.example.labadabango.databinding.FragmentLaundryListBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class LaundryListFragment : Fragment() {

    private var _binding: FragmentLaundryListBinding? = null
    private lateinit var dataList: ArrayList<LaundryData>
    private lateinit var adapter: MyAdapter<LaundryData>
    private lateinit var noResultsTextView: TextView
    private lateinit var scanBtn: ImageView
    private lateinit var searchSV: SearchView

    private lateinit var cvFilterWetWashingBtn: CardView
    private lateinit var cvFilterDryCleaningBtn: CardView
    private lateinit var cvFilterPremiumWashingBtn: CardView
    private lateinit var cvFilterIroningBtn: CardView

    private lateinit var loadingPB: ProgressBar
    private lateinit var loadingText: TextView

    // class-level property to get the current user's id
    private val userID: String? by lazy {
        FirebaseAuth.getInstance().currentUser?.uid
    }

    // class-level property to get the WetWashing reference to the Bookings node
    private val wetWashRef: DatabaseReference by lazy {
        FirebaseDatabase.getInstance().getReference("Bookings").child("Wet Washing")
    }

    // class-level property to get the DryCleaning reference to the Bookings node
    private val dryCleanRef: DatabaseReference by lazy {
        FirebaseDatabase.getInstance().getReference("Bookings").child("Dry Cleaning")
    }

    private val premiumWashRef: DatabaseReference by lazy {
        FirebaseDatabase.getInstance().getReference("Bookings").child("Premium Washing")
    }

    private val ironLaundryRef: DatabaseReference by lazy {
        FirebaseDatabase.getInstance().getReference("Bookings").child("Ironing")
    }

    // logic for arranging the laundry based on its status
    val statusOrder = mapOf(
        "In Progress" to 1,
        "Collected" to 2,
        "Pending" to 3,
    )

    private val binding get() = _binding!!

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val result = data?.getStringExtra(ProductSearchActivity.RESULT)
            searchSV.setQuery(result, false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLaundryListBinding.inflate(inflater, container, false)
        val view = binding.root

        val gridLayoutManager = GridLayoutManager(requireContext(), 1)
        binding.recyclerView.layoutManager = gridLayoutManager

        val mode = "Laundry List"

        dataList = ArrayList()
        adapter = MyAdapter(mode, requireContext(), dataList)
        binding.recyclerView.adapter = adapter

        loadingPB = binding.loadingPB
        loadingText = binding.loadingText

        var isWetWashingSelected = false
        var isDryCleaningSelected = false
        var isPremiumWashingSelected = false
        var isIroningLaundrySelected = false

        scanBtn = binding.scanBtn
        searchSV = binding.searchSV
        noResultsTextView = binding.noResultsTextView

        cvFilterWetWashingBtn = binding.cvFilterWetWashingBtn
        cvFilterDryCleaningBtn = binding.cvFilterDryCleaningBtn
        cvFilterPremiumWashingBtn = binding.cvFilterPremiumWashingBtn
        cvFilterIroningBtn = binding.cvFilterIroningBtn

        binding.scanBtn.setOnClickListener {
            val intent = Intent(activity, ProductSearchActivity::class.java)
            startActivityForResult(intent, 1)
        }

        // Load combined data when the fragment is created
        loadDataForAllTypes()

        cvFilterWetWashingBtn.setOnClickListener {
            // Check if Wet Washing is already selected
            if (isWetWashingSelected) {
                // If selected, reset the background to normal state
                cvFilterWetWashingBtn.setBackgroundResource(R.drawable.cardview_normal_state)
                // Load combined data here (e.g., by fetching both Wet Washing and Dry Cleaning data)
                loadDataForAllTypes()
            } else {
                // If not selected, proceed with the filtering
                // Clear the dataList before adding filtered data
                dataList.clear()

                // Reset the background of cvFilterWetWashingBtn to its normal state
                cvFilterDryCleaningBtn.setBackgroundResource(R.drawable.cardview_normal_state)

                // Set the background color to indicate the button is selected
                cvFilterWetWashingBtn.setBackgroundResource(R.drawable.cardview_bg)

                // Fetch data for the specific user
                wetWashRef.orderByChild("userID").equalTo(userID).addValueEventListener (object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        dataList.clear()

                        for (itemSnapshot in snapshot.children) {
                            val dataClassWetLaundry = itemSnapshot.getValue(DataClassWetLaundry::class.java)

                            if (dataClassWetLaundry != null && dataClassWetLaundry.etStatus != "Completed") {
                                // Add the product's unique id to a product Text View
                                dataClassWetLaundry.laundryReferenceNo = itemSnapshot.key.toString()
                                dataList.add(dataClassWetLaundry)
                            }
                        }

                        // Sort the combined data based on custom sorting order
                        dataList.sortBy { statusOrder[it.etStatus] }

                        // Notify the adapter about changes in the data
                        adapter.filter("")

                        // show no result found
                        if(adapter.itemCount == 0) {
                            noResultsTextView?.visibility = View.VISIBLE
                        } else {
                            noResultsTextView?.visibility = View.GONE
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle database error (if needed)
                    }
                })
            }

            // Toggle the selection state
            isWetWashingSelected = !isWetWashingSelected
        }

        cvFilterDryCleaningBtn.setOnClickListener {

            cvFilterPremiumWashingBtn.setBackgroundResource(R.drawable.cardview_normal_state)
            cvFilterWetWashingBtn.setBackgroundResource(R.drawable.cardview_normal_state)
            // Check if Wet Washing is already selected
            if (isDryCleaningSelected) {
                // If selected, reset the background to normal state
                cvFilterDryCleaningBtn.setBackgroundResource(R.drawable.cardview_normal_state)
                // Load combined data here (e.g., by fetching both Wet Washing and Dry Cleaning data)
                loadDataForAllTypes()
            } else {
                // If not selected, proceed with the filtering
                // Clear the dataList before adding filtered data
                dataList.clear()

                // Reset the background of cvFilterWetWashingBtn to its normal state
                cvFilterWetWashingBtn.setBackgroundResource(R.drawable.cardview_normal_state)

                // Set the background color to indicate the button is selected
                cvFilterDryCleaningBtn.setBackgroundResource(R.drawable.cardview_bg)

                // Fetch data for the specific user
                dryCleanRef.orderByChild("userID").equalTo(userID).addValueEventListener (object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        dataList.clear()

                        for (itemSnapshot in snapshot.children) {
                            val dataClassDryLaundry = itemSnapshot.getValue(DataClassDryLaundry::class.java)

                            if (dataClassDryLaundry != null && dataClassDryLaundry.etStatus != "Completed") {
                                // Add the product's unique id to a product Text View
                                dataClassDryLaundry.laundryReferenceNo = itemSnapshot.key.toString()
                                dataList.add(dataClassDryLaundry)
                            }
                        }
                        // Sort the combined data based on custom sorting order
                        dataList.sortBy { statusOrder[it.etStatus] }

                        // Notify the adapter about changes in the data
                        adapter.filter("")

                        // show no result found
                        if(adapter.itemCount == 0) {
                            noResultsTextView?.visibility = View.VISIBLE
                        } else {
                            noResultsTextView?.visibility = View.GONE
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle database error (if needed)
                    }
                })
            }

            // Toggle the selection state
            isDryCleaningSelected = !isDryCleaningSelected
        }

        cvFilterPremiumWashingBtn.setOnClickListener {

            cvFilterDryCleaningBtn.setBackgroundResource(R.drawable.cardview_normal_state)
            cvFilterWetWashingBtn.setBackgroundResource(R.drawable.cardview_normal_state)
            // Check if Wet Washing is already selected
            if (isPremiumWashingSelected) {
                // If selected, reset the background to normal state
                cvFilterPremiumWashingBtn.setBackgroundResource(R.drawable.cardview_normal_state)
                // Load combined data here (e.g., by fetching both Wet Washing and Dry Cleaning data)
                loadDataForAllTypes()
            } else {
                // If not selected, proceed with the filtering
                // Clear the dataList before adding filtered data
                dataList.clear()

                // Reset the background of cvFilterWetWashingBtn to its normal state
                cvFilterPremiumWashingBtn.setBackgroundResource(R.drawable.cardview_normal_state)

                // Set the background color to indicate the button is selected
                cvFilterPremiumWashingBtn.setBackgroundResource(R.drawable.cardview_bg)

                // Fetch data for the specific user
                premiumWashRef.orderByChild("userID").equalTo(userID).addValueEventListener (object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        dataList.clear()

                        for (itemSnapshot in snapshot.children) {
                            val dataClassPremiumLaundry = itemSnapshot.getValue(DataClassPremiumLaundry::class.java)

                            if (dataClassPremiumLaundry != null && dataClassPremiumLaundry.etStatus != "Completed") {
                                // Add the product's unique id to a product Text View
                                dataClassPremiumLaundry.laundryReferenceNo = itemSnapshot.key.toString()
                                dataList.add(dataClassPremiumLaundry)
                            }
                        }
                        // Sort the combined data based on custom sorting order
                        dataList.sortBy { statusOrder[it.etStatus] }

                        // Notify the adapter about changes in the data
                        adapter.filter("")

                        // show no result found
                        if(adapter.itemCount == 0) {
                            noResultsTextView?.visibility = View.VISIBLE
                        } else {
                            noResultsTextView?.visibility = View.GONE
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle database error (if needed)
                    }
                })
            }

            // Toggle the selection state
            isPremiumWashingSelected = !isPremiumWashingSelected
        }

        cvFilterIroningBtn.setOnClickListener {

            cvFilterPremiumWashingBtn.setBackgroundResource(R.drawable.cardview_normal_state)
            cvFilterWetWashingBtn.setBackgroundResource(R.drawable.cardview_normal_state)
            // Check if Wet Washing is already selected
            if (isIroningLaundrySelected) {
                // If selected, reset the background to normal state
                cvFilterIroningBtn.setBackgroundResource(R.drawable.cardview_normal_state)
                // Load combined data here (e.g., by fetching both Wet Washing and Dry Cleaning data)
                loadDataForAllTypes()
            } else {
                // If not selected, proceed with the filtering
                // Clear the dataList before adding filtered data
                dataList.clear()

                // Reset the background of cvFilterWetWashingBtn to its normal state
                cvFilterIroningBtn.setBackgroundResource(R.drawable.cardview_normal_state)

                // Set the background color to indicate the button is selected
                cvFilterIroningBtn.setBackgroundResource(R.drawable.cardview_bg)

                // Fetch data for the specific user
                ironLaundryRef.orderByChild("userID").equalTo(userID).addValueEventListener (object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        dataList.clear()

                        for (itemSnapshot in snapshot.children) {
                            val dataClassIronLaundry = itemSnapshot.getValue(DataClassIronLaundry::class.java)

                            if (dataClassIronLaundry != null && dataClassIronLaundry.etStatus != "Completed") {
                                // Add the product's unique id to a product Text View
                                dataClassIronLaundry.laundryReferenceNo = itemSnapshot.key.toString()
                                dataList.add(dataClassIronLaundry)
                            }
                        }
                        // Sort the combined data based on custom sorting order
                        dataList.sortBy { statusOrder[it.etStatus] }

                        // Notify the adapter about changes in the data
                        adapter.filter("")

                        // show no result found
                        if(adapter.itemCount == 0) {
                            noResultsTextView?.visibility = View.VISIBLE
                        } else {
                            noResultsTextView?.visibility = View.GONE
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle database error (if needed)
                    }
                })
            }

            // Toggle the selection state
            isIroningLaundrySelected = !isIroningLaundrySelected
        }

        binding.searchSV.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter(newText ?: "")
                checkForSearchResults(newText ?: "")
                return true
            }
        })

        return view
    }
    private fun checkForSearchResults(searchQuery: String) {
        if (searchQuery.isEmpty())
        {
            noResultsTextView?.visibility = View.GONE
        }
        else if (adapter.itemCount == 0)
        {
            noResultsTextView?.visibility = View.VISIBLE
        } else
        {
            noResultsTextView?.visibility = View.GONE
        }
    }

    // load all data types
    private fun loadDataForAllTypes() {
        // Lists to store data for each type
        val wetWashingList = mutableListOf<LaundryData>()
        val dryCleaningList = mutableListOf<LaundryData>()
        val premiumWashingList = mutableListOf<LaundryData>()
        val ironLaundryList = mutableListOf<LaundryData>()
        // Combine data for all types
        val allLaundryList = mutableListOf<LaundryData>()

        showProgressBar(true)

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                wetWashingList.clear()
                dryCleaningList.clear()
                premiumWashingList.clear()
                ironLaundryList.clear()
                allLaundryList.clear()

                for (itemSnapshot in snapshot.children) {
                    val dataClassWetLaundry = itemSnapshot.getValue(DataClassWetLaundry::class.java)
                    val dataClassDryLaundry = itemSnapshot.getValue(DataClassDryLaundry::class.java)
                    val dataClassPremiumLaundry = itemSnapshot.getValue(DataClassPremiumLaundry::class.java)
                    val dataClassIronLaundry = itemSnapshot.getValue(DataClassIronLaundry::class.java)

                    if (dataClassWetLaundry != null && dataClassWetLaundry.etStatus != "Completed") {
                        dataClassWetLaundry.laundryReferenceNo = itemSnapshot.key.toString()
                        // Remove existing data if present
                        dataList.removeAll { it.laundryReferenceNo == dataClassWetLaundry.laundryReferenceNo }
                        wetWashingList.add(dataClassWetLaundry)
                    } else if (dataClassDryLaundry != null && dataClassDryLaundry.etStatus != "Completed") {
                        dataClassDryLaundry.laundryReferenceNo = itemSnapshot.key.toString()
                        // Remove existing data if present
                        dataList.removeAll { it.laundryReferenceNo == dataClassDryLaundry.laundryReferenceNo }
                        dryCleaningList.add(dataClassDryLaundry)
                    } else if (dataClassPremiumLaundry != null && dataClassPremiumLaundry.etStatus != "Completed") {
                        dataClassPremiumLaundry.laundryReferenceNo = itemSnapshot.key.toString()
                        // Remove existing data if present
                        dataList.removeAll { it.laundryReferenceNo == dataClassPremiumLaundry.laundryReferenceNo }
                        premiumWashingList.add(dataClassPremiumLaundry)
                    } else if (dataClassIronLaundry != null && dataClassIronLaundry.etStatus != "Completed") {
                        dataClassIronLaundry.laundryReferenceNo = itemSnapshot.key.toString()
                        // Remove existing data if present
                        dataList.removeAll { it.laundryReferenceNo == dataClassIronLaundry.laundryReferenceNo }
                        ironLaundryList.add(dataClassIronLaundry)
                    }
                }

                allLaundryList.addAll(wetWashingList)
                allLaundryList.addAll(dryCleaningList)
                allLaundryList.addAll(premiumWashingList)
                allLaundryList.addAll(ironLaundryList)

                // Sort the combined data based on custom sorting order
                allLaundryList.sortBy { statusOrder[it.etStatus] }

                // Update the adapter with the combined data
                dataList.addAll(allLaundryList)
                adapter.notifyDataSetChanged()
                adapter.filter("")

                // Show no result found
                if (adapter.itemCount == 0) {
                    noResultsTextView?.visibility = View.VISIBLE
                } else {
                    noResultsTextView?.visibility = View.GONE
                }

                showProgressBar(false)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error (if needed)
                Log.e("FirebaseError", "Error fetching data: $error")
                showProgressBar(false)
            }
        }

        // Fetch data for the specific user for each type
        wetWashRef.orderByChild("userID").equalTo(userID).addValueEventListener(valueEventListener)
        dryCleanRef.orderByChild("userID").equalTo(userID).addValueEventListener(valueEventListener)
        premiumWashRef.orderByChild("userID").equalTo(userID).addValueEventListener(valueEventListener)
        ironLaundryRef.orderByChild("userID").equalTo(userID).addValueEventListener(valueEventListener)
    }

    private fun showProgressBar(show: Boolean) {
        // Show or hide the progress bar based on the 'show' parameter
        loadingPB.visibility = if (show) View.VISIBLE else View.GONE
        loadingText.visibility = if (show) View.VISIBLE else View.GONE
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val searchQuery = binding.searchSV.query.toString()
        checkForSearchResults(searchQuery)
    }
}
