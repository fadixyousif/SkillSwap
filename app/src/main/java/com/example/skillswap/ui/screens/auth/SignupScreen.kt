package com.example.skillswap.ui.screens.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.skillswap.ui.components.AuthTextField
import com.example.skillswap.ui.components.PrimaryButton
import com.example.skillswap.ui.theme.*
import com.example.skillswap.ui.viewmodel.AuthViewModel

@Composable
fun SignupScreen(
    authViewModel: AuthViewModel,
    onSignupSuccess: () -> Unit,
    onBackToLoginClick: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var localError by remember { mutableStateOf("") }
    val authState by authViewModel.authState.collectAsState()

    // Clear any previous auth error when entering this screen
    LaunchedEffect(Unit) { authViewModel.clearError() }

    Box(modifier = Modifier.fillMaxSize().background(SkillSwapBackground)) {
        Box(modifier = Modifier.fillMaxWidth().height(280.dp).background(
            brush = Brush.verticalGradient(colors = listOf(
                SkillSwapPrimary.copy(alpha = 0.22f),
                SkillSwapSecondary.copy(alpha = 0.10f),
                SkillSwapBackground))))

        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp, vertical = 28.dp),
            verticalArrangement = Arrangement.Center) {

            Text("Create account", style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.ExtraBold)
            Spacer(modifier = Modifier.height(10.dp))
            Text("Join SkillSwap and start building connections through real skill exchange.",
                style = MaterialTheme.typography.bodyLarge, color = SkillSwapTextSecondary)
            Spacer(modifier = Modifier.height(28.dp))

            Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(28.dp),
                color = SkillSwapSurface, border = BorderStroke(1.dp, SkillSwapBorder),
                shadowElevation = 8.dp) {
                Column(modifier = Modifier.padding(20.dp)) {
                    AuthTextField(value = fullName, onValueChange = { fullName = it; localError = "" },
                        label = "Full Name", placeholder = "Enter your full name",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text))
                    Spacer(modifier = Modifier.height(14.dp))
                    AuthTextField(value = email, onValueChange = { email = it; localError = "" },
                        label = "Email", placeholder = "Enter your email",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email))
                    Spacer(modifier = Modifier.height(14.dp))
                    AuthTextField(value = password, onValueChange = { password = it; localError = "" },
                        label = "Password", placeholder = "Create a password (min 6 chars)",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        visualTransformation = PasswordVisualTransformation())
                    Spacer(modifier = Modifier.height(14.dp))
                    AuthTextField(value = confirmPassword, onValueChange = { confirmPassword = it; localError = "" },
                        label = "Confirm Password", placeholder = "Re-enter your password",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        visualTransformation = PasswordVisualTransformation())

                    val errorToShow = localError.ifEmpty { authState.errorMessage ?: "" }
                    if (errorToShow.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(errorToShow, color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall)
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                    if (authState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(40.dp).align(Alignment.CenterHorizontally),
                            color = SkillSwapPrimary)
                    } else {
                        PrimaryButton(text = "Create Account", onClick = {
                            when {
                                fullName.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank() ->
                                    localError = "Please fill in all fields."
                                password != confirmPassword -> localError = "Passwords do not match."
                                password.length < 6 -> localError = "Password must be at least 6 characters."
                                else -> authViewModel.signup(fullName, email, password) { onSignupSuccess() }
                            }
                        }, modifier = Modifier.fillMaxWidth().height(54.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Already have an account?", color = SkillSwapTextSecondary,
                    style = MaterialTheme.typography.bodyMedium)
                TextButton(onClick = onBackToLoginClick) {
                    Text("Back to login", color = SkillSwapPrimary, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}