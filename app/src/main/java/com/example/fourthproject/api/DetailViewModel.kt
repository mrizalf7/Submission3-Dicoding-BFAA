package com.example.fourthproject.api

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fourthproject.entity.FavoriteUser
import com.example.fourthproject.entity.GithubUserData
import com.google.gson.Gson
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class DetailViewModel : ViewModel() {

    companion object {
        private val TAG = DetailViewModel::class.java.simpleName
    }

    val detailUser = MutableLiveData<GithubUserData>()
    val detailFavoriteUser = MutableLiveData<FavoriteUser>()

    fun setDetailUser(users: String) {
        val url = "https://api.github.com/users/$users"
        val asyncClient = AsyncHttpClient()
        asyncClient.addHeader("Authorization", "token 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")
        asyncClient.addHeader("User-Agent", "request")
        asyncClient.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val responObjects = JSONObject(result)
                    val gson = Gson()
                    val user = gson.fromJson(responObjects.toString(), GithubUserData::class.java)
                    val favoriteUser = gson.fromJson(responObjects.toString(), FavoriteUser::class.java)
                    detailUser.postValue(user)
                    detailFavoriteUser.postValue(favoriteUser)

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray,
                error: Throwable?
            ) {
                Log.d("onFailure", error?.message.toString())
            }

        })
    }

    fun getDetailUser(): LiveData<GithubUserData> {
        return detailUser
    }

    fun getDetailFavoriteUser(): LiveData<FavoriteUser> {
        return detailFavoriteUser
    }

}