package com.example.credentialmanagermvvm.data.model

import com.example.credentialmanager.data.database.entity.CredentialData

data class CredentialState(
    val credentials:List<CredentialData> = emptyList(),
    val credentialTitle:String ="",
    val credentialDesc:String="",
    val credentialIsEncrypted:Boolean=false,
    var credentialDecrypted:String ="",
    val isCredentialAdding:Boolean = false,
    val sortType:SortType = SortType.ID


)