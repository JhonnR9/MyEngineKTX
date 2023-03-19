package me.jhonn.game.views

import com.badlogic.gdx.physics.box2d.World
import me.jhonn.game.MyGAme
import me.jhonn.game.actor.FloorActor
import me.jhonn.game.actor.PlayerActor

class TestGame(game: MyGAme) : AbstractScreen(game) {
    private var world: World = game.world
    private lateinit var player: PlayerActor
    private lateinit var floor: FloorActor

    override fun show() {
        super.show()
        player = PlayerActor(viewport.worldWidth / 2, viewport.worldHeight / 2, world)
        floor = FloorActor(0f, 0f, world)

        addActor(player)
        addActor(floor)
        inputMultiplexer.addProcessor(player)
    }





}
