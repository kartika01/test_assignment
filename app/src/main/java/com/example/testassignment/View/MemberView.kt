package com.example.testassignment.View

import com.example.testassignment.DB.CompanyDataItem
import com.example.testassignment.DB.Member

interface MemberView {
    fun setMemberData(membersData: CompanyDataItem)
    fun setSortingOptions(sortingOptions: List<String>)
    fun onItemClick(adapterPosition: Int)
}