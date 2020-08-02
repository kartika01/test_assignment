package com.example.testassignment.View

import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.testassignment.DB.CompanyDataItem
import com.example.testassignment.R
import com.example.testassignment.inflate
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_company.view.*


class CompanyListAdapter (val companyDataItem: List<CompanyDataItem>, private val listener: (Int, String) -> Unit) :
    RecyclerView.Adapter<CompanyListAdapter.MyViewHolder>(), Filterable {

    var filteredList: List<CompanyDataItem> = ArrayList()

    init{
        filteredList = companyDataItem
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(inflate(parent.context, R.layout.item_company, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(filteredList[position], listener)
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(companyDataItemList: CompanyDataItem, listener: (Int, String) -> Unit) = with(itemView) {
            Picasso.get().load(companyDataItemList.logo).into(companyLogoIv)
            nameTv.text = companyDataItemList.company
            websiteTv.text = companyDataItemList.website
            descTv.text = companyDataItemList.about
            followTv.text = if(companyDataItemList.isFollowed) "Unfollow" else "Follow"
            viewMembersTv.setOnClickListener{listener(adapterPosition, "view")}
            followTv.setOnClickListener{listener(adapterPosition, "follow")}
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString().toLowerCase()

                if (charString.isEmpty()) {
                    filteredList = companyDataItem
                } else {
                    val filteredList1 = ArrayList<CompanyDataItem>()
                    for (fm in companyDataItem) {
                        if (fm.company!!.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList1.add(fm)
                        }
                    }
                    filteredList = filteredList1
                }
                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            override fun publishResults(
                charSequence: CharSequence,
                filterResults: FilterResults) {
                filteredList = filterResults.values as ArrayList<CompanyDataItem>
                notifyDataSetChanged()
            }
        }
    }
}