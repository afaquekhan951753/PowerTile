package com.afaque.powertile

import android.content.Intent
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.util.Log

/**
 * Quick Settings Tile that opens the system Power Menu.
 *
 * When the user taps the tile:
 * - If [PowerMenuService] is active → triggers the power menu immediately.
 * - If not active → opens [MainActivity] which guides the user to enable
 *   the accessibility service in Settings.
 */
class PowerTile : TileService() {

    companion object {
        private const val TAG = "PowerTile"
    }

    override fun onStartListening() {
        super.onStartListening()
        updateTileState()
    }

    override fun onClick() {
        super.onClick()
        Log.d(TAG, "Tile clicked. Service active: ${PowerMenuService.isActive}")

        if (PowerMenuService.isActive) {
            // Collapse the Quick Settings panel first, then trigger power menu
            collapseQuickSettingsPanel()
            PowerMenuService.instance?.openPowerMenu()
        } else {
            // Service not enabled — open the setup activity
            collapseQuickSettingsPanel()
            val intent = Intent(this, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivityAndCollapse(intent)
        }
    }

    /**
     * Updates the tile state (active/inactive) and subtitle based on
     * whether the accessibility service is currently running.
     */
    private fun updateTileState() {
        val tile = qsTile ?: return
        if (PowerMenuService.isActive) {
            tile.state = Tile.STATE_ACTIVE
            tile.subtitle = "Tap to Power Off"
        } else {
            tile.state = Tile.STATE_INACTIVE
            tile.subtitle = "Setup required"
        }
        tile.updateTile()
    }

    /**
     * Collapses the Quick Settings panel.
     * Uses [statusBarService] reflection for broader device compatibility.
     */
    private fun collapseQuickSettingsPanel() {
        try {
            val sbService = getSystemService("statusbar")
            val sbClass = Class.forName("android.app.StatusBarManager")
            val collapse = sbClass.getMethod("collapsePanels")
            collapse.invoke(sbService)
        } catch (e: Exception) {
            Log.w(TAG, "Could not collapse QS panel: ${e.message}")
        }
    }
}
