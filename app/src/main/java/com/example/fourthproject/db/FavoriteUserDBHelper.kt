package com.example.fourthproject.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.fourthproject.db.FavoriteUserContract.Columns.Companion.table_name


class FavoriteUserDBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {

        private const val DATABASE_NAME = "dbfavoriteuserapp"
        private const val DATABASE_VERSION = 1
        private val SQL_CREATE_TABLE_USER = "CREATE TABLE ${table_name}" +
                " (${FavoriteUserContract.Columns._id} INTEGER PRIMARY KEY AUTOINCREMENT," +
                " ${FavoriteUserContract.Columns._username} TEXT NOT NULL," +
                " ${FavoriteUserContract.Columns.avatar} TEXT NOT NULL)"

    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_USER)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $table_name")
        onCreate(db)
    }
}