package com.example.credentialmanagermvvm.ui.viewmodel

import android.util.Base64
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.credentialmanager.data.database.entity.CredentialData
import com.example.credentialmanagermvvm.data.repository.CredentialRepository
import com.example.credentialmanagermvvm.data.model.CredentialState
import com.example.credentialmanagermvvm.data.model.SortType
import com.example.credentialmanagermvvm.ui.view.CredentialEvent
import com.example.credentialmanagermvvm.utils.CryptoManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream


class CredentialViewModel(
    private val repository: CredentialRepository
): ViewModel() {

    private val _sortType = MutableStateFlow(SortType.ID)
    private val _state = MutableStateFlow(CredentialState())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _credentials = _sortType.flatMapLatest { sortType->
        when(sortType){
            SortType.ID -> repository.getAllCredentialById()
            SortType.TITLE -> repository.getAllCredentialByTitle()
            SortType.IS_ENCRYPTED -> repository.getAllCredentialByIsEncrypted()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val state = combine(_state,_sortType,_credentials){state,sortType,credentials ->
        state.copy(
            credentials = credentials,
            sortType = sortType
        )

    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CredentialState())
    fun onCredentialEvent(credentialEvent: CredentialEvent){
        when(credentialEvent){
           is CredentialEvent.DeleteCredential -> {
               viewModelScope.launch {
                   repository.deleteCredential(credentialEvent.credentialData)
               }
           }


            CredentialEvent.HideDialog -> {
                _state.update {
                    it.copy(
                        isCredentialAdding = false
                    )
                }
            }
            CredentialEvent.SaveCredential -> {
                val title = state.value.credentialTitle
                var description = state.value.credentialDesc
                val isEncrypted = state.value.credentialIsEncrypted
                if (title.isEmpty() || description.isEmpty() ){
                    return
                }else{
                    if (isEncrypted){
                        val bytesValue = description.encodeToByteArray()
                        val cryptoManager = CryptoManager()
                        val outputStreamValue = ByteArrayOutputStream()
                        cryptoManager.encrypt(bytesValue, outputStreamValue)

                        // Convert the encrypted data to Base64 format
                        val encryptedBytes = outputStreamValue.toByteArray()
                        description = Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
                    }
                    val credential = CredentialData(title, description, isEncrypted)
                    viewModelScope.launch {
                        repository.insertCredential(credential)
                    }

                    _state.update {
                        it.copy(
                            isCredentialAdding = false,
                            credentialTitle = "",
                            credentialDesc = "",
                            credentialIsEncrypted = false
                        )
                    }

                }

            }
            is CredentialEvent.SetDescription -> {

                _state.update {
                    it.copy(
                         credentialDesc = credentialEvent.description

                    )
                }
            }
            is CredentialEvent.SetIsEncrypted -> {
                _state.update {
                    it.copy(
                        credentialIsEncrypted = credentialEvent.isEncrypted

                    )

                }

            }

            is CredentialEvent.SetDecryptValue -> {
                val valueToDecrypt = credentialEvent.encryptedValue
                val bytesArray = valueToDecrypt.let { Base64.decode(it, Base64.DEFAULT) }

                if (bytesArray != null) {
                    val cryptoManager = CryptoManager()
                    val decryptedBytes = cryptoManager.decrypt(bytesArray)
                    state.value.credentialDecrypted = decryptedBytes.decodeToString()





                }
            }
            is CredentialEvent.SetTitle -> {
                _state.update {
                    it.copy(
                        credentialTitle = credentialEvent.title
                    )
                }
            }
            CredentialEvent.ShowDialog -> {
                _state.update {
                    it.copy(
                        isCredentialAdding = true
                    )
                }
            }
            is CredentialEvent.SortCredential -> {
                _sortType.value = credentialEvent.sortType
            }
        }
    }
















}