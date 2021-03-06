package com.example.fourthproject.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.fourthproject.db.FavoriteUserContract.AUTHORITY
import com.example.fourthproject.db.FavoriteUserContract.Columns.Companion.CONTENT_URI
import com.example.fourthproject.db.FavoriteUserContract.Columns.Companion.tableName
import com.example.fourthproject.db.FavoriteUserHelper

class FavoriteUserProvider : ContentProvider() {

    companion object {
        private const val FAV = 1
        private const val FAV_ID = 2
        private lateinit var favHelper: FavoriteUserHelper
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            sUriMatcher.addURI(AUTHORITY, tableName, FAV)

            sUriMatcher.addURI(
                AUTHORITY,
                "$tableName/#",
                FAV_ID
            )
        }
    }

    override fun onCreate(): Boolean {
        favHelper = FavoriteUserHelper.getInstance(context as Context)
        favHelper.open()
        return true
    }

    override fun query(
        uri: Uri,
        strings: Array<String>?,
        s: String?,
        strings1: Array<String>?,
        s1: String?
    ): Cursor? {
        return when (sUriMatcher.match(uri)) {
            FAV -> favHelper.queryAll() // get all data
            FAV_ID -> favHelper.queryById(uri.lastPathSegment.toString()) // get data by id
            else -> null
        }
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, contentValues: ContentValues?): Uri? {
        val added: Long = when (FAV) {
            sUriMatcher.match(uri) -> favHelper.insert(contentValues)
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun update(
        uri: Uri,
        contentValues: ContentValues?,
        s: String?,
        strings: Array<String>?
    ): Int {
        val updated: Int = when (FAV_ID) {
            sUriMatcher.match(uri) -> favHelper.update(
                uri.lastPathSegment.toString(),
                contentValues
            )
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return updated
    }

    override fun delete(uri: Uri, s: String?, strings: Array<String>?): Int {
        val deleted: Int = when (FAV_ID) {
            sUriMatcher.match(uri) -> favHelper.deleteById(uri.lastPathSegment.toString())
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return deleted
    }
}