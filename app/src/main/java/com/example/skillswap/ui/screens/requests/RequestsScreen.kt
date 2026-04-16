package com.example.skillswap.ui.screens.requests

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.skillswap.ui.theme.*
import com.example.skillswap.ui.viewmodel.RequestViewModel

@Composable
fun RequestsScreen(
    requestViewModel: RequestViewModel,
    authToken: String
) {
    val requestState by requestViewModel.requestState.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Incoming", "Outgoing")

    LaunchedEffect(authToken) {
        if (authToken.isNotEmpty()) {
            requestViewModel.loadIncomingRequests(authToken)
            requestViewModel.loadOutgoingRequests(authToken)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(SkillSwapBackground).padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text("Requests", style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.ExtraBold)
        Spacer(modifier = Modifier.height(6.dp))
        Text("Manage your incoming and outgoing skill swap requests.",
            style = MaterialTheme.typography.bodyMedium, color = SkillSwapTextSecondary)
        Spacer(modifier = Modifier.height(16.dp))

        if (authToken.isEmpty()) {
            Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp),
                color = SkillSwapSurfaceAlt, border = BorderStroke(1.dp, SkillSwapBorder)) {
                Text("Please log in to see your requests.",
                    modifier = Modifier.padding(20.dp), color = SkillSwapTextSecondary)
            }
            return@Column
        }

        // Tab row
        TabRow(selectedTabIndex = selectedTab,
            containerColor = SkillSwapSurface,
            contentColor = SkillSwapPrimary) {
            tabs.forEachIndexed { index, title ->
                Tab(selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title, fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when {
            requestState.isLoading -> {
                Box(modifier = Modifier.fillMaxWidth().padding(40.dp),
                    contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = SkillSwapPrimary)
                }
            }
            else -> {
                if (selectedTab == 0) {
                    // INCOMING
                    val incoming = requestState.incomingRequests
                    if (incoming.isEmpty()) {
                        EmptyState("No incoming requests yet.",
                            "When someone sends you a swap request, it'll appear here.")
                    } else {
                        LazyColumn(contentPadding = PaddingValues(bottom = 24.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)) {
                            items(incoming) { req ->
                                IncomingRequestCard(
                                    name = req.fromUserName,
                                    offeredSkill = req.offeredSkill,
                                    neededSkill = req.requestedSkill,
                                    message = req.message ?: "",
                                    status = req.status,
                                    onAccept = { requestViewModel.acceptRequest(authToken, req.requestId) },
                                    onDecline = { requestViewModel.declineRequest(authToken, req.requestId) }
                                )
                            }
                        }
                    }
                } else {
                    // OUTGOING
                    val outgoing = requestState.outgoingRequests
                    if (outgoing.isEmpty()) {
                        EmptyState("No outgoing requests yet.",
                            "Requests you send to others will appear here.")
                    } else {
                        LazyColumn(contentPadding = PaddingValues(bottom = 24.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)) {
                            items(outgoing) { req ->
                                OutgoingRequestCard(
                                    name = req.toUserName,
                                    offeredSkill = req.offeredSkill,
                                    neededSkill = req.requestedSkill,
                                    status = req.status
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyState(title: String, subtitle: String) {
    Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp),
        color = SkillSwapSurfaceAlt, border = BorderStroke(1.dp, SkillSwapBorder)) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))
            Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = SkillSwapTextSecondary)
        }
    }
}

@Composable
private fun IncomingRequestCard(
    name: String, offeredSkill: String, neededSkill: String,
    message: String, status: String,
    onAccept: () -> Unit, onDecline: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = SkillSwapSurface),
        border = BorderStroke(1.dp, SkillSwapBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(modifier = Modifier.size(52.dp), shape = CircleShape,
                    color = SkillSwapSecondary.copy(alpha = 0.85f)) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(name.take(1), style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(name, fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface)
                    Text("Wants to swap with you", style = MaterialTheme.typography.bodySmall,
                        color = SkillSwapTextSecondary)
                }
                StatusBadge(status)
            }
            Spacer(modifier = Modifier.height(14.dp))
            SkillRow("Offers", offeredSkill)
            Spacer(modifier = Modifier.height(6.dp))
            SkillRow("Needs", neededSkill)
            if (message.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Text("\"$message\"", style = MaterialTheme.typography.bodySmall,
                    color = SkillSwapTextSecondary)
            }
            if (status.equals("pending", ignoreCase = true)) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedButton(onClick = onDecline, modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, SkillSwapBorder)) {
                        Icon(Icons.Outlined.Close, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Decline")
                    }
                    Button(onClick = onAccept, modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SkillSwapPrimary)) {
                        Icon(Icons.Outlined.Check, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Accept")
                    }
                }
            }
        }
    }
}

@Composable
private fun OutgoingRequestCard(
    name: String, offeredSkill: String, neededSkill: String, status: String
) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = SkillSwapSurface),
        border = BorderStroke(1.dp, SkillSwapBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(modifier = Modifier.size(52.dp), shape = CircleShape,
                    color = SkillSwapPrimary.copy(alpha = 0.8f)) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(name.take(1), style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(name, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    Text("You sent a request", style = MaterialTheme.typography.bodySmall,
                        color = SkillSwapTextSecondary)
                }
                StatusBadge(status)
            }
            Spacer(modifier = Modifier.height(14.dp))
            SkillRow("You offer", offeredSkill)
            Spacer(modifier = Modifier.height(6.dp))
            SkillRow("You need", neededSkill)
        }
    }
}

@Composable
private fun StatusBadge(status: String) {
    val color = when (status.lowercase()) {
        "accepted" -> SkillSwapPrimary
        "declined" -> MaterialTheme.colorScheme.error
        else -> SkillSwapSecondary
    }
    Surface(shape = RoundedCornerShape(50.dp), color = color.copy(alpha = 0.14f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.3f))) {
        Text(status.replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.labelMedium, color = color,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp))
    }
}

@Composable
private fun SkillRow(label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Outlined.SwapHoriz, contentDescription = null,
            tint = SkillSwapSecondary, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text("$label: ", style = MaterialTheme.typography.bodySmall,
            color = SkillSwapSecondary, fontWeight = FontWeight.Bold)
        Text(value, style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface)
    }
}