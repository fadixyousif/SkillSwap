package com.example.skillswap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.skillswap.ui.navigation.SkillSwapNavGraph
import com.example.skillswap.ui.theme.SkillSwapTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SkillSwapTheme {
                SkillSwapNavGraph()
            }
        }
    }
}