package me.jhonn.game.views

import com.badlogic.gdx.graphics.Color
import me.jhonn.game.MyGAme
import me.jhonn.game.actor.FloorActor
import me.jhonn.game.actor.TestActor
import kotlin.random.Random

class TestGame(val game: MyGAme) : AbstractScreen(game) {
    override fun show() {

        addActor(TestActor(1f, 1f, game.box2DModel))

        super.show()
    }


}
