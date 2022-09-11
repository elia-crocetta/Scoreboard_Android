package eliapp.scoreboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import android.util.Log
import android.view.HapticFeedbackConstants
import android.view.View
import android.widget.TextView

enum class MatchTime {
    FirstHalf, SecondHalf, FirstExtraHalf, SecondExtraHalf, Penalties, EndGame;
    fun isInExtraTime() : Boolean {
        return this == FirstExtraHalf || this == SecondExtraHalf
    }
}


class ScoreboardActivity : AppCompatActivity() {

    private lateinit var configuration: ScoreboardConfiguration
    private lateinit var homePointTextView: TextView
    private lateinit var homeNameTextView: TextView
    private lateinit var awayPointTextView: TextView
    private lateinit var awayNameTextView: TextView
    private lateinit var additionalUpTextView: TextView
    private lateinit var additionalBottomTextView: TextView
    private lateinit var timeTextView: TextView
    private lateinit var soundManager: SoundManager

    private var homePointValue: Int = 0
    private var awayPointValue: Int = 0

    private lateinit var regularTimeTimer: CountDownTimer
    private var regularTimeTimerIsRunning = false
    private var valueTimer: Long = 0
    private var _millisUntilFinished: Long = 0
    private var _millisUntilFinishedForExtraTime: Long = 0

    private lateinit var extraTimeTimer: CountDownTimer
    private var valueTimerExtra: Long = 0
    private fun actualExtraTimeValue(): Long {
        return when (valueTimerExtra) {
            in 0 .. 50 -> (if (BuildConfig.DEBUG) 60000 else 0)
            in 51 .. 110 -> 60000
            in 111 .. 170 -> 120000
            in 171 .. 230 -> 180000
            in 231 .. 290 -> 240000
            else -> 300000
        }
    }

    private var extraTimeTimerIsRunning = false
    private var matchIsInExtraMinutes = false
    private var elapsedExtraMinutes = 0

