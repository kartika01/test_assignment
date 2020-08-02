package com.example.testassignment.View

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import com.example.testassignment.DB.*
import com.example.testassignment.Presenter.MemberPresenter
import com.example.testassignment.R
import kotlinx.android.synthetic.main.activity_member_listing.*
import java.util.*
import java.util.concurrent.Executors
import kotlin.Comparator
import kotlin.collections.ArrayList

class MemberListing : AppCompatActivity(), MemberView, AdapterView.OnItemSelectedListener {

    private lateinit var memberPresenter: MemberPresenter
    private lateinit var mAdapter: MemberListAdapter
    private lateinit var companyDataItem: CompanyDataItem
    private lateinit var membersData: ArrayList<Member>
    private lateinit var companyDao: CompanyDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_listing)

        memberPresenter = MemberPresenter(this, intent)
        companyDao = DatabaseClass.getDatabase(this).companyDao()
        memberListRv.setHasFixedSize(true)
        searchMemberEt.addTextChangedListener(watcher)
        sortMemberSPinner.onItemSelectedListener = this
        backIv.setOnClickListener { finish() }
    }

    override fun onResume() {
        super.onResume()
        memberPresenter.getData()
    }

    val watcher = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            mAdapter.getFilter().filter(p0!!)
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (position > 0) {
            Collections.sort(membersData, object : Comparator<Member?> {
                override fun compare(o1: Member?, o2: Member?): Int {
                    return when (position) {
                        1 -> o1!!.name!!.first!!.compareTo(o2!!.name!!.first!!)
                        2 -> o2!!.name!!.first!!.compareTo(o1!!.name!!.first!!)
                        3 -> o1!!.age!!.compareTo(o2!!.age!!)
                        else -> o2!!.age!!.compareTo(o1!!.age!!)
                    }
                }
            })
            mAdapter = MemberListAdapter(this.membersData, memberPresenter::onItemClick)
            memberListRv.adapter = mAdapter
        }
    }

    override fun setMemberData(companyData: CompanyDataItem) {
        companyDataItem = companyData
        membersData = companyData.members
        mAdapter = MemberListAdapter(membersData, memberPresenter::onItemClick)
        memberListRv.adapter = mAdapter
    }

    override fun setSortingOptions(sortingOptions: List<String>) {
        sortMemberSPinner.adapter = SortingAdapter(this, sortingOptions)
    }

    override fun onItemClick(adapterPosition: Int) {
        membersData[adapterPosition].isFavorite = if(membersData[adapterPosition].isFavorite) false else true
        mAdapter.notifyDataSetChanged()

        Executors.newSingleThreadExecutor()
            .execute {
                companyDao.updateMember(membersData, companyDataItem.company_id!!)
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        memberPresenter.onDestroy()
    }
}