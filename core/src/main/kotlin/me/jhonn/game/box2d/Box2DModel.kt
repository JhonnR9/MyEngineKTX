package me.jhonn.game.box2d

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Box2D
import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.viewport.Viewport
import ktx.assets.disposeSafely
import me.jhonn.game.actor.AbstractActor
import me.jhonn.game.constant.GameConstant


class Box2DModel {
    private val gravity: Vector2
    val world: World
    private var accumulator = 0f
    private lateinit var worldBounds: Rectangle
    private lateinit var listener: ContactListener
    var isPaused = false

    companion object {
        const val GRAVITY_FORCE_Y = -9.8f
        const val GRAVITY_FORCE_X = 0f
    }

    init {
        Box2D.init()
        gravity = Vector2(GRAVITY_FORCE_X, GRAVITY_FORCE_Y)
        world = World(gravity, true)
        listener = Box2DContactListener()
        world.setContactListener(listener)
    }


    fun disposeSafely() {
        world.disposeSafely()
    }

    fun stepWorldBox2d() {
        if (!isPaused) {
            val deltaTime = Gdx.graphics.deltaTime
            accumulator += deltaTime.coerceAtMost(0.25f)
            if (accumulator >= GameConstant.Physical.STEP_TIME) {
                accumulator -= GameConstant.Physical.STEP_TIME
                world.step(
                    GameConstant.Physical.STEP_TIME,
                    GameConstant.Physical.VELOCITY_ITERATIONS,
                    GameConstant.Physical.POSITION_ITERATIONS
                )
            }
        }
    }

    fun createWorldBounds(tiledMap: TiledMap) {
        val w = (
            tiledMap.properties.get("width").toString().toFloat()
                * tiledMap.properties.get("tilewidth")
                .toString()
                .toFloat()) / GameConstant.ConvertUnits.PPM
        val h = (tiledMap.properties.get("height").toString().toFloat()
            * tiledMap.properties.get("tileheight")
            .toString()
            .toFloat()) / GameConstant.ConvertUnits.PPM
        worldBounds = Rectangle(0f, 0f, w, h)
    }

    fun createWorldBounds(viewport: Viewport) {
        val w = viewport.worldWidth
        val h = viewport.worldHeight
        worldBounds = Rectangle(0f, 0f, w, h)
    }

    fun alignCameraToActor(camera: Camera, actor: AbstractActor) {
        camera.apply {
            ///  position.set(
            // actor.body.position.x + GameConstant.ConvertUnits.toBox2DUnits(actor.originX),
            // actor.body.position.y + GameConstant.ConvertUnits.toBox2DUnits(actor.originY), 0f
            // )

            val minX = viewportWidth / 2
            val maxX = worldBounds.width - viewportWidth / 2
            position.x = MathUtils.clamp(position.x, minX, maxX)

            val minY = viewportHeight / 2
            val maxY = worldBounds.height - viewportHeight / 2
            position.y = MathUtils.clamp(position.y, minY, maxY)

            update()
        }
    }

}