    private var currentMatchTime: MatchTime = MatchTime.FirstHalf

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scoreboard)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()

        val config = intent.getSerializableExtra("CONFIG") as? ScoreboardConfiguration ?: return
        configuration = config

        soundManager = SoundManager(applicationContext, configuration.crowd)

        createHomePointTextView()
        createAwayPointTextView()
        createTimeTextView()
        createExtraTimeCounter()
        additionalUpTextView = findViewById(R.id.additionalUpTextView)
        additionalBottomTextView = findViewById(R.id.additionalBottomTextView)
        additionalBottomTextView.text = ""

        soundManager.crowdStartupScoreboard()
    }

    private fun createHomePointTextView() {
        homeNameTextView = findViewById(R.id.homeNameTextView)
        homeNameTextView.text = configuration.homeName
        homePointTextView = findViewById(R.id.homePointTextView)
        homePointTextView.isClickable = true
        homePointTextView.setOnClickListener {
            it.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY, HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING)
            if (regularTimeTimerIsRunning) {
                soundManager.crowdGoalHome()
                regularTimeTimerIsRunning = false
                homePointValue+=1
                homePointTextView.text = "$homePointValue"
                additionalBottomTextView.text = getString(R.string.goal_team, configuration.homeName)
            } else if (currentMatchTime == MatchTime.Penalties) {
                homePointValue+=1
                homePointTextView.text = "$homePointValue"
                soundManager.crowdPenaltyScored()
            }
        }
    }

    private fun createAwayPointTextView() {
        awayNameTextView = findViewById(R.id.awayNameTextView)
        awayNameTextView.text = configuration.awayName
        awayPointTextView = findViewById(R.id.awayPointTextView)
        awayPointTextView.isClickable = true
        awayPointTextView.setOnClickListener {
            it.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY, HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING)
            if (regularTimeTimerIsRunning) {
                soundManager.crowdGoalAway()
                regularTimeTimerIsRunning = false
                awayPointValue+=1
                awayPointTextView.text = "$awayPointValue"
                additionalBottomTextView.text = getString(R.string.goal_team, configuration.awayName)
            } else if (currentMatchTime == MatchTime.Penalties) {
                awayPointValue+=1
                awayPointTextView.text = "$awayPointValue"
                soundManager.crowdPenaltyScored()
            }
        }
    }

    private var countDownToStart = 3
    private fun createTimeTextView() {
        timeTextView = findViewById(R.id.timeTextView)
        timeTextView.isClickable = true
        timeTextView.setOnClickListener {
            soundManager.crowdBackground()
            if (currentMatchTime == MatchTime.Penalties || currentMatchTime == MatchTime.EndGame) {
                return@setOnClickListener
            }
            it.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY, HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING)
            regularTimeTimerIsRunning = !regularTimeTimerIsRunning
            additionalBottomTextView.text = null
            if (!regularTimeTimerIsRunning) {
                soundManager.refereeAction(R.raw.foul)
                soundManager.crowdBackground()
                createRegularTimeCounter()
            } else {
                object  : CountDownTimer(3000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        additionalUpTextView.text = countDownToStart.toString()
                        countDownToStart--
                    }

                    override fun onFinish() {
                        soundManager.refereeAction(R.raw.foul)
                        countDownToStart = 3
                        setAdditionalUpTextView()
                        createRegularTimeCounter()
                    }
                }.start()
            }
        }
    }

    private fun createRegularTimeCounter() {
        val zeroLong: Long = 0
        val durationHalf: Long = if (currentMatchTime.isInExtraTime()) 2700000 / 3 else 2700000
        val actualTime = if (matchIsInExtraMinutes) {
            if (_millisUntilFinishedForExtraTime == zeroLong) {
                actualExtraTimeValue()
            } else {
                _millisUntilFinishedForExtraTime
            }
        } else  {
            if (_millisUntilFinished == zeroLong) {
                durationHalf
            } else {
                _millisUntilFinished
            }
        }
        val actualInterval: Long = if (matchIsInExtraMinutes) 1000 else { 1000/(2700000 / configuration.minutes) }

        regularTimeTimer = object : CountDownTimer(actualTime, actualInterval) {
            override fun onTick(millisUntilFinished: Long) {
                Log.d("TIMER", "${actualTime}")
                if (regularTimeTimerIsRunning) {
                    if (extraTimeTimerIsRunning) {
                        extraTimeTimerIsRunning = false
                        extraTimeTimer.cancel()
                    }
                    if (matchIsInExtraMinutes) {
                        _millisUntilFinishedForExtraTime = millisUntilFinished
                        elapsedExtraMinutes++
                        val formatted = elapsedExtraMinutes.formatValueForScoreboard()
                        additionalUpTextView.text = getString(R.string.over_minutes, formatted)
                        if (TextUtils.isEmpty(additionalBottomTextView.text)) {
                            additionalBottomTextView.text = getString(R.string.announce_over_time, (actualExtraTimeValue() / 60000).toString())
                        }
                    } else {
                        _millisUntilFinished = millisUntilFinished
                        valueTimer++
                        val formatted = valueTimer.formatValueForScoreboard()
                        timeTextView.text = formatted
                        val timeElapsed = durationHalf-millisUntilFinished
                        val realTimeMax = if (currentMatchTime.isInExtraTime()) configuration.minutes/3 else configuration.minutes
                        if (timeElapsed >= realTimeMax) {
                            cancel()
                            onFinish()
                        }
                    }
                } else {
                    Log.d("TIMER", "Pause")
                    cancel()
                    if (!extraTimeTimerIsRunning) {
                        extraTimeTimer.start()
                        extraTimeTimerIsRunning = true
                    }
                    additionalUpTextView.text = getString(R.string.paused)
                }
            }
            override fun onFinish() {
                if (actualExtraTimeValue() > 0 && !matchIsInExtraMinutes) {
                    when (currentMatchTime) {
                        MatchTime.FirstHalf -> valueTimer = 2700
                        MatchTime.SecondHalf -> valueTimer = 5400
                        MatchTime.FirstExtraHalf -> valueTimer = 6300
                        MatchTime.SecondExtraHalf -> valueTimer = 7200
                        else -> {}
                    }

                    additionalBottomTextView.text = getString(R.string.announce_over_time, (actualExtraTimeValue() / 60000).toString())
                    timeTextView.text = valueTimer.formatValueForScoreboard()
                    matchIsInExtraMinutes = true
                    createRegularTimeCounter()
                } else {
                    regularTimeTimerIsRunning = false
                    matchIsInExtraMinutes = false
                    _millisUntilFinished = 0
                    _millisUntilFinishedForExtraTime = 0
                    valueTimerExtra = 0
                    elapsedExtraMinutes = 0
                    additionalBottomTextView.text = ""

                    when (currentMatchTime) {
                        MatchTime.FirstHalf -> {
                            valueTimer = 2700
                            currentMatchTime = MatchTime.SecondHalf
                        }
                        MatchTime.SecondHalf -> {
                            valueTimer = 5400
                            currentMatchTime = if (configuration.extraTime && homePointValue == awayPointValue) {
                                MatchTime.FirstExtraHalf
                            } else {
                                MatchTime.EndGame
                            }
                        }
                        MatchTime.FirstExtraHalf -> {
                            valueTimer = 6300
                            currentMatchTime = MatchTime.SecondExtraHalf
                        }
                        MatchTime.SecondExtraHalf -> {
                            valueTimer = 7200
                            currentMatchTime = if (homePointValue == awayPointValue) {
                                MatchTime.Penalties
                            } else {
                                MatchTime.EndGame
                            }
                        }
                        else -> {}
                    }
                    timeTextView.text = valueTimer.formatValueForScoreboard()
                    setAdditionalUpTextView()
                }
            }
        }
        regularTimeTimer.start()
    }

    private fun createExtraTimeCounter() {
        extraTimeTimer = object : CountDownTimer(300000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                if (valueTimerExtra<300 && !matchIsInExtraMinutes) {
                    Log.d("EXTRA-TIMER", "$valueTimerExtra")
                    valueTimerExtra++
                } else {
                    Log.d("EXTRA-TIMER", "5 min max reached")
                    cancel()
                }
            }
            override fun onFinish() {}
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (regularTimeTimerIsRunning) {regularTimeTimer.cancel()}
        if (extraTimeTimerIsRunning) {extraTimeTimer.cancel()}
        soundManager.stopAll()
    }

    private fun setAdditionalUpTextView() {
        when (currentMatchTime) {
            MatchTime.FirstHalf -> additionalUpTextView.text = getString(R.string.first_half)
            MatchTime.SecondHalf -> {
                if(regularTimeTimerIsRunning) {
                    additionalUpTextView.text = getString(R.string.second_half)
                } else {
                    soundManager.crowdHalfTime()
                    soundManager.refereeAction(R.raw.half)
                    additionalUpTextView.text = getString(R.string.half_time)
                }
            }
            MatchTime.FirstExtraHalf -> {
                if(regularTimeTimerIsRunning) {
                    additionalUpTextView.text = getString(R.string.first_extra_half)
                } else {
                    soundManager.crowdHalfTime()
                    soundManager.refereeAction(R.raw.end)
                    additionalUpTextView.text = getString(R.string.extra_time)
                }
            }
            MatchTime.SecondExtraHalf -> {
                if(regularTimeTimerIsRunning) {
                    additionalUpTextView.text = getString(R.string.second_extra_half)
                } else {
                    soundManager.crowdHalfTime()
                    soundManager.refereeAction(R.raw.half)
                    additionalUpTextView.text = getString(R.string.half_time)
                }
            }
            MatchTime.Penalties -> {
                soundManager.crowdHalfTime()
                soundManager.refereeAction(R.raw.end)
                additionalUpTextView.text = getString(R.string.penalties)
            }
            MatchTime.EndGame -> {
                soundManager.crowdHalfTime()
                soundManager.refereeAction(R.raw.end)
                if (homePointValue > awayPointValue) {
                    additionalUpTextView.text = getString(R.string.end_game, getString(R.string.home_wins))
                } else if (homePointValue < awayPointValue) {
                    additionalUpTextView.text = getString(R.string.end_game, getString(R.string.away_wins))
                } else if (homePointValue == awayPointValue) {
                    additionalUpTextView.text = getString(R.string.end_game, getString(R.string.tie))
                }
            }
        }
    }
}

fun Long.formatValueForScoreboard() : String {
    return "${(this / 60).toString().padStart(2, '0')}:${(this % 60).toString().padStart(2, '0')}"
}

fun Int.formatValueForScoreboard() : String {
    return "${(this / 60).toString().padStart(2, '0')}:${(this % 60).toString().padStart(2, '0')}"
}
/*
fun Long.map(in_min: Long, in_max: Long, out_min: Long, out_max: Long): Long {
    return (this - in_min) * (out_max - out_min) / (in_max - in_min) + out_min
}
*/