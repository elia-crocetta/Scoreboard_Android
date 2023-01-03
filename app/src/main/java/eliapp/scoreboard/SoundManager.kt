package eliapp.scoreboard

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import kotlin.Exception

data class SoundManager(val context: Context, val crowdEnabled: Boolean) {
    private var refereeMediaPlayer: MediaPlayer? = null
    private var crowdBackgroundMediaPlayer: MediaPlayer? = null
    private var crowdOnActionMediaPlayer: MediaPlayer? = null

    fun refereeAction(id: Int) {
        if (!crowdEnabled) return
        val stringFileName = "refereeWhistle"

        if (refereeMediaPlayer?.isPlaying == true) {
            refereeMediaPlayer?.stop(stringFileName)
            refereeMediaPlayer?.reset(stringFileName)
        }

        val assetFileDescriptor = context.resources.openRawResourceFd(id) ?: return

        refereeMediaPlayer = MediaPlayer().apply {
            setOnPreparedListener {
                start(stringFileName)
            }

            setOnCompletionListener {
                reset(stringFileName)
            }
        }

        refereeMediaPlayer?.run {
            reset(stringFileName)
            setDataSource(
                assetFileDescriptor.fileDescriptor,
                assetFileDescriptor.startOffset,
                assetFileDescriptor.declaredLength
            )
            prepareAsync()
        }
    }

    fun crowdStartupScoreboard() {
        if (!crowdEnabled) return
        val stringFileName = "crowdBackground"

        if (crowdBackgroundMediaPlayer?.isPlaying == true) {
            crowdBackgroundMediaPlayer?.stop(stringFileName)
            crowdBackgroundMediaPlayer?.reset(stringFileName)
        }

        val assetFileDescriptor =
            context.resources.openRawResourceFd(R.raw.crowd_startup_scoreboard) ?: return

        crowdBackgroundMediaPlayer = MediaPlayer().apply {
            setOnPreparedListener {
                start(stringFileName)
            }

            setOnCompletionListener {
                reset(stringFileName)
                crowdStartupScoreboard()
            }
        }

        crowdBackgroundMediaPlayer?.run {
            reset("crowdStartupScoreboard")
            setDataSource(
                assetFileDescriptor.fileDescriptor,
                assetFileDescriptor.startOffset,
                assetFileDescriptor.declaredLength
            )
            prepareAsync()
        }
    }

    fun crowdHalfTime() {
        if (!crowdEnabled) return
        val stringFileName = "crowdHalfTime"

        if (crowdBackgroundMediaPlayer?.isPlaying == true) {
            crowdBackgroundMediaPlayer?.stop(stringFileName)
            crowdBackgroundMediaPlayer?.reset(stringFileName)
        }

        val assetFileDescriptor =
            context.resources.openRawResourceFd(R.raw.crowd_half_time) ?: return

        crowdBackgroundMediaPlayer = MediaPlayer().apply {
            setOnPreparedListener {
                start(stringFileName)
            }

            setOnCompletionListener {
                reset(stringFileName)
                crowdHalfTime()
            }
        }

        crowdBackgroundMediaPlayer?.run {
            reset(stringFileName)
            setDataSource(
                assetFileDescriptor.fileDescriptor,
                assetFileDescriptor.startOffset,
                assetFileDescriptor.declaredLength
            )
            prepareAsync()
        }
    }

    fun crowdGoalHome(idResource: Int) {
        if (!crowdEnabled) return
        val stringFileName = "crowdGoalHome"

        if (crowdOnActionMediaPlayer?.isPlaying == true) {
            crowdOnActionMediaPlayer?.stop(stringFileName)
            crowdOnActionMediaPlayer?.reset(stringFileName)
        }


        val assetFileDescriptor = context.resources.openRawResourceFd(idResource) ?: return

        crowdOnActionMediaPlayer = MediaPlayer().apply {
            setOnPreparedListener {
                start(stringFileName)
            }

            setOnCompletionListener {
                reset(stringFileName)
                if (idResource == R.raw.crowd_goal_home_2) return@setOnCompletionListener
                crowdGoalHome(R.raw.crowd_goal_home_2)
            }
        }

        crowdOnActionMediaPlayer?.run {
            reset(stringFileName)
            setDataSource(
                assetFileDescriptor.fileDescriptor,
                assetFileDescriptor.startOffset,
                assetFileDescriptor.declaredLength
            )
            prepareAsync()
        }
    }

    fun crowdGoalAway() {
        if (!crowdEnabled) return
        val stringFileName = "crowdAwayGoal"

        if (crowdOnActionMediaPlayer?.isPlaying == true) {
            crowdOnActionMediaPlayer?.stop(stringFileName)
            crowdOnActionMediaPlayer?.reset(stringFileName)
        }

        val assetFileDescriptor =
            context.resources.openRawResourceFd(R.raw.crowd_goal_away) ?: return

        crowdOnActionMediaPlayer = MediaPlayer().apply {
            setOnPreparedListener {
                start(stringFileName)
            }

            setOnCompletionListener {
                reset(stringFileName)
            }
        }

        crowdOnActionMediaPlayer?.run {
            reset(stringFileName)
            setDataSource(
                assetFileDescriptor.fileDescriptor,
                assetFileDescriptor.startOffset,
                assetFileDescriptor.declaredLength
            )
            prepareAsync()
        }
    }

