package com.nova.launcher

import android.content.Context
import android.content.Intent
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap

data class AppEntry(
    val label: String,
    val packageName: String,
    val icon: ImageBitmap?
)

object AppRepository {

    private const val ICON_PX = 192

    /** Lista las apps lanzables (de TV y normales), sin duplicados y sin Nova. Llamar fuera del hilo principal. */
    @Suppress("DEPRECATION")
    fun loadApps(context: Context): List<AppEntry> {
        val pm = context.packageManager
        val leanbackIntent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER)
        val launcherIntent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)

        val resolved = pm.queryIntentActivities(leanbackIntent, 0) +
            pm.queryIntentActivities(launcherIntent, 0)

        val seen = HashSet<String>()
        val result = ArrayList<AppEntry>()

        for (info in resolved) {
            val pkg = info.activityInfo?.packageName ?: continue
            if (pkg == context.packageName) continue
            if (!seen.add(pkg)) continue

            val canLaunch = pm.getLeanbackLaunchIntentForPackage(pkg) != null ||
                pm.getLaunchIntentForPackage(pkg) != null
            if (!canLaunch) continue

            val label = info.loadLabel(pm).toString()
            val icon: ImageBitmap? = try {
                info.loadIcon(pm).toBitmap(ICON_PX, ICON_PX).asImageBitmap()
            } catch (e: Exception) {
                null
            }
            result.add(AppEntry(label = label, packageName = pkg, icon = icon))
        }

        return result.sortedBy { it.label.lowercase() }
    }
}
