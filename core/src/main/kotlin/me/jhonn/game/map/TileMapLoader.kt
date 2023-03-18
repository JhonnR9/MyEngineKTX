package me.jhonn.game.map

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.objects.EllipseMapObject
import com.badlogic.gdx.maps.objects.PolygonMapObject
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.objects.TextureMapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapRenderer
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import me.jhonn.game.constant.GameConstant

class TiledMapLoader(fileName: String, private val world: World) {
    private lateinit var tiledMap: TiledMap
    private lateinit var tiledMapRenderer: TiledMapRenderer
    private var isLoadMap = false
    private var tilesLayers = intArrayOf(0)
    private var foregroundLayers = intArrayOf(2)

    init {
        createTileMap(fileName)
        //BaseActor.createWorldBounds(tiledMap)
    }

    private fun createTileMap(fileName: String) {
        tiledMap = TmxMapLoader().load(fileName)
        val unitScale = GameConstant.ConvertUnits.toBox2DUnits(1f)
        tiledMapRenderer = OrthogonalTiledMapRenderer(tiledMap, unitScale)
        isLoadMap = true
    }

    fun setOpacityLayer(layerName: String, opacity: Float) {
        try {
            tiledMap.layers.get(layerName).opacity = opacity
        } catch (_: IllegalStateException) {
            Gdx.app.error(
                "TileMapLoader",
                "failed to find a layer called \"$layerName\" check that the name is correct"
            )
        }

    }

    fun renderTiles(camera: Camera) {
        camera as OrthographicCamera
        if (isLoadMap) {
            tiledMapRenderer.setView(camera)
            tiledMapRenderer.render(tilesLayers)
        }
    }

    fun renderForeground(camera: Camera) {
        camera as OrthographicCamera
        if (isLoadMap) {
            tiledMapRenderer.setView(camera)
            tiledMapRenderer.render(foregroundLayers)
        }
    }

    fun addContactCollider(layerColliderName: String) {
        try {
            val collisions = tiledMap.layers.get(layerColliderName).objects

            var shape: Shape = PolygonShape()
            val bodyDef = BodyDef()


            bodyDef.type = BodyDef.BodyType.StaticBody

            for (mapObject in collisions) {

                if (mapObject is TextureMapObject) {
                    continue

                } else if (mapObject is RectangleMapObject) {
                    shape = getRectangle(mapObject)

                }

                val body: Body = world.createBody(bodyDef)
                with(body.createFixture(shape, 1f)){
                    isSensor =true
                    // userData= UserData.SENSOR_FOREGROUND
                }




            }
            Gdx.app.log(
                "TilledMapLoader",
                "Layer \"$layerColliderName\" add"
            )
        } catch (_: IllegalStateException) {
            Gdx.app.error(
                "TilledMapLoader",
                "failed to find a layer called \"$layerColliderName\" check that the name is correct"
            )
        }

    }

    fun addMapColliders(layerColliderName: String) {
        try {
            val collisions = tiledMap.layers.get(layerColliderName).objects

            var shape: Shape = PolygonShape()
            val bodyDef = BodyDef()


            bodyDef.type = BodyDef.BodyType.StaticBody
            for (mapObject in collisions) {

                if (mapObject is TextureMapObject) {
                    continue

                } else if (mapObject is RectangleMapObject) {
                    shape = getRectangle(mapObject)

                } else if (mapObject is EllipseMapObject) {
                    // shape = getCircle(mapObject)

                } else if (mapObject is PolygonMapObject) {
                    shape = getPolygon(mapObject)
                }

                val body: Body = world.createBody(bodyDef)
                body.createFixture(shape, 1f)


            }
            Gdx.app.log(
                "TilledMapLoader",
                "Layer \"$layerColliderName\" add"
            )
        } catch (_: IllegalStateException) {
            Gdx.app.error(
                "TilledMapLoader",
                "failed to find a layer called \"$layerColliderName\" check that the name is correct"
            )
        }

    }

    private fun getRectangle(rectangleObject: RectangleMapObject): PolygonShape {
        val rectangle: Rectangle = rectangleObject.rectangle
        val polygon = PolygonShape()
        val x = GameConstant.ConvertUnits.toBox2DUnits(rectangle.x) + (GameConstant.ConvertUnits.toBox2DUnits(rectangle.width) / 2)
        val y = GameConstant.ConvertUnits.toBox2DUnits(rectangle.y) + (GameConstant.ConvertUnits.toBox2DUnits(rectangle.height) / 2)

        val center = Vector2(x, y)
        polygon.setAsBox(
            GameConstant.ConvertUnits.toBox2DUnits(rectangle.width / 2),
            GameConstant.ConvertUnits.toBox2DUnits(rectangle.height / 2),
            center,
            0f
        )
        return polygon
    }

    private fun getPolygon(polygonObject: PolygonMapObject): PolygonShape {
        val polygon = PolygonShape()
        val vertices = polygonObject.polygon.transformedVertices
        val worldVertices = FloatArray(vertices.size)


        for (i in vertices.indices) {
            println(vertices[i] / GameConstant.ConvertUnits.PPM)
            worldVertices[i] = vertices[i] / GameConstant.ConvertUnits.PPM
        }
        polygon.set(worldVertices)
        return polygon
    }

    fun dispose() {
        tiledMap.dispose()
    }

    private fun getEllipse(ellipseObject: EllipseMapObject): CircleShape {
        TODO("not implemented")
    }
}

