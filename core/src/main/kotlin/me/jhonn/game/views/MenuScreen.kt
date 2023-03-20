package me.jhonn.game.views

import com.badlogic.gdx.scenes.scene2d.Event
import ktx.scene2d.scene2d
import ktx.scene2d.table
import ktx.scene2d.textButton
import me.jhonn.game.MyGAme

class MenuScreen(private val game: MyGAme) : AbstractScreen(game) {
    init {
        val table = scene2d.table {
           if (game.isDebug){
               debug = true
           }
            centerActor(it, 1f)
            background("window")
            textButton("Start").addCaptureListener { event: Event ->
                if (isTouchDownEvent(event)) {
                    game.setScreen<TestGame>()
                    return@addCaptureListener true
                }
                return@addCaptureListener true
            }
        }
        uiStage.addActor(table)
    }


}
