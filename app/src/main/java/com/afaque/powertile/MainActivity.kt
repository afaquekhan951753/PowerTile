package com.afaque.powertile

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

/**
 * Premium setup activity with animations.
 * Guides the user to enable the accessibility service, or provides
 * a direct power-menu button when the service is already active.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val setupButton = findViewById<Button>(R.id.setupButton)
        val powerButton = findViewById<Button>(R.id.powerButton)

        setupButton.setOnClickListener {
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        }

        powerButton.setOnClickListener {
            if (PowerMenuService.isActive) {
                PowerMenuService.instance?.openPowerMenu()
            }
        }

        // Entrance animations
        playEntranceAnimations()
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    private fun updateUI() {
        val statusPill = findViewById<TextView>(R.id.statusPill)
        val statusText = findViewById<TextView>(R.id.statusText)
        val stepsContainer = findViewById<LinearLayout>(R.id.stepsContainer)
        val setupButton = findViewById<Button>(R.id.setupButton)
        val powerButton = findViewById<Button>(R.id.powerButton)
        val iconBg = findViewById<View>(R.id.iconBg)
        val appIcon = findViewById<ImageView>(R.id.appIcon)

        if (PowerMenuService.isActive) {
            // Active state
            statusPill.text = "● Service Active"
            statusPill.setTextColor(ContextCompat.getColor(this, R.color.green_active))
            statusPill.setBackgroundResource(R.drawable.bg_status_pill_active)

            statusText.text = getString(R.string.instruction_active)
            stepsContainer.visibility = View.GONE

            setupButton.visibility = View.GONE
            powerButton.visibility = View.VISIBLE

            // Tint the drawableStart (power icon) to white on the green button
            for (d in powerButton.compoundDrawablesRelative) {
                d?.setTint(ContextCompat.getColor(this, R.color.white))
            }

            iconBg.setBackgroundResource(R.drawable.bg_icon_circle_active)
            appIcon.setColorFilter(ContextCompat.getColor(this, R.color.green_active))

            // Pulse animation on the icon
            playPulseAnimation(findViewById(R.id.iconContainer))
        } else {
            // Inactive state
            statusPill.text = "● Setup Required"
            statusPill.setTextColor(ContextCompat.getColor(this, R.color.red_primary))
            statusPill.setBackgroundResource(R.drawable.bg_status_pill_inactive)

            statusText.text = getString(R.string.instruction_inactive)
            stepsContainer.visibility = View.VISIBLE

            setupButton.visibility = View.VISIBLE
            powerButton.visibility = View.GONE

            iconBg.setBackgroundResource(R.drawable.bg_icon_circle)
            appIcon.setColorFilter(ContextCompat.getColor(this, R.color.red_primary))
        }
    }

    private fun playEntranceAnimations() {
        val iconContainer = findViewById<FrameLayout>(R.id.iconContainer)
        val statusCard = findViewById<LinearLayout>(R.id.statusCard)
        val tileCard = findViewById<LinearLayout>(R.id.tileCard)

        // Icon: scale + fade in
        iconContainer.scaleX = 0f
        iconContainer.scaleY = 0f
        iconContainer.alpha = 0f

        val iconScale = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(iconContainer, "scaleX", 0f, 1f),
                ObjectAnimator.ofFloat(iconContainer, "scaleY", 0f, 1f),
                ObjectAnimator.ofFloat(iconContainer, "alpha", 0f, 1f)
            )
            duration = 600
            startDelay = 100
            interpolator = OvershootInterpolator(1.5f)
        }

        // Status card: slide up + fade
        statusCard.translationY = 60f
        statusCard.alpha = 0f
        val cardAnim = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(statusCard, "translationY", 60f, 0f),
                ObjectAnimator.ofFloat(statusCard, "alpha", 0f, 1f)
            )
            duration = 500
            startDelay = 300
            interpolator = DecelerateInterpolator(1.5f)
        }

        // Tile card: slide up + fade
        tileCard.translationY = 60f
        tileCard.alpha = 0f
        val tileAnim = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(tileCard, "translationY", 60f, 0f),
                ObjectAnimator.ofFloat(tileCard, "alpha", 0f, 1f)
            )
            duration = 500
            startDelay = 450
            interpolator = DecelerateInterpolator(1.5f)
        }

        AnimatorSet().apply {
            playTogether(iconScale, cardAnim, tileAnim)
            start()
        }
    }

    private fun playPulseAnimation(view: View) {
        val pulseX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.08f, 1f).apply {
            duration = 1500
            repeatCount = ValueAnimator.INFINITE
        }
        val pulseY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.08f, 1f).apply {
            duration = 1500
            repeatCount = ValueAnimator.INFINITE
        }
        AnimatorSet().apply {
            playTogether(pulseX, pulseY)
            start()
        }
    }
}
