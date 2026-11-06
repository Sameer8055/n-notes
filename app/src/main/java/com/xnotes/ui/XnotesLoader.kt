package com.xnotes.ui

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xnotes.R

/** Branded loading screen using the user's Sameer Notes logo with a gentle breathing animation. */
@Composable
fun XnotesLoader(modifier: Modifier = Modifier) {
    val cfg = LocalConfiguration.current
    val shortEdge = minOf(cfg.screenWidthDp, cfg.screenHeightDp)
    val side = minOf(shortEdge * 0.34f, 180f).dp

    var visibleCharacters by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        val title = "Sameer Notes"
        for (index in 1..title.length) {
            visibleCharacters = index
            kotlinx.coroutines.delay(72L)
        }
    }

    val transition = rememberInfiniteTransition(label = "sameer-notes-loader")
    val scale by transition.animateFloat(
        initialValue = 0.94f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1100),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "logo-pulse",
    )

    Column(
        modifier.fillMaxSize().background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(R.drawable.sameer_notes_logo),
            contentDescription = "Sameer Notes logo",
            modifier = Modifier.size(side).graphicsLayer(scaleX = scale, scaleY = scale),
        )
        Spacer(Modifier.height(18.dp))
        Text(
            "Sameer Notes".take(visibleCharacters),
            color = Color.White,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.SemiBold,
            fontSize = 23.sp,
            letterSpacing = 0.35.sp,
            textAlign = TextAlign.Center,
        )
    }
}
