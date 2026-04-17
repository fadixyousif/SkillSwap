package com.example.skillswap.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.skillswap.ui.components.AuthTextField
import com.example.skillswap.ui.components.PrimaryButton
import com.example.skillswap.ui.theme.SkillSwapBackground
import com.example.skillswap.ui.theme.SkillSwapBorder
import com.example.skillswap.ui.theme.SkillSwapPrimary
import com.example.skillswap.ui.theme.SkillSwapSecondary
import com.example.skillswap.ui.theme.SkillSwapSurface
import com.example.skillswap.ui.theme.SkillSwapTextSecondary
import com.example.skillswap.ui.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onLoginSuccess: (hasProfile: Boolean, token: String, meUser: com.example.skillswap.data.model.MeUser?) -> Unit,
    onSignupClick: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var localError by remember { mutableStateOf("") }
    val authState by authViewModel.authState.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize().background(SkillSwapBackground)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().height(260.dp).background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        SkillSwapPrimary.copy(alpha = 0.22f),
                        SkillSwapSecondary.copy(alpha = 0.10f),
                        SkillSwapBackground
                    )
                )
            )
        )
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text("Welcome back", style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.ExtraBold)
            Spacer(modifier = Modifier.height(10.dp))
            Text("Log in to continue building your profile and swapping skills.",
                style = MaterialTheme.typography.bodyLarge, color = SkillSwapTextSecondary)
            Spacer(modifier = Modifier.height(28.dp))
            Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(28.dp),
                color = SkillSwapSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, SkillSwapBorder),
                shadowElevation = 8.dp) {
                Column(modifier = Modifier.padding(20.dp)) {
                    AuthTextField(value = email, onValueChange = { email = it; localError = "" },
                        label = "Email", placeholder = "Enter your email",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email))
                    Spacer(modifier = Modifier.height(16.dp))
                    AuthTextField(value = password, onValueChange = { password = it; localError = "" },
                        label = "Password", placeholder = "Enter your password",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        visualTransformation = PasswordVisualTransformation())
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("Forgot password?", color = SkillSwapPrimary,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.align(Alignment.End).clickable { onForgotPasswordClick() })
                    val errorToShow = localError.ifEmpty { authState.errorMessage ?: "" }
                    if (errorToShow.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(errorToShow, color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall)
                    }
                    Spacer(modifier = Modifier.height(22.dp))
                    if (authState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(40.dp).align(Alignment.CenterHorizontally),
                            color = SkillSwapPrimary)
                    } else {
                        PrimaryButton(text = "Log In", onClick = {
                            if (email.isBlank() || password.isBlank()) {
                                localError = "Please fill in all fields."
                            } else {
                                authViewModel.login(email, password) { hasProfile, token, meUser -> onLoginSuccess(hasProfile, token, meUser) }
                            }
                        }, modifier = Modifier.fillMaxWidth().height(54.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.height(22.dp))
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Don't have an account yet?", color = SkillSwapTextSecondary,
                    style = MaterialTheme.typography.bodyMedium)
                TextButton(onClick = onSignupClick) {
                    Text("Create account", color = SkillSwapPrimary, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}