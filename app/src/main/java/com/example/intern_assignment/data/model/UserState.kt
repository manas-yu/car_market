package com.example.intern_assignment.data.model

sealed class UserState {
    object Loading : UserState()
    data class Success(val message: String) : UserState()
    data class Error(val message: String) : UserState()
    object NullState : UserState()
}