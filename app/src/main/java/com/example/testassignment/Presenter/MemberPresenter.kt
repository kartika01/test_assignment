package com.example.testassignment.Presenter

import android.content.Intent
import com.example.testassignment.DB.CompanyDataItem
import com.example.testassignment.DB.Member
import com.example.testassignment.View.MemberListing

class MemberPresenter(private var memberView: MemberListing?, var intent: Intent) {

    fun getData() {
        var memberList = intent.getSerializableExtra("membersList") as CompanyDataItem
        memberView?.setMemberData(memberList)
        addSortData()
    }

    fun onDestroy() {
        memberView = null
    }

    fun onItemClick(adapterPosition: Int) {
        memberView?.onItemClick(adapterPosition)
    }

    fun addSortData() {
        var list = ArrayList<String>()
        list.add("Sort")
        list.add("A-Z")
        list.add("Z-A")
        list.add("High - Low(age)")
        list.add("Low - High(age)")
        memberView?.setSortingOptions(list)
    }

}