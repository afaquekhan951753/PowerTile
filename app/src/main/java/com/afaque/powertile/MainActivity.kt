package com.afaque.powertile

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

/**
 * Simple setup activity shown when the accessibility service is not yet enabled.
 * Provides a button to navigate directly to Accessibility Settings.
 *
 * If the service is already active, it shows a confirmation message instead.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val statusText = findViewById<TextView>(R.id.statusText)
        val setupButton = findViewById<Button>(R.id.setupButton)
        val powerButton = findViewById<Button>(R.id.powerButton)

        setupButton.setOnClickListener {
            // Open system Accessibility Settings
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            startActivity(intent)
        }

        powerButton.setOnClickListener {
            if (PowerMenuService.isActive) {
                PowerMenuService.instance?.openPowerMenu()
            }
        }

        updateUI(statusText, setupButton, powerButton)
    }

    override fun onResume() {
        super.onResume()
        val statusText = findViewById<TextView>(R.id.statusText)
        val setupButton = findViewById<Button>(R.id.setupButton)
        val powerButton = findViewById<Button>(R.id.powerButton)
        updateUI(statusText, setupButton, powerButton)
    }

    private fun updateUI(statusText: TextView, setupButton: Button, powerButton: Button) {
        if (PowerMenuService.isActive) {
            statusText.text = "✅ Service is active!\n\nYou can now use the Quick Settings tile to open the Power Menu.\n\nOr tap the button below."
            setupButton.visibility = Button.GONE
            powerButton.visibility = Button.VISIBLE
        } else {
            statusText.text = "⚠️ Accessibility Service is not enabled.\n\nTap the button below to open Accessibility Settings, then find and enable \"Power Menu Helper\"."
            setupButton.visibility = Button.VISIBLE
            powerButton.visibility = Button.GONE
        }
    }
}
