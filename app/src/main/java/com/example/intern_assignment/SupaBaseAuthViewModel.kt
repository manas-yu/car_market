package com.example.intern_assignment

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.intern_assignment.data.model.User
import com.example.intern_assignment.data.model.UserState
import com.example.intern_assignment.data.network.SupabaseClient.client
import com.example.intern_assignment.presentation.navGraph.Routes
import com.example.intern_assignment.utils.SharedPreferenceHelper
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import kotlinx.coroutines.launch

class SupabaseAuthViewModel : ViewModel() {
    private val _userState = mutableStateOf<UserState>(UserState.NullState)
    val userState: State<UserState> = _userState
    var startDestination = Routes.LoadingScreen.route
    var currentUser: User = User("", "", true, "")
    fun changeUserState(userState: UserState) {
        _userState.value = userState
    }

    fun signUp(
        context: Context,
        userEmail: String,
        userPassword: String, isBuyer: Boolean
    ) {
        viewModelScope.launch {

            try {
                _userState.value = UserState.Loading
                client.auth.signUpWith(Email) {
                    email = userEmail
                    password = userPassword
                }


                println("User api call successfully!")
                saveToken(context)
                saveIsBuyer(context, isBuyer)
                val token = getToken(context)
                if (!token.isNullOrEmpty()) {
                    val user = client.auth.retrieveUser(token)
                    currentUser = user.email?.let {
                        currentUser.copy(
                            id = user.id,
                            email = it,
                            isBuyer = isBuyer
                        )
                    }!!
                }
                println("User save successfully!")
                _userState.value = UserState.Success("Registered successfully!")
                println("User registered successfully!")
            } catch (e: Exception) {
                _userState.value = UserState.Error(e.message ?: "")
            }
        }
    }

    private fun saveToken(context: Context) {
        viewModelScope.launch {
            val accessToken = client.auth.currentAccessTokenOrNull()
            currentUser = currentUser.copy(token = accessToken!!)
            val sharedPref = SharedPreferenceHelper(context)
            sharedPref.saveStringData("accessToken", accessToken)
        }

    }

    private fun getToken(context: Context): String? {
        val sharedPref = SharedPreferenceHelper(context)
        return sharedPref.getStringData("accessToken")
    }

    private fun saveIsBuyer(context: Context, isBuyer: Boolean) {
        viewModelScope.launch {
            val sharedPref = SharedPreferenceHelper(context)
            sharedPref.saveBooleanData("isBuyer", isBuyer)
        }

    }

    private fun getIsBuyer(context: Context): Boolean {
        val sharedPref = SharedPreferenceHelper(context)
        return sharedPref.getBooleanData("isBuyer")
    }

    fun login(
        context: Context,
        userEmail: String,
        userPassword: String, isBuyer: Boolean
    ) {
        viewModelScope.launch {
            try {
                _userState.value = UserState.Loading
                client.auth.signInWith(Email) {
                    email = userEmail
                    password = userPassword
                }

                saveToken(context)
                saveIsBuyer(context, isBuyer)

                val token = getToken(context)
                if (!token.isNullOrEmpty()) {
                    val user = client.auth.retrieveUser(token)
                    currentUser = user.email?.let {
                        currentUser.copy(
                            id = user.id,
                            email = it,
                            isBuyer = isBuyer
                        )
                    }!!
                }

                _userState.value = UserState.Success("Logged in successfully!")
            } catch (e: Exception) {
                _userState.value = UserState.Error(e.message ?: "")
            }

        }
    }

    fun logout(context: Context) {
        val sharedPref = SharedPreferenceHelper(context)
        viewModelScope.launch {
            try {
                _userState.value = UserState.Loading
                client.auth.signOut()
                sharedPref.clearPreferences()
                currentUser = User("", "", true, "")
                _userState.value = UserState.Success("Logged out successfully!")
            } catch (e: Exception) {
                _userState.value = UserState.Error(e.message ?: "")
                println(e.message ?: "")
            }
        }
    }

    fun isUserLoggedIn(
        context: Context,
    ) {
        viewModelScope.launch {
            try {
                _userState.value = UserState.Loading
                val token = getToken(context)
                val isBuyer = getIsBuyer(context)
                if (token.isNullOrEmpty()) {
                    startDestination = Routes.AuthScreen.route
                    _userState.value = UserState.Success("User not logged in!")
                } else {
                    val user = client.auth.retrieveUser(token)
                    client.auth.refreshCurrentSession()

                    currentUser = currentUser.copy(
                        id = user.id,
                        email = user.email!!,
                        isBuyer = isBuyer
                    )

                    saveToken(context)

                    println("isBuyer :  $isBuyer")
                    startDestination = if (isBuyer) {
                        Routes.BuyerScreen.route
                    } else {
                        Routes.SellerScreen.route
                    }
                    _userState.value = UserState.Success("User already logged in!")
                }
            } catch (e: RestException) {
                _userState.value = UserState.Error(e.error)
            }
        }
    }

}