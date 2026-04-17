package com.example.skillswap

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.skillswap.ui.theme.SkillSwapTheme
import com.example.skillswap.ui.screens.auth.LoginScreen
import com.example.skillswap.ui.screens.onboarding.WelcomeScreen
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumentation tests for SkillSwap UI interactions.
 * Tests cover: login screen, welcome screen, app package, UI components.
 */
@RunWith(AndroidJUnit4::class)
class SkillSwapInstrumentedTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // ─── APP CONTEXT TEST ─────────────────────────────────────────────

    @Test
    fun app_hasCorrectPackageName() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.skillswap", appContext.packageName)
    }

    // ─── WELCOME SCREEN TESTS ─────────────────────────────────────────

    @Test
    fun welcomeScreen_displaysGetStartedButton() {
        composeTestRule.setContent {
            SkillSwapTheme {
                WelcomeScreen(onGetStartedClick = {})
            }
        }
        composeTestRule.onNodeWithText("Get Started").assertIsDisplayed()
    }

    @Test
    fun welcomeScreen_getStartedButton_isClickable() {
        var clicked = false
        composeTestRule.setContent {
            SkillSwapTheme {
                WelcomeScreen(onGetStartedClick = { clicked = true })
            }
        }
        composeTestRule.onNodeWithText("Get Started").performClick()
        assertEquals(true, clicked)
    }

    // ─── LOGIN SCREEN TESTS ───────────────────────────────────────────

    @Test
    fun loginScreen_displaysEmailField() {
        composeTestRule.setContent {
            SkillSwapTheme {
                LoginScreen(
                    authViewModel = FakeAuthViewModelForTest(),
                    onLoginSuccess = { _, _, _ -> },
                    onSignupClick = {},
                    onForgotPasswordClick = {}
                )
            }
        }
        composeTestRule.onNodeWithText("Email").assertIsDisplayed()
    }

    @Test
    fun loginScreen_displaysPasswordField() {
        composeTestRule.setContent {
            SkillSwapTheme {
                LoginScreen(
                    authViewModel = FakeAuthViewModelForTest(),
                    onLoginSuccess = { _, _, _ -> },
                    onSignupClick = {},
                    onForgotPasswordClick = {}
                )
            }
        }
        composeTestRule.onNodeWithText("Password").assertIsDisplayed()
    }

    @Test
    fun loginScreen_displaysLoginButton() {
        composeTestRule.setContent {
            SkillSwapTheme {
                LoginScreen(
                    authViewModel = FakeAuthViewModelForTest(),
                    onLoginSuccess = { _, _, _ -> },
                    onSignupClick = {},
                    onForgotPasswordClick = {}
                )
            }
        }
        composeTestRule.onNodeWithText("Log In").assertIsDisplayed()
    }

    @Test
    fun loginScreen_displaysCreateAccountLink() {
        composeTestRule.setContent {
            SkillSwapTheme {
                LoginScreen(
                    authViewModel = FakeAuthViewModelForTest(),
                    onLoginSuccess = { _, _, _ -> },
                    onSignupClick = {},
                    onForgotPasswordClick = {}
                )
            }
        }
        composeTestRule.onNodeWithText("Create account").assertIsDisplayed()
    }

    @Test
    fun loginScreen_createAccount_isClickable() {
        var clicked = false
        composeTestRule.setContent {
            SkillSwapTheme {
                LoginScreen(
                    authViewModel = FakeAuthViewModelForTest(),
                    onLoginSuccess = { _, _, _ -> },
                    onSignupClick = { clicked = true },
                    onForgotPasswordClick = {}
                )
            }
        }
        composeTestRule.onNodeWithText("Create account").performClick()
        assertEquals(true, clicked)
    }

    @Test
    fun loginScreen_typingEmail_updatesField() {
        composeTestRule.setContent {
            SkillSwapTheme {
                LoginScreen(
                    authViewModel = FakeAuthViewModelForTest(),
                    onLoginSuccess = { _, _, _ -> },
                    onSignupClick = {},
                    onForgotPasswordClick = {}
                )
            }
        }
        // Changed "Enter your email" to "Email" to match the label
        composeTestRule.onNodeWithText("Email").performTextInput("test@test.com")
        composeTestRule.onNodeWithText("test@test.com").assertIsDisplayed()
    }

    @Test
    fun loginScreen_emptyFields_showsError() {
        composeTestRule.setContent {
            SkillSwapTheme {
                LoginScreen(
                    authViewModel = FakeAuthViewModelForTest(),
                    onLoginSuccess = { _, _, _ -> },
                    onSignupClick = {},
                    onForgotPasswordClick = {}
                )
            }
        }
        // Click login without entering anything
        composeTestRule.onNodeWithText("Log In").performClick()
        composeTestRule.onNodeWithText("Please fill in all fields.").assertIsDisplayed()
    }
}
