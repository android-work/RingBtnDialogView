package com.jay.android.work.ring_btn_dialog_view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import com.jay.android.work.customview.RingBtnDialogView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ringBtnView.setViewClickListener(object: RingBtnDialogView.ViewClickListener{
            override fun onClick() =
                Toast.makeText(this@MainActivity,"在区域内",Toast.LENGTH_LONG).show()

        })
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(event?.action == MotionEvent.ACTION_DOWN){
            Log.e("tag","x-->"+event.x+"     y--->"+event.y)
            Log.e("tag","      x-->"+event.rawX+"      y--->"+event.rawY)
        }
        return super.onTouchEvent(event)
    }
}
