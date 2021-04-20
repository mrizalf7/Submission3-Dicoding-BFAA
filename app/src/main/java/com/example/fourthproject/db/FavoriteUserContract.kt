package com.example.fourthproject.db

import android.provider.BaseColumns

internal class FavoriteUserContract {

    internal class Columns : BaseColumns{
        companion object{
            const val table_name = "githubFavoriteUser"
            const val _id = "id"
            const val _username = "username"
            const val avatar = "avatar_url"

        }
    }

}