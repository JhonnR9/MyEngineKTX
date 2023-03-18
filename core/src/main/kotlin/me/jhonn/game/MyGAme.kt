package me.jhonn.game

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Event
import ktx.app.KtxGame
import ktx.app.clearScreen
import ktx.graphics.use
import ktx.scene2d.*
import me.jhonn.game.box2d.Box2DModel
import me.jhonn.game.constant.GameConstant.ConvertUnits.toBox2DUnits
import me.jhonn.game.views.AbstractScreen
import me.jhonn.game.views.MenuScreen
import me.jhonn.game.views.TestGame

class MyGAme : KtxGame<AbstractScreen>() {
    val isDebug = false
    lateinit var box2DModel: Box2DModel

    override fun create() {
        box2DModel = Box2DModel()
        addScreens()
        setScreen<MenuScreen>()
    }

    private fun addScreens() {
        addScreen(MenuScreen(this))
        addScreen(TestGame(this))
    }

    override fun render() {
        super.render()
        box2DModel.stepWorldBox2d()
    }

    override fun dispose() {
        box2DModel.disposeSafely()
        super.dispose()
    }


}


