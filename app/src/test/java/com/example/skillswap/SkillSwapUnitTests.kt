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
 * Tests cover: auth state, profile state, request state, skill filtering, input validation.
 */
class SkillSwapUnitTests {

    // ─── AUTH STATE TESTS ────────────────────────────────────────────

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
        assertTrue(state.token!!.isNotEmpty())
    }

    @Test
    fun authState_loading_hasNoError() {
        val state = AuthState(isLoading = true)
        assertTrue(state.isLoading)
        assertNull(state.errorMessage)
    }

    @Test
    fun authState_withError_isNotLoading() {
        val state = AuthState(isLoading = false, errorMessage = "Invalid credentials")
        assertFalse(state.isLoading)
        assertEquals("Invalid credentials", state.errorMessage)
    }

    // ─── USER PROFILE TESTS ──────────────────────────────────────────

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
            name = "Salma Test",
            role = "Developer",
            offeredSkills = listOf("Kotlin", "Jetpack Compose"),
            neededSkills = listOf("UI Design")
        )
        assertEquals("Salma Test", profile.name)
        assertEquals(2, profile.offeredSkills.size)
        assertEquals(1, profile.neededSkills.size)
        assertTrue(profile.offeredSkills.contains("Kotlin"))
    }

    @Test
    fun userProfile_isBlank_whenNameEmpty() {
        val profile = UserProfile(name = "")
        assertTrue(profile.name.isBlank())
    }

    // ─── SKILL FILTERING TESTS ───────────────────────────────────────

    @Test
    fun skills_filterByType_offer_returnsOnlyOffers() {
        val skills = listOf(
            SkillDto(skillName = "Figma", type = "OFFER"),
            SkillDto(skillName = "React", type = "NEED"),
            SkillDto(skillName = "UI Design", type = "OFFER")
        )
        val offered = skills.filter { it.type == "OFFER" }
        assertEquals(2, offered.size)
        assertTrue(offered.all { it.type == "OFFER" })
    }

    @Test
    fun skills_filterByType_need_returnsOnlyNeeds() {
        val skills = listOf(
            SkillDto(skillName = "Figma", type = "OFFER"),
            SkillDto(skillName = "React", type = "NEED"),
            SkillDto(skillName = "Backend", type = "NEED")
        )
        val needed = skills.filter { it.type == "NEED" }
        assertEquals(2, needed.size)
        assertTrue(needed.all { it.type == "NEED" })
    }

    @Test
    fun skills_emptyList_filtersToEmpty() {
        val skills = emptyList<SkillDto>()
        val offered = skills.filter { it.type == "OFFER" }
        assertTrue(offered.isEmpty())
    }

    // ─── PROFILE SEARCH/FILTER TESTS ─────────────────────────────────

    @Test
    fun profiles_searchByName_returnsCorrectResult() {
        val profiles = listOf(
            ProfileDto(userId = 1, fullName = "Marcus Thorne", headlineRole = "UI Designer"),
            ProfileDto(userId = 2, fullName = "Sarah Jenkins", headlineRole = "Developer"),
            ProfileDto(userId = 3, fullName = "Leila Chen", headlineRole = "Content Creator")
        )
        val result = profiles.filter {
            it.fullName.contains("Sarah", ignoreCase = true)
        }
        assertEquals(1, result.size)
        assertEquals("Sarah Jenkins", result[0].fullName)
    }

    @Test
    fun profiles_searchByRole_returnsCorrectResult() {
        val profiles = listOf(
            ProfileDto(userId = 1, fullName = "Marcus Thorne", headlineRole = "UI Designer"),
            ProfileDto(userId = 2, fullName = "Sarah Jenkins", headlineRole = "Developer"),
        )
        val result = profiles.filter {
            it.headlineRole?.contains("Developer", ignoreCase = true) == true
        }
        assertEquals(1, result.size)
    }

    @Test
    fun profiles_emptySearch_returnsAll() {
        val profiles = listOf(
            ProfileDto(userId = 1, fullName = "Marcus"),
            ProfileDto(userId = 2, fullName = "Sarah")
        )
        val searchText = ""
        val result = profiles.filter {
            searchText.isBlank() || it.fullName.contains(searchText, ignoreCase = true)
        }
        assertEquals(2, result.size)
    }

    // ─── REQUEST STATE TESTS ─────────────────────────────────────────

    @Test
    fun requestState_default_hasEmptyLists() {
        val state = RequestActionState()
        assertTrue(state.incomingRequests.isEmpty())
        assertTrue(state.outgoingRequests.isEmpty())
        assertFalse(state.isLoading)
        assertNull(state.successMessage)
        assertNull(state.errorMessage)
    }

    @Test
    fun requestState_successMessage_isSet() {
        val state = RequestActionState(successMessage = "Swap request sent successfully!")
        assertNotNull(state.successMessage)
        assertEquals("Swap request sent successfully!", state.successMessage)
    }

    // ─── INPUT VALIDATION TESTS ──────────────────────────────────────

    @Test
    fun validation_email_isValid() {
        val email = "test@example.com"
        assertTrue(email.contains("@") && email.contains("."))
    }

    @Test
    fun validation_email_isInvalid_noAt() {
        val email = "testexample.com"
        assertFalse(email.contains("@"))
    }

    @Test
    fun validation_password_tooShort_fails() {
        val password = "abc"
        assertTrue(password.length < 6)
    }

    @Test
    fun validation_password_validLength_passes() {
        val password = "password123"
        assertTrue(password.length >= 6)
    }

    @Test
    fun validation_passwordsMatch_isTrue() {
        val password = "mySecret1"
        val confirm = "mySecret1"
        assertEquals(password, confirm)
    }

    @Test
    fun validation_passwordsMatch_isFalse() {
        val password = "mySecret1"
        val confirm = "different"
        assertNotEquals(password, confirm)
    }

    // ─── POSTS STATE TESTS ───────────────────────────────────────────

    @Test
    fun postsUiState_default_isEmpty() {
        val state = PostsUiState()
        assertTrue(state.posts.isEmpty())
        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
    }

    @Test
    fun postsUiState_loading_isTrue() {
        val state = PostsUiState(isLoading = true)
        assertTrue(state.isLoading)
    }
}