package me.jhonn.game.manager

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.graphics.g2d.TextureRegion
import ktx.scene2d.Scene2DSkin

class AnimationManager() {
    private var stateTime: Float = 0f
    private lateinit var animation: Animation<AtlasRegion>
    private val animationsCache = mutableMapOf<String, Animation<AtlasRegion>>()
    val defaultFrameDuration = 1 / 8f

    fun update(deltaTime: Float) {
        stateTime += deltaTime
    }

    fun getFrame(): TextureRegion? {
        return animation.getKeyFrame(stateTime)
    }


    fun loadAnimationFromAtlas(regionName: String) {
        val animationRegion = Scene2DSkin.defaultSkin.atlas.findRegion(regionName)

        if (animationsCache.containsKey(regionName)){
            animation = animationsCache[regionName]!!
        }else{
            animationsCache[regionName] = Animation(defaultFrameDuration, animationRegion)
        }

    }
    fun setCurrentAnimation(name:String){
        stateTime = 0f
        animation = animationsCache[name]!!
    }
}
