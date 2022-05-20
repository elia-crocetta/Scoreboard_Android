package eliapp.scoreboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.TextView

enum class MatchTime {
    firstHalf, secondHalf, firstExtraHalf, secondExtraHalf, penalties, endGame
}

class ScoreboardActivity : AppCompatActivity() {

    private lateinit var configuration: ScoreboardConfiguration
    private lateinit var homePointTextView: TextView
    private lateinit var awayPointTextView: TextView
    private lateinit var additionalUpTextView: TextView
    private lateinit var additionalBottomTextView: TextView
    private lateinit var timeTextView: TextView

    private var homePointValue: Int = 0
    private var awayPointValue: Int = 0

    private lateinit var regularTimeTimer: CountDownTimer
    private var regularTimeTimerIsRunning = false
    private var valueTimer: Long = 0
    private var _millisUntilFinished: Long = Long.MIN_VALUE

    private lateinit var extraTimeTimer: CountDownTimer
    private var valueTimerExtra: Long = 0
    private var extraTimeTimerIsRunning = false

    private var currentMatchTime: MatchTime = MatchTime.firstHalf

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scoreboard)

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
        homePointTextView = findViewById(R.id.homePointTextView)
        homePointTextView.isClickable = true
        homePointTextView.setOnClickListener {
            if (regularTimeTimerIsRunning) {
                regularTimeTimerIsRunning = false
                homePointValue+=1
                homePointTextView.text = "$homePointValue"
                additionalBottomTextView.text = getString(R.string.goal_home)
            }
        }
    }

    private fun createAwayPointTextView() {
        awayPointTextView = findViewById(R.id.awayPointTextView)
        awayPointTextView.isClickable = true
        awayPointTextView.setOnClickListener {
            if (regularTimeTimerIsRunning) {
                regularTimeTimerIsRunning = false
                awayPointValue+=1
                awayPointTextView.text = "$awayPointValue"
                additionalBottomTextView.text = getString(R.string.goal_away)
            }
        }
    }

    private fun createTimeTextView() {
        timeTextView = findViewById(R.id.timeTextView)
        timeTextView.isClickable = true
        timeTextView.setOnClickListener {
            if (currentMatchTime == MatchTime.penalties || currentMatchTime == MatchTime.endGame) {
                return@setOnClickListener
            }
            regularTimeTimerIsRunning = !regularTimeTimerIsRunning
            setAdditionalUpTextView()
            createRegularTimeCounter()
            additionalBottomTextView.text = null
        }
    }

    private fun createRegularTimeCounter() {
        val time = if (_millisUntilFinished == Long.MIN_VALUE) configuration.minutes else {_millisUntilFinished}
        regularTimeTimer = object : CountDownTimer(time, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                _millisUntilFinished = millisUntilFinished
                if (regularTimeTimerIsRunning) {
                    if (extraTimeTimerIsRunning) {
                        extraTimeTimer.cancel()
                        extraTimeTimerIsRunning = false
                    }
                    valueTimer++
                    val minutesMapped = map(valueTimer, 0, configuration.minutes, 0, 2700000)
                    Log.d("TIMER", "$millisUntilFinished")
                    val formatted = "${(minutesMapped / 60).toString().padStart(2, '0')}:${(minutesMapped % 60).toString().padStart(2, '0')}"
                    timeTextView.text = formatted
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
                regularTimeTimerIsRunning = false
                _millisUntilFinished = Long.MIN_VALUE
                when (currentMatchTime) {
                    MatchTime.firstHalf -> {
                        currentMatchTime = MatchTime.secondHalf
                    }
                    MatchTime.secondHalf -> {
                         if (configuration.extraTime && homePointValue == awayPointValue) {
                             currentMatchTime = MatchTime.firstExtraHalf
                             configuration.minutes = configuration.minutes/3
                        } else {
                             currentMatchTime = MatchTime.endGame
                        }
                    }
                    MatchTime.firstExtraHalf -> {
                        currentMatchTime = MatchTime.secondExtraHalf
                        configuration.minutes = configuration.minutes/3
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
        regularTimeTimer.cancel()
        extraTimeTimer.cancel()
    }

    private fun setAdditionalUpTextView() {
        when (currentMatchTime) {
            MatchTime.firstHalf -> additionalUpTextView.text = getString(R.string.first_half)
            MatchTime.secondHalf -> {
                additionalUpTextView.text = if(regularTimeTimerIsRunning) {
                    getString(R.string.second_half)
                } else {
                    getString(R.string.half_time)
                }
            }
            MatchTime.firstExtraHalf -> additionalUpTextView.text = getString(R.string.first_extra_half)
            MatchTime.secondExtraHalf -> {
                additionalUpTextView.text = if(regularTimeTimerIsRunning) {
                    getString(R.string.second_extra_half)
                } else {
                    getString(R.string.half_time)
                }
            }
            MatchTime.penalties -> additionalUpTextView.text = getString(R.string.penalties)
            MatchTime.endGame ->
                if (homePointValue > awayPointValue) {
                    additionalUpTextView.text = "${getString(R.string.end_game)}\n${getString(R.string.home_wins)}"
                } else if (homePointValue < awayPointValue) {
                    additionalUpTextView.text = "${getString(R.string.end_game)}\n${getString(R.string.away)}"
                } else if (homePointValue == awayPointValue) {
                    additionalUpTextView.text = "${getString(R.string.end_game)}\n${getString(R.string.tie)}"
                }
        }
    }
}