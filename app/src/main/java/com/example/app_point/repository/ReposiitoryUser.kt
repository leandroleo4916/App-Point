package com.example.app_point.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.app_point.database.ConstantsUser
import com.example.app_point.database.DataBaseUser
import com.example.app_point.utils.UserEntity

class ReposiitoryUser(context: Context) {

    private val mDataBaseUser: DataBaseUser = DataBaseUser(context)

    fun getUser(name: String, email: String, password: String): Boolean {

        return try{
            val db = mDataBaseUser.writableDatabase
            val insertValues = ContentValues()
            insertValues.put(ConstantsUser.USER.COLUNAS.NAME, name)
            insertValues.put(ConstantsUser.USER.COLUNAS.EMAIL, email)
            insertValues.put(ConstantsUser.USER.COLUNAS.PASSWORD, password)

            db.insert(ConstantsUser.USER.TABLE_NAME, null, insertValues)
            true

        } catch (e: Exception){
            false
        }
    }

    fun storeUser(email: String, senha: String): UserEntity? {

        var user: UserEntity? = null

        try {
            val cursor: Cursor
            val db = mDataBaseUser.readableDatabase

            val projection = arrayOf(
                ConstantsUser.USER.COLUNAS.ID,
                ConstantsUser.USER.COLUNAS.EMAIL,
                ConstantsUser.USER.COLUNAS.PASSWORD
            )

            val selection = "${ConstantsUser.USER.COLUNAS.EMAIL} = ? AND " +
                    "${ConstantsUser.USER.COLUNAS.PASSWORD} = ?"

            val selectionArgs = arrayOf(email, senha)

            cursor = db.query(
                ConstantsUser.USER.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
            )

            if (cursor.count > 0) {
                cursor.moveToFirst()

                val idUser = cursor.getInt(cursor.getColumnIndex(ConstantsUser.USER.COLUNAS.ID))
                val emailUser= cursor.getString(cursor.getColumnIndex(ConstantsUser.USER.COLUNAS.EMAIL))
                val nameUser = cursor.getString(cursor.getColumnIndex(ConstantsUser.USER.COLUNAS.PASSWORD))

                user = UserEntity(idUser, emailUser, nameUser)
            }
            cursor?.close()
        }catch (e: Exception) {
            return user
        }
        return user
    }
}