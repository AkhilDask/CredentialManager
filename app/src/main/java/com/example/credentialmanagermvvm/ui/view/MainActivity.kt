package com.example.credentialmanagermvvm.ui.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.credentialmanager.data.database.CredentialDatabase
import com.example.credentialmanagermvvm.data.repository.CredentialRepository
import com.example.credentialmanagermvvm.ui.viewmodel.CredentialViewModel
import com.example.credentialmanagermvvm.ui.theme.CredentialManagerMVVMTheme




class MainActivity : ComponentActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dbCredential by lazy {
            Room.databaseBuilder(
                this,
                CredentialDatabase::class.java,
                "credentials.db"
            ).build()
        }

        val repository = CredentialRepository(dbCredential)
        val viewModel by viewModels<CredentialViewModel>(
            factoryProducer = {
                object :ViewModelProvider.Factory{
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return CredentialViewModel(repository) as T
                    }
                }
            }
        )
        setContent {
            CredentialManagerMVVMTheme {
                val state by viewModel.state.collectAsState()
               CredentialUiScreen(state = state, onEvent = viewModel::onCredentialEvent)


            }
        }
    }
}

