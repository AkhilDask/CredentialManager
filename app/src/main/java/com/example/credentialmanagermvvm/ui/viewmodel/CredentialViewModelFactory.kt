package com.example.credentialmanager.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.credentialmanagermvvm.data.repository.CredentialRepository



class CredentialViewModelFactory(
    private val repository: CredentialRepository,
    val context:Context
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        try {
            val constructor = modelClass.getDeclaredConstructor(CredentialRepository::class.java)
            val context = modelClass.getDeclaredConstructor(Context::class.java)
            return constructor.newInstance(repository,context)
        } catch (e: Exception) {

        }
        return super.create(modelClass)
    }
}