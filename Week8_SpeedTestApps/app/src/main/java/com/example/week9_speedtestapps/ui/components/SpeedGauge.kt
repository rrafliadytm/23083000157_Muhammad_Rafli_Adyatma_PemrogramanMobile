package com.example.week9_speedtestapps.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

import java.util.Locale

@Composable
fun SpeedGauge(
    speed: Float,
    modifier: Modifier = Modifier,
    maxSpeed: Float = 100f
) {
    val animatedSpeed = remember { Animatable(0f) }

    LaunchedEffect(speed) {
        animatedSpeed.animateTo(
            targetValue = speed.coerceAtMost(maxSpeed),
            animationSpec = tween(durationMillis = 300)
        )
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2, size.height / 2)
            val radius = size.minDimension / 2
            val strokeWidth = 12.dp.toPx()
            
            // Draw background arc
            drawArc(
                color = Color(0xFFF0F2F5),
                startAngle = 150f,
                sweepAngle = 240f,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                size = Size(radius * 2 - strokeWidth, radius * 2 - strokeWidth),
                topLeft = Offset(center.x - radius + strokeWidth/2, center.y - radius + strokeWidth/2)
            )

            // Draw ticks
            val tickCount = 8
            for (i in 0..tickCount) {
                val angleInDegrees = 150f + (i * (240f / tickCount))
                val angleInRadians = Math.toRadians(angleInDegrees.toDouble()).toFloat()
                val tickLength = 10.dp.toPx()
                val start = Offset(
                    center.x + (radius - 20.dp.toPx()) * cos(angleInRadians),
                    center.y + (radius - 20.dp.toPx()) * sin(angleInRadians)
                )
                val end = Offset(
                    center.x + (radius - 20.dp.toPx() - tickLength) * cos(angleInRadians),
                    center.y + (radius - 20.dp.toPx() - tickLength) * sin(angleInRadians)
                )
                drawLine(
                    color = Color(0xFFE0E0E0),
                    start = start,
                    end = end,
                    strokeWidth = 2.dp.toPx()
                )
            }

            // Draw Needle
            val sweepAngle = (animatedSpeed.value / maxSpeed) * 240f
            val needleAngle = 150f + sweepAngle
            val needleAngleRad = Math.toRadians(needleAngle.toDouble()).toFloat()
            val needleColor = Color(0xFF00A8CC)
            val needleLength = radius - 15.dp.toPx()
            
            drawLine(
                color = needleColor,
                start = center,
                end = Offset(
                    center.x + needleLength * cos(needleAngleRad),
                    center.y + needleLength * sin(needleAngleRad)
                ),
                strokeWidth = 3.dp.toPx(),
                cap = StrokeCap.Round
            )
            
            drawCircle(
                color = needleColor,
                radius = 5.dp.toPx(),
                center = Offset(
                    center.x + needleLength * cos(needleAngleRad),
                    center.y + needleLength * sin(needleAngleRad)
                )
            )
            
            drawCircle(color = needleColor, radius = 6.dp.toPx(), center = center)
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = String.format(Locale.getDefault(), "%.0f", speed),
                fontSize = 64.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1C1E)
            )
            Text(
                text = "Mbps",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF74777F)
            )
        }
    }
}
