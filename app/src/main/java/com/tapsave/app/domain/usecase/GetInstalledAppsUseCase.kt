package com.tapsave.app.domain.usecase

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.tapsave.app.domain.model.AppInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GetInstalledAppsUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    operator fun invoke(): List<AppInfo> {
        val packageManager = context.packageManager
        val packages = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)

        return packages
            .filter { app ->
                // Filter out system apps unless they're common messaging apps
                !app.isSystemApp() || isCommonMessagingApp(app.packageName)
            }
            .map { app ->
                AppInfo(
                    packageName = app.packageName,
                    appName = app.loadLabel(packageManager).toString(),
                    icon = app.loadIcon(packageManager),
                    isSystemApp = app.isSystemApp()
                )
            }
            .sortedBy { it.appName.lowercase() }
    }

    private fun ApplicationInfo.isSystemApp(): Boolean {
        return (flags and ApplicationInfo.FLAG_SYSTEM) != 0
    }

    private fun isCommonMessagingApp(packageName: String): Boolean {
        val messagingApps = listOf(
            "com.whatsapp",
            "org.telegram.messenger",
            "com.google.android.gm", // Gmail
            "com.android.email",
            "com.google.android.talk", // Google Chat
            "com.facebook.orca", // Messenger
            "com.viber.voip",
            "com.skype.raider",
            "org.thoughtcrime.securesms", // Signal
            "com.discord"
        )
        return messagingApps.any { packageName.contains(it) }
    }
}