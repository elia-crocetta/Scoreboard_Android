package eliapp.scoreboard

import android.content.Context
import android.media.MediaPlayer

data class SoundManager(val context: Context) {
    val refreeHalfTime = MediaPlayer.create(context, R.raw.half)
    val refreeWhistle = MediaPlayer.create(context, R.raw.foul)
    val refreeEndTime = MediaPlayer.create(context, R.raw.end)

    val crowdStartupScoreboard = MediaPlayer.create(context, R.raw.crowd_startup_scoreboard)
    val crowdBackground = MediaPlayer.create(context, R.raw.crowd_background)
    val crowdBackground2 = MediaPlayer.create(context, R.raw.crowd_background_2)
    val crowdBackground3 = MediaPlayer.create(context, R.raw.crowd_background_3)
    val crowdBackground4 = MediaPlayer.create(context, R.raw.crowd_background_4)
    val crowdBackground5 = MediaPlayer.create(context, R.raw.crowd_background_5)

    private val media = MediaPlayer.create(context, R.raw.crowd_goal_home)
    private val media2 = MediaPlayer.create(context, R.raw.crowd_goal_home_2)
    fun crowdGoalHome() {
        media.setOnCompletionListener(MediaPlayer.OnCompletionListener {
            media2.start()
        })
        media.start()
    }

    fun stopAll() {
        refreeHalfTime.stop()
        refreeWhistle.stop()
        refreeEndTime.stop()
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
