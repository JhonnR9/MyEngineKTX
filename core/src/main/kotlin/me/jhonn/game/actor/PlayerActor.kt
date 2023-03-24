package me.jhonn.game.actor

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType.DynamicBody
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.physics.box2d.World
import ktx.assets.disposeSafely
import me.jhonn.game.constant.GameConstant.ConvertUnits.toGameUnits


class PlayerActor(
    x: Float, y: Float, private val world: World,
    assetManager: AssetManager
) : AbstractActor(world, assetManager),
    InputProcessor {

    var maxVelocity = 3f
    var moveForce = 20f
    var jumpForce = 13.5f

    private var moveState: MoveState = MoveState.MS_IDLE
    private var runningAction: Boolean = false

    private val soundJump = assetManager.get<Sound>("sounds/jump.mp3")
    private val soundSlash = assetManager.get<Sound>("sounds/slash.mp3")


    var onTheFloor = false
        set(onTheFloor) {
            if (onTheFloor) {
                moveState = MoveState.MS_IDLE
            }
            field = onTheFloor
        }


    init {
        setPosition(x, y)
        loadAnimation()
        frame = animationManager.getFrame()
        createBody()
    }

    private fun loadAnimation() {

        with(animationManager) {
            loadAnimationFromAtlas(AnimationType.IDLE.key, .7f)
            loadAnimationFromAtlas(AnimationType.JUMP.key, Animation.PlayMode.NORMAL, .05f)
            loadAnimationFromAtlas(AnimationType.WALK.key, .3f)
            loadAnimationFromAtlas(AnimationType.ATTACK.key, Animation.PlayMode.NORMAL, .07f)

        }

    }

    private fun createBody() {

        val myBodyDef = BodyDef().apply {
            type = DynamicBody
            position.set(x + originX, y + originY)
            angle = 0f
            fixedRotation = true
        }
        body = world.createBody(myBodyDef)


        val offsetX = .2f
        val polygonShape = PolygonShape()
        polygonShape.setAsBox(originX - offsetX, originY)

        val fixtureDef = FixtureDef()
        fixtureDef.apply {
            shape = polygonShape
            density = 1f
            friction = 2f
        }

        body.createFixture(fixtureDef).apply { userData = this@PlayerActor }
        polygonShape.disposeSafely()
    }

    private fun limitVelocityX(delta: Float) {
        val maxX = maxVelocity
        val minX = -maxVelocity
        val vX = body.linearVelocity.x

        if (vX > maxX || vX < minX) return
        move(delta)

    }

    override fun act(delta: Float) {
        super.act(delta)
        frame = animationManager.getFrame()
        animationManager.update(delta)


        keyBoardListener()
        limitVelocityX(delta)
        changeAnimation()
        playSounds()

    }

    private fun keyBoardListener() {
        val canWalk = (moveState != MoveState.MS_JUMP && moveState != MoveState.MS_ATTACK)

        if (Gdx.input.isKeyPressed(Keys.LEFT)) {
            if (canWalk) {
                moveState = MoveState.MS_LEFT
            }

        } else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
            if (canWalk) {
                moveState = MoveState.MS_RIGHT
            }
        }
    }

    private fun changeAnimation() {
        when (moveState) {
            MoveState.MS_JUMP -> {
                animationManager.setCurrentAnimation(AnimationType.JUMP.key)
            }

            MoveState.MS_ATTACK -> {
                animationManager.setCurrentAnimation(AnimationType.ATTACK.key)
                if (animationManager.isAnimationFinished()) {
                    moveState = MoveState.MS_IDLE
                }
            }

            MoveState.MS_IDLE -> {
                animationManager.setCurrentAnimation(AnimationType.IDLE.key)
            }

            MoveState.MS_LEFT -> {
                animationManager.setCurrentAnimation(AnimationType.WALK.key)
                isFlip = true
            }

            MoveState.MS_RIGHT -> {
                animationManager.setCurrentAnimation(AnimationType.WALK.key)
                isFlip = false
            }
        }

    }

    private fun move(delta: Float) {
        val force = body.mass * delta * toGameUnits(moveForce)
        when (moveState) {
            MoveState.MS_JUMP -> {
                jump(delta)
            }

            MoveState.MS_IDLE -> {
                body.linearDamping = 1f
            }

            MoveState.MS_LEFT -> {
                body.applyForce(Vector2(-force, 0f), body.position, true)
            }

            MoveState.MS_RIGHT -> {
                body.applyForce(Vector2(force, 0f), body.position, true)
            }

            else -> {
                return
            }
        }

    }

    private fun playSounds() {

        runningAction = when (moveState) {
            MoveState.MS_JUMP -> {
                if (runningAction) return
                soundJump.play()
                true
            }

            MoveState.MS_ATTACK -> {
                if (runningAction) return
                soundSlash.play()
                true
            }

            else -> {
                false
            }

        }
    }

    private fun jump(delta: Float) {
        if (onTheFloor) {
            val impulse = body.mass * toGameUnits(jumpForce) * delta
            body.applyLinearImpulse(Vector2(0f, impulse), body.position, true)
            onTheFloor = false
        }
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Keys.SPACE && moveState != MoveState.MS_JUMP) {
            moveState = MoveState.MS_JUMP

        } else if (keycode == Keys.E && moveState != MoveState.MS_ATTACK) {
            soundSlash.play()
            moveState = MoveState.MS_ATTACK
        }
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        if (moveState != MoveState.MS_JUMP && moveState != MoveState.MS_ATTACK) {
            moveState = MoveState.MS_IDLE
        }
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

private enum class AnimationType {
    IDLE {
        override val key: String
            get() = "player/idle"
    },
    WALK {
        override val key: String
            get() = "player/walk"
    },
    ATTACK {
        override val key: String
            get() = "player/attack"
    },
    JUMP {
        override val key: String
            get() = "player/jump"
    };

    abstract val key: String
}

private enum class MoveState {
    MS_IDLE, MS_LEFT, MS_RIGHT, MS_JUMP, MS_ATTACK
}
