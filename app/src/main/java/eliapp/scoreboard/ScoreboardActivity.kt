package eliapp.scoreboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import java.util.*

class ScoreboardActivity : AppCompatActivity() {

    private lateinit var configuration: ScoreboardConfiguration
    private lateinit var homePointTextView: TextView
    private lateinit var awayPointTextView: TextView
    private lateinit var additionalUpTextView: TextView
    private lateinit var additionalBottomTextView: TextView
    private lateinit var timeTextView: TextView

    private var homePointValue: Int = 0
    private var awayPointValue: Int = 0

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
        additionalUpTextView = findViewById(R.id.additionalUpTextView)
        additionalBottomTextView = findViewById(R.id.additionalBottomTextView)
        additionalBottomTextView.text = ""

    }

    private fun createHomePointTextView() {
        homePointTextView = findViewById(R.id.homePointTextView)
        homePointTextView.isClickable = true
        homePointTextView.setOnClickListener {
            homePointValue+=1
            homePointTextView.text = "${homePointValue}"
        }
    }

    private fun createAwayPointTextView() {
        awayPointTextView = findViewById(R.id.awayPointTextView)
        awayPointTextView.isClickable = true
        awayPointTextView.setOnClickListener {
            awayPointValue+=1
            awayPointTextView.text = "${awayPointValue}"
        }
    }

    private fun createTimeTextView() {
        timeTextView = findViewById(R.id.timeTextView)
        timeTextView.isClickable = true
        timeTextView.setOnClickListener {
            regularTimeTimerIsRunning = !regularTimeTimerIsRunning

            object : CountDownTimer(configuration.minutes - valueTimer, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    Log.d("TIMER", "${millisUntilFinished}")
                    timeTextView.text = valueTimer.toString()
                    valueTimer++
                }
                override fun onFinish() {
                    additionalUpTextView.text = getString(R.string.half_time)
                }
            }
        }
    }

}