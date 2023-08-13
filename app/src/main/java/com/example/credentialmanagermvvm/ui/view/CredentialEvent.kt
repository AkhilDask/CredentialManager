package com.example.credentialmanagermvvm.ui.view

import com.example.credentialmanager.data.database.entity.CredentialData
import com.example.credentialmanagermvvm.data.model.SortType

sealed interface CredentialEvent {
    object SaveCredential:CredentialEvent
    data class SetTitle(val title:String):CredentialEvent
    data class SetDescription(val description:String):CredentialEvent
    data class SetIsEncrypted(val isEncrypted:Boolean):CredentialEvent
    data class SetDecryptValue(val encryptedValue:String):CredentialEvent
    object ShowDialog:CredentialEvent
    object HideDialog:CredentialEvent
    data class SortCredential(val sortType: SortType):CredentialEvent
    data class DeleteCredential(val credentialData: CredentialData):CredentialEvent
}