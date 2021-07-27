package com.example.fourthproject.api

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fourthproject.entity.GithubUserData
import com.google.gson.Gson
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class FollowersViewModel: ViewModel() {

    companion object {
        private val TAG = FollowersViewModel::class.java.simpleName
    }
    private val listUser = MutableLiveData<ArrayList<GithubUserData>>()

    fun setFollowers(users: String) {
        val listFollowers = ArrayList<GithubUserData>()
        val url = "https://api.github.com/users/$users/followers"
        val asyncClient = AsyncHttpClient()
        asyncClient.addHeader("Authorization", "token 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")
        asyncClient.addHeader("User-Agent", "request")
        asyncClient.get(url, object : AsyncHttpResponseHandler()
        {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>, responseBody: ByteArray) {
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val responseArray = JSONArray(result)
                    for (i in 0 until responseArray.length()) {
                        val gson = Gson()
                        val user = gson.fromJson(responseArray.getJSONObject(i).toString(), GithubUserData::class.java)
                        listFollowers.add(user)
                    }
                    listUser.postValue(listFollowers)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>, responseBody: ByteArray, error: Throwable) {
                Log.d("onFailure", error.message.toString())
            }

        })
    }

    fun getFollowers(): LiveData<ArrayList<GithubUserData>> {
        return listUser
    }

}