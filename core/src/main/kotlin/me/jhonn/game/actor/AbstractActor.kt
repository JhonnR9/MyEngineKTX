package me.jhonn.game.actor

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.scenes.scene2d.Group
import me.jhonn.game.box2d.Box2DModel
import me.jhonn.game.constant.GameConstant.ConvertUnits.toBox2DUnits
import me.jhonn.game.manager.AnimationManager
import kotlin.random.Random

abstract class AbstractActor(x: Float, y: Float, box2DModel: Box2DModel) : Group() {
    lateinit var body: Body

    init {
        this.x = x
        this.y = y
    }

    var animationManager = AnimationManager()
    var frame: TextureRegion? = null
        get() {
            if (field != null) {

            }
            return field
        }
        set(value) {
            if (value != null) {
                val texture = value.texture
                val w = toBox2DUnits(texture.width)
                val h = toBox2DUnits(texture.height)
                this.setSize(w, h)
                field = value
            }
        }


    fun setBodydefPosition(bodyDef: BodyDef) {
        bodyDef.position.set(x + (width / 2), y + (height / 2))
    }

    fun center(x: Float, y: Float) {
        setPosition(x - width / 2, y - height / 2)
    }

    fun center(other: AbstractActor) {
        center(other.x + other.width / 2, other.y + other.height / 2)
    }

    fun setOpacity(opacity: Float) {
        color.a = opacity
    }

    fun randomColor():Color {
        fun rRGB (): Float{
            return  Random.nextInt(0,100)/100f
        }
        return  Color(rRGB(),rRGB(),rRGB(),1f)
    }



    override fun draw(batch: Batch, parentAlpha: Float) {
        if (frame != null) {
            batch.setColor(color.r, color.b, color.g, color.a)
            batch.draw(frame, x, y, originX, originY, width, height, scaleX, scaleY, rotation)
        }
    }
}
