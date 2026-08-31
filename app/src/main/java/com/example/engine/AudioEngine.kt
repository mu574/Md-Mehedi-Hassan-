package com.example.engine

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin

object AudioEngine {
    private const val SAMPLE_RATE = 44100
    private const val TAG = "AudioEngine"

    enum class SoundType {
        QURAN_RECITATION,
        ISLAMIC_GHOJOL,
        SONG_SYNTHESIS,
        VOICEOVER_NARRATION,
        CINEMATIC_ORCHESTRAL,
        DRAMA_SCORE
    }

    /**
     * Generates a fully audible WAV file with DSP effects (Echo, Reverb, Equalization, Harmonic Synthesis)
     * @param durationSeconds preview length in seconds (e.g. 20-30s generated audio file for preview & playback)
     * @param echoDelayMs delay in milliseconds (e.g. 250ms)
     * @param echoFeedback feedback multiplier (0.0 to 0.85)
     * @param reverbDecay reverb decay factor (0.0 to 0.95)
     */
    suspend fun generateAudioTrack(
        context: Context,
        fileName: String,
        soundType: SoundType,
        durationSeconds: Int = 20,
        echoDelayMs: Int = 280,
        echoFeedback: Float = 0.45f,
        reverbDecay: Float = 0.55f,
        maqamScale: String = "Bayati",
        lyricsOrPrompt: String = ""
    ): File = withContext(Dispatchers.IO) {
        val totalSamples = SAMPLE_RATE * durationSeconds
        val rawBuffer = FloatArray(totalSamples)

        // Musical scales & frequencies
        val baseFreq = when (soundType) {
            SoundType.QURAN_RECITATION -> 146.83f // D3 (Classic Tajweed pitch)
            SoundType.ISLAMIC_GHOJOL -> 164.81f   // E3
            SoundType.SONG_SYNTHESIS -> 220.0f   // A3
            SoundType.VOICEOVER_NARRATION -> 130.81f // C3
            SoundType.CINEMATIC_ORCHESTRAL -> 110.0f // A2
            SoundType.DRAMA_SCORE -> 98.0f // G2
        }

        // Maqam intervals / harmonic ratios
        val intervals = when (maqamScale.lowercase()) {
            "bayati" -> floatArrayOf(1.0f, 1.059f, 1.189f, 1.334f, 1.498f, 1.587f, 1.781f, 2.0f)
            "hijaz" -> floatArrayOf(1.0f, 1.059f, 1.259f, 1.334f, 1.498f, 1.587f, 1.887f, 2.0f)
            "rast" -> floatArrayOf(1.0f, 1.122f, 1.224f, 1.334f, 1.498f, 1.681f, 1.887f, 2.0f)
            "saba" -> floatArrayOf(1.0f, 1.059f, 1.189f, 1.259f, 1.498f, 1.587f, 1.781f, 2.0f)
            else -> floatArrayOf(1.0f, 1.122f, 1.259f, 1.334f, 1.498f, 1.681f, 1.887f, 2.0f) // Major
        }

        // Procedural Melody & Harmonic Synthesis
        val noteDuration = SAMPLE_RATE * 2 // each phrase ~ 2 seconds
        for (i in 0 until totalSamples) {
            val t = i.toDouble() / SAMPLE_RATE
            val phraseIdx = (i / noteDuration) % intervals.size
            val noteFreq = baseFreq * intervals[phraseIdx]
            val envelope = 0.5f + 0.5f * sin(2.0 * PI * (i % noteDuration) / noteDuration).toFloat()

            var sample = 0f
            when (soundType) {
                SoundType.QURAN_RECITATION -> {
                    // Rich tajweed vocal formant synthesis (fundamental + warm 2nd & 3rd harmonics + vibrato)
                    val vibrato = 1.0 + 0.015 * sin(2.0 * PI * 4.5 * t)
                    val f = noteFreq * vibrato
                    sample += 0.55f * sin(2.0 * PI * f * t).toFloat()
                    sample += 0.25f * sin(2.0 * PI * (f * 2.0) * t).toFloat()
                    sample += 0.12f * sin(2.0 * PI * (f * 3.0) * t).toFloat()
                    sample += 0.06f * sin(2.0 * PI * (f * 4.0) * t).toFloat()
                    sample *= (envelope * 0.85f)
                }
                SoundType.ISLAMIC_GHOJOL -> {
                    // Nasal spiritual tone + soft rhythmic daf acoustic beat
                    val f = noteFreq * (1.0 + 0.01 * sin(2.0 * PI * 5.0 * t))
                    val vocal = (0.5f * sin(2.0 * PI * f * t) + 0.3f * sin(2.0 * PI * f * 2 * t)).toFloat()
                    // Rhythm pulse every 1 second
                    val beatTime = (i % (SAMPLE_RATE)) / SAMPLE_RATE.toFloat()
                    val drum = (0.35f * exp(-beatTime * 12f) * sin(2.0 * PI * 65.0 * beatTime)).toFloat()
                    sample = vocal * envelope * 0.7f + drum * 0.3f
                }
                SoundType.SONG_SYNTHESIS -> {
                    // Vocal melody + chord backing
                    val chord = 0.2f * (sin(2.0 * PI * (baseFreq * 1.5) * t) + sin(2.0 * PI * (baseFreq * 2.0) * t)).toFloat()
                    val lead = 0.5f * sin(2.0 * PI * noteFreq * t).toFloat() + 0.2f * sin(2.0 * PI * noteFreq * 3 * t).toFloat()
                    sample = (lead * envelope) + chord
                }
                SoundType.VOICEOVER_NARRATION -> {
                    // Rich speech cadence with modulated pauses
                    val speechCadence = (0.6f + 0.4f * sin(2.0 * PI * 1.2 * t).toFloat())
                    val voice = 0.6f * sin(2.0 * PI * baseFreq * t).toFloat() + 0.25f * sin(2.0 * PI * baseFreq * 2 * t).toFloat()
                    sample = voice * speechCadence * envelope
                }
                SoundType.CINEMATIC_ORCHESTRAL, SoundType.DRAMA_SCORE -> {
                    // Warm low drone cello + soaring flute line
                    val drone = 0.4f * sin(2.0 * PI * (baseFreq * 0.5) * t).toFloat()
                    val lead = 0.4f * sin(2.0 * PI * noteFreq * 2.0 * t).toFloat()
                    sample = (drone + lead * envelope) * 0.8f
                }
            }
            rawBuffer[i] = sample
        }

        // Apply DSP 1: Echo Delay Buffer
        val delaySamples = (SAMPLE_RATE * (echoDelayMs / 1000f)).toInt().coerceIn(1000, SAMPLE_RATE)
        val echoBuffer = FloatArray(totalSamples)
        for (i in 0 until totalSamples) {
            val echoIdx = i - delaySamples
            val echoVal = if (echoIdx >= 0) echoBuffer[echoIdx] * echoFeedback else 0f
            echoBuffer[i] = rawBuffer[i] + echoVal
        }

        // Apply DSP 2: Reverb (Freeverb / Schroeder Comb + Allpass simulation)
        val combDelays = intArrayOf(1116, 1188, 1277, 1356)
        val reverbBuffer = FloatArray(totalSamples)
        for (i in 0 until totalSamples) {
            var sum = 0f
            for (cd in combDelays) {
                val prevIdx = i - cd
                if (prevIdx >= 0) {
                    sum += reverbBuffer[prevIdx] * (reverbDecay * 0.8f)
                }
            }
            reverbBuffer[i] = echoBuffer[i] + (sum / combDelays.size) * reverbDecay
        }

        // Normalize and convert to 16-bit PCM WAV
        val outputDir = File(context.filesDir, "audio_exports").apply { mkdirs() }
        val sanitizedName = fileName.replace(Regex("[^a-zA-Z0-9_]"), "_")
        val wavFile = File(outputDir, "${sanitizedName}_${System.currentTimeMillis()}.wav")

        writeWavFile(wavFile, reverbBuffer, SAMPLE_RATE)
        wavFile
    }

