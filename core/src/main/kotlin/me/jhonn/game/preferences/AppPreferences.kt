package me.jhonn.game.preferences

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences

class AppPreferences {
    val prefs: Preferences
        get() = Gdx.app.getPreferences(PREFS_NAME)
    var isDebug: Boolean
        get() = prefs.getBoolean(IS_DEBUG, true)
        set(isDebug) {
            prefs.putBoolean(IS_DEBUG, isDebug)
            prefs.flush()
        }
    var jumpForce: Float
        get() = prefs.getFloat(JUMP_FORCE, 0.5f)
        set(jumpForce) {
            prefs.putFloat(JUMP_FORCE, jumpForce)
            prefs.flush()
        }
    var maxVelocity: Float
        get() = prefs.getFloat(VELOCITY, 0.5f)
        set(volume) {
            prefs.putFloat(VELOCITY, volume)
            prefs.flush()
        }
    var moveForce: Float
        get() = prefs.getFloat(MOVE_FORCE, 0.5f)
        set(moveForce) {
            prefs.putFloat(MOVE_FORCE, moveForce)
            prefs.flush()
        }

    companion object {
        const val VELOCITY = "maxVelocity"
        const val JUMP_FORCE = "jump"
        const val MOVE_FORCE = "move"
        const val PREFS_NAME = "settings"
        const val IS_DEBUG = "isDebug"
    }
}
