package com.example.skillswap.ui.screens.discover

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.skillswap.data.model.ProfileDto
import com.example.skillswap.ui.theme.*
import com.example.skillswap.ui.viewmodel.DiscoverViewModel
import com.example.skillswap.ui.viewmodel.ProfileViewModel
import com.example.skillswap.ui.viewmodel.RequestViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileDetailScreen(
    userId: Int,
    onBackClick: () -> Unit,
    profileViewModel: ProfileViewModel,
    requestViewModel: RequestViewModel,
    discoverViewModel: DiscoverViewModel? = null,
    authToken: String = ""
) {
    val requestState by requestViewModel.requestState.collectAsState()
    val discoverState = discoverViewModel?.discoverState?.collectAsState()

    // Find the real profile from the API data
    val profile: ProfileDto? = discoverState?.value?.profiles?.find { it.userId == userId }

    val offeredSkills = profile?.skills?.filter { it.type == "OFFER" }?.map { it.skillName } ?: emptyList()
    val neededSkills = profile?.skills?.filter { it.type == "NEED" }?.map { it.skillName } ?: emptyList()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SkillSwapBackground)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(shape = CircleShape, color = SkillSwapSurfaceAlt,
                border = BorderStroke(1.dp, SkillSwapBorder)) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Outlined.ArrowBack, contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface)
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text("Profile Details", style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.ExtraBold)
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (profile == null) {
            Box(modifier = Modifier.fillMaxWidth().padding(32.dp),
                contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = SkillSwapPrimary)
            }
            return@Column
        }

        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = SkillSwapSurface),
            border = BorderStroke(1.dp, SkillSwapBorder),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)) {
            Column(modifier = Modifier.padding(20.dp)) {

                Row(verticalAlignment = Alignment.Top) {
                    Surface(modifier = Modifier.size(76.dp), shape = CircleShape,
                        color = SkillSwapSecondary.copy(alpha = 0.85f)) {
                        Row(modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically) {
                            Text(profile.fullName.take(1),
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(profile.fullName, style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        if (profile.headlineRole != null) {
                            Text(profile.headlineRole, style = MaterialTheme.typography.bodyMedium,
                                color = SkillSwapPrimary, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                        if (profile.program != null || profile.college != null) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Outlined.School, contentDescription = null,
                                    tint = SkillSwapTextSecondary)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("${profile.program ?: ""} • ${profile.college ?: ""}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = SkillSwapTextSecondary)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                        }
                        if (profile.location != null) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Outlined.LocationOn, contentDescription = null,
                                    tint = SkillSwapTextSecondary)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(profile.location, style = MaterialTheme.typography.bodySmall,
                                    color = SkillSwapTextSecondary)
                            }
                        }
                    }
                }

                if (profile.bio != null) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text("About", style = MaterialTheme.typography.titleMedium,
                        color = SkillSwapSecondary, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(profile.bio, style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface)
                }

                if (offeredSkills.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text("Skills Offered", style = MaterialTheme.typography.titleMedium,
                        color = SkillSwapSecondary, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))
                    FlowRow(modifier = Modifier.fillMaxWidth()) {
                        offeredSkills.forEach { skill ->
                            Surface(modifier = Modifier.padding(end = 8.dp, bottom = 8.dp),
                                shape = RoundedCornerShape(50.dp), color = SkillSwapSurfaceAlt,
                                border = BorderStroke(1.dp, SkillSwapBorder)) {
                                Text(skill, style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp))
                            }
                        }
                    }
                }

                if (neededSkills.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Text("Skills Needed", style = MaterialTheme.typography.titleMedium,
                        color = SkillSwapSecondary, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))
                    FlowRow(modifier = Modifier.fillMaxWidth()) {
                        neededSkills.forEach { skill ->
                            Surface(modifier = Modifier.padding(end = 8.dp, bottom = 8.dp),
                                shape = RoundedCornerShape(50.dp), color = SkillSwapSurfaceAlt,
                                border = BorderStroke(1.dp, SkillSwapBorder)) {
                                Text(skill, style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp))
                            }
                        }
                    }
                }

                // Success / Error feedback
                if (requestState.successMessage != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Surface(shape = RoundedCornerShape(16.dp),
                        color = SkillSwapPrimary.copy(alpha = 0.12f),
                        border = BorderStroke(1.dp, SkillSwapPrimary.copy(alpha = 0.25f))) {
                        Text(requestState.successMessage!!,
                            modifier = Modifier.padding(12.dp), color = SkillSwapPrimary,
                            style = MaterialTheme.typography.bodyMedium)
                    }
                }

                if (requestState.errorMessage != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Surface(shape = RoundedCornerShape(16.dp), color = SkillSwapSurfaceAlt,
                        border = BorderStroke(1.dp, SkillSwapBorder)) {
                        Text(requestState.errorMessage!!, modifier = Modifier.padding(12.dp),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Request Swap button — uses real token and real userId
                Button(
                    onClick = {
                        requestViewModel.sendSwapRequest(
                            token = authToken,
                            toUserId = profile.userId,
                            offeredSkill = offeredSkills.firstOrNull() ?: "",
                            requestedSkill = neededSkills.firstOrNull() ?: ""
                        )
                    },
                    modifier = Modifier.fillMaxWidth().height(54.dp),
                    shape = RoundedCornerShape(20.dp),
                    enabled = !requestState.isLoading && authToken.isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(containerColor = SkillSwapPrimary,
                        contentColor = MaterialTheme.colorScheme.onPrimary)
                ) {
                    if (requestState.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp, color = MaterialTheme.colorScheme.onPrimary)
                    } else {
                        Icon(Icons.Outlined.SwapHoriz, contentDescription = "Request Swap")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Request Swap", style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}