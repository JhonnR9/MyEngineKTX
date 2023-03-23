package me.jhonn.game.actor

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.utils.viewport.Viewport
import ktx.scene2d.Scene2DSkin
import me.jhonn.game.constant.GameConstant.ConvertUnits.toBox2DUnits
import me.jhonn.game.manager.AnimationManager
import kotlin.random.Random

abstract class AbstractActor(world: World, myAssetManager: AssetManager) : Group() {

    lateinit var body: Body
    val animationManager = AnimationManager(myAssetManager)
    var isFlip = false

    var frame: TextureRegion? = null
        get() {
            if (field == null) {
                field = Scene2DSkin.defaultSkin.atlas.findRegion("white")
            }
            return field
        }
        set(textureRegion) {
            if (textureRegion != null) {
                val w = toBox2DUnits(textureRegion.regionWidth)
                val h = toBox2DUnits(textureRegion.regionHeight)
                this.setPosition(x, y)
                this.setSize(w, h)
                field = textureRegion
            }
        }


    override fun rotationChanged() {
        val angle = (rotation * (Math.PI / 180)).toFloat()
        if (::body.isInitialized) {
            body.setTransform(body.worldCenter, angle)
        }
    }

    override fun sizeChanged() {
        originX = half(width)
        originY = half(height)
    }

    override fun act(delta: Float) {
        if (::body.isInitialized) {
            x = (body.position.x.minus(originX))
            y = (body.position.y.minus(originY))
            val rotationAngle = (body.angle / (Math.PI / 180)).toFloat()
            rotation = rotationAngle
        }
    }


    private fun half(value: Float): Float {
        return value * 0.5f
    }

    fun center(viewport: Viewport) {
        body.position.set(viewport.worldWidth / 2, viewport.worldHeight / 2)
    }


    fun randomColor(): Color {
        fun rRGB(): Float {
            return Random.nextInt(0, 100) / 100f
        }
        return Color(rRGB(), rRGB(), rRGB(), 1f)
    }


    override fun draw(batch: Batch, parentAlpha: Float) {
        if (frame != null) {
            batch.setColor(color.r, color.g, color.b, color.a)
            if (isFlip) {
                batch.draw(frame, x + width, y, originX, originY, -width, height, scaleX, scaleY, rotation)
            } else {
                batch.draw(frame, x, y, originX, originY, width, height, scaleX, scaleY, rotation)
            }
        }
    }
}
