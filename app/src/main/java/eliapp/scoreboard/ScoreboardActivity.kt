package eliapp.scoreboard

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.HapticFeedbackConstants
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView

enum class MatchTime {
    firstHalf, secondHalf, firstExtraHalf, secondExtraHalf, penalties, endGame
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

    private var homePointValue: Int = 0
    private var awayPointValue: Int = 0

    private lateinit var regularTimeTimer: CountDownTimer
    private var regularTimeTimerIsRunning = false
    private var valueTimer: Long = 0
    private var _millisUntilFinished: Long = 0

    private lateinit var extraTimeTimer: CountDownTimer
    private var valueTimerExtra: Long = 0
    private fun actualExtraTimeValue(): Long {
        return when (valueTimerExtra) {
            in 0 .. 50 -> 0
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

    private var currentMatchTime: MatchTime = MatchTime.firstHalf

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scoreboard)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()

        val config = intent.getSerializableExtra("CONFIG") as? ScoreboardConfiguration ?: return
        configuration = config

        createHomePointTextView()
        createAwayPointTextView()
        createTimeTextView()
        createExtraTimeCounter()
        additionalUpTextView = findViewById(R.id.additionalUpTextView)
        additionalBottomTextView = findViewById(R.id.additionalBottomTextView)
        additionalBottomTextView.text = ""
    }

    private fun createHomePointTextView() {
        homeNameTextView = findViewById(R.id.homeNameTextView)
        homeNameTextView.text = configuration.homeName
        homePointTextView = findViewById(R.id.homePointTextView)
        homePointTextView.isClickable = true
        homePointTextView.setOnClickListener {
            it.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY, HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING)
            if (regularTimeTimerIsRunning) {
                regularTimeTimerIsRunning = false
                homePointValue+=1
                homePointTextView.text = "$homePointValue"
                additionalBottomTextView.text = getString(R.string.goal_home)
            } else if (currentMatchTime == MatchTime.penalties) {
                homePointValue+=1
                homePointTextView.text = "$homePointValue"
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
                regularTimeTimerIsRunning = false
                awayPointValue+=1
                awayPointTextView.text = "$awayPointValue"
                additionalBottomTextView.text = getString(R.string.goal_away)
            } else if (currentMatchTime == MatchTime.penalties) {
                awayPointValue+=1
                awayPointTextView.text = "$awayPointValue"
            }
        }
    }

    var countDownToStart = 3
    private fun createTimeTextView() {
        timeTextView = findViewById(R.id.timeTextView)
        timeTextView.isClickable = true
        timeTextView.setOnClickListener {
            if (currentMatchTime == MatchTime.penalties || currentMatchTime == MatchTime.endGame) {
                return@setOnClickListener
            }
            it.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY, HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING)
            regularTimeTimerIsRunning = !regularTimeTimerIsRunning
            additionalBottomTextView.text = null
            if (!regularTimeTimerIsRunning) {
                MediaPlayer.create(applicationContext, R.raw.foul).start()
                setAdditionalUpTextView()
                createRegularTimeCounter()
            } else {
                object  : CountDownTimer(3000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        additionalUpTextView.text = countDownToStart.toString()
                        countDownToStart--
                    }

                    override fun onFinish() {
                        MediaPlayer.create(applicationContext, R.raw.foul).start()
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
        val time = if (_millisUntilFinished == zeroLong) if (currentMatchTime == MatchTime.firstExtraHalf || currentMatchTime == MatchTime.secondExtraHalf ) configuration.minutes / 3 else {configuration.minutes} else { _millisUntilFinished }
        val actualTime = if (matchIsInExtraMinutes) actualExtraTimeValue() else time
        regularTimeTimer = object : CountDownTimer(actualTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                _millisUntilFinished = millisUntilFinished
                if (regularTimeTimerIsRunning) {
                    if (extraTimeTimerIsRunning) {
                        extraTimeTimer.cancel()
                        extraTimeTimerIsRunning = false
                    }
                    Log.d("TIMER", "$millisUntilFinished")
                    if (matchIsInExtraMinutes) {
                        elapsedExtraMinutes++
                        val formatted = "${(elapsedExtraMinutes / 60).toString().padStart(2, '0')}:${(elapsedExtraMinutes % 60).toString().padStart(2, '0')}"
                        additionalUpTextView.text = getString(R.string.over_time, formatted)
                    } else {
                        valueTimer++
                        val minutesMapped = map(valueTimer, 0, configuration.minutes, 0, 2700000)
                        val formatted = "${(minutesMapped / 60).toString().padStart(2, '0')}:${(minutesMapped % 60).toString().padStart(2, '0')}"
                        timeTextView.text = formatted
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
                    matchIsInExtraMinutes = true
                    createRegularTimeCounter()
                } else {
                    regularTimeTimerIsRunning = false
                    matchIsInExtraMinutes = false
                    _millisUntilFinished = 0
                    valueTimerExtra = 0
                    elapsedExtraMinutes = 0
                    when (currentMatchTime) {
                        MatchTime.firstHalf -> {
                            currentMatchTime = MatchTime.secondHalf
                        }
                        MatchTime.secondHalf -> {
                            if (configuration.extraTime && homePointValue == awayPointValue) {
                                currentMatchTime = MatchTime.firstExtraHalf
                            } else {
                                currentMatchTime = MatchTime.endGame
                            }
                        }
                        MatchTime.firstExtraHalf -> {
                            currentMatchTime = MatchTime.secondExtraHalf
                        }
                        MatchTime.secondExtraHalf -> {
                            currentMatchTime = if (homePointValue == awayPointValue) {
                                MatchTime.penalties
                            } else {
                                MatchTime.endGame
                            }
                        }
                        else -> {}
                    }
                    setAdditionalUpTextView()
                }
            }
        }
        regularTimeTimer.start()
    }

    private fun createExtraTimeCounter() {
        extraTimeTimer = object : CountDownTimer(300000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                if (valueTimerExtra<300000) {
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

    fun map(value: Long, in_min: Long, in_max: Long, out_min: Long, out_max: Long): Long {
        return (value - in_min) * (out_max - out_min) / (in_max - in_min) + out_min
    }

    override fun onDestroy() {
        super.onDestroy()
        if (regularTimeTimerIsRunning) {regularTimeTimer.cancel()}
        if (extraTimeTimerIsRunning) {extraTimeTimer.cancel()}
    }

    private fun setAdditionalUpTextView() {
        when (currentMatchTime) {
            MatchTime.firstHalf -> additionalUpTextView.text = getString(R.string.first_half)
            MatchTime.secondHalf -> {
                additionalUpTextView.text = if(regularTimeTimerIsRunning) {
                    getString(R.string.second_half)
                } else {
                    MediaPlayer.create(applicationContext, R.raw.half).start()
                    getString(R.string.half_time)
                }
            }
            MatchTime.firstExtraHalf -> {
                additionalUpTextView.text = if(regularTimeTimerIsRunning) {
                    getString(R.string.first_extra_half)
                } else {
                    MediaPlayer.create(applicationContext, R.raw.end).start()
                    getString(R.string.extra_time)
                }
            }
            MatchTime.secondExtraHalf -> {
                additionalUpTextView.text = if(regularTimeTimerIsRunning) {
                    getString(R.string.second_extra_half)
                } else {
                    MediaPlayer.create(applicationContext, R.raw.half).start()
                    getString(R.string.half_time)
                }
            }
            MatchTime.penalties -> additionalUpTextView.text = getString(R.string.penalties)
            MatchTime.endGame -> {
                MediaPlayer.create(applicationContext, R.raw.end).start()
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