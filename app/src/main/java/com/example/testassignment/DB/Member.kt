package com.example.testassignment.DB


import androidx.room.*
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.lang.reflect.Member

@Entity
data class Member (
    @PrimaryKey(autoGenerate = true) val id: Int,

    @SerializedName("age")
    var age: Int? = 0,

    @SerializedName("email")
    var email: String? = "",

    @SerializedName("_id")
    var member_id: String? = "",

    @Embedded
    @SerializedName("name")
    var name: Name? = null,

    @SerializedName("phone")
    var phone: String? = "",

    var isFavorite: Boolean = false
):Serializable
