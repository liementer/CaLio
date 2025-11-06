package app.calio.android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

enum class ButtonVariant {
    DEFAULT,
    SECONDARY,
    OUTLINE,
    GHOST,
    DESTRUCTIVE
}

@Composable
fun Button(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: ButtonVariant = ButtonVariant.DEFAULT,
    enabled: Boolean = true,
    content: @Composable () -> Unit
) {
    val backgroundColor = when (variant) {
        ButtonVariant.DEFAULT -> MaterialTheme.colorScheme.primary
        ButtonVariant.SECONDARY -> MaterialTheme.colorScheme.secondary
        ButtonVariant.OUTLINE -> Color.Transparent
        ButtonVariant.GHOST -> Color.Transparent
        ButtonVariant.DESTRUCTIVE -> MaterialTheme.colorScheme.error
    }
    
    val contentColor = when (variant) {
        ButtonVariant.DEFAULT -> MaterialTheme.colorScheme.onPrimary
        ButtonVariant.SECONDARY -> MaterialTheme.colorScheme.onSecondary
        ButtonVariant.OUTLINE -> MaterialTheme.colorScheme.onSurface
        ButtonVariant.GHOST -> MaterialTheme.colorScheme.onSurface
        ButtonVariant.DESTRUCTIVE -> MaterialTheme.colorScheme.onError
    }
    
    val shape = RoundedCornerShape(6.dp)
    
    Box(
        modifier = modifier
            .clip(shape)
            .background(if (enabled) backgroundColor else backgroundColor.copy(alpha = 0.5f))
            .then(
                if (variant == ButtonVariant.OUTLINE) {
                    Modifier.border(1.dp, MaterialTheme.colorScheme.outline, shape)
                } else Modifier
            )
            .clickable(enabled = enabled) { onClick() }
            .padding(horizontal = 16.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(modifier = Modifier) {
            content()
        }
    }
}

@Composable
fun Button(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    variant: ButtonVariant = ButtonVariant.DEFAULT,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        variant = variant,
        enabled = enabled
    ) {
        val contentColor = when (variant) {
            ButtonVariant.DEFAULT -> MaterialTheme.colorScheme.onPrimary
            ButtonVariant.SECONDARY -> MaterialTheme.colorScheme.onSecondary
            ButtonVariant.OUTLINE -> MaterialTheme.colorScheme.onSurface
            ButtonVariant.GHOST -> MaterialTheme.colorScheme.onSurface
            ButtonVariant.DESTRUCTIVE -> MaterialTheme.colorScheme.onError
        }
        
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = if (enabled) contentColor else contentColor.copy(alpha = 0.5f)
        )
    }
}


