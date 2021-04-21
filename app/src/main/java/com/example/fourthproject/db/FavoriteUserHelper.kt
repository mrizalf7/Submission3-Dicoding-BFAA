package com.example.fourthproject.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.fourthproject.db.FavoriteUserContract.Columns.Companion._id
import com.example.fourthproject.db.FavoriteUserContract.Columns.Companion._username
import com.example.fourthproject.db.FavoriteUserContract.Columns.Companion.table_name
import java.sql.SQLException

class FavoriteUserHelper(context: Context) {

    private var databaseHelper: FavoriteUserDBHelper = FavoriteUserDBHelper(context)
    private lateinit var database: SQLiteDatabase

    companion object {
        private const val DATABASE_TABLE = table_name
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

    fun close() {
        databaseHelper.close()

        if (database.isOpen)
            database.close()
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
            "$_username = ?",
            arrayOf(username),
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
        return database.delete(DATABASE_TABLE, "$_username = '$username'", null)
    }

    fun update(id: String, values: ContentValues?): Int {
        return database.update(DATABASE_TABLE, values, "$_username = ?", arrayOf(id))
    }
}