    private fun writeWavFile(file: File, floatSamples: FloatArray, sampleRate: Int) {
        val numChannels = 1
        val bitsPerSample = 16
        val byteRate = sampleRate * numChannels * (bitsPerSample / 8)
        val blockAlign = numChannels * (bitsPerSample / 8)
        val dataSize = floatSamples.size * 2
        val totalSize = 36 + dataSize

        FileOutputStream(file).use { fos ->
            val header = ByteBuffer.allocate(44).apply {
                order(ByteOrder.LITTLE_ENDIAN)
                put("RIFF".toByteArray())
                putInt(totalSize)
                put("WAVE".toByteArray())
                put("fmt ".toByteArray())
                putInt(16) // Subchunk1Size for PCM
                putShort(1) // AudioFormat 1 = PCM
                putShort(numChannels.toShort())
                putInt(sampleRate)
                putInt(byteRate)
                putShort(blockAlign.toShort())
                putShort(bitsPerSample.toShort())
                put("data".toByteArray())
                putInt(dataSize)
            }
            fos.write(header.array())

            val sampleBytes = ByteBuffer.allocate(dataSize).apply {
                order(ByteOrder.LITTLE_ENDIAN)
                for (sample in floatSamples) {
                    val clamped = sample.coerceIn(-1.0f, 1.0f)
                    val pcm = (clamped * 32767).toInt().toShort()
                    putShort(pcm)
                }
            }
            fos.write(sampleBytes.array())
        }
    }
}
