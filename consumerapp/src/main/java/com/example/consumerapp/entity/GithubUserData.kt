package com.example.fourthproject.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class GithubUserData(

    var id : Int = 0,
    @field:SerializedName("login")
    var idGithub:String? ="",
    @field:SerializedName("avatar_url")
    var avatar: String? ="",
    @field:SerializedName("name")
    var name: String? ="",
    @field:SerializedName("followers")
    var followers:String? ="",
    @field:SerializedName("following")
    var following:String? ="",
    @field:SerializedName("company")
    var company:String? ="",
    @field:SerializedName("location")
    var location:String?="",
    @field:SerializedName("public_repos")
    var repository:String?="",


): Parcelable