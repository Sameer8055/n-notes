package com.xnotes.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.graphics.Typeface
import com.xnotes.R

/** A short, premium brand lockup entrance: the mark settles left as its name types in. */
@Composable
fun XnotesLoader(modifier: Modifier = Modifier) {
    // A restrained editorial serif gives the wordmark a premium, crafted contrast to the geometric mark.
    val brandTypeface = remember {
        Typeface.create("serif", Typeface.BOLD)
    }
    val brandFamily = remember(brandTypeface) { FontFamily(brandTypeface) }
    val logoScale = remember { Animatable(0.58f) }
    val logoShift = remember { Animatable(26f) }
    val wordAlpha = remember { Animatable(0f) }
    val wordProgress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        logoScale.animateTo(1.12f, tween(260, easing = FastOutSlowInEasing))
        logoScale.animateTo(0.96f, tween(140, easing = FastOutSlowInEasing))
        logoShift.animateTo(0f, tween(260, easing = FastOutSlowInEasing))
        wordAlpha.animateTo(1f, tween(90))
        wordProgress.animateTo(1f, tween(430, easing = FastOutSlowInEasing))
    }

    val title = "Sameer Notes"
    val visible = (title.length * wordProgress.value).toInt().coerceIn(0, title.length)

    Row(
        modifier.fillMaxSize().background(Color.Black).padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(R.drawable.sameer_notes_logo),
            contentDescription = "Sameer Notes logo",
            modifier = Modifier
                .size(82.dp)
                .graphicsLayer {
                    scaleX = logoScale.value
                    scaleY = logoScale.value
                    translationX = logoShift.value * density
                },
        )
        Text(
            title.take(visible),
            color = Color.White,
            fontFamily = brandFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 25.sp,
            letterSpacing = 0.15.sp,
            textAlign = TextAlign.Start,
            modifier = Modifier.graphicsLayer { alpha = wordAlpha.value },
        )
    }
}
