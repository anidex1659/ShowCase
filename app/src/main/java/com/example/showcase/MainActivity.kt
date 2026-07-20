package com.example.showcase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.showcase.ui.navigation.AppNavGraph
import com.example.showcase.ui.theme.ShowCaseTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShowCaseTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    AppNavGraph()

                }
            }
        }
    }
}

