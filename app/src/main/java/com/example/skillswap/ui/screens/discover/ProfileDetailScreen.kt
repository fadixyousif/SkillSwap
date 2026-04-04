package com.example.skillswap.ui.screens.discover

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

data class ProfileDetailUi(
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileDetailScreen(
    userId: Int,
    onBackClick: () -> Unit,
    profileViewModel: ProfileViewModel
) {
    val users = remember {
        listOf(
            ProfileDetailUi(
                id = 1,
                name = "Marcus Thorne",
                role = "Senior UI/UX Strategy Lead",
                program = "Digital Product Design",
                college = "Bow Valley College",
                location = "Calgary, Alberta",
                about = "Helping early-stage ideas turn into polished digital experiences. I love collaborating with developers, product thinkers, and creators who want to build strong portfolio projects together.",
                offeredSkills = listOf("UI Design", "Figma", "Branding"),
                neededSkills = listOf("React", "Backend", "Motion Design")
            ),
            ProfileDetailUi(
                id = 2,
                name = "Sarah Jenkins",
                role = "Frontend Developer",
                program = "Software Development",
                college = "SAIT",
                location = "Calgary, Alberta",
                about = "Passionate about building responsive and user-friendly websites. Looking to work with designers and writers on small projects that help both sides grow.",
                offeredSkills = listOf("HTML", "CSS", "JavaScript"),
                neededSkills = listOf("Logo Design", "UX Writing")
            ),
            ProfileDetailUi(
                id = 3,
                name = "Leila Chen",
                role = "Content Creator",
                program = "Digital Communications",
                college = "University of Calgary",
                location = "Calgary, Alberta",
                about = "I create engaging digital content and want to collaborate with developers and designers. I enjoy projects that mix storytelling, visuals, and practical digital execution.",
                offeredSkills = listOf("Photography", "Video Editing", "Content Writing"),
                neededSkills = listOf("Portfolio Website", "SEO")
            )
        )
    }

    val user = users.find { it.id == userId } ?: users.first()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SkillSwapBackground)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = SkillSwapSurfaceAlt,
                border = BorderStroke(1.dp, SkillSwapBorder)
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Outlined.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "Profile Details",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.ExtraBold
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = SkillSwapSurface),
            border = BorderStroke(1.dp, SkillSwapBorder),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.Top
                ) {
                    Surface(
                        modifier = Modifier.size(76.dp),
                        shape = CircleShape,
                        color = SkillSwapSecondary.copy(alpha = 0.85f)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = user.name.take(1),
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = user.name,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = user.role,
                            style = MaterialTheme.typography.bodyMedium,
                            color = SkillSwapPrimary,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Outlined.School,
                                contentDescription = null,
                                tint = SkillSwapTextSecondary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${user.program} • ${user.college}",
                                style = MaterialTheme.typography.bodySmall,
                                color = SkillSwapTextSecondary
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Outlined.LocationOn,
                                contentDescription = null,
                                tint = SkillSwapTextSecondary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = user.location,
                                style = MaterialTheme.typography.bodySmall,
                                color = SkillSwapTextSecondary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "About",
                    style = MaterialTheme.typography.titleMedium,
                    color = SkillSwapSecondary,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = user.about,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Skills Offered",
                    style = MaterialTheme.typography.titleMedium,
                    color = SkillSwapSecondary,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    user.offeredSkills.forEach { skill ->
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

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Skills Needed",
                    style = MaterialTheme.typography.titleMedium,
                    color = SkillSwapSecondary,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    user.neededSkills.forEach { skill ->
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

                Button(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SkillSwapPrimary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Outlined.SwapHoriz,
                        contentDescription = "Request Swap"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Request Swap",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}