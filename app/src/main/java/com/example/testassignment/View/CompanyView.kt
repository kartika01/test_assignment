package com.example.testassignment.View

import com.example.testassignment.DB.CompanyDataItem

interface CompanyView {
    fun showProgress()
    fun hideProgress()
    fun setNewsData(companyData: List<CompanyDataItem>)
    fun setSortingOptions(sortingOptions: List<String>)
    fun getDataFailed(strError: String)
    fun onItemClick(adapterPosition: Int, type:String)
}