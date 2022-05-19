package eliapp.scoreboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.TextView
import java.util.*
import kotlin.concurrent.schedule

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

    private lateinit var extraTimeTimer: CountDownTimer
    private var valueTimerExtra: Long = 0
    private var extraTimeTimerIsRunning = false

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
                additionalUpTextView.text = getString(R.string.goal_home)
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
                additionalUpTextView.text = getString(R.string.goal_away)
            }
        }
    }

    private fun createTimeTextView() {
        timeTextView = findViewById(R.id.timeTextView)
        timeTextView.isClickable = true
        timeTextView.setOnClickListener {
            regularTimeTimerIsRunning = !regularTimeTimerIsRunning
            createRegularTimeCounter()
        }
    }

    private fun createRegularTimeCounter() {
        regularTimeTimer = object : CountDownTimer(configuration.minutes - valueTimer, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                if (regularTimeTimerIsRunning) {
                    additionalUpTextView.text = getString(R.string.first_half)
                    if (extraTimeTimerIsRunning) {
                        extraTimeTimer.cancel()
                        extraTimeTimerIsRunning = false
                    }
                    Log.d("TIMER", "$millisUntilFinished")
                    val formatted = "${(valueTimer / 60).toString().padStart(2, '0')}:${(valueTimer % 60).toString().padStart(2, '0')}"
                    timeTextView.text = formatted
                    valueTimer++
                } else {
                    Log.d("TIMER", "Pause")
                    cancel()
                    if (!extraTimeTimerIsRunning) {
                        extraTimeTimer.start()
                        extraTimeTimerIsRunning = true
                    }
                }
            }
            override fun onFinish() {
                additionalUpTextView.text = getString(R.string.half_time)
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

    override fun onDestroy() {
        super.onDestroy()
        regularTimeTimer.cancel()
        extraTimeTimer.cancel()
    }
}