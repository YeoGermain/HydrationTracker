package com.example

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.theme.BackgroundDark
import com.example.ui.theme.ResetRed
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.SurfaceHoverDark
import com.example.ui.theme.SurfaceVariantDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TurquoiseGlow
import com.example.ui.theme.TurquoisePrimary
import com.example.ui.theme.TurquoiseSecondary
import com.example.ui.theme.TurquoiseTertiary
import com.example.ui.theme.TurquoiseTrack
import java.text.NumberFormat
import java.util.Locale

@Composable
fun HydrationScreen(
    viewModel: HydrationViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showResetDialog by remember { mutableStateOf(false) }

    val animatedProgress by animateFloatAsState(
        targetValue = uiState.progress,
        animationSpec = spring(dampingRatio = 0.8f, stiffness = 400f),
        label = "progressAnimation"
    )

    val numberFormat = remember { NumberFormat.getNumberInstance(Locale.FRENCH) }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = BackgroundDark
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header
            HeaderSection(isGoalReached = uiState.isGoalReached)

            // Circular Progress Indicator for 2L goal
            CircularHydrationMeter(
                progress = animatedProgress,
                currentMl = uiState.currentMl,
                goalMl = uiState.goalMl,
                percentage = uiState.percentage,
                isGoalReached = uiState.isGoalReached,
                formattedCurrent = numberFormat.format(uiState.currentMl),
                formattedGoal = numberFormat.format(uiState.goalMl)
            )

            // Status message card
            StatusBanner(
                isGoalReached = uiState.isGoalReached,
                remainingMl = uiState.remainingMl,
                numberFormat = numberFormat
            )

            // Quick Stats Row
            StatsRow(
                currentMl = uiState.currentMl,
                remainingMl = uiState.remainingMl,
                glassesCount = uiState.glassesCount,
                numberFormat = numberFormat
            )

            // Main Action Buttons
            ActionButtonsSection(
                onAdd250 = { viewModel.addWater(250) },
                onAddCustom = { viewModel.addWater(it) },
                onRemove250 = { viewModel.removeWater(250) },
                onResetClick = { showResetDialog = true },
                canRemove = uiState.currentMl > 0
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Reset Confirmation Dialog
    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            containerColor = SurfaceDark,
            titleContentColor = TextPrimary,
            textContentColor = TextSecondary,
            title = {
                Text(
                    text = "Réinitialiser le suivi ?",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text("Voulez-vous remettre votre consommation d'eau à 0 ml pour la journée ?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.reset()
                        showResetDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ResetRed,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.testTag("confirm_reset_button")
                ) {
                    Text("Réinitialiser", fontWeight = FontWeight.SemiBold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("Annuler", color = TextSecondary)
                }
            }
        )
    }
}

@Composable
private fun HeaderSection(isGoalReached: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Filled.WaterDrop,
                contentDescription = null,
                tint = TurquoisePrimary,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Hydratation",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = TextPrimary
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Objectif quotidien : 2 Litres (2 000 ml)",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )
    }
}

