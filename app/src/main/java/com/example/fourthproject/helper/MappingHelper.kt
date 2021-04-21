package com.example.fourthproject.helper

import android.database.Cursor
import com.example.fourthproject.db.FavoriteUserContract
import com.example.fourthproject.entity.GithubUserData

object MappingHelper {

    fun mapCursorToArrayList(notesCursor: Cursor?): ArrayList<GithubUserData> {

        val favoriteListUsers = ArrayList<GithubUserData>()
        notesCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(FavoriteUserContract.Columns._id))
                val username = getString(getColumnIndexOrThrow(FavoriteUserContract.Columns._username))
                val avatar = getString(getColumnIndexOrThrow(FavoriteUserContract.Columns.avatar))
                favoriteListUsers.add(GithubUserData(id, username, avatar))
            }
        }
        return favoriteListUsers
    }

    fun mapCursorToObject(notesCursor: Cursor?): GithubUserData {
        var user =GithubUserData()
        notesCursor?.apply {
            moveToFirst()
            val id = getInt(getColumnIndexOrThrow(FavoriteUserContract.Columns._id))
            val username = getString(getColumnIndexOrThrow(FavoriteUserContract.Columns._username))
            val avatar = getString(getColumnIndexOrThrow(FavoriteUserContract.Columns.avatar))

            user = GithubUserData(id, username, avatar,)
        }
        return user
    }
}