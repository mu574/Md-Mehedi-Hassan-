package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class StudioFeature(
    val id: String,
    val title: String,
    val shortName: String,
    val description: String,
    val iconName: String,
    val isUnlimitedDuration: Boolean = false
) {
    RECITATION_AUDIO(
        id = "recitation_audio",
        title = "Recitation Audio Editor",
        shortName = "Recitation Audio",
        description = "Intelligently analyze audio structure and apply optimal echo, reverb & professional mixing",
        iconName = "graphic_eq"
    ),
    RECITATION_VIDEO(
        id = "recitation_video",
        title = "Recitation Video Editor",
        shortName = "Recitation Video",
        description = "Align audio recitation track with background image/photo and render high-quality video",
        iconName = "movie_creation"
    ),
    GHOJOL_AUDIO(
        id = "ghojol_audio",
        title = "Islamic Song (Ghojol) Audio Editor",
        shortName = "Ghojol Audio",
        description = "Clean, trim, process raw voice recordings of Islamic songs with download & share",
        iconName = "mic"
    ),
    GHOJOL_VIDEO(
        id = "ghojol_video",
        title = "Islamic Song (Ghojol) Video Editor",
        shortName = "Ghojol Video",
        description = "Combine Ghojol audio, photo & stylistic visual themes into finished master video",
        iconName = "videocam"
    ),
    SONG_EDITOR(
        id = "song_editor",
        title = "Song Editor & Vocal Synthesizer",
        shortName = "Song Editor",
        description = "Synthesize vocals from lyrics & compose matching background instrumental music",
        iconName = "music_note"
    ),
    SONG_VIDEO(
        id = "song_video",
        title = "Song Video Editor",
        shortName = "Song Video",
        description = "Generate vocal/instrumental audio and full edited professional music video presentation",
        iconName = "video_library"
    ),
    TEXT_TO_VIDEO(
        id = "text_to_video",
        title = "Text-to-Video Generator",
        shortName = "Text to Video",
        description = "Render complete videos with 3D Motion Animation or Standard cinematic styles",
        iconName = "auto_awesome_motion"
    ),
    QURAN_AUDIO_GEN(
        id = "quran_audio_gen",
        title = "Quran Recitation Audio Generator",
        shortName = "Quran Audio Gen",
        description = "Generate audio recitations from any Surah/Ayah with echo, resonance & acoustic balance",
        iconName = "menu_book"
    ),
    QURAN_VIDEO_GEN(
        id = "quran_video_gen",
        title = "Quran Recitation Video Editor",
        shortName = "Quran Video Gen",
        description = "Generate custom video renders for Quranic recitations with typography & visual styling",
        iconName = "ondemand_video"
    ),
    AUTO_EDITING_SUITE(
        id = "auto_editing_suite",
        title = "Auto Editing Suite",
        shortName = "Auto Editing",
        description = "Multi-fragment video clips + dedicated voiceover with intelligent cuts, transitions & sync (Unlimited duration)",
        iconName = "auto_fix_high",
        isUnlimitedDuration = true
    )
}

@Entity(tableName = "studio_projects")
data class StudioProject(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val featureId: String,
    val title: String,
    val promptOrInput: String,
    val durationMinutes: Int, // 30-50 min for features 1-9, or any for feature 10
    val durationFormatted: String,
    val stylePreset: String,
    val echoLevel: Float = 0.35f,
    val reverbLevel: Float = 0.5f,
    val audioPath: String? = null,
    val videoThumbnailRes: String? = null,
    val isCompleted: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val metadataDetails: String = ""
)
