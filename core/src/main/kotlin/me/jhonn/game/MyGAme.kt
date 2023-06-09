package me.jhonn.game

import com.badlogic.gdx.physics.box2d.World
import ktx.app.KtxGame
import me.jhonn.game.box2d.Box2DModel
import me.jhonn.game.views.AbstractScreen
import me.jhonn.game.views.MenuScreen
import me.jhonn.game.views.TestGame

class MyGAme : KtxGame<AbstractScreen>() {
    var isPaused = false
    var isDebug = false
    private lateinit var box2DModel: Box2DModel
    private lateinit var assetManager: com.badlogic.gdx.assets.AssetManager
    lateinit var world: World

    override fun create() {
        assetManager = me.jhonn.game.manager.AssetManager()
        box2DModel = Box2DModel()
        world = box2DModel.world
        addScreens()
        setScreen<MenuScreen>()
    }

    private fun addScreens() {
        addScreen(MenuScreen(this, assetManager))
        addScreen(TestGame(this, assetManager))
    }

    override fun render() {
        super.render()
        box2DModel.isPaused = isPaused
        box2DModel.stepWorldBox2d()
    }

    override fun dispose() {
        box2DModel.disposeSafely()
        super.dispose()
    }


}


