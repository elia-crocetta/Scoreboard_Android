package eliapp.scoreboard

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import kotlin.Exception

data class SoundManager(val context: Context, val crowdEnabled: Boolean) {
    private lateinit var refereeMediaPlayer: MediaPlayer
    private lateinit var crowdBackgroundMediaPlayer: MediaPlayer
    private lateinit var crowdOnActionMediaPlayer: MediaPlayer

    fun refereeAction(id: Int) {
        refereeMediaPlayer = MediaPlayer.create(context, id)
        refereeMediaPlayer.start("refereeHalfTime")
        refereeMediaPlayer.setOnCompletionListener {
            refereeMediaPlayer.reset("refereeHalfTime")
            refereeMediaPlayer.release("refereeHalfTime")
        }
    }

    private var isCrowdBackgroundPlayerOnStartupSound = false
    fun crowdStartupScoreboard() {
        if (!crowdEnabled) return
        crowdBackgroundMediaPlayer = MediaPlayer.create(context, R.raw.crowd_startup_scoreboard)
        crowdBackgroundMediaPlayer.start("crowdStartupScoreboard")
        isCrowdBackgroundPlayerOnStartupSound = true
        crowdBackgroundMediaPlayer.setOnCompletionListener {
            crowdBackgroundMediaPlayer.reset("crowdStartupScoreboard")
            crowdBackgroundMediaPlayer.release("crowdStartupScoreboard")
            crowdStartupScoreboard()
        }
    }

    fun crowdHalfTime() {
        if (!crowdEnabled) return
        if (crowdBackgroundMediaPlayer.isPlaying) {
            crowdBackgroundMediaPlayer.pause("crowdBackground")
            crowdBackgroundMediaPlayer.reset("crowdBackground")
            crowdBackgroundMediaPlayer.release("crowdBackground")
        }

        crowdBackgroundMediaPlayer = MediaPlayer.create(context, R.raw.crowd_half_time)
        crowdBackgroundMediaPlayer.start("crowdHalfTime")
        crowdBackgroundMediaPlayer.setOnCompletionListener {
            crowdHalfTime()
        }
    }

    fun crowdGoalHome() {
        if (!crowdEnabled) return
        crowdOnActionMediaPlayer = MediaPlayer.create(context, R.raw.crowd_goal_home)
        crowdOnActionMediaPlayer.start("media")
        Log.d("SOUND", "playing crowdHomeGoal")
        crowdOnActionMediaPlayer.setOnCompletionListener {
            crowdOnActionMediaPlayer.reset("crowdGoalHome")
            crowdOnActionMediaPlayer.release("crowdGoalHome")
            Log.d("SOUND", "completed crowdHomeGoal")
            crowdOnActionMediaPlayer = MediaPlayer.create(context, R.raw.crowd_goal_home_2)
            crowdOnActionMediaPlayer.start("media2")
            Log.d("SOUND", "playing crowdHomeGoal2")
            crowdOnActionMediaPlayer.setOnCompletionListener {
                crowdOnActionMediaPlayer.reset("crowdGoalHome2")
                crowdOnActionMediaPlayer.release("crowdGoalHome2")
                Log.d("SOUND", "completed crowdHomeGoal2")
            }
        }
    }

    fun crowdGoalAway() {
        if (!crowdEnabled) return
        crowdOnActionMediaPlayer = MediaPlayer.create(context, R.raw.crowd_goal_away)
        crowdOnActionMediaPlayer.start("crowdAwayGoal")
        Log.d("SOUND", "playing crowdAwayGoal")
        crowdOnActionMediaPlayer.setOnCompletionListener {
            crowdOnActionMediaPlayer.reset("crowdAwayGoal")
            crowdOnActionMediaPlayer.release("crowdAwayGoal")
            Log.d("SOUND", "completed crowdAwayGoal")
        }
    }

    fun crowdPenaltyScored() {
        if (!crowdEnabled) return
        crowdOnActionMediaPlayer = MediaPlayer.create(context, R.raw.crowd_goal_home)
        crowdOnActionMediaPlayer.start("media")
        crowdOnActionMediaPlayer.setOnCompletionListener {
            crowdOnActionMediaPlayer.reset("crowdPenaltyScored")
            crowdOnActionMediaPlayer.release("crowdPenaltyScored")
        }
    }

    fun crowdPenaltyMissed() {
        if (!crowdEnabled) return
        crowdOnActionMediaPlayer = MediaPlayer.create(context, R.raw.crowd_penalties_miss)
        crowdOnActionMediaPlayer.start("crowdPenaltyMissed")
        crowdOnActionMediaPlayer.setOnCompletionListener {
            crowdOnActionMediaPlayer.reset("crowdPenaltyMissed")
            crowdOnActionMediaPlayer.release("crowdPenaltyMissed")
        }
    }

