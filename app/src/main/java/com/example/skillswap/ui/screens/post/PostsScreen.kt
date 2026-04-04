package com.example.skillswap.ui.screens.posts

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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

data class CommunityPostUi(
    val id: Int,
    val userId: Int,
    val name: String,
    val role: String,
    val content: String,
    val offeredSkill: String,
    val neededSkill: String
)

@Composable
fun PostsScreen(
    profileViewModel: ProfileViewModel,
    onProfileClick: (Int) -> Unit
) {
    val profile by profileViewModel.profile.collectAsState()

    val staticPosts = remember {
        listOf(
            CommunityPostUi(
                id = 1,
                userId = 1,
                name = "Marcus Thorne",
                role = "Senior UI/UX Strategy Lead",
                content = "Looking for someone strong in React to help turn a design concept into a polished portfolio-ready build. I can help with UI, branding, and product thinking in return.",
                offeredSkill = "UI Design",
                neededSkill = "React"
            ),
            CommunityPostUi(
                id = 2,
                userId = 2,
                name = "Sarah Jenkins",
                role = "Frontend Developer",
                content = "Need help with logo design for a personal project. I can offer frontend support and responsive landing page work in return.",
                offeredSkill = "Frontend Development",
                neededSkill = "Logo Design"
            )
        )
    }

    val currentUserPost = if (
        profile.name.isNotBlank() &&
        profile.offeredSkills.isNotEmpty() &&
        profile.neededSkills.isNotEmpty()
    ) {
        CommunityPostUi(
            id = 999,
            userId = 999,
            name = profile.name,
            role = profile.role,
            content = "Hey, I’m looking for help with ${profile.neededSkills.first()} and I can help you with ${profile.offeredSkills.first()} in return. Feel free to connect.",
            offeredSkill = profile.offeredSkills.first(),
            neededSkill = profile.neededSkills.first()
        )
    } else null

    val posts = if (currentUserPost != null) listOf(currentUserPost) + staticPosts else staticPosts

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SkillSwapBackground)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Posts",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "See what people are looking for and connect through public posts.",
            style = MaterialTheme.typography.bodyMedium,
            color = SkillSwapTextSecondary
        )

        Spacer(modifier = Modifier.height(18.dp))

        LazyColumn(
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(posts) { post ->
                PostCard(
                    post = post,
                    onProfileClick = { onProfileClick(post.userId) }
                )
            }
        }
    }
}

@Composable
private fun PostCard(
    post: CommunityPostUi,
    onProfileClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = SkillSwapSurface),
        border = BorderStroke(1.dp, SkillSwapBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier.clickable { onProfileClick() }
            ) {
                Surface(
                    modifier = Modifier.size(60.dp),
                    shape = CircleShape,
                    color = SkillSwapSecondary.copy(alpha = 0.85f)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = post.name.take(1),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = post.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = post.role,
                        style = MaterialTheme.typography.bodyMedium,
                        color = SkillSwapPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = post.content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Outlined.SwapHoriz,
                    contentDescription = null,
                    tint = SkillSwapSecondary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Offers: ${post.offeredSkill}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SkillSwapSecondary,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Needs: ${post.neededSkill}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}