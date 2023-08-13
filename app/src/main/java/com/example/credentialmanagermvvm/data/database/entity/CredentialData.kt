package com.example.credentialmanager.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class  CredentialData(

    val title: String,
    val description: String,
    val isEncrypted:Boolean,
    @PrimaryKey(autoGenerate = true)
    val id: Int=0

)