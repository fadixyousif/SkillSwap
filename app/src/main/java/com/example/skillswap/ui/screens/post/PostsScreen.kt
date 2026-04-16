package com.example.skillswap.ui.screens.posts

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.skillswap.ui.theme.*
import com.example.skillswap.ui.viewmodel.PostsViewModel
import com.example.skillswap.ui.viewmodel.ProfileViewModel

@Composable
fun PostsScreen(
    profileViewModel: ProfileViewModel,
    postsViewModel: PostsViewModel,
    authToken: String,
    onProfileClick: (Int) -> Unit
) {
    val postsState by postsViewModel.uiState.collectAsState()
    var content by remember { mutableStateOf("") }
    var offeredSkill by remember { mutableStateOf("") }
    var neededSkill by remember { mutableStateOf("") }
    var postError by remember { mutableStateOf("") }

    LaunchedEffect(Unit) { postsViewModel.loadPosts() }

    Column(
        modifier = Modifier.fillMaxSize().background(SkillSwapBackground).padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text("Posts", style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onBackground)
        Spacer(modifier = Modifier.height(6.dp))
        Text("See what people are looking for and connect through public posts.",
            style = MaterialTheme.typography.bodyMedium, color = SkillSwapTextSecondary)
        Spacer(modifier = Modifier.height(16.dp))

        // Create post card - only shown when logged in
        if (authToken.isNotEmpty()) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SkillSwapSurface),
                border = BorderStroke(1.dp, SkillSwapBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Create a post", fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface)
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(value = content, onValueChange = { content = it; postError = "" },
                        label = { Text("What do you need or offer?") },
                        modifier = Modifier.fillMaxWidth(), minLines = 2)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = offeredSkill, onValueChange = { offeredSkill = it; postError = "" },
                        label = { Text("Offered Skill") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = neededSkill, onValueChange = { neededSkill = it; postError = "" },
                        label = { Text("Needed Skill") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                    if (postError.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(postError, color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            if (content.isBlank()) { postError = "Please write something."; return@Button }
                            if (offeredSkill.isBlank()) { postError = "Enter the skill you offer."; return@Button }
                            if (neededSkill.isBlank()) { postError = "Enter the skill you need."; return@Button }
                            postsViewModel.createPost(
                                token = authToken,
                                content = content,
                                offeredSkill = offeredSkill,
                                neededSkill = neededSkill
                            )
                            content = ""; offeredSkill = ""; neededSkill = ""; postError = ""
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = SkillSwapPrimary)
                    ) { Text("Post") }
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
        }

        when {
            postsState.isLoading -> {
                Box(modifier = Modifier.fillMaxWidth().padding(32.dp),
                    contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = SkillSwapPrimary)
                }
            }
            postsState.errorMessage != null -> {
                Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp),
                    color = SkillSwapSurfaceAlt, border = BorderStroke(1.dp, SkillSwapBorder)) {
                    Text("Could not load posts: ${postsState.errorMessage}",
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium)
                }
            }
            postsState.posts.isEmpty() -> {
                Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp),
                    color = SkillSwapSurfaceAlt, border = BorderStroke(1.dp, SkillSwapBorder)) {
                    Text("No posts yet. Be the first to post!",
                        modifier = Modifier.padding(16.dp), color = SkillSwapTextSecondary)
                }
            }
            else -> {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)) {
                    items(postsState.posts) { post ->
                        PostCard(
                            authorName = post.author.fullName,
                            authorRole = post.author.role ?: "",
                            authorUserId = post.author.id,
                            content = post.content,
                            offeredSkill = post.offeredSkill,
                            neededSkill = post.neededSkill,
                            onProfileClick = { onProfileClick(post.author.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PostCard(
    authorName: String,
    authorRole: String,
    authorUserId: Int,
    content: String,
    offeredSkill: String,
    neededSkill: String,
    onProfileClick: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = SkillSwapSurface),
        border = BorderStroke(1.dp, SkillSwapBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(modifier = Modifier.clickable { onProfileClick() },
                verticalAlignment = Alignment.CenterVertically) {
                Surface(modifier = Modifier.size(50.dp), shape = CircleShape,
                    color = SkillSwapSecondary.copy(alpha = 0.85f)) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(authorName.take(1), fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.titleMedium)
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(authorName, fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface)
                    if (authorRole.isNotBlank())
                        Text(authorRole, color = SkillSwapPrimary,
                            style = MaterialTheme.typography.bodySmall)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(content, style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface)
            Spacer(modifier = Modifier.height(12.dp))
            Text("Offers: $offeredSkill", color = SkillSwapSecondary,
                fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodySmall)
            Text("Needs: $neededSkill", color = SkillSwapTextSecondary,
                style = MaterialTheme.typography.bodySmall)
        }
    }
}