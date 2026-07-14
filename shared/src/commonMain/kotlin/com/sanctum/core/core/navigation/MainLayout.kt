package com.sanctum.core.core.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.sanctum.core.core.design.LocalWhiteLabelConfig
import com.sanctum.core.core.designsystem.theme.SanctumTheme
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeChild
import kotlin.math.roundToInt

val LocalHazeState = staticCompositionLocalOf<HazeState> {
    error("No HazeState provided")
}

@Composable
fun MainLayout(
    currentScreenId: String,
    onNavigate: (String) -> Unit,
    content: @Composable () -> Unit,
) {
    val maxOffsetPx = with(LocalDensity.current) { 120.dp.roundToPx().toFloat() }
    val bottomBarOffsetHeightPx = remember { mutableStateOf(0f) }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = bottomBarOffsetHeightPx.value - delta
                bottomBarOffsetHeightPx.value = newOffset.coerceIn(0f, maxOffsetPx)
                return Offset.Zero
            }
        }
    }

    val hazeState = remember { HazeState() }

    CompositionLocalProvider(LocalHazeState provides hazeState) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(nestedScrollConnection),
            contentAlignment = Alignment.TopCenter,
        ) {
            // Main content area
            Box(
                modifier = Modifier
                    .widthIn(max = 600.dp)
                    .fillMaxHeight(),
            ) {
                content()
            }

            // ─── Floating Bottom Navigation Bar ─────────────────
            val navBarShape = RoundedCornerShape(40.dp)
            val navBarBg = if (SanctumTheme.colors.isLight) {
                Color.White.copy(alpha = 0.55f)
            } else {
                Color.White.copy(alpha = 0.06f)
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset { IntOffset(x = 0, y = bottomBarOffsetHeightPx.value.roundToInt()) }
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 28.dp)
                    .clip(navBarShape)
                    .hazeChild(state = hazeState, shape = navBarShape)
                    .background(navBarBg)
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                contentAlignment = Alignment.Center,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    val config = LocalWhiteLabelConfig.current
                    config.navItems.forEach { navItem ->
                        NavItem(
                            icon = navItem.icon,
                            isSelected = currentScreenId == navItem.id,
                            activeColor = config.primaryColor,
                            onClick = { onNavigate(navItem.id) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NavItem(
    icon: ImageVector,
    isSelected: Boolean,
    activeColor: Color,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }

    // Animate the circle size
    val circleSize = animateDpAsState(
        targetValue = if (isSelected) 52.dp else 44.dp,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "circleSize",
    )

    // Animate colors
    val bgColor = animateColorAsState(
        targetValue = if (isSelected) activeColor else Color.Transparent,
        label = "bgColor",
    )
    val iconTint = animateColorAsState(
        targetValue = if (isSelected) Color.White else SanctumTheme.colors.textPrimary.copy(alpha = 0.45f),
        label = "iconTint",
    )

    Box(
        modifier = Modifier
            .size(circleSize.value)
            .clip(CircleShape)
            .background(bgColor.value)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint.value,
            modifier = Modifier.size(if (isSelected) 24.dp else 22.dp),
        )
    }
}
