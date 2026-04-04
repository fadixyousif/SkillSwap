package com.example.skillswap.ui.screens.post

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.example.skillswap.ui.components.AuthTextField
import com.example.skillswap.ui.components.PrimaryButton
import com.example.skillswap.ui.theme.SkillSwapBackground
import com.example.skillswap.ui.theme.SkillSwapBorder
import com.example.skillswap.ui.theme.SkillSwapPrimary
import com.example.skillswap.ui.theme.SkillSwapSecondary
import com.example.skillswap.ui.theme.SkillSwapSurface
import com.example.skillswap.ui.theme.SkillSwapTextSecondary
import com.example.skillswap.ui.viewmodel.ProfileViewModel

@Composable
fun CreateProfileScreen(
    profileViewModel: ProfileViewModel,
    onSaveProfileClick: () -> Unit
) {
    val savedProfile by profileViewModel.profile.collectAsState()

    var name by remember { mutableStateOf(savedProfile.name) }
    var role by remember { mutableStateOf(savedProfile.role) }
    var program by remember { mutableStateOf(savedProfile.program) }
    var college by remember { mutableStateOf(savedProfile.college) }
    var location by remember { mutableStateOf(savedProfile.location) }
    var bio by remember { mutableStateOf(savedProfile.bio) }
    var errorMessage by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SkillSwapBackground)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            SkillSwapPrimary.copy(alpha = 0.20f),
                            SkillSwapSecondary.copy(alpha = 0.08f),
                            SkillSwapBackground
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(36.dp))

            Text(
                text = "Create your profile",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Tell others who you are and what kind of work you do.",
                style = MaterialTheme.typography.bodyLarge,
                color = SkillSwapTextSecondary
            )

            Spacer(modifier = Modifier.height(24.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                color = SkillSwapSurface,
                border = BorderStroke(1.dp, SkillSwapBorder),
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    AuthTextField(
                        value = name,
                        onValueChange = {
                            name = it
                            errorMessage = ""
                        },
                        label = "Full Name",
                        placeholder = "Enter your name",
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Words
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    AuthTextField(
                        value = role,
                        onValueChange = {
                            role = it
                            errorMessage = ""
                        },
                        label = "Role",
                        placeholder = "e.g. Frontend Developer",
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Words
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    AuthTextField(
                        value = program,
                        onValueChange = {
                            program = it
                            errorMessage = ""
                        },
                        label = "Program",
                        placeholder = "e.g. Software Development",
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Words
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    AuthTextField(
                        value = college,
                        onValueChange = {
                            college = it
                            errorMessage = ""
                        },
                        label = "College",
                        placeholder = "e.g. Bow Valley College",
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Words
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    AuthTextField(
                        value = location,
                        onValueChange = {
                            location = it
                            errorMessage = ""
                        },
                        label = "Location",
                        placeholder = "e.g. Calgary, Alberta",
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Words
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    AuthTextField(
                        value = bio,
                        onValueChange = {
                            bio = it
                            errorMessage = ""
                        },
                        label = "Short Bio",
                        placeholder = "Tell people a little about yourself",
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences
                        )
                    )

                    if (errorMessage.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(modifier = Modifier.height(22.dp))

                    PrimaryButton(
                        text = "Continue",
                        onClick = {
                            if (
                                name.isBlank() ||
                                role.isBlank() ||
                                program.isBlank() ||
                                college.isBlank() ||
                                location.isBlank() ||
                                bio.isBlank()
                            ) {
                                errorMessage = "Please fill in all fields."
                            } else {
                                profileViewModel.saveBasicProfile(
                                    name = name,
                                    role = role,
                                    program = program,
                                    college = college,
                                    location = location,
                                    bio = bio
                                )
                                onSaveProfileClick()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}