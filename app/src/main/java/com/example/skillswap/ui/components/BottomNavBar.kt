package com.example.skillswap.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PostAdd
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.skillswap.ui.navigation.BottomNavItem
import com.example.skillswap.ui.navigation.NavRoutes
import com.example.skillswap.ui.theme.SkillSwapPrimary
import com.example.skillswap.ui.theme.SkillSwapSurface
import com.example.skillswap.ui.theme.SkillSwapSurfaceAlt
import com.example.skillswap.ui.theme.SkillSwapTextSecondary

@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(
        BottomNavItem(
            route = NavRoutes.DISCOVER,
            label = "Discover",
            icon = Icons.Outlined.Explore
        ),
        BottomNavItem(
            route = NavRoutes.REQUESTS,
            label = "Requests",
            icon = Icons.Outlined.SwapHoriz
        ),
        BottomNavItem(
            route = NavRoutes.POSTS,
            label = "Posts",
            icon = Icons.Outlined.PostAdd
        ),
        BottomNavItem(
            route = NavRoutes.MESSAGES,
            label = "Messages",
            icon = Icons.Outlined.ChatBubbleOutline
        ),
        BottomNavItem(
            route = NavRoutes.PROFILE,
            label = "Profile",
            icon = Icons.Outlined.Person
        )
    )

    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    NavigationBar(
        modifier = Modifier.height(84.dp),
        containerColor = SkillSwapSurface,
        tonalElevation = 0.dp
    ) {
        items.forEach { item ->
            val selected = currentRoute == item.route

            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                alwaysShowLabel = true,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = SkillSwapPrimary,
                    selectedTextColor = MaterialTheme.colorScheme.onSurface,
                    unselectedIconColor = SkillSwapTextSecondary,
                    unselectedTextColor = SkillSwapTextSecondary,
                    indicatorColor = SkillSwapSurfaceAlt
                )
            )
        }
    }
}