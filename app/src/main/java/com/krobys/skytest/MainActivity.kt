package com.krobys.skytest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.krobys.skytest.manager.MessageManager
import com.krobys.skytest.navigation.graphs.general.GeneralGraph
import com.krobys.skytest.ui.custom.MainInfoMessage
import com.krobys.skytest.ui.system.TransparentSystemBars
import com.krobys.skytest.ui.theme.SkyTestTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            TransparentSystemBars(false)
            SkyTestTheme {
                val errorMessageState = MessageManager.appMessages.collectAsStateWithLifecycle(null)
                Box {
                    GeneralGraph()
                    MainInfoMessage(errorMessageState = errorMessageState)
                }
            }
        }
    }
}