package com.example.revealdemo

import android.os.Bundle
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

    lateinit var revealAnimation:RevealAnimation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        revealAnimation = RevealAnimation(rootLayout, intent, this)
    }

    override fun onBackPressed() {
        revealAnimation.unRevealActivity()
    }

}