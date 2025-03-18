package com.dalmuina.portolioapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.dalmuina.portolioapp.navigation.NavManager
import com.dalmuina.portolioapp.ui.theme.PortolioAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PortolioAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.Companion.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavManager()
                }
            }
        }
    }
}
