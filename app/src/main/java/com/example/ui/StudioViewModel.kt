package com.example.ui

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.R
import com.example.ai.GeminiAssistantService
import com.example.data.db.AppDatabase
import com.example.data.model.StudioFeature
import com.example.data.model.StudioProject
import com.example.data.repository.StudioProjectRepository
import com.example.engine.AudioEngine
import com.example.engine.PlaybackController
import com.example.engine.VideoEngine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

enum class ScreenDestination {
    CHAT_STUDIO,
    FEATURE_WORKSPACE,
    PROJECT_LIBRARY,
    MEDIA_PLAYER
}

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val isUser: Boolean,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val attachedFeature: StudioFeature? = null,
    val suggestedDurationMin: Int? = null,
    val generatedProject: StudioProject? = null
)

class StudioViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: StudioProjectRepository
    val playbackController = PlaybackController(application)

    init {
        val db = AppDatabase.getDatabase(application)
        repository = StudioProjectRepository(db.studioProjectDao())
    }

    val savedProjects: StateFlow<List<StudioProject>> = repository.allProjects
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _currentScreen = MutableStateFlow(ScreenDestination.CHAT_STUDIO)
    val currentScreen: StateFlow<ScreenDestination> = _currentScreen.asStateFlow()

    private val _selectedFeature = MutableStateFlow(StudioFeature.RECITATION_AUDIO)
    val selectedFeature: StateFlow<StudioFeature> = _selectedFeature.asStateFlow()

    private val _activeProject = MutableStateFlow<StudioProject?>(null)
    val activeProject: StateFlow<StudioProject?> = _activeProject.asStateFlow()

    // Studio Configuration State
    val promptInput = MutableStateFlow("")
    val selectedSurahOrTitle = MutableStateFlow("Surah Ar-Rahman (The Most Merciful)")
    val selectedMaqamOrGenre = MutableStateFlow("Maqam Bayati")
    val selectedVisualTheme = MutableStateFlow("Golden Sanctuary Archway")
    val is3DMotionRequested = MutableStateFlow(false)
    val backgroundDrawableName = MutableStateFlow("img_recitation_bg")
    
    // Duration in minutes (Strictly 30-50 min for features 1-9, unlimited for feature 10)
    val durationMinutes = MutableStateFlow(35)
    
    // DSP Audio Parameters
    val echoDelayMs = MutableStateFlow(280f)
    val echoFeedback = MutableStateFlow(0.40f)
    val reverbDecay = MutableStateFlow(0.55f)
    val vocalCleaningEnabled = MutableStateFlow(true)
    val bassWarmthGain = MutableStateFlow(3.5f)
    val trebleSparkleGain = MutableStateFlow(2.8f)

    // Auto Editing Suite specific state
    val sceneClipsCount = MutableStateFlow(4)
    val selectedTransition = MutableStateFlow("Cross Dissolve")
    val voiceoverSyncMode = MutableStateFlow("Smart Auto-Cut & Ducking")

    // Processing & Progress
    private val _isProcessing = MutableStateFlow(false)
    val isProcessing: StateFlow<Boolean> = _isProcessing.asStateFlow()

    private val _processingStep = MutableStateFlow("")
    val processingStep: StateFlow<String> = _processingStep.asStateFlow()

    private val _processingProgress = MutableStateFlow(0f)
    val processingProgress: StateFlow<Float> = _processingProgress.asStateFlow()

    // Chat History
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                isUser = false,
                text = "Salam & Welcome to **MediaStudio AI**! 🎙️✨\n\nI am your intelligent studio assistant for audio processing, Quran recitation & Ghojol editing, song vocal synthesis, text-to-video generation, and auto video editing.\n\nChoose a studio from the top menu or tell me what you'd like to create!",
                attachedFeature = null
            )
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    fun navigateTo(destination: ScreenDestination) {
        _currentScreen.value = destination
    }

    fun openFeatureStudio(feature: StudioFeature) {
        _selectedFeature.value = feature
        // Adjust duration constraints
        if (feature.isUnlimitedDuration) {
            durationMinutes.value = 60 // 1 hour default for auto editing
        } else {
            durationMinutes.value = durationMinutes.value.coerceIn(30, 50)
        }
        
        // Contextual defaults per feature
        when (feature) {
            StudioFeature.RECITATION_AUDIO, StudioFeature.RECITATION_VIDEO, StudioFeature.QURAN_AUDIO_GEN, StudioFeature.QURAN_VIDEO_GEN -> {
                selectedMaqamOrGenre.value = "Maqam Bayati"
                backgroundDrawableName.value = "img_recitation_bg"
                selectedVisualTheme.value = "Golden Sanctuary Archway"
            }
            StudioFeature.GHOJOL_AUDIO, StudioFeature.GHOJOL_VIDEO -> {
                selectedMaqamOrGenre.value = "Spiritual Nasheed / Ghojol"
                backgroundDrawableName.value = "img_recitation_bg"
                selectedVisualTheme.value = "Celestial Starry Night"
            }
            StudioFeature.SONG_EDITOR, StudioFeature.SONG_VIDEO -> {
                selectedMaqamOrGenre.value = "Acoustic Melody & Strings"
                backgroundDrawableName.value = "img_music_studio_bg"
                selectedVisualTheme.value = "Modern Neon Studio"
            }
            StudioFeature.TEXT_TO_VIDEO -> {
                backgroundDrawableName.value = "img_cinema_bg"
                selectedVisualTheme.value = if (is3DMotionRequested.value) "3D Motion Animation" else "Standard Cinematic"
            }
            StudioFeature.AUTO_EDITING_SUITE -> {
                backgroundDrawableName.value = "img_cinema_bg"
                selectedVisualTheme.value = "Cinematic Multi-Scene Drama"
            }
        }

        _currentScreen.value = ScreenDestination.FEATURE_WORKSPACE
    }

    fun setDuration(minutes: Int) {
        if (_selectedFeature.value.isUnlimitedDuration) {
            durationMinutes.value = minutes.coerceAtLeast(1)
        } else {
            // Strictly 30 to 50 minutes restriction for features 1 through 9
            durationMinutes.value = minutes.coerceIn(30, 50)
        }
    }

    fun sendChatMessage(userText: String) {
        if (userText.isBlank()) return
        val userMsg = ChatMessage(isUser = true, text = userText)
        _chatMessages.value = _chatMessages.value + userMsg

        viewModelScope.launch {
            _isProcessing.value = true
            _processingStep.value = "AI Assistant analyzing requirements..."
            
            val lower = userText.lowercase()
            val matchedFeature = when {
                lower.contains("recitation audio") || lower.contains("recitation echo") -> StudioFeature.RECITATION_AUDIO
                lower.contains("recitation video") -> StudioFeature.RECITATION_VIDEO
                lower.contains("ghojol audio") || lower.contains("clean ghojol") -> StudioFeature.GHOJOL_AUDIO
                lower.contains("ghojol video") -> StudioFeature.GHOJOL_VIDEO
                lower.contains("song editor") || lower.contains("synthesize vocals") -> StudioFeature.SONG_EDITOR
                lower.contains("song video") -> StudioFeature.SONG_VIDEO
                lower.contains("3d motion") || lower.contains("text to video") || lower.contains("story video") -> StudioFeature.TEXT_TO_VIDEO
                lower.contains("quran audio") || lower.contains("recite surah") -> StudioFeature.QURAN_AUDIO_GEN
                lower.contains("quran video") -> StudioFeature.QURAN_VIDEO_GEN
                lower.contains("auto edit") || lower.contains("drama") || lower.contains("voiceover") -> StudioFeature.AUTO_EDITING_SUITE
                else -> null
            }

            if (lower.contains("3d")) {
                is3DMotionRequested.value = true
            }

            val aiResponseText = GeminiAssistantService.analyzeOrGenerateContent(
                prompt = userText,
                systemInstruction = "You are MediaStudio AI, a professional multimedia AI assistant executing audio DSP, Quran & Ghojol mastering, song synthesis, text-to-video rendering, and auto video editing with strict 30-50m duration limits (or unlimited for auto editing suite)."
            )

            val suggestedMin = if (matchedFeature?.isUnlimitedDuration == true) 60 else 35
            val aiMsg = ChatMessage(
                isUser = false,
                text = aiResponseText,
                attachedFeature = matchedFeature,
                suggestedDurationMin = suggestedMin
            )

            _chatMessages.value = _chatMessages.value + aiMsg
            _isProcessing.value = false
        }
    }

    fun executeGeneration(context: Context) {
        viewModelScope.launch {
            _isProcessing.value = true
            val feature = _selectedFeature.value
            val targetMinutes = if (feature.isUnlimitedDuration) {
                durationMinutes.value.coerceAtLeast(1)
            } else {
                durationMinutes.value.coerceIn(30, 50)
            }
            val formattedDuration = if (targetMinutes >= 60) {
                val hrs = targetMinutes / 60
                val mins = targetMinutes % 60
                String.format("%02d:%02d:00", hrs, mins)
            } else {
                String.format("%02d:00", targetMinutes)
            }

            _processingStep.value = "Initializing DSP audio synthesis & analysis engine..."
            _processingProgress.value = 0.15f

            val soundType = when (feature) {
                StudioFeature.RECITATION_AUDIO, StudioFeature.RECITATION_VIDEO, StudioFeature.QURAN_AUDIO_GEN, StudioFeature.QURAN_VIDEO_GEN -> AudioEngine.SoundType.QURAN_RECITATION
                StudioFeature.GHOJOL_AUDIO, StudioFeature.GHOJOL_VIDEO -> AudioEngine.SoundType.ISLAMIC_GHOJOL
                StudioFeature.SONG_EDITOR, StudioFeature.SONG_VIDEO -> AudioEngine.SoundType.SONG_SYNTHESIS
                StudioFeature.TEXT_TO_VIDEO -> AudioEngine.SoundType.VOICEOVER_NARRATION
                StudioFeature.AUTO_EDITING_SUITE -> AudioEngine.SoundType.DRAMA_SCORE
            }

            _processingStep.value = "Applying acoustic reverb (${(reverbDecay.value * 100).toInt()}%) & echo (${echoDelayMs.value.toInt()}ms)..."
            _processingProgress.value = 0.45f

            val generatedAudioFile = AudioEngine.generateAudioTrack(
                context = context,
                fileName = "${feature.id}_${selectedSurahOrTitle.value.take(15)}",
                soundType = soundType,
                durationSeconds = 25,
                echoDelayMs = echoDelayMs.value.toInt(),
                echoFeedback = echoFeedback.value,
                reverbDecay = reverbDecay.value,
                maqamScale = selectedMaqamOrGenre.value,
                lyricsOrPrompt = promptInput.value
            )

            _processingStep.value = "Assembling video visualizer, 3D motion layers & typography..."
            _processingProgress.value = 0.75f

            val videoSpec = VideoEngine.VideoRenderSpec(
                title = selectedSurahOrTitle.value.ifBlank { "${feature.title} Master" },
                feature = feature,
                durationMinutes = targetMinutes,
                visualTheme = selectedVisualTheme.value,
                is3DMotion = is3DMotionRequested.value,
                backgroundDrawableName = backgroundDrawableName.value,
                audioFile = generatedAudioFile,
                sceneClipsCount = sceneClipsCount.value,
                transitionType = selectedTransition.value
            )

            val generatedVideoFile = VideoEngine.renderVideoPackage(context, videoSpec)

            _processingStep.value = "Saving project master to database..."
            _processingProgress.value = 0.95f

            val project = StudioProject(
                featureId = feature.id,
                title = selectedSurahOrTitle.value.ifBlank { "${feature.title} Master" },
                promptOrInput = promptInput.value.ifBlank { "${feature.description} - ${selectedMaqamOrGenre.value}" },
                durationMinutes = targetMinutes,
                durationFormatted = formattedDuration,
                stylePreset = "${selectedMaqamOrGenre.value} • ${selectedVisualTheme.value}" + if (is3DMotionRequested.value) " [3D Motion]" else "",
                echoLevel = echoFeedback.value,
                reverbLevel = reverbDecay.value,
                audioPath = generatedAudioFile.absolutePath,
                videoThumbnailRes = backgroundDrawableName.value,
                isCompleted = true,
                metadataDetails = "Transition: ${selectedTransition.value} | Scenes: ${sceneClipsCount.value} | Echo: ${echoDelayMs.value.toInt()}ms"
            )

            val newId = repository.saveProject(project)
            val fullProject = project.copy(id = newId)

            _activeProject.value = fullProject
            playbackController.playFile(generatedAudioFile, fullProject.title)

            _processingProgress.value = 1.0f
            _isProcessing.value = false
            _currentScreen.value = ScreenDestination.MEDIA_PLAYER

            Toast.makeText(context, "Production Complete: ${fullProject.title} ($formattedDuration)", Toast.LENGTH_LONG).show()
        }
    }

    fun openProjectInPlayer(project: StudioProject) {
        _activeProject.value = project
        project.audioPath?.let { path ->
            val file = File(path)
            if (file.exists()) {
                playbackController.playFile(file, project.title)
            }
        }
        _currentScreen.value = ScreenDestination.MEDIA_PLAYER
    }

    fun deleteProject(project: StudioProject) {
        viewModelScope.launch {
            repository.deleteProject(project)
            if (_activeProject.value?.id == project.id) {
                playbackController.stop()
                _activeProject.value = null
            }
        }
    }

    fun shareCurrentAudio(context: Context) {
        val project = _activeProject.value ?: return
        val path = project.audioPath ?: return
        val file = File(path)
        if (file.exists()) {
            VideoEngine.shareMediaFile(context, file, "audio/wav", "${project.title}.wav")
        } else {
            Toast.makeText(context, "Audio file not found", Toast.LENGTH_SHORT).show()
        }
    }

    fun shareCurrentVideo(context: Context) {
        val project = _activeProject.value ?: return
        // Share video container
        val outputDir = File(context.filesDir, "video_exports")
        val sanitized = project.title.replace(Regex("[^a-zA-Z0-9_]"), "_")
        val matchingFile = outputDir.listFiles()?.firstOrNull { it.name.startsWith(sanitized) }
        if (matchingFile != null && matchingFile.exists()) {
            VideoEngine.shareMediaFile(context, matchingFile, "video/mp4", "${project.title}.mp4")
        } else {
            // If specific file not found, share the audio
            shareCurrentAudio(context)
        }
    }

    override fun onCleared() {
        super.onCleared()
        playbackController.release()
    }
}
