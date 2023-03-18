package me.jhonn.game.actor

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.PolygonShape
import ktx.scene2d.Scene2DSkin
import me.jhonn.game.box2d.Box2DModel
import me.jhonn.game.views.AbstractScreen
import me.jhonn.game.views.AbstractScreen.Companion.worldHeight
import me.jhonn.game.views.AbstractScreen.Companion.worldWidth


class TestActor(x: Float, y: Float, box2DModel: Box2DModel) : AbstractActor(x, y, box2DModel) {
    private val velocity = Vector2(1f, 1f)

    init {
        frame = Scene2DSkin.defaultSkin.atlas.findRegion("white")
        setSize(1f, 1f)

        val bodyDef = BodyDef()
        bodyDef.type = BodyDef.BodyType.DynamicBody
        bodyDef.position.set(x + (width / 2), y + (height / 2))


        body = box2DModel.world.createBody(bodyDef)

        val shape = PolygonShape()
        shape.setAsBox(width * scaleY / 2, (height * scaleY) / 2)
        val fixtureDef = FixtureDef()
        fixtureDef.shape = shape
        fixtureDef.restitution = 1f
        fixtureDef.friction = 1f
        fixtureDef.density = 5f
        body.createFixture(shape, .2f)

        originX = (width) * .5f
        originY = (height) * .5f


    }


    override fun act(delta: Float) {
        super.act(delta)

        this.x = body.position.x - originX
        this.y = body.position.y - originY


        // if (rotation < 360) { rotation++ } else { rotation = 0f }

        val angle = (rotation * (Math.PI / 180)).toFloat()
        body.setTransform(body.worldCenter, angle)

        movement(delta)


    }

    private fun movement(delta: Float) {

        if (body.position.x > worldWidth - .5f) {
            velocity.x = delta * 100 * -1f
            color = randomColor()
        }
        if (body.position.y > worldHeight - .5f) {
            velocity.y = delta * 100 * -1f
            color = randomColor()
        }
        if (body.position.x < .5) {
            velocity.x = delta * 100 * 1f
            color = randomColor()
        }
        if (body.position.y < .5) {
            velocity.y = delta * 100 * 1f
            color = randomColor()
        }
        body.setLinearVelocity(velocity.x, velocity.y)
    }



}
