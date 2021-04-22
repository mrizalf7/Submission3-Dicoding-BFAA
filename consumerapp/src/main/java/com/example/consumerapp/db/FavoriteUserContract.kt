package com.example.fourthproject.db

import android.net.Uri
import android.provider.BaseColumns
import android.service.notification.Condition.SCHEME

object FavoriteUserContract {

    const val AUTHORITY = "com.example.fourthproject"
    const val SCHEME = "content"

    class Columns : BaseColumns {
        companion object {
            const val table_name = "githubFavoriteUser"
            const val _id = "id"
            const val _username = "username"
            const val avatar = "avatar_url"


          val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
              .authority(AUTHORITY)
               .appendPath(table_name)
               .build()


      }

    }
}