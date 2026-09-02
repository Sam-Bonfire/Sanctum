package com.sanctum.app.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.sanctum.app.BuildConfig
import com.sanctum.app.MainActivity
import com.sanctum.app.R
import com.sanctum.core.feature.scripture.domain.DailyVerseManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DailyVerseWidgetProvider : AppWidgetProvider(), KoinComponent {

    private val dailyVerseManager: DailyVerseManager by inject()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray,
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
    ) {
        // Show loading state initially
        val views = RemoteViews(context.packageName, R.layout.widget_daily_verse)
        views.setTextViewText(R.id.verse_text, "Loading...")
        views.setTextViewText(R.id.verse_reference, BuildConfig.TERM_SCRIPTURE_TITLE)
        appWidgetManager.updateAppWidget(appWidgetId, views)

        scope.launch {
            try {
                val verse = dailyVerseManager.getDailyVerse(BuildConfig.FLAVOR_ID)
                if (verse != null) {
                    views.setTextViewText(R.id.verse_text, verse.translation)

                    // For reference, since we are mocking with chapter 1, we can just say Chapter 1, Verse X
                    val referenceText = "${BuildConfig.TERM_CHAPTER_UNIT} 1, ${BuildConfig.TERM_VERSE_UNIT} ${verse.number}"
                    views.setTextViewText(R.id.verse_reference, referenceText)

                    // Create an Intent to launch MainActivity and optionally deep-link
                    val intent = Intent(context, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    val pendingIntent = PendingIntent.getActivity(
                        context,
                        0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
                    )
                    views.setOnClickPendingIntent(R.id.widget_container, pendingIntent)
                } else {
                    views.setTextViewText(R.id.verse_text, "No verse found.")
                    views.setTextViewText(R.id.verse_reference, BuildConfig.TERM_SCRIPTURE_TITLE)
                }
                appWidgetManager.updateAppWidget(appWidgetId, views)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
