package me.jhonn.game.actor

import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.PolygonShape
import ktx.assets.disposeSafely
import ktx.scene2d.Scene2DSkin
import me.jhonn.game.box2d.Box2DModel

class FloorActor(x: Float, y: Float, box2DModel: Box2DModel) : AbstractActor(x, y, box2DModel) {
    init {

        frame = Scene2DSkin.defaultSkin.atlas.findRegion("white")
        width = 16f
        height = 1f

        val bodyDef = BodyDef()
        bodyDef.type = BodyDef.BodyType.StaticBody
        bodyDef.position.set(x + (width / 2), y + (height / 2))


        body = box2DModel.world.createBody(bodyDef)

        val shape = PolygonShape()
        shape.setAsBox(width / 2, height / 2)
        val fixtureDef = FixtureDef()
        fixtureDef.shape = shape
        fixtureDef.density = 2f
        body.createFixture(shape, 2f)

        shape.disposeSafely()

    }

    override fun act(delta: Float) {
        super.act(delta)
        this.x = body.position.x - width / 2
        this.y = body.position.y - height / 2
    }
}
