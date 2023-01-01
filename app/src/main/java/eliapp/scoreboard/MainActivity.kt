package eliapp.scoreboard

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.core.widget.addTextChangedListener
import com.google.android.material.switchmaterial.SwitchMaterial
import java.io.Serializable

data class ScoreboardConfiguration(
    var extraTime: Boolean,
    var crowd: Boolean,
    var minutes: Long,
    var homeName: String,
    var awayName: String,
    var homeFactor: Boolean
    ) : Serializable

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private var scoreboardConfig: ScoreboardConfiguration = ScoreboardConfiguration(
        false,
        true,
        900000,
        "HOME",
        "AWAY",
        false
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        createSpinner()
        createSwitch()
        createCrowdSwitch()
        createProceedButton()
        createHomeTextView()
        createAwayTextView()
        createHomeFactorSwitch()
        if (BuildConfig.DEBUG) {
            arraySelectionMinutes+=10000
        }
    }

    private lateinit var spinner: Spinner
    private fun createSpinner() {
        spinner = findViewById(R.id.select_minutes_spinner)
        ArrayAdapter.createFromResource(
            this,
            if (BuildConfig.DEBUG) { R.array.Array_Minutes_Debug } else { R.array.Array_Minutes},
            android.R.layout.simple_spinner_item,
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = this
    }

    private var arraySelectionMinutes = arrayOf<Long>(900000, 600000, 300000)
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        scoreboardConfig.minutes = arraySelectionMinutes[position]
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        scoreboardConfig.minutes = arraySelectionMinutes[0]
    }

    private lateinit var extraTimeSwitch: SwitchMaterial
    private fun createSwitch() {
        extraTimeSwitch = findViewById(R.id.extratime_switch)
        extraTimeSwitch.setOnCheckedChangeListener { _, isChecked ->
            scoreboardConfig.extraTime = isChecked
        }
    }

    private lateinit var crowdSwitch: SwitchMaterial
    private fun createCrowdSwitch() {
        crowdSwitch = findViewById(R.id.crowd_switch)
        crowdSwitch.setOnCheckedChangeListener { _, isChecked ->
            scoreboardConfig.crowd = isChecked
        }
    }

    private lateinit var crowdHomeFactorSwitch: SwitchMaterial
    private fun createHomeFactorSwitch() {
        crowdHomeFactorSwitch = findViewById(R.id.home_factor_switch)
        crowdHomeFactorSwitch.setOnCheckedChangeListener { _, isChecked ->
            scoreboardConfig.homeFactor = isChecked
        }
    }

    private lateinit var proceedButton: Button
    private fun createProceedButton() {
        proceedButton = findViewById(R.id.proceed_button)
        proceedButton.setOnClickListener {
            val intent = Intent(this, ScoreboardActivity::class.java).apply {
                putExtra("CONFIG", scoreboardConfig)
            }
            startActivity(intent)
        }
    }

    private lateinit var homeTextView: EditText
    private fun createHomeTextView() {
        homeTextView = findViewById(R.id.homePlainTextView)
        homeTextView.setOnEditorActionListener { v, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_DONE) {
                val homeName = v.text.toString().uppercase()
                scoreboardConfig.homeName = homeName.ifBlank { "HOME" }
            }
            return@setOnEditorActionListener false
        }
    }

    private lateinit var awayTextView: EditText
    private fun createAwayTextView() {
        awayTextView = findViewById(R.id.awayPlainTextView)
        awayTextView.setOnEditorActionListener { v, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_DONE) {
                val awayName = v.text.toString().uppercase()
                scoreboardConfig.awayName = awayName.ifBlank { "AWAY" }
            }
            return@setOnEditorActionListener false
        }
    }
}