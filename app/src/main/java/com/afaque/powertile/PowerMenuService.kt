package com.afaque.powertile

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent

/**
 * AccessibilityService that provides the ability to open the system Power Menu.
 *
 * This service uses [GLOBAL_ACTION_POWER_DIALOG] to trigger the power menu,
 * which is useful when the physical power button is broken.
 *
 * The service itself does nothing in terms of accessibility event processing —
 * it only exists so we can call [performGlobalAction] from elsewhere in the app.
 */
class PowerMenuService : AccessibilityService() {

    companion object {
        private const val TAG = "PowerMenuService"

        /** Singleton reference so other components can trigger the power menu. */
        var instance: PowerMenuService? = null
            private set

        /** Returns true if the service is currently active and connected. */
        val isActive: Boolean
            get() = instance != null
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
        Log.d(TAG, "PowerMenuService connected and ready.")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // We don't need to process accessibility events.
    }

    override fun onInterrupt() {
        Log.d(TAG, "PowerMenuService interrupted.")
    }

    override fun onDestroy() {
        super.onDestroy()
        instance = null
        Log.d(TAG, "PowerMenuService destroyed.")
    }

    /**
     * Triggers the system Power Dialog (Power Menu).
     * Returns true if the action was performed successfully.
     */
    fun openPowerMenu(): Boolean {
        Log.d(TAG, "Performing GLOBAL_ACTION_POWER_DIALOG")
        return performGlobalAction(GLOBAL_ACTION_POWER_DIALOG)
    }
}
