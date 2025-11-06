package app.calio.android.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

data class BarChartData(
    val label: String,
    val value: Float,
    val target: Float = 0f,
    val date: Long = 0L
)

@Composable
fun BarChart(
    data: List<BarChartData>,
    modifier: Modifier = Modifier,
    barColor: Color = MaterialTheme.colorScheme.primary,
    targetColor: Color = MaterialTheme.colorScheme.error.copy(alpha = 0.3f)
) {
    val maxValue = data.maxOfOrNull { maxOf(it.value, it.target) } ?: 1f
    val chartHeight = 200.dp
    
    Column(modifier = modifier.fillMaxWidth()) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(chartHeight)
        ) {
            val barWidth = size.width / (data.size * 2)
            val spacing = barWidth / 2
            
            data.forEachIndexed { index, barData ->
                val barHeight = (barData.value / maxValue) * size.height
                val xPosition = index * (barWidth + spacing) + spacing
                
                // Draw target line
                if (barData.target > 0) {
                    val targetHeight = (barData.target / maxValue) * size.height
                    drawRect(
                        color = targetColor,
                        topLeft = Offset(xPosition, size.height - targetHeight - 2.dp.toPx()),
                        size = Size(barWidth, 4.dp.toPx())
                    )
                }
                
                // Draw bar
                drawRoundRect(
                    color = barColor,
                    topLeft = Offset(xPosition, size.height - barHeight),
                    size = Size(barWidth, barHeight),
                    cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Labels
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            data.forEach { barData ->
                Text(
                    text = barData.label,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.weight(1f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun CircularProgress(
    current: Int,
    target: Int,
    modifier: Modifier = Modifier,
    size: Int = 120
) {
    val progress = if (target > 0) (current.toFloat() / target.toFloat()).coerceIn(0f, 1f) else 0f
    val color = when {
        progress >= 1f -> MaterialTheme.colorScheme.error
        progress >= 0.8f -> Color(0xFFFFA500) // Orange
        else -> MaterialTheme.colorScheme.primary
    }
    
    // Extract colors before Canvas block
    val outlineColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
    val surfaceColor = MaterialTheme.colorScheme.surface
    
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Canvas(
            modifier = Modifier
                .width(size.dp)
                .height(size.dp)
        ) {
            // Background circle
            drawCircle(
                color = outlineColor,
                radius = size.dp.toPx() / 2
            )
            
            // Progress arc
            val sweepAngle = 360f * progress
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = sweepAngle,
                useCenter = true,
                topLeft = Offset(0f, 0f),
                size = Size(size.dp.toPx(), size.dp.toPx())
            )
            
            // Inner white circle
            drawCircle(
                color = surfaceColor,
                radius = (size.dp.toPx() / 2) * 0.7f
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$current",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "of $target kcal",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

fun formatDate(timestamp: Long, pattern: String = "MMM dd"): String {
    val sdf = SimpleDateFormat(pattern, Locale.getDefault())
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = timestamp
    return sdf.format(calendar.time)
}

fun getDayOfWeek(timestamp: Long): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = timestamp
    return when (calendar.get(Calendar.DAY_OF_WEEK)) {
        Calendar.SUNDAY -> "Sun"
        Calendar.MONDAY -> "Mon"
        Calendar.TUESDAY -> "Tue"
        Calendar.WEDNESDAY -> "Wed"
        Calendar.THURSDAY -> "Thu"
        Calendar.FRIDAY -> "Fri"
        Calendar.SATURDAY -> "Sat"
        else -> ""
    }
}

