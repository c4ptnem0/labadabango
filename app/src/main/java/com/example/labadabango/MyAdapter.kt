package com.example.labadabango

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class MyAdapter<T : LaundryData>(private val mode: String, private val context: Context, private val datalist:List<T>): RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    private var filteredData = datalist


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.product_items, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredData.size
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = filteredData[position]

        when (currentItem) {
            is DataClassWetLaundry,
            is DataClassDryLaundry,
            is DataClassPremiumLaundry,
            is DataClassIronLaundry -> {
                bindCommonData(holder, currentItem)
                bindTypeSpecificData(holder, currentItem)
                setClickListener(holder.itemView, currentItem)
                currentItem.etStatus?.let { setLaundryStatusImage(holder, it) }
            }
        }
    }

    private fun bindCommonData(holder: MyViewHolder, currentItem: LaundryData) {
        holder.laundryType.text = currentItem.laundryType
        holder.laundryReferenceNo.text = currentItem.laundryReferenceNo
        holder.laundryStatus.text = currentItem.etStatus
        holder.inputAddressInformation.text = currentItem.inputAddressInformation
        holder.totalPrice.text = currentItem.totalPrice.toString()
        holder.totalItems.text = "${currentItem.totalItems} items"
        holder.laundryDate.text = currentItem.etLaundryDate.toString()
        holder.qrCode.text = currentItem.qrCode.toString()
    }

    private fun bindTypeSpecificData(holder: MyViewHolder, currentItem: LaundryData) {
        when (currentItem) {
            is DataClassWetLaundry -> {
                // Bind WetLaundry specific data
                holder.itemCountShirts.text = currentItem.itemCountShirts.toString()
                holder.itemCountShorts.text = currentItem.itemCountShorts.toString()
                holder.itemCountUnderwears.text = currentItem.itemCountUnderwears.toString()
                holder.itemCountPants.text = currentItem.itemCountPants.toString()
                holder.itemCountJackets.text = currentItem.itemCountJackets.toString()
                holder.itemCountBedCovers.text = currentItem.itemCountBedCovers.toString()
                holder.itemCountCarpets.text = currentItem.itemCountCarpets.toString()

                holder.countShirts.text = currentItem.countShirts.toString()
                holder.countShorts.text = currentItem.countShorts.toString()
                holder.countUnderwears.text = currentItem.countUnderwears.toString()
                holder.countPants.text = currentItem.countPants.toString()
                holder.countJackets.text = currentItem.countJackets.toString()
                holder.countBedCovers.text = currentItem.countBedCovers.toString()
                holder.countCarpets.text = currentItem.countCarpets.toString()
                // Bind other WetLaundry specific data...
            }
            is DataClassDryLaundry -> {
                // Bind DryLaundry specific data
                holder.itemCountShirts.text = currentItem.itemCountShirts.toString()
                holder.itemCountShorts.text = currentItem.itemCountShorts.toString()
                holder.itemCountGowns.text = currentItem.itemCountGowns.toString()
                holder.itemCountCoats.text = currentItem.itemCountCoats.toString()
                holder.itemCountCurtains.text = currentItem.itemCountCurtains.toString()
                holder.itemCountHats.text = currentItem.itemCountHats.toString()
                holder.itemCountShoes.text = currentItem.itemCountShoes.toString()
                holder.itemCountLeatherBags.text = currentItem.itemCountLeatherBags.toString()
                holder.itemCountBedComforters.text = currentItem.itemCountBedComforters.toString()
                holder.itemCountStuffedToys.text = currentItem.itemCountStuffedToys.toString()
                holder.itemCountWoolSweaters.text = currentItem.itemCountWoolSweaters.toString()
                holder.itemCountDenimJackets.text = currentItem.itemCountDenimJackets.toString()
                holder.itemCountDenimPants.text = currentItem.itemCountDenimPants.toString()
                holder.itemCountLeatherJackets.text = currentItem.itemCountLeatherJackets.toString()

                holder.countShirts.text = currentItem.countShirts.toString()
                holder.countShorts.text = currentItem.countShorts.toString()
                holder.countGowns.text = currentItem.countGowns.toString()
                holder.countCoats.text = currentItem.countCoats.toString()
                holder.countCurtains.text = currentItem.countCurtains.toString()
                holder.countHats.text = currentItem.countHats.toString()
                holder.countShoes.text = currentItem.countShoes.toString()
                holder.countLeatherBags.text = currentItem.countLeatherBags.toString()
                holder.countBedComforters.text = currentItem.countBedComforters.toString()
                holder.countStuffedToys.text = currentItem.countStuffedToys.toString()
                holder.countWoolSweaters.text = currentItem.countWoolSweaters.toString()
                holder.countDenimJackets.text = currentItem.countDenimJackets.toString()
                holder.countDenimPants.text = currentItem.countDenimPants.toString()
                holder.countLeatherJackets.text = currentItem.countLeatherJackets.toString()
                // Bind other DryLaundry specific data...
            }
            is DataClassPremiumLaundry -> {
                // Bind WetLaundry specific data
                holder.itemCountShirts.text = currentItem.itemCountShirts.toString()
                holder.itemCountShorts.text = currentItem.itemCountShorts.toString()
                holder.itemCountUnderwears.text = currentItem.itemCountUnderwears.toString()
                holder.itemCountPants.text = currentItem.itemCountPants.toString()
                holder.itemCountJackets.text = currentItem.itemCountJackets.toString()
                holder.itemCountBedCovers.text = currentItem.itemCountBedCovers.toString()
                holder.itemCountCarpets.text = currentItem.itemCountCarpets.toString()

                holder.countShirts.text = currentItem.countShirts.toString()
                holder.countShorts.text = currentItem.countShorts.toString()
                holder.countUnderwears.text = currentItem.countUnderwears.toString()
                holder.countPants.text = currentItem.countPants.toString()
                holder.countJackets.text = currentItem.countJackets.toString()
                holder.countBedCovers.text = currentItem.countBedCovers.toString()
                holder.countCarpets.text = currentItem.countCarpets.toString()
                // Bind other WetLaundry specific data...
            }
            is DataClassIronLaundry -> {
                // Bind DryLaundry specific data
                holder.itemCountShirts.text = currentItem.itemCountShirts.toString()
                holder.itemCountShorts.text = currentItem.itemCountShorts.toString()
                holder.itemCountGowns.text = currentItem.itemCountGowns.toString()
                holder.itemCountCoats.text = currentItem.itemCountCoats.toString()
                holder.itemCountCurtains.text = currentItem.itemCountCurtains.toString()
                holder.itemCountHats.text = currentItem.itemCountHats.toString()
                holder.itemCountShoes.text = currentItem.itemCountShoes.toString()
                holder.itemCountLeatherBags.text = currentItem.itemCountLeatherBags.toString()
                holder.itemCountBedComforters.text = currentItem.itemCountBedComforters.toString()
                holder.itemCountStuffedToys.text = currentItem.itemCountStuffedToys.toString()
                holder.itemCountWoolSweaters.text = currentItem.itemCountWoolSweaters.toString()
                holder.itemCountDenimJackets.text = currentItem.itemCountDenimJackets.toString()
                holder.itemCountDenimPants.text = currentItem.itemCountDenimPants.toString()
                holder.itemCountLeatherJackets.text = currentItem.itemCountLeatherJackets.toString()

                holder.countShirts.text = currentItem.countShirts.toString()
                holder.countShorts.text = currentItem.countShorts.toString()
                holder.countGowns.text = currentItem.countGowns.toString()
                holder.countCoats.text = currentItem.countCoats.toString()
                holder.countCurtains.text = currentItem.countCurtains.toString()
                holder.countHats.text = currentItem.countHats.toString()
                holder.countShoes.text = currentItem.countShoes.toString()
                holder.countLeatherBags.text = currentItem.countLeatherBags.toString()
                holder.countBedComforters.text = currentItem.countBedComforters.toString()
                holder.countStuffedToys.text = currentItem.countStuffedToys.toString()
                holder.countWoolSweaters.text = currentItem.countWoolSweaters.toString()
                holder.countDenimJackets.text = currentItem.countDenimJackets.toString()
                holder.countDenimPants.text = currentItem.countDenimPants.toString()
                holder.countLeatherJackets.text = currentItem.countLeatherJackets.toString()
                // Bind other DryLaundry specific data...
            }
        }
    }

    private fun setClickListener(itemView: View, currentItem: LaundryData) {
        itemView.setOnClickListener {
            val intent = createIntent(currentItem)
            context.startActivity(intent)
        }
    }

    private fun createIntent(currentItem: LaundryData): Intent {
        val intent = Intent(context, LaundryViewActivity::class.java)
        intent.putExtra("mode", mode)
        intent.putExtra("laundryType", currentItem.laundryType)
        intent.putExtra("laundryReferenceNo", currentItem.laundryReferenceNo)
        intent.putExtra("laundryStatus", currentItem.etStatus)
        intent.putExtra("deliveryAddress", currentItem.inputAddressInformation)
        intent.putExtra("totalPrice", currentItem.totalPrice)
        intent.putExtra("totalItems", currentItem.totalItems)
        intent.putExtra("qrCode", currentItem.qrCode)
        intent.putExtra("laundryDate", currentItem.etLaundryDate)

        // Add type-specific data
        when (currentItem) {
            is DataClassWetLaundry -> {
                    intent.putExtra("itemCountShirts", currentItem.itemCountShirts)
                    intent.putExtra("itemCountShorts", currentItem.itemCountShorts)
                    intent.putExtra("itemCountUnderwears", currentItem.itemCountUnderwears)
                    intent.putExtra("itemCountPants", currentItem.itemCountPants)
                    intent.putExtra("itemCountJackets", currentItem.itemCountJackets)
                    intent.putExtra("itemCountBedCovers", currentItem.itemCountBedCovers)
                    intent.putExtra("itemCountCarpets", currentItem.itemCountCarpets)

                    intent.putExtra("countShirts", currentItem.countShirts)
                    intent.putExtra("countShorts", currentItem.countShorts)
                    intent.putExtra("countUnderwears", currentItem.countUnderwears)
                    intent.putExtra("countPants", currentItem.countPants)
                    intent.putExtra("countJackets", currentItem.countJackets)
                    intent.putExtra("countBedCovers", currentItem.countBedCovers)
                    intent.putExtra("countCarpets", currentItem.countJackets)
                // Add other WetLaundry specific data...
            }
            is DataClassDryLaundry -> {
                    intent.putExtra("itemCountShirts", currentItem.itemCountShirts)
                    intent.putExtra("itemCountShorts", currentItem.itemCountShorts)
                    intent.putExtra("itemCountGowns", currentItem.itemCountGowns)
                    intent.putExtra("itemCountCoats", currentItem.itemCountCoats)
                    intent.putExtra("itemCountCurtains", currentItem.itemCountCurtains)
                    intent.putExtra("itemCountHats", currentItem.itemCountHats)
                    intent.putExtra("itemCountShoes", currentItem.itemCountShoes)
                    intent.putExtra("itemCountLeatherBags", currentItem.itemCountLeatherBags)
                    intent.putExtra("itemCountBedComforters", currentItem.itemCountBedComforters)
                    intent.putExtra("itemCountStuffedToys", currentItem.itemCountStuffedToys)
                    intent.putExtra("itemCountWoolSweaters", currentItem.itemCountWoolSweaters)
                    intent.putExtra("itemCountDenimJackets", currentItem.itemCountDenimJackets)
                    intent.putExtra("itemCountDenimPants", currentItem.itemCountDenimPants)
                    intent.putExtra("itemCountLeatherJackets", currentItem.itemCountLeatherJackets)

                    intent.putExtra("countShirts", currentItem.countShirts)
                    intent.putExtra("countShorts", currentItem.countShorts)
                    intent.putExtra("countGowns", currentItem.countGowns)
                    intent.putExtra("countCoats", currentItem.countCoats)
                    intent.putExtra("countCurtains", currentItem.countCurtains)
                    intent.putExtra("countHats", currentItem.countHats)
                    intent.putExtra("countShoes", currentItem.countShoes)
                    intent.putExtra("countLeatherBags", currentItem.countLeatherBags)
                    intent.putExtra("countBedComforters", currentItem.countBedComforters)
                    intent.putExtra("countStuffedToys", currentItem.countStuffedToys)
                    intent.putExtra("countWoolSweaters", currentItem.countWoolSweaters)
                    intent.putExtra("countDenimJackets", currentItem.countDenimJackets)
                    intent.putExtra("countDenimPants", currentItem.countDenimPants)
                    intent.putExtra("countLeatherJackets", currentItem.countLeatherJackets)
                // Add other DryLaundry specific data...
            }
            is DataClassPremiumLaundry -> {
                intent.putExtra("itemCountShirts", currentItem.itemCountShirts)
                intent.putExtra("itemCountShorts", currentItem.itemCountShorts)
                intent.putExtra("itemCountUnderwears", currentItem.itemCountUnderwears)
                intent.putExtra("itemCountPants", currentItem.itemCountPants)
                intent.putExtra("itemCountJackets", currentItem.itemCountJackets)
                intent.putExtra("itemCountBedCovers", currentItem.itemCountBedCovers)
                intent.putExtra("itemCountCarpets", currentItem.itemCountCarpets)

                intent.putExtra("countShirts", currentItem.countShirts)
                intent.putExtra("countShorts", currentItem.countShorts)
                intent.putExtra("countUnderwears", currentItem.countUnderwears)
                intent.putExtra("countPants", currentItem.countPants)
                intent.putExtra("countJackets", currentItem.countJackets)
                intent.putExtra("countBedCovers", currentItem.countBedCovers)
                intent.putExtra("countCarpets", currentItem.countJackets)
                // Add other WetLaundry specific data...
            }
            is DataClassIronLaundry -> {
                intent.putExtra("itemCountShirts", currentItem.itemCountShirts)
                intent.putExtra("itemCountShorts", currentItem.itemCountShorts)
                intent.putExtra("itemCountGowns", currentItem.itemCountGowns)
                intent.putExtra("itemCountCoats", currentItem.itemCountCoats)
                intent.putExtra("itemCountCurtains", currentItem.itemCountCurtains)
                intent.putExtra("itemCountHats", currentItem.itemCountHats)
                intent.putExtra("itemCountShoes", currentItem.itemCountShoes)
                intent.putExtra("itemCountLeatherBags", currentItem.itemCountLeatherBags)
                intent.putExtra("itemCountBedComforters", currentItem.itemCountBedComforters)
                intent.putExtra("itemCountStuffedToys", currentItem.itemCountStuffedToys)
                intent.putExtra("itemCountWoolSweaters", currentItem.itemCountWoolSweaters)
                intent.putExtra("itemCountDenimJackets", currentItem.itemCountDenimJackets)
                intent.putExtra("itemCountDenimPants", currentItem.itemCountDenimPants)
                intent.putExtra("itemCountLeatherJackets", currentItem.itemCountLeatherJackets)

                intent.putExtra("countShirts", currentItem.countShirts)
                intent.putExtra("countShorts", currentItem.countShorts)
                intent.putExtra("countGowns", currentItem.countGowns)
                intent.putExtra("countCoats", currentItem.countCoats)
                intent.putExtra("countCurtains", currentItem.countCurtains)
                intent.putExtra("countHats", currentItem.countHats)
                intent.putExtra("countShoes", currentItem.countShoes)
                intent.putExtra("countLeatherBags", currentItem.countLeatherBags)
                intent.putExtra("countBedComforters", currentItem.countBedComforters)
                intent.putExtra("countStuffedToys", currentItem.countStuffedToys)
                intent.putExtra("countWoolSweaters", currentItem.countWoolSweaters)
                intent.putExtra("countDenimJackets", currentItem.countDenimJackets)
                intent.putExtra("countDenimPants", currentItem.countDenimPants)
                intent.putExtra("countLeatherJackets", currentItem.countLeatherJackets)
                // Add other DryLaundry specific data...
            }
        }
        return intent
    }

    private fun setLaundryStatusImage(holder: MyViewHolder, status: String) {
        when (status) {
            "Collected" -> {
                holder.laundryStatusImageIV.setImageResource(R.drawable.laundry_collected)
                holder.laundryStatus.setTextColor(ContextCompat.getColor(context, R.color.collectedTextColor))
            }
            "In Progress" -> {
                holder.laundryStatusImageIV.setImageResource(R.drawable.laundry_inprogress)
                holder.laundryStatus.setTextColor(ContextCompat.getColor(context, R.color.inProgressTextColor))
            }
            "Completed" -> {
                holder.laundryStatusImageIV.setImageResource(R.drawable.laundry_completed)
                holder.laundryStatus.setTextColor(ContextCompat.getColor(context, R.color.completedTextColor))
            }
            else -> {
                holder.laundryStatusImageIV.setImageResource(R.drawable.laundry_placed)
                holder.laundryStatus.setTextColor(ContextCompat.getColor(context, R.color.pendingTextColor))
            }
        }
    }

    fun filter(query: String) {
        filteredData = if (query.isEmpty()) {
            datalist
        } else {
            datalist.filter {
                it.laundryReferenceNo?.toLowerCase()!!.contains(query.toLowerCase()) == true
            }
        }
        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var laundryType: TextView
        var laundryReferenceNo: TextView
        var totalPrice: TextView
        var totalItems: TextView
        var laundryStatus: TextView
        var laundryDate: TextView
        var laundryStatusImageIV: ImageView
        var inputAddressInformation: TextView
        var qrCode: TextView

        var itemCountShirts: TextView
        var itemCountShorts: TextView
        var itemCountUnderwears: TextView
        var itemCountPants: TextView
        var itemCountJackets: TextView
        var itemCountBedCovers: TextView
        var itemCountCarpets: TextView
        var itemCountGowns: TextView
        var itemCountCoats: TextView
        var itemCountCurtains: TextView
        var itemCountHats: TextView
        var itemCountShoes: TextView
        var itemCountLeatherBags: TextView
        var itemCountBedComforters: TextView
        var itemCountStuffedToys: TextView
        var itemCountWoolSweaters: TextView
        var itemCountDenimJackets: TextView
        var itemCountDenimPants: TextView
        var itemCountLeatherJackets: TextView

        var countShirts: TextView
        var countShorts: TextView
        var countUnderwears: TextView
        var countPants: TextView
        var countJackets: TextView
        var countBedCovers: TextView
        var countCarpets: TextView
        var countGowns: TextView
        var countCoats: TextView
        var countCurtains: TextView
        var countHats: TextView
        var countShoes: TextView
        var countLeatherBags: TextView
        var countBedComforters: TextView
        var countStuffedToys: TextView
        var countWoolSweaters: TextView
        var countDenimJackets: TextView
        var countDenimPants: TextView
        var countLeatherJackets: TextView

        init {
            laundryType = itemView.findViewById(R.id.laundryType)
            laundryReferenceNo = itemView.findViewById(R.id.laundryReferenceNo)
            totalPrice = itemView.findViewById(R.id.totalPrice)
            totalItems = itemView.findViewById(R.id.totalItems)
            laundryStatus = itemView.findViewById(R.id.laundryStatus)
            laundryDate = itemView.findViewById(R.id.laundryDate)
            laundryStatusImageIV = itemView.findViewById(R.id.laundryStatusImageIV)
            inputAddressInformation = itemView.findViewById(R.id.inputAddressInformation)
            qrCode = itemView.findViewById(R.id.qrCode)

            itemCountShirts = itemView.findViewById(R.id.itemCountShirts)
            itemCountShorts = itemView.findViewById(R.id.itemCountShorts)
            itemCountUnderwears = itemView.findViewById(R.id.itemCountUnderwears)
            itemCountPants = itemView.findViewById(R.id.itemCountPants)
            itemCountJackets = itemView.findViewById(R.id.itemCountJackets)
            itemCountBedCovers = itemView.findViewById(R.id.itemCountBedCovers)
            itemCountCarpets = itemView.findViewById(R.id.itemCountCarpets)
            itemCountGowns = itemView.findViewById(R.id.itemCountGowns)
            itemCountCoats = itemView.findViewById(R.id.itemCountCoats)
            itemCountCurtains = itemView.findViewById(R.id.itemCountCurtains)
            itemCountHats = itemView.findViewById(R.id.itemCountHats)
            itemCountShoes = itemView.findViewById(R.id.itemCountShoes)
            itemCountLeatherBags = itemView.findViewById(R.id.itemCountLeatherBags)
            itemCountBedComforters = itemView.findViewById(R.id.itemCountBedComforters)
            itemCountStuffedToys = itemView.findViewById(R.id.itemCountStuffedToys)
            itemCountWoolSweaters = itemView.findViewById(R.id.itemCountWoolSweaters)
            itemCountDenimJackets = itemView.findViewById(R.id.itemCountDenimJackets)
            itemCountDenimPants = itemView.findViewById(R.id.itemCountDenimPants)
            itemCountLeatherJackets = itemView.findViewById(R.id.itemCountLeatherJackets)

            countShirts = itemView.findViewById(R.id.countShirts)
            countShorts = itemView.findViewById(R.id.countShorts)
            countUnderwears = itemView.findViewById(R.id.countUnderwears)
            countPants = itemView.findViewById(R.id.countPants)
            countJackets = itemView.findViewById(R.id.countJackets)
            countBedCovers = itemView.findViewById(R.id.countBedCovers)
            countCarpets = itemView.findViewById(R.id.countCarpets)
            countGowns = itemView.findViewById(R.id.countGowns)
            countCoats = itemView.findViewById(R.id.countCoats)
            countCurtains = itemView.findViewById(R.id.countCurtains)
            countHats = itemView.findViewById(R.id.countHats)
            countShoes = itemView.findViewById(R.id.countShoes)
            countLeatherBags = itemView.findViewById(R.id.countLeatherBags)
            countBedComforters = itemView.findViewById(R.id.countBedComforters)
            countStuffedToys = itemView.findViewById(R.id.countStuffedToys)
            countWoolSweaters = itemView.findViewById(R.id.countWoolSweaters)
            countBedComforters = itemView.findViewById(R.id.countBedComforters)
            countDenimPants = itemView.findViewById(R.id.countDenimPants)
            countDenimJackets = itemView.findViewById(R.id.countDenimJackets)
            countLeatherJackets = itemView.findViewById(R.id.countLeatherJackets)

        }
    }
}