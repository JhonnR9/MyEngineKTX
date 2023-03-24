package me.jhonn.game.actor

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.physics.box2d.*
import ktx.assets.disposeSafely

class FloorActor(x: Float, y: Float, world: World, assetManager: AssetManager) :
    AbstractActor(world, assetManager) {
    private lateinit var fixture: Fixture

    init {
        setPosition(x, y)
        setSize(16f, 0.5f)
        color = Color.FOREST
    }

    init {
        val myBodyDef = BodyDef().apply {
            type = BodyDef.BodyType.StaticBody
            position.set(x + originX, y + originY)
            angle = 0f
        }
        body = world.createBody(myBodyDef).apply { userData = this }

        updateFixture()


    }

    private fun updateFixture() {
        try {
            if (::fixture.isInitialized) body.destroyFixture(fixture)
            val polygonShape = PolygonShape()
            polygonShape.setAsBox(originX, originY)

            val fixtureDef = FixtureDef()
            fixtureDef.apply {
                shape = polygonShape
                density = 1f
            }
            fixture = body.createFixture(fixtureDef)
            fixture.userData = this
            polygonShape.disposeSafely()
        } catch (_: RuntimeException) {

        }
    }


    override fun sizeChanged() {
        super.sizeChanged()
        updateFixture()
    }
}
