package com.example.revealdemo

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        if(event?.keyCode == KeyEvent.KEYCODE_DPAD_CENTER){
            presentActivity(progressbar)
        }
        return super.dispatchKeyEvent(event)
    }

    override fun onRestart() {
        super.onRestart()
    }

    private fun presentActivity(view: View) {
        val options =
            ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, "transition")
        val revealX = (view.x + view.width / 2).toInt()
        val revealY = (view.y + view.height / 2).toInt()
        val intent = Intent(this, SecondActivity::class.java)
        intent.putExtra(RevealAnimation.EXTRA_CIRCULAR_REVEAL_X, revealX)
        intent.putExtra(RevealAnimation.EXTRA_CIRCULAR_REVEAL_Y, revealY)
        ActivityCompat.startActivity(this, intent, options.toBundle())

        //just start the activity as an shared transition, but set the options bundle to null
//        ActivityCompat.startActivity(this, intent, null)
        //to prevent strange behaviours override the pending transitions
//        overridePendingTransition(0, 0)
    }
}
