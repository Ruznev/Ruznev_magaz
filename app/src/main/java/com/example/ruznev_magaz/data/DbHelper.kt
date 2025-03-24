package com.example.ruznev_magaz.data


import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.ruznev_magaz.domian.Models.User
import java.io.ByteArrayOutputStream


class DbHelper(private val context: Context, private val factory: SQLiteDatabase.CursorFactory?) :
        SQLiteOpenHelper(context, "database", factory, 2) {


    override fun onCreate(db: SQLiteDatabase?) {
        val query =
            "CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT DEFAULT NULL, login TEXT, email TEXT, password TEXT, profile_image BLOB)"
        db!!.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS users")
        onCreate(db)
    }

    fun saveProfileImage(email: String, bitmap: Bitmap): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream)
            val imageBytes = stream.toByteArray()
            put("profile_image", imageBytes)
        }
        val rowsAffected = db.update("users", values, "email = ?", arrayOf(email))
        db.close()

        return rowsAffected > 0
    }

    @SuppressLint("Recycle")
    fun loadProfileImage(email: String): Bitmap? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT profile_image FROM users WHERE email=?", arrayOf(email))
        return if (cursor.moveToFirst()) {
            val imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow("profile_image"))
            if (imageBytes != null) {
                BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            } else {
                null
            }
        } else {
            null
        }
    }

    fun addUser(user: User) {
        val values = ContentValues()
        values.put("login", user.login)
        values.put("email", user.email)
        values.put("password", user.password)

        val db = this.writableDatabase
        db.insert("users", null, values)
        db.close()
    }

    @SuppressLint("Recycle")
    fun getUser(login: String, password: String): Boolean {
        val db = this.readableDatabase

        val result = db.rawQuery("SELECT * FROM users WHERE login='$login'AND password='$password'", null)
        return result.moveToFirst()
    }

    @SuppressLint("Recycle")
    fun getUserByLogin(login: String): User? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM users WHERE login=?", arrayOf(login))

        return if (cursor.moveToFirst()) {
            val email = cursor.getString(cursor.getColumnIndexOrThrow("email"))
            val password = cursor.getString(cursor.getColumnIndexOrThrow("password"))
            User(login, email, password)
        } else {
            null
        }
    }

    @SuppressLint("Recycle")
    fun checkUserByEmail(email: String): Boolean {
        val db = this.readableDatabase
        val result = db.rawQuery("SELECT * FROM users WHERE email='$email'", null)
        return result.moveToFirst()
    }

    fun updatePassword(email: String, newPassword: String): Boolean {
        val db = this.readableDatabase
        val values = ContentValues()
        values.put("password", newPassword)

        val rowsAffected = db.update("users", values, "email=?", arrayOf(email))
        db.close()

        return rowsAffected > 0
    }

    @SuppressLint("Recycle")
    fun getUserByEmail(email: String): User? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM users WHERE email='$email'", null)
        return if (cursor.moveToFirst()) {
            val login = cursor.getString(cursor.getColumnIndexOrThrow("login"))
            val password = cursor.getString(cursor.getColumnIndexOrThrow("password"))
            User(login, email, password)
        } else {
            null
        }
    }
}
