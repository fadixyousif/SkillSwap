package com.example.skillswap.ui.screens.discover

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.skillswap.data.model.ProfileDto
import com.example.skillswap.ui.theme.*
import com.example.skillswap.ui.viewmodel.DiscoverViewModel
import com.example.skillswap.ui.viewmodel.ProfileViewModel

@Composable
fun DiscoverScreen(
    profileViewModel: ProfileViewModel,
    discoverViewModel: DiscoverViewModel,
    onUserClick: (Int) -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    val discoverState by discoverViewModel.discoverState.collectAsState()

    val filteredProfiles = discoverState.profiles.filter {
        searchText.isBlank() ||
                it.fullName.contains(searchText, ignoreCase = true) ||
                (it.headlineRole?.contains(searchText, ignoreCase = true) == true) ||
                it.skills.any { skill -> skill.skillName.contains(searchText, ignoreCase = true) }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
        contentPadding = PaddingValues(top = 20.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            Text("Discover", style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground)
            Text("Explore students and creatives ready to swap skills.",
                style = MaterialTheme.typography.bodyMedium, color = SkillSwapTextSecondary,
                modifier = Modifier.padding(top = 6.dp, bottom = 18.dp))

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(value = searchText, onValueChange = { searchText = it },
                    placeholder = { Text("Search skills or roles") },
                    leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = "Search") },
                    modifier = Modifier.weight(1f), singleLine = true,
                    shape = RoundedCornerShape(18.dp), colors = discoverFieldColors())
                Spacer(modifier = Modifier.width(12.dp))
                Surface(modifier = Modifier.size(56.dp), shape = RoundedCornerShape(18.dp),
                    color = SkillSwapSurfaceAlt, tonalElevation = 2.dp, shadowElevation = 4.dp) {
                    IconButton(onClick = { }) {
                        Icon(Icons.Outlined.Tune, contentDescription = "Filter", tint = SkillSwapPrimary)
                    }
                }
            }
            Spacer(modifier = Modifier.height(22.dp))
            Text("Recommended for you", style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold)
        }

        if (discoverState.isLoading) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(32.dp),
                    contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = SkillSwapPrimary)
                }
            }
        } else if (discoverState.errorMessage != null) {
            item {
                Text("Could not load profiles: ${discoverState.errorMessage}",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp))
            }
        } else {
            itemsIndexed(filteredProfiles) { _, profile ->
                DiscoverProfileCard(profile = profile, onCardClick = { onUserClick(profile.userId) })
            }

            if (filteredProfiles.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().wrapContentHeight()
                        .background(color = SkillSwapSurfaceAlt, shape = RoundedCornerShape(20.dp))
                        .padding(18.dp)) {
                        Text("No matching users found.", style = MaterialTheme.typography.bodyMedium,
                            color = SkillSwapTextSecondary)
                    }
                }
            }
        }
    }
}

@Composable
private fun DiscoverProfileCard(profile: ProfileDto, onCardClick: () -> Unit) {
    val offeredSkills = profile.skills.filter { it.type == "OFFER" }.map { it.skillName }
    val neededSkills = profile.skills.filter { it.type == "NEED" }.map { it.skillName }

    Card(modifier = Modifier.fillMaxWidth().clickable { onCardClick() },
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = SkillSwapSurface),
        border = BorderStroke(1.dp, SkillSwapBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Box(modifier = Modifier.size(68.dp).clip(CircleShape)
                    .background(SkillSwapSecondary.copy(alpha = 0.85f)),
                    contentAlignment = Alignment.Center) {
                    Text(profile.fullName.take(1), style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(profile.fullName, style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface)
                    if (profile.headlineRole != null) {
                        Text(profile.headlineRole, style = MaterialTheme.typography.bodyMedium,
                            color = SkillSwapPrimary, modifier = Modifier.padding(top = 4.dp))
                    }
                    if (profile.program != null || profile.college != null) {
                        Text("${profile.program ?: ""} • ${profile.college ?: ""}",
                            style = MaterialTheme.typography.bodySmall, color = SkillSwapTextSecondary,
                            modifier = Modifier.padding(top = 4.dp))
                    }
                    if (profile.location != null) {
                        Text(profile.location, style = MaterialTheme.typography.bodySmall,
                            color = SkillSwapTextSecondary, modifier = Modifier.padding(top = 2.dp))
                    }
                }
            }

            if (profile.bio != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(profile.bio, style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface)
            }

            if (offeredSkills.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text("Offers", style = MaterialTheme.typography.titleMedium, color = SkillSwapSecondary)
                Spacer(modifier = Modifier.height(8.dp))
                SkillChipRow(skills = offeredSkills)
            }

            if (neededSkills.isNotEmpty()) {
                Spacer(modifier = Modifier.height(14.dp))
                Text("Needs", style = MaterialTheme.typography.titleMedium, color = SkillSwapSecondary)
                Spacer(modifier = Modifier.height(8.dp))
                SkillChipRow(skills = neededSkills)
            }

            Spacer(modifier = Modifier.height(18.dp))
            Button(onClick = onCardClick, modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SkillSwapPrimary,
                    contentColor = MaterialTheme.colorScheme.onPrimary)) {
                Icon(Icons.Outlined.SwapHoriz, contentDescription = "View Profile")
                Spacer(modifier = Modifier.width(8.dp))
                Text("View Profile", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@Composable
private fun SkillChipRow(skills: List<String>) {
    Column {
        skills.chunked(3).forEach { rowSkills ->
            Row(modifier = Modifier.fillMaxWidth()) {
                rowSkills.forEach { skill ->
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
    }
}

@Composable
private fun discoverFieldColors() = TextFieldDefaults.colors(
    focusedContainerColor = SkillSwapSurface, unfocusedContainerColor = SkillSwapSurface,
    disabledContainerColor = SkillSwapSurface,
    focusedTextColor = MaterialTheme.colorScheme.onSurface,
    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
    focusedIndicatorColor = SkillSwapPrimary, unfocusedIndicatorColor = SkillSwapBorder,
    cursorColor = SkillSwapPrimary
)