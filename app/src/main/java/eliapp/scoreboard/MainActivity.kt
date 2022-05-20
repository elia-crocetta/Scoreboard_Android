package eliapp.scoreboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.View
import android.widget.*
import com.google.android.material.switchmaterial.SwitchMaterial
import java.io.Serializable

data class ScoreboardConfiguration(var extraTime: Boolean, var minutes: Long) : Serializable {}

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private var scoreboardConfig: ScoreboardConfiguration = ScoreboardConfiguration(false, 900)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createSpinner()
        createSwitch()
        createProceedButton()
    }

    private lateinit var spinner: Spinner
    private fun createSpinner() {
        spinner = findViewById<Spinner>(R.id.select_minutes_spinner)
        ArrayAdapter.createFromResource(
            this,
            R.array.Array_Minutes,
            android.R.layout.simple_spinner_item,
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = this
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (position) {
            0 -> scoreboardConfig.minutes = 900000
            1 -> scoreboardConfig.minutes = 600000
            2 -> scoreboardConfig.minutes = 10000
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        scoreboardConfig.minutes = 900
    }

    private lateinit var extraTimeSwitch: SwitchMaterial
    private fun createSwitch() {
        extraTimeSwitch = findViewById<SwitchMaterial>(R.id.extratime_switch)
        extraTimeSwitch.setOnCheckedChangeListener { _, isChecked ->
            scoreboardConfig.extraTime = isChecked
        }
    }

    private lateinit var proceedButton: Button
    private fun createProceedButton() {
        proceedButton = findViewById<Button>(R.id.proceed_button)
        proceedButton.setOnClickListener {
            val intent = Intent(this, ScoreboardActivity::class.java).apply {
                putExtra("CONFIG", scoreboardConfig)
            }
            startActivity(intent)
        }
    }
}