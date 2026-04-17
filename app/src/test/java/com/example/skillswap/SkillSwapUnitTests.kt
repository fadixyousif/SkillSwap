package com.example.skillswap

import com.example.skillswap.data.model.ProfileDto
import com.example.skillswap.data.model.SkillDto
import com.example.skillswap.ui.viewmodel.AuthState
import com.example.skillswap.ui.viewmodel.PostsUiState
import com.example.skillswap.ui.viewmodel.RequestActionState
import com.example.skillswap.ui.viewmodel.UserProfile
import org.junit.Assert.*
import org.junit.Test

/**
 * Unit tests for SkillSwap app logic.
 */
class SkillSwapUnitTests {

    @Test
    fun authState_defaultValues_areCorrect() {
        val state = AuthState()
        assertNull(state.token)
        assertNull(state.userId)
        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
    }

    @Test
    fun authState_withToken_isLoggedIn() {
        val state = AuthState(token = "abc123", userId = 5)
        assertNotNull(state.token)
        assertEquals(5, state.userId)
    }

    @Test
    fun userProfile_defaultValues_areBlank() {
        val profile = UserProfile()
        assertTrue(profile.name.isBlank())
        assertTrue(profile.offeredSkills.isEmpty())
        assertTrue(profile.neededSkills.isEmpty())
    }

    @Test
    fun userProfile_withData_hasCorrectValues() {
        val profile = UserProfile(
            name = "Test User",
            role = "Developer",
            offeredSkills = listOf("Kotlin"),
            neededSkills = listOf("Design")
        )
        assertEquals("Test User", profile.name)
        assertEquals(1, profile.offeredSkills.size)
    }

    @Test
    fun skills_filterByType_offer_returnsOnlyOffers() {
        val skills = listOf(
            SkillDto(skillName = "Figma", type = "OFFER"),
            SkillDto(skillName = "React", type = "NEED")
        )
        val offered = skills.filter { it.type == "OFFER" }
        assertEquals(1, offered.size)
        assertEquals("OFFER", offered[0].type)
    }

    @Test
    fun requestState_default_hasEmptyLists() {
        val state = RequestActionState()
        assertTrue(state.incomingRequests.isEmpty())
        assertFalse(state.isLoading)
    }

    @Test
    fun validation_email_isValid() {
        val email = "test@example.com"
        assertTrue(email.contains("@") && email.contains("."))
    }

    @Test
    fun postsUiState_default_isEmpty() {
        val state = PostsUiState()
        assertTrue(state.posts.isEmpty())
        assertFalse(state.isLoading)
    }
}
