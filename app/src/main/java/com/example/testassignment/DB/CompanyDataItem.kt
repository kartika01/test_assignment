package com.example.testassignment.DB


import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity
data class CompanyDataItem (
    @PrimaryKey(autoGenerate = true) val id: Int,

    @SerializedName("about")
    var about: String? = "",

    @SerializedName("company")
    var company: String? = "",

    @SerializedName("_id")
    var company_id: String? = "",

    @SerializedName("logo")
    var logo: String? = "",

    @SerializedName("members")
    @TypeConverters(Members::class)
    var members: ArrayList<Member> = ArrayList(),

    @SerializedName("website")
    var website: String? = "",

    var isFollowed: Boolean = false
):Serializable
