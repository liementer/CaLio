package app.calio.android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class BadgeVariant {
    SUCCESS,
    WARNING,
    ERROR,
    INFO,
    SECONDARY
}

@Composable
fun Badge(
    text: String,
    modifier: Modifier = Modifier,
    variant: BadgeVariant = BadgeVariant.SECONDARY
) {
    val backgroundColor = when (variant) {
        BadgeVariant.SUCCESS -> Color(0xFF22C55E).copy(alpha = 0.1f)
        BadgeVariant.WARNING -> Color(0xFFFFA500).copy(alpha = 0.1f)
        BadgeVariant.ERROR -> MaterialTheme.colorScheme.error.copy(alpha = 0.1f)
        BadgeVariant.INFO -> MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        BadgeVariant.SECONDARY -> MaterialTheme.colorScheme.secondary
    }
    
    val textColor = when (variant) {
        BadgeVariant.SUCCESS -> Color(0xFF16A34A)
        BadgeVariant.WARNING -> Color(0xFFEA580C)
        BadgeVariant.ERROR -> MaterialTheme.colorScheme.error
        BadgeVariant.INFO -> MaterialTheme.colorScheme.primary
        BadgeVariant.SECONDARY -> MaterialTheme.colorScheme.onSecondary
    }
    
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = textColor
        )
    }
}


