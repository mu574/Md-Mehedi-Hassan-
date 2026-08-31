package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MovieCreation
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.OndemandVideo
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.StudioFeature
import com.example.ui.ScreenDestination
import com.example.ui.StudioViewModel
import com.example.ui.theme.PurpleAccent
import com.example.ui.theme.PurpleDeep
import com.example.ui.theme.PurpleLight
import com.example.ui.theme.PurplePrimary
import com.example.ui.theme.StudioBorder
import com.example.ui.theme.StudioCardBg
import com.example.ui.theme.StudioCardElevated
import com.example.ui.theme.StudioDarkSurface
import com.example.ui.theme.StudioObsidian
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun StudioDrawerContent(
    viewModel: StudioViewModel,
    currentScreen: ScreenDestination,
    selectedFeature: StudioFeature,
    onCloseDrawer: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .fillMaxHeight(),
        color = StudioDarkSurface
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(vertical = 16.dp, horizontal = 12.dp)
        ) {
            // App Branding Header (Clean Minimalism)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(PurplePrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "App Logo",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "MediaStudio AI",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Audio, Video & Recitation Suite",
                        fontSize = 11.sp,
                        color = PurpleDeep
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Primary Navigation Items
            DrawerActionItem(
                title = "AI Assistant Chat",
                subtitle = "Conversational Prompt Studio",
                icon = Icons.Default.Chat,
                isSelected = currentScreen == ScreenDestination.CHAT_STUDIO,
                badge = "AI",
                badgeColor = PurplePrimary,
                onClick = {
                    viewModel.navigateTo(ScreenDestination.CHAT_STUDIO)
                    onCloseDrawer()
                }
            )

            DrawerActionItem(
                title = "Saved Projects & Library",
                subtitle = "Exports, Downloads & Sharing",
                icon = Icons.Outlined.Folder,
                isSelected = currentScreen == ScreenDestination.PROJECT_LIBRARY,
                badge = null,
                badgeColor = PurplePrimary,
                onClick = {
                    viewModel.navigateTo(ScreenDestination.PROJECT_LIBRARY)
                    onCloseDrawer()
                }
            )

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = StudioBorder, thickness = 1.dp)
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "CORE STUDIOS (10 FEATURES)",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextMuted,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                letterSpacing = 1.sp
            )

            // List of 10 Features
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(StudioFeature.values()) { feature ->
                    val isSelected = currentScreen == ScreenDestination.FEATURE_WORKSPACE && selectedFeature == feature
                    val featureIcon = getFeatureIcon(feature)
                    val durationBadge = if (feature.isUnlimitedDuration) "Unlimited" else "30-50m"

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) PurpleLight else Color.Transparent)
                            .border(
                                1.dp,
                                if (isSelected) StudioBorder else Color.Transparent,
                                RoundedCornerShape(12.dp)
                            )
                            .clickable {
                                viewModel.openFeatureStudio(feature)
                                onCloseDrawer()
                            }
                            .padding(horizontal = 10.dp, vertical = 10.dp)
                            .testTag("drawer_feature_${feature.id}"),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) PurplePrimary else StudioCardBg),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = featureIcon,
                                contentDescription = feature.title,
                                tint = if (isSelected) Color.White else TextSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = feature.shortName,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                                color = if (isSelected) TextPrimary else TextSecondary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = feature.description,
                                fontSize = 10.sp,
                                color = TextMuted,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        // Duration Restriction Badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(StudioCardBg)
                                .border(1.dp, StudioBorder, RoundedCornerShape(6.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = durationBadge,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = PurpleDeep
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = StudioBorder, thickness = 1.dp)
            Spacer(modifier = Modifier.height(8.dp))

            // Footer Policy Note
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(StudioCardBg)
                    .border(1.dp, StudioBorder, RoundedCornerShape(10.dp))
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = PurplePrimary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Strict 30-50m output rules enforced for Features 1-9. Unlimited for Suite.",
                    fontSize = 10.sp,
                    color = TextSecondary,
                    lineHeight = 13.sp
                )
            }
        }
    }
}

@Composable
fun DrawerActionItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    isSelected: Boolean,
    badge: String?,
    badgeColor: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) PurpleLight else Color.Transparent)
            .border(
                1.dp,
                if (isSelected) StudioBorder else Color.Transparent,
                RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(if (isSelected) PurplePrimary else StudioCardBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isSelected) Color.White else TextSecondary,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                color = if (isSelected) TextPrimary else TextSecondary
            )
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = TextMuted
            )
        }

        if (badge != null) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(PurpleLight)
                    .border(1.dp, StudioBorder, RoundedCornerShape(6.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = badge,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = PurpleDeep
                )
            }
        }
    }
}

fun getFeatureIcon(feature: StudioFeature): ImageVector {
    return when (feature) {
        StudioFeature.RECITATION_AUDIO -> Icons.Default.GraphicEq
        StudioFeature.RECITATION_VIDEO -> Icons.Default.MovieCreation
        StudioFeature.GHOJOL_AUDIO -> Icons.Default.RecordVoiceOver
        StudioFeature.GHOJOL_VIDEO -> Icons.Default.Videocam
        StudioFeature.SONG_EDITOR -> Icons.Default.MusicNote
        StudioFeature.SONG_VIDEO -> Icons.Default.VideoLibrary
        StudioFeature.TEXT_TO_VIDEO -> Icons.Default.AutoAwesome
        StudioFeature.QURAN_AUDIO_GEN -> Icons.Default.MenuBook
        StudioFeature.QURAN_VIDEO_GEN -> Icons.Default.OndemandVideo
        StudioFeature.AUTO_EDITING_SUITE -> Icons.Default.AutoFixHigh
    }
}

