package me.jhonn.game.constant

class GameConstant {
    object ConvertUnits {
        const val PPM = 32f


        fun toGameUnits(value: Float): Float {
            return value * PPM
        }

        fun toBox2DUnits(value: Float): Float {
            return value / PPM
        }
        fun toBox2DUnits(value: Int): Float {
            value.toFloat()
            return value / PPM
        }

    }

    object Physical {
        const val STEP_TIME = 1f / 60f
        const val VELOCITY_ITERATIONS = 6
        const val POSITION_ITERATIONS = 2
    }
}
