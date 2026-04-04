package com.example.skillswap.ui.screens.requests

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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

data class SwapRequestUi(
    val id: Int,
    val name: String,
    val role: String,
    val message: String,
    val offeredSkill: String,
    val neededSkill: String,
    val status: String
)

@Composable
fun RequestsScreen() {
    val requests = remember {
        listOf(
            SwapRequestUi(
                id = 1,
                name = "Sarah Jenkins",
                role = "Frontend Developer",
                message = "Hey, I can help with frontend in exchange for logo design support.",
                offeredSkill = "HTML/CSS/JavaScript",
                neededSkill = "Logo Design",
                status = "Pending"
            ),
            SwapRequestUi(
                id = 2,
                name = "Leila Chen",
                role = "Content Creator",
                message = "I’d love help with a portfolio site and can offer content writing in return.",
                offeredSkill = "Content Writing",
                neededSkill = "Portfolio Website",
                status = "Pending"
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SkillSwapBackground)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Requests",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Review incoming skill swap requests and decide what to do next.",
            style = MaterialTheme.typography.bodyMedium,
            color = SkillSwapTextSecondary
        )

        Spacer(modifier = Modifier.height(18.dp))

        if (requests.isEmpty()) {
            EmptyRequestsState()
        } else {
            LazyColumn(
                contentPadding = PaddingValues(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(requests) { request ->
                    RequestCard(request = request)
                }
            }
        }
    }
}

@Composable
private fun EmptyRequestsState() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = SkillSwapSurfaceAlt,
        border = BorderStroke(1.dp, SkillSwapBorder)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "No swap requests yet",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "When you connect with someone, your requests will appear here.",
                style = MaterialTheme.typography.bodyMedium,
                color = SkillSwapTextSecondary
            )
        }
    }
}

@Composable
private fun RequestCard(request: SwapRequestUi) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
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
                            text = request.name.take(1),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = request.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = request.role,
                        style = MaterialTheme.typography.bodyMedium,
                        color = SkillSwapPrimary
                    )
                }

                Surface(
                    shape = RoundedCornerShape(50.dp),
                    color = SkillSwapPrimary.copy(alpha = 0.14f),
                    border = BorderStroke(1.dp, SkillSwapPrimary.copy(alpha = 0.22f))
                ) {
                    Text(
                        text = request.status,
                        style = MaterialTheme.typography.labelLarge,
                        color = SkillSwapPrimary,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = request.message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            RequestSkillLine(
                label = "Offers",
                value = request.offeredSkill
            )

            Spacer(modifier = Modifier.height(10.dp))

            RequestSkillLine(
                label = "Needs",
                value = request.neededSkill
            )

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(18.dp),
                    border = BorderStroke(1.dp, SkillSwapBorder),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = "Decline"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Decline")
                }

                Button(
                    onClick = { },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SkillSwapPrimary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Check,
                        contentDescription = "Accept"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Accept")
                }
            }
        }
    }
}

@Composable
private fun RequestSkillLine(
    label: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.SwapHoriz,
            contentDescription = null,
            tint = SkillSwapSecondary
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = "$label: ",
            style = MaterialTheme.typography.bodyMedium,
            color = SkillSwapSecondary,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}