    fun crowdPenaltyScored() {
        if (!crowdEnabled) return
        val stringFileName = "crowdPenaltyScored"

        if (crowdOnActionMediaPlayer?.isPlaying == true) {
            crowdOnActionMediaPlayer?.stop(stringFileName)
            crowdOnActionMediaPlayer?.reset(stringFileName)
        }

        val assetFileDescriptor =
            context.resources.openRawResourceFd(R.raw.crowd_goal_home) ?: return

        crowdOnActionMediaPlayer = MediaPlayer().apply {
            setOnPreparedListener {
                start(stringFileName)
            }

            setOnCompletionListener {
                reset(stringFileName)
            }
        }

        crowdOnActionMediaPlayer?.run {
            reset(stringFileName)
            setDataSource(
                assetFileDescriptor.fileDescriptor,
                assetFileDescriptor.startOffset,
                assetFileDescriptor.declaredLength
            )
            prepareAsync()
        }
    }

    fun crowdPenaltyMissed() {
        if (!crowdEnabled) return
        val stringFileName = "crowdPenaltyMissed"

        if (crowdOnActionMediaPlayer?.isPlaying == true) {
            crowdOnActionMediaPlayer?.stop(stringFileName)
            crowdOnActionMediaPlayer?.reset(stringFileName)
        }

        val assetFileDescriptor =
            context.resources.openRawResourceFd(R.raw.crowd_penalties_miss) ?: return

        crowdOnActionMediaPlayer = MediaPlayer().apply {
            setOnPreparedListener {
                start(stringFileName)
            }

            setOnCompletionListener {
                reset(stringFileName)
            }
        }

        crowdOnActionMediaPlayer?.run {
            reset(stringFileName)
            setDataSource(
                assetFileDescriptor.fileDescriptor,
                assetFileDescriptor.startOffset,
                assetFileDescriptor.declaredLength
            )
            prepareAsync()
        }
    }

    fun stopCrowdGoals() {
        if (crowdOnActionMediaPlayer?.isPlaying == true) {
            crowdOnActionMediaPlayer?.stop("crowdOnActionMediaPlayer")
            crowdOnActionMediaPlayer?.reset("crowdOnActionMediaPlayer")
        }
    }

    fun crowdBackground(id: Int) {
        if (!crowdEnabled) return
        val stringFileName = "$id"
        try {
            if (crowdBackgroundMediaPlayer?.isPlaying == true) {
                crowdBackgroundMediaPlayer?.stop(stringFileName)
                crowdBackgroundMediaPlayer?.reset(stringFileName)
                // Here we need release to flush previous audio from crowd background startup scoreboard
                crowdBackgroundMediaPlayer?.release(stringFileName)
            }
            stopCrowdGoals()
        } catch (e: Exception) {
            Log.e("ERRORE ISPLAYING", e.toString())
        }

        val assetFileDescriptor = context.resources.openRawResourceFd(id) ?: return

        crowdBackgroundMediaPlayer = MediaPlayer().apply {
            setOnPreparedListener {
                start(stringFileName)
            }

            setOnCompletionListener {
                reset(stringFileName)
                when (id) {
                    R.raw.crowd_background -> {
                        crowdBackground(R.raw.crowd_background_2)
                        Log.d("CROWDBACKGROUND PLAYING", "crowd_background_2")
                    }
                    R.raw.crowd_background_2 -> {
                        crowdBackground(R.raw.crowd_background_3)
                        Log.d("CROWDBACKGROUND PLAYING", "crowd_background_3")
                    }
                    R.raw.crowd_background_3 -> {
                        crowdBackground(R.raw.crowd_background_4)
                        Log.d("CROWDBACKGROUND PLAYING", "crowd_background_4")
                    }
                    R.raw.crowd_background_4 -> {
                        crowdBackground(R.raw.crowd_background_5)
                        Log.d("CROWDBACKGROUND PLAYING", "crowd_background_5")
                    }
                    else -> {
                        crowdBackground(R.raw.crowd_background)
                        Log.v("CROWDBACKGROUND PLAYING", "crowd_background")
                    }
                }
            }
        }

        crowdBackgroundMediaPlayer?.run {
            reset(stringFileName)
            setDataSource(
                assetFileDescriptor.fileDescriptor,
                assetFileDescriptor.startOffset,
                assetFileDescriptor.declaredLength
            )
            prepareAsync()
        }
    }

    fun stopAll() {
        if (!crowdEnabled) return

        crowdOnActionMediaPlayer?.stop("crowdOnActionMediaPlayer")
        crowdOnActionMediaPlayer?.reset("crowdOnActionMediaPlayer")
        crowdOnActionMediaPlayer?.release("crowdOnActionMediaPlayer")
        crowdOnActionMediaPlayer = null

        crowdBackgroundMediaPlayer?.stop("crowdBackgroundMediaPlayer")
        crowdBackgroundMediaPlayer?.reset("crowdBackgroundMediaPlayer")
        crowdBackgroundMediaPlayer?.release("crowdBackgroundMediaPlayer")
        crowdBackgroundMediaPlayer = null

        refereeMediaPlayer?.stop("refereeMediaPlayer")
        refereeMediaPlayer?.reset("refereeMediaPlayer")
        refereeMediaPlayer?.release("refereeMediaPlayer")
        refereeMediaPlayer = null
    }
}

fun MediaPlayer.reset(file: String) {
    try {
        this.reset()
    } catch (e: Exception) {
        Log.e("ERRORE RESET $file", e.toString())
    }
}

fun MediaPlayer.stop(file: String) {
    try {
        this.stop()
    } catch (e: Exception) {
        Log.e("ERRORE STOP $file", e.toString())
    }
}

fun MediaPlayer.release(file: String) {
    try {
        this.release()
    } catch (e: Exception) {
        Log.e("ERRORE RELEASE $file", e.toString())
    }
}

fun MediaPlayer.start(file: String) {
    try {
        this.start()
    } catch (e: Exception) {
        Log.e("ERRORE START $file", e.toString())
    }
}