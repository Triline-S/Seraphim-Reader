package com.example.myapplication

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel(){
    val visiblePermissionDialogueQueue = mutableStateListOf<String>()
    fun dismissDialogue() {
        visiblePermissionDialogueQueue.removeLast()
    }

    fun onPermissionResult(
        permission: String,
        isGranted: Boolean
    ){
        if(!isGranted){
            visiblePermissionDialogueQueue.add(0, permission)
        }
    }
}