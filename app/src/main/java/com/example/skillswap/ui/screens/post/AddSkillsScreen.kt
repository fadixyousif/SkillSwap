package com.example.skillswap.ui.screens.post

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.skillswap.ui.components.AuthTextField
import com.example.skillswap.ui.components.PrimaryButton
import com.example.skillswap.ui.theme.SkillSwapBackground
import com.example.skillswap.ui.theme.SkillSwapBorder
import com.example.skillswap.ui.theme.SkillSwapPrimary
import com.example.skillswap.ui.theme.SkillSwapSecondary
import com.example.skillswap.ui.theme.SkillSwapSurface
import com.example.skillswap.ui.theme.SkillSwapSurfaceAlt
import com.example.skillswap.ui.theme.SkillSwapTextSecondary
import com.example.skillswap.ui.viewmodel.ProfileViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddSkillsScreen(
    profileViewModel: ProfileViewModel,
    onSaveSkillsClick: () -> Unit
) {
    val savedProfile by profileViewModel.profile.collectAsState()

    var offeredInput by remember { mutableStateOf("") }
    var neededInput by remember { mutableStateOf("") }
    val offeredSkills = remember { mutableStateListOf<String>().apply { addAll(savedProfile.offeredSkills) } }
    val neededSkills = remember { mutableStateListOf<String>().apply { addAll(savedProfile.neededSkills) } }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        SkillSwapPrimary.copy(alpha = 0.20f),
                        SkillSwapSecondary.copy(alpha = 0.08f),
                        SkillSwapBackground,
                        SkillSwapBackground
                    )
                )
            )
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(36.dp))

        Text(
            text = "Add your skills",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "List what you can offer and what you’re looking for.",
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
            Column(modifier = Modifier.padding(20.dp)) {

                Text(
                    text = "Skills Offered",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(12.dp))

                AuthTextField(
                    value = offeredInput,
                    onValueChange = { offeredInput = it },
                    label = "Offered Skill",
                    placeholder = "e.g. UI Design",
                    keyboardOptions = KeyboardOptions.Default
                )

                Spacer(modifier = Modifier.height(10.dp))

                PrimaryButton(
                    text = "Add Offered Skill",
                    onClick = {
                        if (offeredInput.isNotBlank()) {
                            offeredSkills.add(offeredInput.trim())
                            offeredInput = ""
                            errorMessage = ""
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                )

                Spacer(modifier = Modifier.height(14.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    offeredSkills.forEach { skill ->
                        Surface(
                            modifier = Modifier.padding(end = 8.dp, bottom = 8.dp),
                            shape = RoundedCornerShape(50.dp),
                            color = SkillSwapSurfaceAlt,
                            border = BorderStroke(1.dp, SkillSwapBorder)
                        ) {
                            Text(
                                text = skill,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Skills Needed",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(12.dp))

                AuthTextField(
                    value = neededInput,
                    onValueChange = { neededInput = it },
                    label = "Needed Skill",
                    placeholder = "e.g. React",
                    keyboardOptions = KeyboardOptions.Default
                )

                Spacer(modifier = Modifier.height(10.dp))

                PrimaryButton(
                    text = "Add Needed Skill",
                    onClick = {
                        if (neededInput.isNotBlank()) {
                            neededSkills.add(neededInput.trim())
                            neededInput = ""
                            errorMessage = ""
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                )

                Spacer(modifier = Modifier.height(14.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    neededSkills.forEach { skill ->
                        Surface(
                            modifier = Modifier.padding(end = 8.dp, bottom = 8.dp),
                            shape = RoundedCornerShape(50.dp),
                            color = SkillSwapSurfaceAlt,
                            border = BorderStroke(1.dp, SkillSwapBorder)
                        ) {
                            Text(
                                text = skill,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                            )
                        }
                    }
                }

                if (errorMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                PrimaryButton(
                    text = "Finish Setup",
                    onClick = {
                        if (offeredSkills.isEmpty() || neededSkills.isEmpty()) {
                            errorMessage = "Add at least one offered skill and one needed skill."
                        } else {
                            profileViewModel.saveSkills(
                                offeredSkills = offeredSkills.toList(),
                                neededSkills = neededSkills.toList()
                            )
                            onSaveSkillsClick()
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