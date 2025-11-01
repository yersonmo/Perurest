package com.perurest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.perurest.navigation.PerurestApp
import com.perurest.ui.theme.PerurestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PerurestTheme {
                PerurestApp()
            }
        }
    }
}