@Composable
private fun CircularHydrationMeter(
    progress: Float,
    currentMl: Int,
    goalMl: Int,
    percentage: Int,
    isGoalReached: Boolean,
    formattedCurrent: String,
    formattedGoal: String,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(280.dp)
            .padding(8.dp)
    ) {
        // Glowing halo when completed
        if (isGoalReached) {
            Box(
                modifier = Modifier
                    .size(260.dp)
                    .clip(CircleShape)
                    .background(TurquoiseGlow)
            )
        }

        // Custom Canvas for crisp progress ring
        Canvas(modifier = Modifier.size(250.dp)) {
            val strokeWidth = 20.dp.toPx()
            val diameter = size.minDimension - strokeWidth
            val topLeft = Offset((size.width - diameter) / 2, (size.height - diameter) / 2)
            val arcSize = Size(diameter, diameter)

            // Background Track
            drawArc(
                color = TurquoiseTrack,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Progress Arc with Gradient
            if (progress > 0f) {
                val gradientBrush = Brush.sweepGradient(
                    0.0f to TurquoisePrimary,
                    0.5f to TurquoiseSecondary,
                    1.0f to TurquoiseTertiary,
                    center = Offset(size.width / 2, size.height / 2)
                )

                drawArc(
                    brush = gradientBrush,
                    startAngle = -90f,
                    sweepAngle = 360f * progress,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            }
        }

        // Inner Content
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = if (isGoalReached) Icons.Filled.CheckCircle else Icons.Filled.WaterDrop,
                contentDescription = if (isGoalReached) "Objectif atteint" else "Goutte d'eau",
                tint = TurquoisePrimary,
                modifier = Modifier.size(34.dp)
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Current Volume
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = formattedCurrent,
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Black,
                    color = TextPrimary,
                    letterSpacing = (-1).sp
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "ml",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TurquoiseSecondary,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
            }

            Text(
                text = "sur $formattedGoal ml",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Percentage Chip
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (isGoalReached) TurquoisePrimary else SurfaceVariantDark,
                contentColor = if (isGoalReached) Color(0xFF002025) else TurquoisePrimary,
                modifier = Modifier.border(
                    width = 1.dp,
                    color = if (isGoalReached) TurquoisePrimary else TurquoisePrimary.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(12.dp)
                )
            ) {
                Text(
                    text = "$percentage %",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun StatusBanner(
    isGoalReached: Boolean,
    remainingMl: Int,
    numberFormat: NumberFormat
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isGoalReached) SurfaceHoverDark else SurfaceVariantDark
        ),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(
                colors = if (isGoalReached) {
                    listOf(TurquoisePrimary, TurquoiseSecondary)
                } else {
                    listOf(Color(0xFF233B49), Color(0xFF1E323E))
                }
            )
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isGoalReached) Icons.Filled.CheckCircle else Icons.Filled.LocalDrink,
                contentDescription = null,
                tint = TurquoisePrimary,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                if (isGoalReached) {
                    Text(
                        text = "Objectif de 2L atteint ! 🎉",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TurquoisePrimary
                    )
                    Text(
                        text = "Félicitations, vous avez atteint votre hydratation recommandée !",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                } else {
                    Text(
                        text = "Encore ${numberFormat.format(remainingMl)} ml restants",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Soit environ ${(remainingMl / 250f).coerceAtLeast(0.1f).let { String.format(Locale.FRENCH, "%.1f", it) }} verre(s) d'eau",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}

@Composable
private fun StatsRow(
    currentMl: Int,
    remainingMl: Int,
    glassesCount: Int,
    numberFormat: NumberFormat
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatItemCard(
            title = "Verres bus",
            value = "$glassesCount",
            unit = "× 250 ml",
            modifier = Modifier.weight(1f)
        )
        StatItemCard(
            title = "Total bu",
            value = numberFormat.format(currentMl),
            unit = "ml",
            modifier = Modifier.weight(1f)
        )
        StatItemCard(
            title = "Restant",
            value = numberFormat.format(remainingMl),
            unit = "ml",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun StatItemCard(
    title: String,
    value: String,
    unit: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = SurfaceDark,
        tonalElevation = 2.dp,
        modifier = modifier.border(
            width = 1.dp,
            color = Color(0xFF1E323E),
            shape = RoundedCornerShape(14.dp)
        )
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = TurquoisePrimary
            )
            Text(
                text = unit,
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
private fun ActionButtonsSection(
    onAdd250: () -> Unit,
    onAddCustom: (Int) -> Unit,
    onRemove250: () -> Unit,
    onResetClick: () -> Unit,
    canRemove: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .widthIn(max = 500.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Main Prominent Button: "+ 250 ml" (User's primary request)
        Button(
            onClick = onAdd250,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .testTag("add_250_button"),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = TurquoisePrimary,
                contentColor = Color(0xFF002025)
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp, pressedElevation = 1.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Filled.LocalDrink,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Ajouter 250 ml",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Secondary quick presets row: +100ml, +500ml, and optional -250ml
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Quick preset: +100 ml (Gorgée)
            QuickAddButton(
                label = "+ 100 ml",
                onClick = { onAddCustom(100) },
                modifier = Modifier.weight(1f),
                testTag = "add_100_button"
            )

            // Quick preset: +500 ml (Gourde)
            QuickAddButton(
                label = "+ 500 ml",
                onClick = { onAddCustom(500) },
                modifier = Modifier.weight(1f),
                testTag = "add_500_button"
            )

            // Undo / remove 250ml if needed
            if (canRemove) {
                OutlinedButton(
                    onClick = onRemove250,
                    modifier = Modifier.height(44.dp).testTag("remove_250_button"),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = TextSecondary
                    ),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = Brush.linearGradient(listOf(Color(0xFF2B4452), Color(0xFF2B4452)))
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.Remove,
                        contentDescription = "Retirer 250 ml",
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Reset Button (User requested: "un bouton de reset")
        OutlinedButton(
            onClick = onResetClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("reset_button"),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = TextSecondary
            ),
            border = ButtonDefaults.outlinedButtonBorder.copy(
                brush = Brush.linearGradient(
                    listOf(Color(0xFF2B4452), Color(0xFF1E323E))
                )
            )
        ) {
            Icon(
                imageVector = Icons.Filled.Refresh,
                contentDescription = null,
                tint = ResetRed,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Réinitialiser la journée",
                color = TextSecondary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun QuickAddButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(44.dp)
            .testTag(testTag),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = SurfaceVariantDark,
            contentColor = TurquoiseSecondary
        ),
        border = ButtonDefaults.outlinedButtonBorder.copy(
            brush = Brush.linearGradient(
                listOf(Color(0xFF233B49), Color(0xFF1E323E))
            )
        )
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
