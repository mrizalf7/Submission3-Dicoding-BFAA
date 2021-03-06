package com.example.fourthproject.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.fourthproject.db.FavoriteUserContract.Columns.Companion._id
import com.example.fourthproject.db.FavoriteUserContract.Columns.Companion.username
import com.example.fourthproject.db.FavoriteUserContract.Columns.Companion.tableName
import java.sql.SQLException

class FavoriteUserHelper(context: Context) {

    private var databaseHelper: FavoriteUserDBHelper = FavoriteUserDBHelper(context)
    private lateinit var database: SQLiteDatabase

    companion object {
        private const val DATABASE_TABLE = tableName
        private var INSTANCE: FavoriteUserHelper? = null
        fun getInstance(context: Context): FavoriteUserHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: FavoriteUserHelper(context)
            }
    }


    @Throws(SQLException::class)
    fun open() {
        database = databaseHelper.writableDatabase
    }


    fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$_id ASC"

        )
    }

    fun queryByUsername(username: String): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "$username = ?",
            arrayOf(username),
            null,
            null,
            null,
            null
        )
    }

    fun queryById(id: String): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "$_id = ?",
            arrayOf(id),
            null,
            null,
            null,
            null
        )
    }
    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun deleteByUsername(username: String): Int {
        return database.delete(DATABASE_TABLE, "$username = '$username'", null)
    }
    fun deleteById(id: String): Int {
        return database.delete(DATABASE_TABLE, "$_id = '$id'", null)
    }

    fun update(id: String, values: ContentValues?): Int {
        return database.update(DATABASE_TABLE, values, "$username = ?", arrayOf(id))
    }
}
