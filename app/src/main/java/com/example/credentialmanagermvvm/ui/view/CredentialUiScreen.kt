package com.example.credentialmanagermvvm.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.credentialmanager.data.database.entity.CredentialData
import com.example.credentialmanagermvvm.data.model.CredentialState
import com.example.credentialmanagermvvm.data.model.SortType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun  CredentialUiScreen(
    state:CredentialState,
    onEvent:(CredentialEvent) -> Unit
) {

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { onEvent(CredentialEvent.ShowDialog) }) {
                Icon(imageVector = Icons.Default.Add , contentDescription = "Add Credential" )
            }
        }
    ) {
            padding ->
        if (state.isCredentialAdding){
            CredentialAddDialog(state = state, onEvent = onEvent)
        }


        LazyColumn(
        contentPadding = padding,
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
       item {
           Row(
               modifier = Modifier
                   .fillMaxWidth()
                   .horizontalScroll(rememberScrollState()),
               verticalAlignment = Alignment.CenterVertically
           )
           {
               SortType.values().forEach { sortType ->
                   Row(
                       modifier = Modifier.clickable {
                           onEvent(CredentialEvent.SortCredential(sortType))
                       },
                       verticalAlignment = CenterVertically
                   ) {
                       RadioButton(
                           selected = state.sortType == sortType,
                           onClick = {
                               onEvent(CredentialEvent.SortCredential(sortType))
                           },

                           )
                       Text(text = sortType.name)

                   }

               }

           }
       }
        items(state.credentials){credential ->
            Row(modifier = Modifier.fillMaxWidth()) {
                var descriptionValue by remember { mutableStateOf(credential.description) }
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = credential.title, fontSize = 20.sp,)
                    Text(descriptionValue,fontSize = 20.sp)
                    
                }
                IconButton(onClick = { onEvent(CredentialEvent.DeleteCredential(credential)) }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Credential")
                }
                if (credential.isEncrypted){
                    IconButton(onClick = {
                        onEvent(CredentialEvent.SetDecryptValue(credential.description)
                        )
                         descriptionValue = state.credentialDecrypted

                    }) {
                        Icon(imageVector = Icons.Default.Build, contentDescription = "Decrypt")
                    }
                }

                
            }
        }
    }
    }


}

@Composable
fun setTitleAndDescription(title:String,description:String){

}