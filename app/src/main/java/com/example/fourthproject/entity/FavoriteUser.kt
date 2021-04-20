package com.example.fourthproject.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class FavoriteUser (

    var id:Int? = 0,
    @field:SerializedName("login")
    var username:String? ="",
    @field:SerializedName("avatar_url")
    var avatar:String?="",



): Parcelable
