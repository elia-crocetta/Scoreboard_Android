package eliapp.scoreboard

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.SoundPool
import android.util.Log
import java.util.*

data class SoundManager(val context: Context) {
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
        crowdStartupScoreboard = MediaPlayer.create(context, R.raw.crowd_startup_scoreboard)
        crowdStartupScoreboard.start()
    }
    fun crowdHalfTime() {
        crowdBackground.stop()
        crowdBackground2.stop()
        crowdBackground3.stop()
        crowdBackground4.stop()
        crowdBackground5.stop()
        crowdStartupScoreboard.stop()
        crowdStarted = false
        crowdHalfTime = MediaPlayer.create(context, R.raw.crowd_half_time)
        crowdHalfTime.start()
    }
    fun crowdGoalHome() {
        media = MediaPlayer.create(context, R.raw.crowd_goal_home)
        media2 = MediaPlayer.create(context, R.raw.crowd_goal_home_2)
        media.setOnCompletionListener{
            media2.start()
        }
        media.start()
    }
    fun crowdGoalAway() {
        crowdAwayGoal = MediaPlayer.create(context, R.raw.crowd_goal_away)
        crowdAwayGoal.start()
    }
    fun crowdPenaltyScored() {
        media = MediaPlayer.create(context, R.raw.crowd_goal_home)
        media.start()
    }
    fun crowdPenaltyMissed() {
        crowdPenaltyMissed = MediaPlayer.create(context, R.raw.crowd_penalties_miss)
        crowdPenaltyMissed.start()
    }

    private var crowdStarted = false
    fun crowdBackground() {
        media.stop()
        media2.stop()
        crowdAwayGoal.stop()
        if (crowdStarted) {
            crowdStartupScoreboard.stop()
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
        crowdStartupScoreboard.stop()
    }

    fun stopAll() {
        crowdStarted = false
        refereeHalfTime.stop()
        refereeWhistle.stop()
        refereeEndTime.stop()
        crowdHalfTime.stop()
        crowdStartupScoreboard.stop()
        crowdBackground.stop()
        crowdBackground2.stop()
        crowdBackground3.stop()
        crowdBackground4.stop()
        crowdBackground5.stop()
        media.stop()
        media2.stop()
    }
}
