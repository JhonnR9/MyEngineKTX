package me.jhonn.game.box2d

import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.Manifold
import me.jhonn.game.actor.PlayerActor

class Box2DContactListener : ContactListener {
    override fun beginContact(contact: Contact?) {
        val fa = contact?.fixtureA
        val fb = contact?.fixtureB
        if (fa == null || fb == null) return

        if (fa.userData == null || fb.userData == null) return

        if (fa.userData is PlayerActor) {
            val player = fa.userData as PlayerActor
            player.onTheFloor = true
        }
        if (fb.userData is PlayerActor) {
            val player = fb.userData as PlayerActor
            player.onTheFloor = true
        }
    }

    override fun endContact(contact: Contact?) {
        val fa = contact?.fixtureA
        val fb = contact?.fixtureB
        if (fa == null || fb == null) return

        if (fa.userData == null || fb.userData == null) return

        if (fa.userData is PlayerActor) {
            val player = fa.userData as PlayerActor
            player.onTheFloor = false
        }
        if (fb.userData is PlayerActor) {
            val player = fb.userData as PlayerActor
            player.onTheFloor = false
        }
    }

    override fun preSolve(contact: Contact?, oldManifold: Manifold?) {

    }

    override fun postSolve(contact: Contact?, impulse: ContactImpulse?) {

    }

}
