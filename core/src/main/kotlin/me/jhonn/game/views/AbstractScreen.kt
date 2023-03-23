package me.jhonn.game.views

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.viewport.ExtendViewport
import ktx.app.KtxScreen
import ktx.app.clearScreen
import ktx.assets.disposeSafely
import ktx.scene2d.Scene2DSkin
import me.jhonn.game.MyGAme
import me.jhonn.game.constant.GameConstant.ConvertUnits.toGameUnits

abstract class AbstractScreen(private val game: MyGAme, myAssetManager: AssetManager) :
    Stage(ExtendViewport(VIEWPORT_WIDTH, VIEWPORT_HEIGHT)),
    KtxScreen {
    val uiStage = Stage(ExtendViewport(UI_VIEWPORT_WIDTH, UI_VIEWPORT_HEIGHT))
    private val skin: Skin = myAssetManager.get("ui/neon-ui.json")
    private val box2DDebugRenderer = Box2DDebugRenderer(true, true, true, true, true, true)
    val inputMultiplexer = InputMultiplexer()

    companion object {
        private const val scale = .7f
        private var uiScale = 1

        const val VIEWPORT_WIDTH = 16f * scale
        const val VIEWPORT_HEIGHT = 9f * scale

        val UI_VIEWPORT_WIDTH = toGameUnits(VIEWPORT_WIDTH) / uiScale
        val UI_VIEWPORT_HEIGHT = toGameUnits(VIEWPORT_HEIGHT) / uiScale

        var worldWidth: Float = 0f
        var worldHeight: Float = 0f
    }


    init {
        Scene2DSkin.defaultSkin = skin
    }

    override fun show() {
        Gdx.input.inputProcessor = inputMultiplexer
        inputMultiplexer.setProcessors(uiStage, this)
        worldWidth = viewport.worldWidth
        worldHeight = viewport.worldHeight
    }

    fun isTouchDownEvent(e: Event): Boolean {
        return if (e is InputEvent) {
            e.type.equals(InputEvent.Type.touchDown)
        } else false
    }

    private fun halfOf(value: Float) = value * .5f

    fun centerActor(actor: Actor, percent: Float) {
        actor.setBounds(
            halfOf(UI_VIEWPORT_WIDTH) - halfOf(UI_VIEWPORT_WIDTH * percent),
            halfOf(UI_VIEWPORT_HEIGHT) - halfOf(UI_VIEWPORT_HEIGHT * percent),
            UI_VIEWPORT_WIDTH * percent,
            UI_VIEWPORT_HEIGHT * percent
        )
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
        uiStage.viewport.update(width, height, true)
    }

    override fun render(delta: Float) {
        clearScreen(red = 0.1f, green = 0.1f, blue = 0.3f)
        this.act()
        uiStage.act()
        this.draw()
        uiStage.draw()
        if (game.isDebug) {
            box2DDebugRenderer.render(game.world, this.viewport.camera.combined)
        }
    }


    override fun hide() {
        (Gdx.input.inputProcessor as InputMultiplexer).clear()
        super.hide()
        clear()
        uiStage.clear()
    }

    override fun dispose() {
        skin.disposeSafely()
    }
}
