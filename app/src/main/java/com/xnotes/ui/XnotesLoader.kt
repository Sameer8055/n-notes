package com.xnotes.ui

import android.graphics.Matrix
import android.graphics.RectF
import android.media.MediaPlayer
import android.net.Uri
import android.view.Surface
import android.view.TextureView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import com.xnotes.R
import kotlin.math.min

/**
 * Plays the 1.725-second H.264 brand animation at its original speed.
 * TextureView keeps the video in Compose's normal drawing order and the centre-fit transform
 * shows the complete 16:9 frame instead of cropping it. MediaPlayer leaves its last frame visible
 * after completion while MainActivity holds the loader for the full two seconds.
 */
@Composable
fun XnotesLoader(modifier: Modifier = Modifier) {
    AndroidView(
        modifier = modifier.fillMaxSize().background(Color.Black),
        factory = { context ->
            TextureView(context).apply {
                val videoUri = Uri.parse(
                    "android.resource://${context.packageName}/${R.raw.sameer_notes_intro}",
                )
                var player: MediaPlayer? = null
                var output: Surface? = null

                fun fitFrame(videoWidth: Int, videoHeight: Int, viewWidth: Int, viewHeight: Int) {
                    if (videoWidth <= 0 || videoHeight <= 0 || viewWidth <= 0 || viewHeight <= 0) return
                    val scale = min(viewWidth.toFloat() / videoWidth, viewHeight.toFloat() / videoHeight)
                    val drawnWidth = videoWidth * scale
                    val drawnHeight = videoHeight * scale
                    setTransform(Matrix().apply {
                        setScale(scale, scale)
                        postTranslate((viewWidth - drawnWidth) / 2f, (viewHeight - drawnHeight) / 2f)
                    })
                }

                surfaceTextureListener = object : TextureView.SurfaceTextureListener {
                    override fun onSurfaceTextureAvailable(surfaceTexture: android.graphics.SurfaceTexture, width: Int, height: Int) {
                        val surface = Surface(surfaceTexture)
                        output = surface
                        val media = MediaPlayer()
                        player = media
                        try {
                            media.setDataSource(context, videoUri)
                            media.setSurface(surface)
                            media.setVolume(0f, 0f)
                            media.setOnVideoSizeChangedListener { mp, videoWidth, videoHeight ->
                                fitFrame(videoWidth, videoHeight, width, height)
                            }
                            media.setOnPreparedListener { mp ->
                                fitFrame(mp.videoWidth, mp.videoHeight, this@apply.width, this@apply.height)
                                mp.start()
                            }
                            // Intentionally do not release or replace the final decoded frame.
                            media.setOnCompletionListener { }
                            media.setOnErrorListener { mp, _, _ ->
                                mp.reset()
                                true
                            }
                            media.isLooping = false
                            media.prepareAsync()
                        } catch (_: Exception) {
                            media.release()
                            player = null
                            surface.release()
                            output = null
                        }
                    }

                    override fun onSurfaceTextureSizeChanged(surfaceTexture: android.graphics.SurfaceTexture, width: Int, height: Int) {
                        player?.let { fitFrame(it.videoWidth, it.videoHeight, width, height) }
                    }

                    override fun onSurfaceTextureUpdated(surfaceTexture: android.graphics.SurfaceTexture) = Unit

                    override fun onSurfaceTextureDestroyed(surfaceTexture: android.graphics.SurfaceTexture): Boolean {
                        player?.runCatching {
                            setOnPreparedListener(null)
                            setOnCompletionListener(null)
                            setOnVideoSizeChangedListener(null)
                            setOnErrorListener(null)
                            stop()
                            release()
                        }
                        player = null
                        output?.release()
                        output = null
                        return true
                    }
                }
            }
        },
    )
}
