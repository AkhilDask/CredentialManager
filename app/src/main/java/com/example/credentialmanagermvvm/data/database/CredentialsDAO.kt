package com.example.credentialmanager.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.credentialmanager.data.database.entity.CredentialData
import kotlinx.coroutines.flow.Flow

@Dao
interface CredentialsDAO {

    @Upsert
    suspend fun upsertCredential(inputCredential: CredentialData):Long


    @Delete
    suspend fun deleteCredential(inputCredential: CredentialData)

    @Query("SELECT * FROM CredentialData ORDER BY id ASC")
    fun getAllCredentialById(): Flow<List<CredentialData>>

    @Query("SELECT * FROM CredentialData ORDER BY title ASC")
    fun getAllCredentialByTitle(): Flow<List<CredentialData>>

    @Query("SELECT * FROM CredentialData WHERE isEncrypted=1")
    fun getAllCredentialByIsEncrypted(): Flow<List<CredentialData>>



}

