package com.example.testassignment.DB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CompanyDao {

    @Query("SELECT * from CompanyDataItem ORDER BY id ASC")
    fun getCompanyList(): List<CompanyDataItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(company: List<CompanyDataItem>)

    @Query("UPDATE CompanyDataItem SET isFollowed=:follow WHERE company_id = :id")
    fun updateCompany(follow: Boolean, id: String)

    @Query("UPDATE CompanyDataItem SET members=:member WHERE company_id = :id")
    fun updateMember(member: ArrayList<Member>, id: String)

    @Query("DELETE FROM CompanyDataItem")
    fun deleteAll()
}