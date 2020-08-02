package com.example.testassignment.View

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import com.example.testassignment.DB.CompanyDao
import com.example.testassignment.DB.DatabaseClass
import com.example.testassignment.DB.CompanyDataItem
import com.example.testassignment.Model.CompanyInteractor
import com.example.testassignment.Presenter.CompanyPresenter
import com.example.testassignment.R
import com.example.testassignment.showToast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import java.util.concurrent.Executors
import kotlin.Comparator


class CompanyListing : AppCompatActivity(), CompanyView, AdapterView.OnItemSelectedListener {

    private lateinit var companyPresenter: CompanyPresenter
    private lateinit var mAdapter: CompanyListAdapter
    private lateinit var companyData: List<CompanyDataItem>
    private lateinit var companyDao: CompanyDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        companyPresenter = CompanyPresenter(this, CompanyInteractor())
        companyDao = DatabaseClass.getDatabase(this).companyDao()
        progressBar.visibility = View.GONE
        companyListRv.setHasFixedSize(true)
        searchEt.addTextChangedListener(watcher)
        sortSpinner.setOnItemSelectedListener(this)
    }

    override fun onResume() {
        super.onResume()
        companyPresenter.getData(companyDao)
    }

    override fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progressBar.visibility = View.GONE
    }

    override fun setNewsData(companyData: List<CompanyDataItem>) {
        this.companyData = companyData
        mAdapter = CompanyListAdapter(this.companyData, companyPresenter::onItemClick)
        companyListRv.adapter = mAdapter
    }

    override fun setSortingOptions(sortingOptions: List<String>) {
        sortSpinner.adapter = SortingAdapter(this, sortingOptions)
    }

    override fun getDataFailed(strError: String) {
        showToast(this, strError)
    }

    override fun onItemClick(adapterPosition: Int, type:String) {
        if(type.equals("view",true))
            startActivity(
                Intent(this, MemberListing::class.java)
                .putExtra("membersList", companyData[adapterPosition]))
        else{
            companyData[adapterPosition].isFollowed = if(companyData[adapterPosition].isFollowed) false else true
            mAdapter.notifyDataSetChanged()
            Executors.newSingleThreadExecutor()
                .execute {
                    companyDao.updateCompany(companyData[adapterPosition].isFollowed, companyData[adapterPosition].company_id!!)
                }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        companyPresenter.onDestroy()
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
        if(position > 0){
            Collections.sort(companyData, object : Comparator<CompanyDataItem?> {
                override fun compare(o1: CompanyDataItem?, o2: CompanyDataItem?): Int {
                    if(position == 1)
                        return o1!!.company!!.compareTo(o2!!.company!!)
                    else
                        return o2!!.company!!.compareTo(o1!!.company!!)
                }
            })
            mAdapter = CompanyListAdapter(companyData, companyPresenter::onItemClick)
            companyListRv.adapter = mAdapter
        }
    }
}