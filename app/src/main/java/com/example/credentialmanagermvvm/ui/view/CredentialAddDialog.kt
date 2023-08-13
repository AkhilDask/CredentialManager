package com.example.credentialmanagermvvm.ui.view

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.credentialmanagermvvm.data.model.CredentialState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CredentialAddDialog(
    state: CredentialState,
    onEvent:(CredentialEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = { onEvent(CredentialEvent.HideDialog) },
    title = {Text(text = "Add Credential")},
        text = {
            Column(verticalArrangement =
            Arrangement.spacedBy(8.dp)
            ) {
                TextField(value = state.credentialTitle, 
                    onValueChange = {
                        onEvent(CredentialEvent.SetTitle(it))
                                    },
                placeholder = {
                    Text(text = "Enter title")
                })
                TextField(value = state.credentialDesc,
                    onValueChange = {onEvent(CredentialEvent.SetDescription(it)) },
                    placeholder = {
                        Text(text = "Enter description")
                    })
                Button(onClick = { onEvent(CredentialEvent.SetIsEncrypted(true)) }) {
                    Text(text = "Encrypt")
                }

            }
        }, confirmButton = {
            Box(modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd){
                Button(onClick = { onEvent(CredentialEvent.SaveCredential) }) {
                    Text(text = "Save Credential")
                }
            }
        }
    )

}