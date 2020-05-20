package com.example.revealdemo

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.ViewTreeObserver
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_second.*


/**
 * @author zeke.wang
 * @date 2020/5/14
 * @maintainer zeke.wang
 * @copyright 2020 www.xgimi.com Inc. All rights reserved.
 * @desc:
 */
class SecondActivity: AppCompatActivity() {

    private lateinit var revealAnimation:RevealAnimation
    private var revealX = 0
    private var revealY = 0
    private var duration = 0L
    private var delayReveal = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        revealX = intent.getIntExtra(RevealAnimation.EXTRA_CIRCULAR_REVEAL_X, 0)
        revealY = intent.getIntExtra(RevealAnimation.EXTRA_CIRCULAR_REVEAL_Y, 0)
        duration = intent.getLongExtra(RevealAnimation.EXTRA_CIRCULAR_DURATION, 500L)
        delayReveal = intent.getBooleanExtra(RevealAnimation.EXTRA_CIRCULAR_DELAY, false)

        revealAnimation = RevealAnimation(rootLayout, revealActivity = this){
            Log.d("SecondActivity","onAnimationStart ")
        }

        val viewTreeObserver: ViewTreeObserver = rootLayout.viewTreeObserver
        if (viewTreeObserver.isAlive) {
            viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    if(delayReveal){
                        val commonPopupWindow: CommonPopupWindow = CommonPopupWindow.Builder(this@SecondActivity.baseContext)
                            .setView(R.layout.loading)
                            .create()
                        commonPopupWindow.showAsDropDown(rootLayout)

                        handler.postDelayed({
                            //revealAnimation.revealActivity(revealX, revealY, duration) //使用Reveal效果则必须调用此方法
                        },3000)
                    }else{
                        revealAnimation.revealActivity(revealX, revealY, duration) //使用Reveal效果则必须调用此方法
                    }
                    rootLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            })
        }
    }

    val handler = Handler()

    override fun onBackPressed() {
        revealAnimation.unRevealActivity(revealX,revealY,duration)
    }

}