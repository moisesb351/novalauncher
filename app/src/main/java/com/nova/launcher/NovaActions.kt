package com.nova.launcher

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.widget.Toast

object NovaActions {

    fun launchApp(context: Context, packageName: String) {
        val pm = context.packageManager
        val intent = pm.getLeanbackLaunchIntentForPackage(packageName)
            ?: pm.getLaunchIntentForPackage(packageName)
        if (intent == null) {
            toast(context, "No se pudo abrir la app")
            return
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (!tryStart(context, intent)) {
            toast(context, "No se pudo abrir la app")
        }
    }

    fun openSettings(context: Context) {
        val intent = Intent(Settings.ACTION_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (!tryStart(context, intent)) {
            toast(context, "No se pudieron abrir los ajustes")
        }
    }

    fun openNetworkSettings(context: Context) {
        val wifi = Intent(Settings.ACTION_WIFI_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (tryStart(context, wifi)) return
        openSettings(context)
    }

    /** Abre el asistente de voz que tenga la TV (Gemini o Google Assistant, según el modelo). */
    fun launchAssistant(context: Context) {
        val voice = Intent(Intent.ACTION_VOICE_COMMAND).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (tryStart(context, voice)) return
        val assist = Intent(Intent.ACTION_ASSIST).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (tryStart(context, assist)) return
        toast(context, "No hay un asistente disponible en esta TV")
    }

    private fun tryStart(context: Context, intent: Intent): Boolean {
        return try {
            context.startActivity(intent)
            true
        } catch (e: ActivityNotFoundException) {
            false
        } catch (e: SecurityException) {
            false
        }
    }

    private fun toast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
