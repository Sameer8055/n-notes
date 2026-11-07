package com.xnotes.ui

import android.net.Uri
import android.widget.VideoView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.xnotes.R

/**
 * Plays the supplied short brand animation at its original speed.
 * MainActivity keeps this screen visible for two seconds; if restoring the
 * previous session takes longer, VideoView naturally remains on its final frame.
 */
@Composable
fun XnotesLoader(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    AndroidView(
        modifier = modifier.fillMaxSize().background(Color.Black),
        factory = { viewContext ->
            VideoView(viewContext).apply {
                setBackgroundColor(android.graphics.Color.BLACK)
                setVideoURI(Uri.parse("android.resource://${viewContext.packageName}/${R.raw.sameer_notes_intro}"))
                setOnPreparedListener { player ->
                    player.isLooping = false
                    player.setVolume(0f, 0f)
                    start()
                }
                // Leave the final frame visible until the app has finished restoring its session.
                setOnCompletionListener { }
            }
        },
    )
}
