package com.example.skillswap.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.skillswap.ui.components.BottomNavBar
import com.example.skillswap.ui.screens.auth.ForgotPasswordScreen
import com.example.skillswap.ui.screens.auth.LoginScreen
import com.example.skillswap.ui.screens.auth.SignupScreen
import com.example.skillswap.ui.screens.discover.DiscoverScreen
import com.example.skillswap.ui.screens.discover.ProfileDetailScreen
import com.example.skillswap.ui.screens.messages.ChatScreen
import com.example.skillswap.ui.screens.messages.MessagesScreen
import com.example.skillswap.ui.screens.onboarding.WelcomeScreen
import com.example.skillswap.ui.screens.posts.PostsScreen
import com.example.skillswap.ui.screens.post.AddSkillsScreen
import com.example.skillswap.ui.screens.post.CreateProfileScreen
import com.example.skillswap.ui.screens.profile.MyProfileScreen
import com.example.skillswap.ui.screens.requests.RequestsScreen
import com.example.skillswap.ui.viewmodel.ChatViewModel
import com.example.skillswap.ui.viewmodel.ProfileViewModel

@Composable
fun SkillSwapNavGraph() {
    val navController = rememberNavController()
    val profileViewModel: ProfileViewModel = viewModel()
    val chatViewModel: ChatViewModel = viewModel()

    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    val showBottomBar = currentRoute in listOf(
        NavRoutes.DISCOVER,
        NavRoutes.REQUESTS,
        NavRoutes.POSTS,
        NavRoutes.MESSAGES,
        NavRoutes.PROFILE
    )

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(navController = navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavRoutes.LOGIN,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(NavRoutes.LOGIN) {
                LoginScreen(
                    onLoginClick = {
                        navController.navigate(NavRoutes.WELCOME) {
                            popUpTo(NavRoutes.LOGIN) { inclusive = true }
                        }
                    },
                    onSignupClick = {
                        navController.navigate(NavRoutes.SIGNUP)
                    },
                    onForgotPasswordClick = {
                        navController.navigate(NavRoutes.FORGOT_PASSWORD)
                    }
                )
            }

            composable(NavRoutes.SIGNUP) {
                SignupScreen(
                    onSignupSuccess = {
                        navController.navigate(NavRoutes.WELCOME) {
                            popUpTo(NavRoutes.LOGIN) { inclusive = true }
                        }
                    },
                    onBackToLoginClick = {
                        navController.popBackStack()
                    }
                )
            }

            composable(NavRoutes.FORGOT_PASSWORD) {
                ForgotPasswordScreen(
                    onBackToLoginClick = {
                        navController.popBackStack()
                    }
                )
            }

            composable(NavRoutes.WELCOME) {
                WelcomeScreen(
                    onGetStartedClick = {
                        navController.navigate(NavRoutes.CREATE_PROFILE)
                    }
                )
            }

            composable(NavRoutes.CREATE_PROFILE) {
                CreateProfileScreen(
                    profileViewModel = profileViewModel,
                    onSaveProfileClick = {
                        navController.navigate(NavRoutes.ADD_SKILLS)
                    }
                )
            }

            composable(NavRoutes.ADD_SKILLS) {
                AddSkillsScreen(
                    profileViewModel = profileViewModel,
                    onSaveSkillsClick = {
                        navController.navigate(NavRoutes.DISCOVER) {
                            popUpTo(NavRoutes.WELCOME) { inclusive = true }
                        }
                    }
                )
            }

            composable(NavRoutes.DISCOVER) {
                DiscoverScreen(
                    profileViewModel = profileViewModel,
                    onUserClick = { userId ->
                        navController.navigate("${NavRoutes.PROFILE_DETAIL}/$userId")
                    }
                )
            }

            composable(NavRoutes.REQUESTS) {
                RequestsScreen()
            }

            composable(NavRoutes.POSTS) {
                PostsScreen(
                    profileViewModel = profileViewModel,
                    onProfileClick = { userId ->
                        navController.navigate("${NavRoutes.PROFILE_DETAIL}/$userId")
                    }
                )
            }

            composable(NavRoutes.MESSAGES) {
                MessagesScreen(
                    onChatClick = { chatName ->
                        navController.navigate("chat/$chatName")
                    }
                )
            }

            composable("chat/{chatName}") { backStackEntry ->
                val chatName = backStackEntry.arguments?.getString("chatName") ?: "Chat"

                ChatScreen(
                    chatName = chatName,
                    chatViewModel = chatViewModel,
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }

            composable(NavRoutes.PROFILE) {
                MyProfileScreen(profileViewModel = profileViewModel)
            }

            composable(
                route = "${NavRoutes.PROFILE_DETAIL}/{userId}",
                arguments = listOf(
                    navArgument("userId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getInt("userId") ?: 1

                ProfileDetailScreen(
                    userId = userId,
                    profileViewModel = profileViewModel,
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}