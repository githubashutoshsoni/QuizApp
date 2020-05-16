package com.example.quizapp

import android.view.View
import androidx.annotation.IdRes
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce


fun View.spring(
    property: DynamicAnimation.ViewProperty,
    stiffness: Float = 200f,
    damping: Float = 0.3f,
    startVelocity: Float? = null
): SpringAnimation {

    val key = getKey(property)
    var springAnim = getTag(key) as? SpringAnimation?
    if (springAnim == null) {
        springAnim = SpringAnimation(this, property).apply {
            spring = SpringForce().apply {
                this.dampingRatio = damping
                this.stiffness = stiffness
                startVelocity?.let { setStartVelocity(it) }
            }
        }
        setTag(key, springAnim)
    }
    return springAnim
}


@IdRes
private fun getKey(property: DynamicAnimation.ViewProperty): Int {
    return when (property) {
        SpringAnimation.TRANSLATION_X -> R.id.translation_x
        SpringAnimation.TRANSLATION_Y -> R.id.translation_y
        SpringAnimation.TRANSLATION_Z -> R.id.translation_z
        SpringAnimation.SCALE_X -> R.id.scale_x
        SpringAnimation.SCALE_Y -> R.id.scale_y
        SpringAnimation.ROTATION -> R.id.rotation
        SpringAnimation.ROTATION_X -> R.id.rotation_x
        SpringAnimation.ROTATION_Y -> R.id.rotation_y
        SpringAnimation.X -> R.id.x
        SpringAnimation.Y -> R.id.y
        SpringAnimation.Z -> R.id.z
        SpringAnimation.ALPHA -> R.id.alpha
        SpringAnimation.SCROLL_X -> R.id.scroll_x
        SpringAnimation.SCROLL_Y -> R.id.scroll_y
        else -> throw IllegalAccessException("Unknown ViewProperty: $property")
    }
}
