package com.example.testassignment.Presenter

import com.example.testassignment.DB.CompanyDao
import com.example.testassignment.DB.CompanyDataItem
import com.example.testassignment.Model.CompanyInteractor
import com.example.testassignment.View.CompanyListing
import java.util.concurrent.Executors

class CompanyPresenter (private var companyView: CompanyListing?, private val companyInteractor: CompanyInteractor)
    : CompanyInteractor.OnFinishedListener {

    lateinit var companyDao: CompanyDao

    fun getData(companyDao: CompanyDao) {
        this.companyDao = companyDao
        if(this.companyDao.getCompanyList().isEmpty()) {
            companyView?.showProgress()
            companyInteractor.requestNewsDataAPI(this)
        } else {
            companyView?.setNewsData(this.companyDao.getCompanyList())
            addSortData()
        }
    }

    fun onDestroy() {
        companyView = null
    }

    override fun onResultSuccess(companyDataItem: List<CompanyDataItem>) {
        companyView?.hideProgress()
        Executors.newSingleThreadExecutor()
            .execute {
                companyDao.deleteAll()
                companyDao.insert(companyDataItem)
            }
        companyView?.setNewsData(companyDataItem)
        addSortData()
    }

    override fun onResultFail(strError: String) {
        companyView?.hideProgress()
        companyView?.getDataFailed(strError)
    }

    fun onItemClick(adapterPosition: Int, type:String) {
        companyView?.onItemClick(adapterPosition, type)
    }

    fun addSortData() {
        var list = ArrayList<String>()
        list.add("Sort")
        list.add("A-Z")
        list.add("Z-A")
        companyView?.setSortingOptions(list)
    }

}