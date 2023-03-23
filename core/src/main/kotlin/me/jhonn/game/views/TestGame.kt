package me.jhonn.game.views

import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.Align
import ktx.scene2d.*
import me.jhonn.game.MyGAme
import me.jhonn.game.actor.FloorActor
import me.jhonn.game.actor.PlayerActor
import me.jhonn.game.preferences.AppPreferences


class TestGame(val game: MyGAme, val myAssetManager: AssetManager) : AbstractScreen(game, myAssetManager) {
    private var world: World = game.world
    private lateinit var player: PlayerActor
    private lateinit var floor: FloorActor
    private lateinit var myWindow: Window

    private lateinit var mLabel: Label
    private lateinit var vLabel: Label
    private lateinit var jLabel: Label


    val prefs = AppPreferences()

    override fun show() {
        super.show()
        player = PlayerActor(viewport.worldWidth / 2, viewport.worldHeight / 2, world, myAssetManager)
        floor = FloorActor(0f, 0f, world, myAssetManager)
        val myTable = scene2d.table {
            align(Align.top)
            textButton("reset") {
                addListener(object : ChangeListener() {
                    override fun changed(event: ChangeEvent?, actor: Actor?) {
                        world.destroyBody(player.body)
                        world.destroyBody(floor.body)
                        hide()
                        show()
                    }

                })
            }
            padLeft(20f)
            checkBox("Is Debug") {
                isChecked = prefs.isDebug
                game.isDebug = isChecked
                addListener(object : ChangeListener() {
                    override fun changed(event: ChangeEvent, actor: Actor) {
                        game.isDebug = isChecked
                        prefs.isDebug = isChecked
                    }
                })
            }

        }
        myWindow = scene2d.window("Settings") {
            isVisible = false
            isMovable = false
            pad(10f)
            label("maxVelocity ")
            val vSlider = slider(.5f, 20f, 1f, false) {
                value = prefs.maxVelocity
                player.maxVelocity = prefs.maxVelocity
                addListener(object : ChangeListener() {
                    override fun changed(event: ChangeEvent?, actor: Actor?) {
                        vLabel.setText("$value")
                        player.maxVelocity = value
                        prefs.maxVelocity = value
                    }

                })
            }
            vLabel = label("${vSlider.value}")
            row()
            label("Move Force ")
            val mSlider = slider(.5f, 30f, 1f, false) {
                value = prefs.moveForce
                player.moveForce = value
                addListener(object : ChangeListener() {
                    override fun changed(event: ChangeEvent?, actor: Actor?) {
                        player.moveForce = value
                        mLabel.setText("$value")
                        prefs.moveForce = value
                    }

                })
            }
            mLabel = label("${mSlider.value}")
            row()

            label("jump Force ")
            val jSlider = slider(0f, 20f, .2f, false) {
                value = prefs.jumpForce
                player.jumpForce = prefs.jumpForce
                addListener(object : ChangeListener() {
                    override fun changed(event: ChangeEvent?, actor: Actor?) {
                        player.jumpForce = value
                        jLabel.setText(String.format("%.1f", value))
                        prefs.jumpForce = value
                    }

                })
            }
            jLabel = label(String.format("%.1f", jSlider.value))

        }

        centerActor(myWindow, .9f)
        centerActor(myTable, 1f)
        uiStage.addActor(myTable)
        uiStage.addActor(myWindow)

        addActor(player)
        addActor(floor)
        inputMultiplexer.addProcessor(player)
    }


    override fun keyDown(keyCode: Int): Boolean {
        if (keyCode == Keys.ESCAPE) {
            myWindow.isVisible = !myWindow.isVisible
            game.isPaused = myWindow.isVisible
        }
        return super.keyDown(keyCode)
    }

}
