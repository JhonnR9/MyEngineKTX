package me.jhonn.game.manager

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.ui.Skin

class AssetManager : AssetManager() {

    init {
        loadAssets()
    }

    private fun loadAssets() {
        load("ui/neon-ui.json", Skin::class.java)
        load("sounds/jump.mp3", Sound::class.java)
        load("sounds/slash.mp3", Sound::class.java)
        load("graphics/graphics.atlas", TextureAtlas::class.java)
        finishLoading()

    }
}
