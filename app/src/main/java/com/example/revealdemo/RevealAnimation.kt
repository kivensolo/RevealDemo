package com.example.revealdemo

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewTreeObserver
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.animation.AccelerateInterpolator
import kotlin.math.max


/**
 * @author zeke.wang
 * @date 2020/5/14
 * @maintainer zeke.wang
 * @copyright 2020 www.xgimi.com Inc. All rights reserved.
 * @desc:
 */
class RevealAnimation(val rootView: View,
                      intent: Intent,
                      val mActivity: Activity) {
    private var revealX = 0
    private var revealY = 0

    companion object {
        const val EXTRA_CIRCULAR_REVEAL_X = "EXTRA_CIRCULAR_REVEAL_X"
        const val EXTRA_CIRCULAR_REVEAL_Y = "EXTRA_CIRCULAR_REVEAL_Y"
    }

    init {
        //when you're android version is at least Lollipop it starts the reveal activity
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
            intent.hasExtra(EXTRA_CIRCULAR_REVEAL_X) &&
            intent.hasExtra(EXTRA_CIRCULAR_REVEAL_Y) ) {

            rootView.visibility = View.INVISIBLE
            revealX = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_X, 0)
            revealY = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_Y, 0)
            val viewTreeObserver: ViewTreeObserver = rootView.viewTreeObserver
            if (viewTreeObserver.isAlive) {
                viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        revealActivity(revealX, revealY)
                        rootView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                })
            }
        } else { //if you are below android 5 it just shows the activity
            rootView.visibility = View.VISIBLE
        }
    }

    fun revealActivity(x: Int, y: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val finalRadius =
                (max(rootView.width, rootView.height) * 1.1).toFloat()
            // create the animator for this view (the start radius is zero)
            val circularReveal =
                ViewAnimationUtils.createCircularReveal(rootView, x, y, 0f, finalRadius)
            circularReveal.duration = 500
            circularReveal.interpolator = AccelerateInterpolator()
            // make the view visible and start the animation
            rootView.visibility = View.VISIBLE
            circularReveal.start()
        } else {
            mActivity.finish()
        }
    }

    fun unRevealActivity() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            mActivity.finish()
        } else {
            val finalRadius =
                (max(rootView.width, rootView.height) * 1.1).toFloat()
            val circularReveal =
                ViewAnimationUtils.createCircularReveal(
                    rootView, revealX, revealY, finalRadius, 0f
                )
            circularReveal.duration = 500
            circularReveal.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    rootView.visibility = View.INVISIBLE
                    mActivity.finish()
                    mActivity.overridePendingTransition(0, 0)
                }
            })
            circularReveal.start()
        }
    }

}