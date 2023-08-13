package com.example.credentialmanager.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.credentialmanager.data.database.entity.CredentialData


@Database(
    entities = [CredentialData::class],
    version = 1  //for when there is an update to database
)
abstract class CredentialDatabase: RoomDatabase() {
    abstract fun getCredentialDao(): CredentialsDAO


}