package com.example.skillswap.ui.screens.profile

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.skillswap.ui.theme.SkillSwapBackground
import com.example.skillswap.ui.theme.SkillSwapBorder
import com.example.skillswap.ui.theme.SkillSwapPrimary
import com.example.skillswap.ui.theme.SkillSwapSecondary
import com.example.skillswap.ui.theme.SkillSwapSurface
import com.example.skillswap.ui.theme.SkillSwapSurfaceAlt
import com.example.skillswap.ui.theme.SkillSwapTextSecondary
import com.example.skillswap.ui.viewmodel.ProfileViewModel

@Composable
fun MyProfileScreen(
    profileViewModel: ProfileViewModel
) {
    val profile by profileViewModel.profile.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SkillSwapBackground)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Text(
            text = "My Profile",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        if (profile.name.isBlank()) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                shape = RoundedCornerShape(20.dp),
                color = SkillSwapSurfaceAlt,
                border = BorderStroke(1.dp, SkillSwapBorder)
            ) {
                Text(
                    text = "You haven’t created your profile yet.",
                    modifier = Modifier.padding(18.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = SkillSwapTextSecondary
                )
            }
            return
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(containerColor = SkillSwapSurface),
            border = BorderStroke(1.dp, SkillSwapBorder),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.Top) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(SkillSwapSecondary.copy(alpha = 0.85f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = profile.name.take(1),
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = profile.name,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = profile.role,
                            style = MaterialTheme.typography.bodyMedium,
                            color = SkillSwapPrimary,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "${profile.program} • ${profile.college}",
                            style = MaterialTheme.typography.bodySmall,
                            color = SkillSwapTextSecondary
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = profile.location,
                            style = MaterialTheme.typography.bodySmall,
                            color = SkillSwapTextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = profile.bio,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "Skills Offered",
                    style = MaterialTheme.typography.titleMedium,
                    color = SkillSwapSecondary
                )

                Spacer(modifier = Modifier.height(10.dp))

                SkillChipRow(profile.offeredSkills)

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Skills Needed",
                    style = MaterialTheme.typography.titleMedium,
                    color = SkillSwapSecondary
                )

                Spacer(modifier = Modifier.height(10.dp))

                SkillChipRow(profile.neededSkills)
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