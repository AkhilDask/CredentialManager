package com.example.credentialmanagermvvm.data.repository

import com.example.credentialmanager.data.database.CredentialDatabase
import com.example.credentialmanager.data.database.entity.CredentialData

class CredentialRepository(
    private val credentialDatabase: CredentialDatabase
) {

    suspend fun insertCredential(credentialData: CredentialData) = credentialDatabase.getCredentialDao().upsertCredential(credentialData)

    suspend fun deleteCredential(credentialData: CredentialData) = credentialDatabase.getCredentialDao().deleteCredential(credentialData)

    fun getAllCredentialById() = credentialDatabase.getCredentialDao().getAllCredentialById()

    fun getAllCredentialByTitle() = credentialDatabase.getCredentialDao().getAllCredentialByTitle()

    fun getAllCredentialByIsEncrypted() = credentialDatabase.getCredentialDao().getAllCredentialByIsEncrypted()



}