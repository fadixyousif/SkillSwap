package com.example.skillswap.ui.screens.discover

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.skillswap.ui.theme.SkillSwapBorder
import com.example.skillswap.ui.theme.SkillSwapPrimary
import com.example.skillswap.ui.theme.SkillSwapSecondary
import com.example.skillswap.ui.theme.SkillSwapSurface
import com.example.skillswap.ui.theme.SkillSwapSurfaceAlt
import com.example.skillswap.ui.theme.SkillSwapTextSecondary
import com.example.skillswap.ui.viewmodel.ProfileViewModel

data class DiscoverUser(
    val id: Int,
    val name: String,
    val role: String,
    val program: String,
    val college: String,
    val location: String,
    val about: String,
    val offeredSkills: List<String>,
    val neededSkills: List<String>
)

@Composable
fun DiscoverScreen(
    profileViewModel: ProfileViewModel,
    onUserClick: (Int) -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    val createdProfile by profileViewModel.profile.collectAsState()

    val staticUsers = listOf(
        DiscoverUser(
            id = 1,
            name = "Marcus Thorne",
            role = "Senior UI/UX Strategy Lead",
            program = "Digital Product Design",
            college = "Bow Valley College",
            location = "Calgary, Alberta",
            about = "Helping early-stage ideas turn into polished digital experiences.",
            offeredSkills = listOf("UI Design", "Figma", "Branding"),
            neededSkills = listOf("React", "Backend", "Motion Design")
        ),
        DiscoverUser(
            id = 2,
            name = "Sarah Jenkins",
            role = "Frontend Developer",
            program = "Software Development",
            college = "SAIT",
            location = "Calgary, Alberta",
            about = "Passionate about building responsive and user-friendly websites.",
            offeredSkills = listOf("HTML", "CSS", "JavaScript"),
            neededSkills = listOf("Logo Design", "UX Writing")
        ),
        DiscoverUser(
            id = 3,
            name = "Leila Chen",
            role = "Content Creator",
            program = "Digital Communications",
            college = "University of Calgary",
            location = "Calgary, Alberta",
            about = "I create engaging digital content and want to collaborate with developers and designers.",
            offeredSkills = listOf("Photography", "Video Editing", "Content Writing"),
            neededSkills = listOf("Portfolio Website", "SEO")
        )
    )

    val currentUser = if (createdProfile.name.isNotBlank()) {
        DiscoverUser(
            id = 999,
            name = createdProfile.name,
            role = createdProfile.role,
            program = createdProfile.program,
            college = createdProfile.college,
            location = createdProfile.location,
            about = createdProfile.bio,
            offeredSkills = createdProfile.offeredSkills,
            neededSkills = createdProfile.neededSkills
        )
    } else null

    val users = if (currentUser != null) listOf(currentUser) + staticUsers else staticUsers

    val filteredUsers = users.filter {
        searchText.isBlank() ||
                it.name.contains(searchText, ignoreCase = true) ||
                it.role.contains(searchText, ignoreCase = true) ||
                it.offeredSkills.any { skill -> skill.contains(searchText, ignoreCase = true) } ||
                it.neededSkills.any { skill -> skill.contains(searchText, ignoreCase = true) }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(top = 20.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            Text(
                text = "Discover",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = "Explore students and creatives ready to swap skills.",
                style = MaterialTheme.typography.bodyMedium,
                color = SkillSwapTextSecondary,
                modifier = Modifier.padding(top = 6.dp, bottom = 18.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    placeholder = { Text("Search skills or roles") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = "Search"
                        )
                    },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    shape = RoundedCornerShape(18.dp),
                    colors = discoverFieldColors()
                )

                Spacer(modifier = Modifier.width(12.dp))

                Surface(
                    modifier = Modifier.size(56.dp),
                    shape = RoundedCornerShape(18.dp),
                    color = SkillSwapSurfaceAlt,
                    tonalElevation = 2.dp,
                    shadowElevation = 4.dp
                ) {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Outlined.Tune,
                            contentDescription = "Filter",
                            tint = SkillSwapPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            Text(
                text = if (currentUser != null) "Recommended for you + your profile" else "Recommended for you",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )
        }

        itemsIndexed(filteredUsers) { index, user ->
            DiscoverUserCard(
                user = user,
                isCurrentUser = user.id == 999,
                onCardClick = { onUserClick(user.id) }
            )
        }

        if (filteredUsers.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .background(
                            color = SkillSwapSurfaceAlt,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(18.dp)
                ) {
                    Text(
                        text = "No matching users found.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SkillSwapTextSecondary
                    )
                }
            }
        }
    }
}

@Composable
private fun DiscoverUserCard(
    user: DiscoverUser,
    isCurrentUser: Boolean,
    onCardClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClick() },
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(
            containerColor = SkillSwapSurface
        ),
        border = BorderStroke(
            1.dp,
            if (isCurrentUser) SkillSwapPrimary.copy(alpha = 0.4f) else SkillSwapBorder
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            if (isCurrentUser) {
                Surface(
                    shape = RoundedCornerShape(50.dp),
                    color = SkillSwapPrimary.copy(alpha = 0.14f),
                    border = BorderStroke(1.dp, SkillSwapPrimary.copy(alpha = 0.22f))
                ) {
                    Text(
                        text = "Your profile",
                        color = SkillSwapPrimary,
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))
            }

            Row(
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(SkillSwapSecondary.copy(alpha = 0.85f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = user.name.take(1),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = user.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = user.role,
                        style = MaterialTheme.typography.bodyMedium,
                        color = SkillSwapPrimary,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    Text(
                        text = "${user.program} • ${user.college}",
                        style = MaterialTheme.typography.bodySmall,
                        color = SkillSwapTextSecondary,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    Text(
                        text = user.location,
                        style = MaterialTheme.typography.bodySmall,
                        color = SkillSwapTextSecondary,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = user.about,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Offers",
                style = MaterialTheme.typography.titleMedium,
                color = SkillSwapSecondary
            )

            Spacer(modifier = Modifier.height(8.dp))
            SkillChipRow(skills = user.offeredSkills)

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Needs",
                style = MaterialTheme.typography.titleMedium,
                color = SkillSwapSecondary
            )

            Spacer(modifier = Modifier.height(8.dp))
            SkillChipRow(skills = user.neededSkills)

            Spacer(modifier = Modifier.height(18.dp))

            Button(
                onClick = onCardClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SkillSwapPrimary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Icon(
                    imageVector = Icons.Outlined.SwapHoriz,
                    contentDescription = "View Profile"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "View Profile",
                    style = MaterialTheme.typography.titleMedium
                )
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
        }
    }
}

@Composable
private fun discoverFieldColors() = TextFieldDefaults.colors(
    focusedContainerColor = SkillSwapSurface,
    unfocusedContainerColor = SkillSwapSurface,
    disabledContainerColor = SkillSwapSurface,
    focusedTextColor = MaterialTheme.colorScheme.onSurface,
    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
    focusedIndicatorColor = SkillSwapPrimary,
    unfocusedIndicatorColor = SkillSwapBorder,
    cursorColor = SkillSwapPrimary
)