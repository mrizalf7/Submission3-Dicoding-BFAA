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

class FollowingViewModel: ViewModel() {

    private val listUser = MutableLiveData<ArrayList<GithubUserData>>()

    companion object {
        private val TAG = FollowingViewModel::class.java.simpleName
    }
    fun setFollowing(users: String) {
        val listFollowing = ArrayList<GithubUserData>()
        val url = "https://api.github.com/users/$users/following"
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
                        listFollowing.add(user)
                    }
                    listUser.postValue(listFollowing)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>, responseBody: ByteArray, error: Throwable) {
                Log.d("onFailure", error.message.toString())
            }

        })
    }

    fun getFollowing(): LiveData<ArrayList<GithubUserData>> {
        return listUser
    }
}