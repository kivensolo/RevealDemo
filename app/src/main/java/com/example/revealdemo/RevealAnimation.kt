package com.example.revealdemo

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.TimeInterpolator
import android.app.Activity
import android.os.Build
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateInterpolator
import kotlin.math.max


/**
 * @author zeke.wang
 * @date 2020/5/14
 * @maintainer zeke.wang
 * @copyright 2020 www.xgimi.com Inc. All rights reserved.
 * @desc:
 *
 * @param rootView reveal页面的rootView
 * @param intent 传递给reveal页面的intent
 * @param revealActivity 需要展示reveal动画的activity
 */
class RevealAnimation(val rootView: View,
                      val hideRoot:Boolean = true,
                      private val revealActivity: Activity,
                      val revealAnimExec:() -> Unit) {


    companion object {
        const val TAG = "RevealAnimation"
        const val EXTRA_CIRCULAR_REVEAL_X = "EXTRA_CIRCULAR_REVEAL_X"
        const val EXTRA_CIRCULAR_REVEAL_Y = "EXTRA_CIRCULAR_REVEAL_Y"
        const val EXTRA_CIRCULAR_DURATION = "EXTRA_CIRCULAR_DURATION"
        const val EXTRA_CIRCULAR_DELAY = "EXTRA_CIRCULAR_DELAY"
    }

    init {
        //when you're android version is at least Lollipop it starts the reveal activity
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && hideRoot) {
            rootView.visibility = View.INVISIBLE

        } else { //if you are below android 5 it just shows the activity
            rootView.visibility = View.VISIBLE
        }
    }

    fun revealActivity(x: Int = (rootView.width / 2),
                       y: Int = (rootView.height / 2),
                       duration: Long = 500,
                       interpolator: TimeInterpolator = AccelerateInterpolator()) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val finalRadius = (max(rootView.width, rootView.height) * 1.1).toFloat()
            // create the animator for this view (the start radius is zero)
            val circularReveal = ViewAnimationUtils.createCircularReveal(rootView, x, y, 0f, finalRadius)
            circularReveal.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationStart(animation: Animator?) {
                        super.onAnimationStart(animation)
                        this@RevealAnimation.revealAnimExec()
                    }

                    override fun onAnimationEnd(animation: Animator) {
                    }
                })
            circularReveal.duration = duration
            circularReveal.interpolator = interpolator
            // make the view visible and start the animation
            rootView.visibility = View.VISIBLE
            circularReveal.start()
        } else {
            // make the view visible directly
            rootView.visibility = View.VISIBLE
        }
    }

    fun unRevealActivity(revealX: Int = (rootView.width / 2),
                         revealY: Int = (rootView.height / 2),
                         duration: Long = 500,
                         autoClose:Boolean = true){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            revealActivity.finish()
        } else {
            val finalRadius = (max(rootView.width, rootView.height) * 1.1).toFloat()
            val circularReveal = ViewAnimationUtils.createCircularReveal(rootView, revealX,
                    revealY, finalRadius, 0f)
            circularReveal.duration = duration

            circularReveal.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator?) {
                    super.onAnimationStart(animation)
                }

                override fun onAnimationEnd(animation: Animator) {
                    rootView.visibility = View.INVISIBLE
                    if(autoClose){
                        revealActivity.finish()
                        revealActivity.overridePendingTransition(0, 0)
                    }
                }
            })
            circularReveal.start()
        }
    }
}