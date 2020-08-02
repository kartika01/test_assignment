package com.example.testassignment.View

import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.testassignment.DB.Member
import com.example.testassignment.R
import com.example.testassignment.inflate
import kotlinx.android.synthetic.main.item_company.view.*
import kotlinx.android.synthetic.main.item_member.view.*

class MemberListAdapter (val memberData: List<Member>, private val listener: (Int) -> Unit) :
    RecyclerView.Adapter<MemberListAdapter.MyViewHolder>(), Filterable {

    var filteredList: List<Member> = ArrayList()

    init{
        filteredList = memberData
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(inflate(parent.context, R.layout.item_member, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(filteredList[position], listener)
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(memberDataItem: Member, listener: (Int) -> Unit) = with(itemView) {
            nameMemberTv.text = memberDataItem.name!!.first + " " + memberDataItem.name!!.last
            ageTv.text = memberDataItem.age.toString()+" Yrs"
            phnTv.text = memberDataItem.phone
            emailTv.text = memberDataItem.email
            favTv.text = if(memberDataItem.isFavorite) "Remove from Favorite" else "Mark as Favorite"
            favTv.setOnClickListener{listener(adapterPosition)}
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString().toLowerCase()

                if (charString.isEmpty()) {
                    filteredList = memberData
                } else {
                    val filteredList1 = ArrayList<Member>()
                    for (fm in memberData) {
                        if (fm.name!!.first!!.toLowerCase().contains(charString.toLowerCase()) ||
                            fm.name!!.last!!.toLowerCase().contains(charString.toLowerCase())) {
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
                filterResults: FilterResults
            ) {
                filteredList = filterResults.values as ArrayList<Member>
                notifyDataSetChanged()
            }
        }
    }
}