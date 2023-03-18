@file:JvmName("Lwjgl3Launcher")

package me.jhonn.game.lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import me.jhonn.game.MyGAme

/** Launches the desktop (LWJGL3) application. */
fun main() {
    Lwjgl3Application(MyGAme(), Lwjgl3ApplicationConfiguration().apply {
        setTitle("MyEngineKTX")
        setWindowedMode(1280, 720)
        setWindowIcon(*(arrayOf(128, 64, 32, 16).map { "libgdx$it.png" }.toTypedArray()))
    })
}
