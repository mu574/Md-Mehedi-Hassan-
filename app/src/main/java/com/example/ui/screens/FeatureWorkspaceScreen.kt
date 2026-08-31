package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.StudioFeature
import com.example.ui.StudioViewModel
import com.example.ui.components.getFeatureIcon
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldLight
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldGlow
import com.example.ui.theme.PurpleDark
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
fun FeatureWorkspaceScreen(
    viewModel: StudioViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val selectedFeature by viewModel.selectedFeature.collectAsState()
    val promptInput by viewModel.promptInput.collectAsState()
    val surahOrTitle by viewModel.selectedSurahOrTitle.collectAsState()
    val maqamOrGenre by viewModel.selectedMaqamOrGenre.collectAsState()
    val visualTheme by viewModel.selectedVisualTheme.collectAsState()
    val is3DMotion by viewModel.is3DMotionRequested.collectAsState()
    val backgroundDrawable by viewModel.backgroundDrawableName.collectAsState()
    val durationMin by viewModel.durationMinutes.collectAsState()

    val echoDelay by viewModel.echoDelayMs.collectAsState()
    val echoFeedback by viewModel.echoFeedback.collectAsState()
    val reverbDecay by viewModel.reverbDecay.collectAsState()
    val vocalCleaning by viewModel.vocalCleaningEnabled.collectAsState()
    val bassWarmth by viewModel.bassWarmthGain.collectAsState()
    val trebleSparkle by viewModel.trebleSparkleGain.collectAsState()

    val sceneClips by viewModel.sceneClipsCount.collectAsState()
    val selectedTransition by viewModel.selectedTransition.collectAsState()
    val isProcessing by viewModel.isProcessing.collectAsState()
    val processingStep by viewModel.processingStep.collectAsState()
    val processingProgress by viewModel.processingProgress.collectAsState()

    val featureIcon = getFeatureIcon(selectedFeature)
    val isUnlimited = selectedFeature.isUnlimitedDuration

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(StudioObsidian)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 16.dp)
    ) {
        // Header Banner (Clean Minimalism)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = StudioCardBg),
                border = BorderStroke(1.dp, StudioBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(PurplePrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = featureIcon,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = selectedFeature.title,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = selectedFeature.description,
                                fontSize = 11.sp,
                                color = TextSecondary,
                                lineHeight = 15.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = StudioBorder, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(10.dp))

                    // Duration Policy Badge
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(PurpleLight)
                            .border(1.dp, StudioBorder, RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = null,
                            tint = PurpleDeep,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isUnlimited) {
                                "Duration Policy: Unlimited Generation Length (1h, 2h, or longer)"
                            } else {
                                "Strict Duration Policy: 30 to 50 Minutes Maximum Output"
                            },
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = PurpleDeep
                        )
                    }
                }
            }
        }

        // Section 1: Inputs & Context
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = StudioCardBg),
                border = BorderStroke(1.dp, StudioBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "1. SCRIPT, CONTENT & ARRANGEMENT",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = PurplePrimary,
                        letterSpacing = 0.8.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Title or Surah selection
                    val titleLabel = when (selectedFeature) {
                        StudioFeature.RECITATION_AUDIO, StudioFeature.RECITATION_VIDEO, StudioFeature.QURAN_AUDIO_GEN, StudioFeature.QURAN_VIDEO_GEN -> "Surah / Ayah Selection"
                        StudioFeature.GHOJOL_AUDIO, StudioFeature.GHOJOL_VIDEO -> "Ghojol Title & Vocalist"
                        StudioFeature.SONG_EDITOR, StudioFeature.SONG_VIDEO -> "Song Title & Theme"
                        StudioFeature.TEXT_TO_VIDEO -> "Story / Narrative Title"
                        StudioFeature.AUTO_EDITING_SUITE -> "Drama Project / Episode Title"
                    }

                    OutlinedTextField(
                        value = surahOrTitle,
                        onValueChange = { viewModel.selectedSurahOrTitle.value = it },
                        label = { Text(titleLabel, fontSize = 12.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("surah_title_field"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = StudioObsidian,
                            unfocusedContainerColor = StudioObsidian,
                            focusedBorderColor = PurplePrimary,
                            unfocusedBorderColor = StudioBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Detailed Prompt / Lyrics / Story script input
                    val promptLabel = when (selectedFeature) {
                        StudioFeature.RECITATION_AUDIO, StudioFeature.RECITATION_VIDEO -> "Acoustic instructions or Ayah text (e.g., Mosque Echo, Deep Reverb)"
                        StudioFeature.GHOJOL_AUDIO, StudioFeature.GHOJOL_VIDEO -> "Ghojol lyrics & voice style instructions"
                        StudioFeature.SONG_EDITOR, StudioFeature.SONG_VIDEO -> "Enter text lyrics for vocal synthesis & harmony composition"
                        StudioFeature.TEXT_TO_VIDEO -> "Enter story prompt or narrative script to generate video"
                        StudioFeature.QURAN_AUDIO_GEN, StudioFeature.QURAN_VIDEO_GEN -> "Surah Ayah range & Qari vocal specifications"
                        StudioFeature.AUTO_EDITING_SUITE -> "Scene breakdown instructions & voiceover synchronization notes"
                    }

                    OutlinedTextField(
                        value = promptInput,
                        onValueChange = { viewModel.promptInput.value = it },
                        label = { Text(promptLabel, fontSize = 12.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("prompt_details_field"),
                        minLines = 3,
                        maxLines = 5,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = StudioObsidian,
                            unfocusedContainerColor = StudioObsidian,
                            focusedBorderColor = PurplePrimary,
                            unfocusedBorderColor = StudioBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Style / Maqam Selection
                    Text(
                        text = "Harmonic Style / Musical Scale",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    val styleOptions = when (selectedFeature) {
                        StudioFeature.RECITATION_AUDIO, StudioFeature.RECITATION_VIDEO, StudioFeature.QURAN_AUDIO_GEN, StudioFeature.QURAN_VIDEO_GEN ->
                            listOf("Maqam Bayati", "Maqam Rast", "Maqam Hijaz", "Maqam Nahawand", "Maqam Saba", "Maqam Kurd")
                        StudioFeature.GHOJOL_AUDIO, StudioFeature.GHOJOL_VIDEO ->
                            listOf("Spiritual Nasheed", "Sufi Mystic Melody", "Warm Nasal Cadence", "Percussive Daf Rhythm")
                        StudioFeature.SONG_EDITOR, StudioFeature.SONG_VIDEO ->
                            listOf("Acoustic Melody & Strings", "Orchestral Cinematic", "Modern Polyphonic", "Piano Ballad")
                        StudioFeature.TEXT_TO_VIDEO ->
                            listOf("Cinematic Narrative", "Documentary Tone", "Epic Mythological", "Serene Ambient")
                        StudioFeature.AUTO_EDITING_SUITE ->
                            listOf("Dramatic Episode", "Documentary Narrative", "Fast-Paced Action Cuts", "Emotional Symphony")
                    }

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(styleOptions) { style ->
                            val isSelected = maqamOrGenre == style
                            FilterChip(
                                selected = isSelected,
                                onClick = { viewModel.selectedMaqamOrGenre.value = style },
                                label = { Text(style, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = PurplePrimary,
                                    selectedLabelColor = Color.White,
                                    containerColor = StudioObsidian,
                                    labelColor = TextSecondary
                                )
                            )
                        }
                    }

                    // Feature 7 & Video Editors: 3D Motion Animation vs Standard non-3D Video
                    if (selectedFeature == StudioFeature.TEXT_TO_VIDEO ||
                        selectedFeature == StudioFeature.RECITATION_VIDEO ||
                        selectedFeature == StudioFeature.GHOJOL_VIDEO ||
                        selectedFeature == StudioFeature.SONG_VIDEO ||
                        selectedFeature == StudioFeature.QURAN_VIDEO_GEN
                    ) {
                        Spacer(modifier = Modifier.height(14.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(StudioObsidian)
                                .border(1.dp, StudioBorder, RoundedCornerShape(12.dp))
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "3D Motion Animation Style",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (is3DMotion) PurplePrimary else TextPrimary
                                )
                                Text(
                                    text = if (is3DMotion) "Enabled: Dynamic 3D camera pan, depth parallax & particle layers" else "Standard non-3D Video (Default option)",
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                            }
                            Switch(
                                checked = is3DMotion,
                                onCheckedChange = { viewModel.is3DMotionRequested.value = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = PurplePrimary
                                )
                            )
                        }
                    }

                    // Auto Editing Suite Specific: Scene Clips & Transitions
                    if (selectedFeature == StudioFeature.AUTO_EDITING_SUITE) {
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "Scene Clips & Intelligent Cuts",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = PurplePrimary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Fragments Count: $sceneClips clips", fontSize = 12.sp, color = TextPrimary)
                            Spacer(modifier = Modifier.width(12.dp))
                            Slider(
                                value = sceneClips.toFloat(),
                                onValueChange = { viewModel.sceneClipsCount.value = it.toInt() },
                                valueRange = 2f..12f,
                                steps = 9,
                                modifier = Modifier.weight(1f),
                                colors = SliderDefaults.colors(
                                    thumbColor = PurplePrimary,
                                    activeTrackColor = PurplePrimary
                                )
                            )
                        }

                        val transitions = listOf("Cross Dissolve", "Cinematic Fade", "Whip Pan", "Glitch Cut", "Zoom Punch")
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(transitions) { trans ->
                                FilterChip(
                                    selected = selectedTransition == trans,
                                    onClick = { viewModel.selectedTransition.value = trans },
                                    label = { Text(trans, fontSize = 11.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = PurplePrimary,
                                        selectedLabelColor = Color.White,
                                        containerColor = StudioObsidian,
                                        labelColor = TextSecondary
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        // Section 2: Audio DSP & Acoustic Mixing Deck
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = StudioCardBg),
                border = BorderStroke(1.dp, StudioBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.GraphicEq,
                            contentDescription = null,
                            tint = PurplePrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "2. INTELLIGENT AUDIO & DSP MIXING CONSOLE",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = PurplePrimary,
                            letterSpacing = 0.8.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Echo Delay Slider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Multi-Tap Echo Delay", fontSize = 12.sp, color = TextPrimary)
                        Text("${echoDelay.toInt()} ms", fontSize = 12.sp, color = PurplePrimary, fontWeight = FontWeight.Bold)
                    }
                    Slider(
                        value = echoDelay,
                        onValueChange = { viewModel.echoDelayMs.value = it },
                        valueRange = 100f..600f,
                        modifier = Modifier.fillMaxWidth(),
                        colors = SliderDefaults.colors(
                            thumbColor = PurplePrimary,
                            activeTrackColor = PurplePrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Echo Feedback Slider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Echo Feedback Decay", fontSize = 12.sp, color = TextPrimary)
                        Text("${(echoFeedback * 100).toInt()} %", fontSize = 12.sp, color = PurplePrimary, fontWeight = FontWeight.Bold)
                    }
                    Slider(
                        value = echoFeedback,
                        onValueChange = { viewModel.echoFeedback.value = it },
                        valueRange = 0.05f..0.80f,
                        modifier = Modifier.fillMaxWidth(),
                        colors = SliderDefaults.colors(
                            thumbColor = PurplePrimary,
                            activeTrackColor = PurplePrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Acoustic Reverb Slider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Mosque Sanctuary Reverb", fontSize = 12.sp, color = TextPrimary)
                        Text("${(reverbDecay * 100).toInt()} %", fontSize = 12.sp, color = PurplePrimary, fontWeight = FontWeight.Bold)
                    }
                    Slider(
                        value = reverbDecay,
                        onValueChange = { viewModel.reverbDecay.value = it },
                        valueRange = 0.10f..0.90f,
                        modifier = Modifier.fillMaxWidth(),
                        colors = SliderDefaults.colors(
                            thumbColor = PurplePrimary,
                            activeTrackColor = PurplePrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Vocal Cleaning / De-hiss toggle
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(StudioObsidian)
                            .border(1.dp, StudioBorder, RoundedCornerShape(12.dp))
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Spectral Voice Cleaning & De-Noise", fontSize = 12.sp, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                            Text("Automatic 85Hz high-pass filter & vocal air boost", fontSize = 10.sp, color = TextSecondary)
                        }
                        Switch(
                            checked = vocalCleaning,
                            onCheckedChange = { viewModel.vocalCleaningEnabled.value = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = PurplePrimary
                            )
                        )
                    }
                }
            }
        }

        // Section 3: Visuals / Background Staging
        if (selectedFeature != StudioFeature.RECITATION_AUDIO &&
            selectedFeature != StudioFeature.GHOJOL_AUDIO &&
            selectedFeature != StudioFeature.SONG_EDITOR &&
            selectedFeature != StudioFeature.QURAN_AUDIO_GEN
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = StudioCardBg),
                    border = BorderStroke(1.dp, StudioBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Image,
                                contentDescription = null,
                                tint = PurplePrimary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "3. BACKGROUND PHOTO & VISUAL STAGE",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = PurplePrimary,
                                letterSpacing = 0.8.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        val bgDrawables = listOf(
                            Pair("img_recitation_bg", "Sanctuary Arch"),
                            Pair("img_music_studio_bg", "Cyber Synth Studio"),
                            Pair("img_cinema_bg", "Cinema Stage 3D")
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            bgDrawables.forEach { (drawableRes, name) ->
                                val isSelected = backgroundDrawable == drawableRes
                                val resId = when (drawableRes) {
                                    "img_recitation_bg" -> R.drawable.img_recitation_bg
                                    "img_music_studio_bg" -> R.drawable.img_music_studio_bg
                                    else -> R.drawable.img_cinema_bg
                                }

                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .border(
                                            2.dp,
                                            if (isSelected) PurplePrimary else StudioBorder,
                                            RoundedCornerShape(12.dp)
                                        )
                                        .clickable { viewModel.backgroundDrawableName.value = drawableRes }
                                        .background(StudioObsidian)
                                        .padding(6.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Image(
                                        painter = painterResource(id = resId),
                                        contentDescription = name,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(60.dp)
                                            .clip(RoundedCornerShape(8.dp)),
                                        contentScale = ContentScale.Crop
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = name,
                                        fontSize = 10.sp,
                                        color = if (isSelected) PurplePrimary else TextSecondary,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Section 4: Target Duration (Enforcing 30-50m for 1-9, unlimited for 10)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = StudioCardBg),
                border = BorderStroke(1.dp, StudioBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = null,
                            tint = PurplePrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isUnlimited) "4. DURATION (UNLIMITED GENERATION LENGTH)" else "4. OUTPUT DURATION (30 TO 50 MINUTES STRICT)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = PurplePrimary,
                            letterSpacing = 0.8.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Target Master Length:",
                            fontSize = 13.sp,
                            color = TextPrimary
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(PurpleLight)
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = if (durationMin >= 60) "${durationMin / 60}h ${durationMin % 60}m" else "$durationMin:00 minutes",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = PurpleDeep
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Preset Buttons
                    val presets = if (isUnlimited) {
                        listOf(30, 45, 60, 90, 120, 180)
                    } else {
                        listOf(30, 35, 40, 45, 50)
                    }

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(presets) { preset ->
                            val label = if (preset >= 60) "${preset / 60}h" else "${preset}m"
                            FilterChip(
                                selected = durationMin == preset,
                                onClick = { viewModel.setDuration(preset) },
                                label = { Text(label, fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = PurplePrimary,
                                    selectedLabelColor = Color.White,
                                    containerColor = StudioObsidian,
                                    labelColor = TextSecondary
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Fine Slider
                    if (isUnlimited) {
                        Slider(
                            value = durationMin.toFloat(),
                            onValueChange = { viewModel.setDuration(it.toInt()) },
                            valueRange = 10f..240f,
                            modifier = Modifier.fillMaxWidth(),
                            colors = SliderDefaults.colors(
                                thumbColor = PurplePrimary,
                                activeTrackColor = PurplePrimary
                            )
                        )
                    } else {
                        // Strictly 30 to 50 min
                        Slider(
                            value = durationMin.toFloat().coerceIn(30f, 50f),
                            onValueChange = { viewModel.setDuration(it.toInt()) },
                            valueRange = 30f..50f,
                            steps = 19,
                            modifier = Modifier.fillMaxWidth(),
                            colors = SliderDefaults.colors(
                                thumbColor = PurplePrimary,
                                activeTrackColor = PurplePrimary
                            )
                        )
                    }
                }
            }
        }

        // Processing progress or Action Button
        item {
            if (isProcessing) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = StudioCardBg),
                    border = BorderStroke(1.dp, StudioBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            color = PurplePrimary,
                            strokeWidth = 3.dp,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = processingStep,
                            fontSize = 13.sp,
                            color = PurplePrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { processingProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = PurplePrimary,
                            trackColor = StudioObsidian,
                        )
                    }
                }
            } else {
                Button(
                    onClick = { viewModel.executeGeneration(context) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("execute_generation_button"),
                    shape = RoundedCornerShape(26.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PurplePrimary,
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "SYNTHESIZE & RENDER MASTER ($durationMin MIN)",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
