package eliapp.scoreboard

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import java.lang.Exception

data class SoundManager(val context: Context, val crowdEnabled: Boolean) {
    private var refereeHalfTime: MediaPlayer = MediaPlayer.create(context, R.raw.half)
    private var refereeWhistle: MediaPlayer = MediaPlayer.create(context, R.raw.foul)
    private var refereeEndTime: MediaPlayer = MediaPlayer.create(context, R.raw.end)

    private var crowdStartupScoreboard: MediaPlayer = MediaPlayer.create(context, R.raw.crowd_startup_scoreboard)
    private var crowdHalfTime: MediaPlayer = MediaPlayer.create(context, R.raw.crowd_half_time)

    private var crowdBackground: MediaPlayer = MediaPlayer.create(context, R.raw.crowd_background)
    private var crowdBackground2: MediaPlayer = MediaPlayer.create(context, R.raw.crowd_background_2)
    private var crowdBackground3: MediaPlayer = MediaPlayer.create(context, R.raw.crowd_background_3)
    private var crowdBackground4: MediaPlayer = MediaPlayer.create(context, R.raw.crowd_background_4)
    private var crowdBackground5: MediaPlayer = MediaPlayer.create(context, R.raw.crowd_background_5)

    private var media = MediaPlayer.create(context, R.raw.crowd_goal_home)
    private var media2 = MediaPlayer.create(context, R.raw.crowd_goal_home_2)
    private var crowdAwayGoal: MediaPlayer = MediaPlayer.create(context, R.raw.crowd_goal_away)
    private var crowdPenaltyMissed: MediaPlayer = MediaPlayer.create(context, R.raw.crowd_penalties_miss)

    fun refereeHalfTime() {
        refereeHalfTime = MediaPlayer.create(context, R.raw.half)
        refereeHalfTime.start()
    }
    fun refereeWhistle() {
        refereeWhistle = MediaPlayer.create(context, R.raw.foul)
        refereeWhistle.start()
    }
    fun refereeEndGame() {
        refereeEndTime = MediaPlayer.create(context, R.raw.end)
        refereeEndTime.start()
    }
    fun crowdStartupScoreboard() {
        if (!crowdEnabled) return
        crowdStartupScoreboard = MediaPlayer.create(context, R.raw.crowd_startup_scoreboard)
        crowdStartupScoreboard.setOnCompletionListener {
            crowdStartupScoreboard()
        }
        crowdStartupScoreboard.start()
    }
    fun crowdHalfTime() {
        if (!crowdEnabled) return
        crowdBackground.fullStop()
        crowdBackground2.fullStop()
        crowdBackground3.fullStop()
        crowdBackground4.fullStop()
        crowdBackground5.fullStop()
        crowdStartupScoreboard.fullStop()
        crowdStarted = false
        crowdHalfTime = MediaPlayer.create(context, R.raw.crowd_half_time)
        crowdHalfTime.setOnCompletionListener {
            crowdHalfTime()
        }
        crowdHalfTime.start()
    }
    fun crowdGoalHome() {
        if (!crowdEnabled) return
        media = MediaPlayer.create(context, R.raw.crowd_goal_home)
        media2 = MediaPlayer.create(context, R.raw.crowd_goal_home_2)
        media.setOnCompletionListener{
            media2.start()
        }
        media.start()
    }
    fun crowdGoalAway() {
        if (!crowdEnabled) return
        crowdAwayGoal = MediaPlayer.create(context, R.raw.crowd_goal_away)
        crowdAwayGoal.start()
    }
    fun crowdPenaltyScored() {
        if (!crowdEnabled) return
        media = MediaPlayer.create(context, R.raw.crowd_goal_home)
        media.start()
    }
    fun crowdPenaltyMissed() {
        if (!crowdEnabled) return
        crowdPenaltyMissed = MediaPlayer.create(context, R.raw.crowd_penalties_miss)
        crowdPenaltyMissed.start()
    }

    private var crowdStarted = false
    fun crowdBackground() {
        if (!crowdEnabled) return
        media.fullStop()
        media2.fullStop()
        crowdAwayGoal.fullStop()

        if (crowdStarted) {
            crowdStartupScoreboard.fullStop()
            return
        }

        crowdBackground = MediaPlayer.create(context, R.raw.crowd_background)
        crowdBackground2 = MediaPlayer.create(context, R.raw.crowd_background_2)
        crowdBackground3 = MediaPlayer.create(context, R.raw.crowd_background_3)
        crowdBackground5 = MediaPlayer.create(context, R.raw.crowd_background_4)
        crowdBackground5 = MediaPlayer.create(context, R.raw.crowd_background_5)

        crowdStarted = true
        crowdBackground.setOnCompletionListener {
            crowdBackground2.setOnCompletionListener {
                crowdBackground3.setOnCompletionListener {
                    crowdBackground4.setOnCompletionListener {
                        crowdBackground5.setOnCompletionListener {
                            this.crowdBackground()
                        }
                    }
                    crowdBackground4.start()
                }
                crowdBackground3.start()
            }
            crowdBackground2.start()
        }
        crowdBackground.start()
        crowdStartupScoreboard.fullStop()
    }

    fun stopAll() {
        if (!crowdEnabled) return
        crowdStarted = false
        refereeHalfTime.fullStop()
        refereeWhistle.fullStop()
        refereeEndTime.fullStop()
        crowdHalfTime.fullStop()
        crowdBackground.fullStop()
        crowdBackground2.fullStop()
        crowdBackground3.fullStop()
        crowdBackground4.fullStop()
        crowdBackground5.fullStop()
        crowdStartupScoreboard.fullStop()
        media.fullStop()
        media2.fullStop()
    }
}

fun MediaPlayer.fullStop() {
    try {
        this.stop()
    } catch (e: Exception) {
        Log.d("ERRORE STOP", e.toString())
    }

    try {
        this.reset()
    } catch (e: Exception) {
        Log.d("ERRORE RESET", e.toString())
    }

    try {
        this.release()
    } catch (e: Exception) {
        Log.d("ERRORE RELEASE", e.toString())
    }
}