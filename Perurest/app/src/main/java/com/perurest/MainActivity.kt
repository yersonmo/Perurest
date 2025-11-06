package com.perurest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.perurest.di.ServiceLocator
import com.perurest.navigation.PerurestNavHost
import com.perurest.ui.theme.PerurestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ServiceLocator.init(this)
        setContent {
            PerurestTheme {
                PerurestNavHost()
            }
        }
    }
}
