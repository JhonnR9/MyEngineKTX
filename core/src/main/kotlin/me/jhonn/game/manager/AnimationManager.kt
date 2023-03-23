package me.jhonn.game.manager

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.graphics.g2d.TextureRegion


class AnimationManager(myAssetManager: AssetManager) {
    private val atlas: TextureAtlas = myAssetManager.get("graphics/graphics.atlas")
    private var stateTime: Float = 0f
    private val animationsCache = mutableMapOf<String, Animation<AtlasRegion>>()
    private lateinit var currentAnimationKey: String
    private val defaultFrameDuration = 1 / 2f

    fun update(deltaTime: Float) {
        stateTime += deltaTime
    }

    fun getFrame(): TextureRegion? {
        return animationsCache[currentAnimationKey]?.getKeyFrame(stateTime)
    }

    fun isAnimationFinished(): Boolean {
        return animationsCache[currentAnimationKey]?.isAnimationFinished(stateTime) ?: false
    }


    fun loadAnimationFromAtlas(regionName: String) {
        loadAnimationFromAtlas(regionName, PlayMode.LOOP)
    }

    fun loadAnimationFromAtlas(regionName: String, playMode: PlayMode) {
        loadAnimationFromAtlas(regionName, playMode, defaultFrameDuration)
    }

    fun loadAnimationFromAtlas(regionName: String, frameDuration: Float) {
        loadAnimationFromAtlas(regionName, PlayMode.LOOP, frameDuration)
    }

    fun loadAnimationFromAtlas(regionName: String, playMode: PlayMode, frameDuration: Float) {
        val animationRegion = atlas.findRegions(regionName)
        animationsCache[regionName] = Animation(frameDuration, animationRegion, playMode)
        if (!::currentAnimationKey.isInitialized) {
            currentAnimationKey = regionName
        }
    }

    fun setCurrentAnimation(regionName: String) {
        if (currentAnimationKey == regionName) return
        stateTime = 0f
        currentAnimationKey = regionName

    }
}

