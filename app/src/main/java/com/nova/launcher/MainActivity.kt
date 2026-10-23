package com.nova.launcher

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue

class MainActivity : ComponentActivity() {

    private lateinit var settings: NovaSettings

    // Sube cada vez que la app vuelve al frente (para refrescar la lista de apps).
    private var resumeTick by mutableIntStateOf(0)

    // Sube cuando se pulsa Inicio estando Nova ya abierto (para volver a la pantalla principal).
    private var homeSignal by mutableIntStateOf(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settings = NovaSettings(getSharedPreferences("nova_prefs", Context.MODE_PRIVATE))

        addOnNewIntentListener { homeSignal += 1 }

        setContent {
            NovaApp(
                settings = settings,
                reloadKey = resumeTick,
                homeSignal = homeSignal
            )
        }
    }

    override fun onResume() {
        super.onResume()
        resumeTick += 1
    }
}