    fun crowdBackground() {
        if (!crowdEnabled) return
        try {
            if (this::crowdBackgroundMediaPlayer.isInitialized && crowdBackgroundMediaPlayer.isPlaying) {
                if (isCrowdBackgroundPlayerOnStartupSound) {
                    crowdBackgroundMediaPlayer.stop("crowdBackgroundMediaPlayer")
                    crowdBackgroundMediaPlayer.reset("crowdBackgroundMediaPlayer")
                    crowdBackgroundMediaPlayer.release("crowdBackgroundMediaPlayer")
                    isCrowdBackgroundPlayerOnStartupSound = false
                } else {

                    if (this::crowdOnActionMediaPlayer.isInitialized && crowdOnActionMediaPlayer.isPlaying) {
                        crowdOnActionMediaPlayer.stop("crowdBackgroundMediaPlayer")
                        crowdOnActionMediaPlayer.reset("crowdBackgroundMediaPlayer")
                        crowdOnActionMediaPlayer.release("crowdBackgroundMediaPlayer")
                    }
                    return
                }
            }
        } catch (e: Exception) {
            Log.d("ERRORE ISPLAYING", e.toString())
        }

        crowdBackgroundMediaPlayer = MediaPlayer.create(context, R.raw.crowd_background)
        crowdBackgroundMediaPlayer.start("crowdBackground")
        Log.d("SOUND", "playing crowdBackground")

        crowdBackgroundMediaPlayer.setOnCompletionListener {
            crowdBackgroundMediaPlayer.reset("crowdBackground")
            Log.d("SOUND", "stopped crowdBackground")
            crowdBackgroundMediaPlayer = MediaPlayer.create(context, R.raw.crowd_background_2)
            crowdBackgroundMediaPlayer.start("crowdBackground2")
            Log.d("SOUND", "playing crowdBackground2")

            crowdBackgroundMediaPlayer.setOnCompletionListener {
                crowdBackgroundMediaPlayer.reset("crowdBackground2")
                Log.d("SOUND", "stopped crowdBackground2")

                crowdBackgroundMediaPlayer = MediaPlayer.create(context, R.raw.crowd_background_3)
                crowdBackgroundMediaPlayer.start("crowdBackground3")
                Log.d("SOUND", "playing crowdBackground3")

                crowdBackgroundMediaPlayer.setOnCompletionListener {
                    crowdBackgroundMediaPlayer.reset("crowdBackground3")
                    Log.d("SOUND", "stopped crowdBackground3")

                    crowdBackgroundMediaPlayer =
                        MediaPlayer.create(context, R.raw.crowd_background_4)
                    crowdBackgroundMediaPlayer.start("crowdBackground4")
                    Log.d("SOUND", "playing crowdBackground4")

                    crowdBackgroundMediaPlayer.setOnCompletionListener {
                        crowdBackgroundMediaPlayer.reset("crowdBackground4")
                        Log.d("SOUND", "stopped crowdBackground4")

                        crowdBackgroundMediaPlayer =
                            MediaPlayer.create(context, R.raw.crowd_background_5)
                        crowdBackgroundMediaPlayer.start("crowdBackground5")
                        Log.d("SOUND", "playing crowdBackground5")

                        crowdBackgroundMediaPlayer.setOnCompletionListener {
                            crowdBackgroundMediaPlayer.reset("crowdBackground5")
                            crowdBackgroundMediaPlayer.release("crowdBackground4")
                            Log.d("SOUND", "stopped crowdBackground5")
                            this.crowdBackground()
                            Log.d("SOUND", "Recursive function crowdBackground()")
                        }
                    }
                }
            }
        }
    }

    fun stopAll() {
        if (!crowdEnabled) return
        if (this::crowdOnActionMediaPlayer.isInitialized) {
            crowdOnActionMediaPlayer.stop("crowdOnActionMediaPlayer")
            crowdOnActionMediaPlayer.reset("crowdOnActionMediaPlayer")
            crowdOnActionMediaPlayer.release("crowdOnActionMediaPlayer")
        }

        if (this::crowdBackgroundMediaPlayer.isInitialized) {
            crowdBackgroundMediaPlayer.stop("crowdBackgroundMediaPlayer")
            crowdBackgroundMediaPlayer.reset("crowdBackgroundMediaPlayer")
            crowdBackgroundMediaPlayer.release("crowdBackgroundMediaPlayer")
        }

        if (this::refereeMediaPlayer.isInitialized) {
            refereeMediaPlayer.stop("refereeMediaPlayer")
            refereeMediaPlayer.reset("refereeMediaPlayer")
            refereeMediaPlayer.release("refereeMediaPlayer")
        }
    }
}

fun MediaPlayer.reset(file: String) {
    try {
        this.reset()
    } catch (e: Exception) {
        Log.d("ERRORE RESET $file", e.toString())
    }
}

fun MediaPlayer.pause(file: String) {
    try {
        this.pause()
    } catch (e: Exception) {
        Log.d("ERRORE PAUSE $file", e.toString())
    }
}

fun MediaPlayer.stop(file: String) {
    try {
        this.stop()
    } catch (e: Exception) {
        Log.d("ERRORE STOP $file", e.toString())
    }
}

fun MediaPlayer.release(file: String) {
    try {
        this.release()
    } catch (e: Exception) {
        Log.d("ERRORE RELEASE $file", e.toString())
    }
}

fun MediaPlayer.start(file: String) {
    try {
        this.start()
    } catch (e: Exception) {
        Log.d("ERRORE START $file", e.toString())
    }
}