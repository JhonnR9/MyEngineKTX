package me.jhonn.game.actor

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType.DynamicBody
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.physics.box2d.World
import ktx.assets.disposeSafely


class PlayerActor(x: Float, y: Float, world: World) : AbstractActor(world), InputProcessor {
    private var moveState: MoveState = MoveState.MS_STOP
    private var isJumping = false

    init {
        setPosition(x, y)
        setSize(1f, 1f)
        color = Color.GOLDENROD
    }

    override fun act(delta: Float) {
        super.act(delta)
        if (Gdx.input.isKeyPressed(Keys.LEFT)) {
            moveState = MoveState.MS_LEFT
        } else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
            moveState = MoveState.MS_RIGHT
        }
        move()

    }

    init {
        val myBodyDef = BodyDef().apply {
            type = DynamicBody
            position.set(x + originX, y + originY)
            angle = 0f
        }
        body = world.createBody(myBodyDef)


        val polygonShape = PolygonShape()
        polygonShape.setAsBox(originX, originY)

        val fixtureDef = FixtureDef()
        fixtureDef.apply {
            shape = polygonShape
            density = 10f
            friction = 5f
        }

        body.createFixture(fixtureDef)
        polygonShape.disposeSafely()

    }

    private fun move() {
        when (moveState) {

            MoveState.MS_LEFT -> {
                val impulse = body.mass * Gdx.graphics.deltaTime * 20
                body.applyLinearImpulse(Vector2(-impulse, 0f), body.position, true)
            }

            MoveState.MS_RIGHT -> {
                val impulse = body.mass * Gdx.graphics.deltaTime * 20
                body.applyLinearImpulse(Vector2(impulse, 0f), body.position, true)
            }

            MoveState.MS_STOP -> {
                return
            }
        }
    }

    private fun jump() {
        isJumping = true
        val impulse = body.mass * 6f
        body.applyLinearImpulse(Vector2(0f, impulse), body.position, true)
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Keys.SPACE) {
            jump()
        }
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        moveState = MoveState.MS_STOP
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return false
    }

    override fun scrolled(amountX: Float, amountY: Float): Boolean {
        return false
    }


}

enum class MoveState {
    MS_STOP,
    MS_LEFT,
    MS_RIGHT
}
