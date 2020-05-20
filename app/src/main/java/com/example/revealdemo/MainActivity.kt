package com.example.revealdemo

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_second.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        jump_one.setOnClickListener{
            startActivity(it)
        }

        jump_two.setOnClickListener{
            startActivity(it,1500)
        }

        jump_three.setOnClickListener{
//            startActivity(it,delayReveal = true)
            val commonPopupWindow: CommonPopupWindow = CommonPopupWindow.Builder(this@MainActivity.baseContext)
                .setView(R.layout.loading)
                .create()
            commonPopupWindow.setBackgroundDrawable(resources.getDrawable(R.drawable.bg_btn_focused))
            commonPopupWindow.showAsDropDown(rootLayout)
        }
    }

    /**
     * @param view Shared element
     * @param duration Duration of reveal effect.
     * @param delayReveal Whether to delay the reveal display.
     */
    private fun startActivity(view: View, duration:Long = 500, delayReveal:Boolean = false) {
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, "transition")
        val revealX = (view.x + view.width / 2).toInt()     // reveal coordinate of x
        val revealY = (view.y + view.height / 2).toInt()    // reveal coordinate of y
        val intent = Intent(this, SecondActivity::class.java)
        intent.putExtra(RevealAnimation.EXTRA_CIRCULAR_REVEAL_X, revealX)
        intent.putExtra(RevealAnimation.EXTRA_CIRCULAR_REVEAL_Y, revealY)
        intent.putExtra(RevealAnimation.EXTRA_CIRCULAR_DURATION, duration)
        intent.putExtra(RevealAnimation.EXTRA_CIRCULAR_DELAY, delayReveal)
        ActivityCompat.startActivity(this, intent, options.toBundle())

        //just start the activity as an shared transition, but set the options bundle to null
//        ActivityCompat.startActivity(this, intent, null)
        //to prevent strange behaviours override the pending transitions
//        overridePendingTransition(0, 0)
    }
}
