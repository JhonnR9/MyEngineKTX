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


class PlayerActor(x: Float, y: Float, private val world: World, myAssetManager: AssetManager) :
    AbstractActor(world, myAssetManager), InputProcessor {
    var maxVelocity = 3f
    var moveForce = 8f
    var jumpForce = 8f

    private var moveState: MoveState = MoveState.MS_STOP
    private val soundJump = myAssetManager.get<Sound>("sounds/jump.mp3")
    private val soundSlash = myAssetManager.get<Sound>("sounds/slash.mp3")
    var onTheFloor = false
        set(onTheFloor) {
            if (onTheFloor) {
                moveState = MoveState.MS_STOP
            }
            field = onTheFloor
        }


    init {
        setPosition(x, y)
        loadAnimation()
        createBody()
    }

    private fun loadAnimation() {
        animationManager.loadAnimationFromAtlas(AnimationType.IDLE.key, .7f)
        animationManager.loadAnimationFromAtlas(AnimationType.JUMP.key, Animation.PlayMode.NORMAL, .05f)
        animationManager.loadAnimationFromAtlas(AnimationType.WALK.key, .3f)
        animationManager.loadAnimationFromAtlas(AnimationType.ATTACK.key, Animation.PlayMode.NORMAL, .07f)
        frame = animationManager.getFrame()

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
            friction = 1f
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

        if (Gdx.input.isKeyPressed(Keys.LEFT)) {
            if (moveState != MoveState.JUMPING) {
                moveState = MoveState.MS_LEFT
            }

        } else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
            if (moveState != MoveState.JUMPING) {
                moveState = MoveState.MS_RIGHT
            }

        }
        limitVelocityX(delta)

    }

    private fun move(delta: Float) {
        if (moveState == MoveState.MS_LEFT) {
            val force = body.mass * delta * toGameUnits(moveForce)
            body.applyForce(Vector2(-force, 0f), body.position, true)
            animationManager.setCurrentAnimation(AnimationType.WALK.key)
            isFlip = true
        } else
            if (moveState == MoveState.MS_RIGHT) {
                val force = body.mass * Gdx.graphics.deltaTime * toGameUnits(moveForce)
                body.applyForce(Vector2(force, 0f), body.position, true)
                animationManager.setCurrentAnimation(AnimationType.WALK.key)
                isFlip = false
            } else
                if (moveState == MoveState.JUMPING) {
                    jump(delta)
                    animationManager.setCurrentAnimation(AnimationType.JUMP.key)
                } else if (moveState == MoveState.MS_STOP) {
                    body.linearDamping = .1f
                    animationManager.setCurrentAnimation(AnimationType.IDLE.key)
                } else if (moveState == MoveState.ATTACK) {
                    animationManager.setCurrentAnimation(AnimationType.ATTACK.key)
                    if (animationManager.isAnimationFinished()) {
                        moveState = MoveState.MS_STOP
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
        if (keycode == Keys.SPACE) {
            if (onTheFloor) soundJump.play()
            moveState = MoveState.JUMPING
        } else if (keycode == Keys.E) {
            moveState = MoveState.ATTACK
            soundSlash.play()
        }
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        if (moveState != MoveState.JUMPING && moveState != MoveState.ATTACK) {
            moveState = MoveState.MS_STOP
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

enum class AnimationType {
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

enum class MoveState {
    MS_STOP,
    MS_LEFT,
    MS_RIGHT,
    JUMPING,
    ATTACK
}
