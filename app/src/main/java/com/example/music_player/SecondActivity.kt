package com.example.music_player

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.*
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.nio.file.FileSystem

@SuppressLint("Registered")
class SecondActivity: Activity() {

    private lateinit var mp: MediaPlayer
    private lateinit var mp1: MediaPlayer
    private var totalTime: Int = 0
    private var totalTime1: Int = 0
    private var cross = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        cross = intent.getStringExtra("text").toInt()

        findViewById<Button>(R.id.backBtn).setOnClickListener {
            mp.stop()
            mp1.stop()
            finish()
        }

        mp = MediaPlayer.create(this, R.raw.music)
        mp.isLooping = false
        mp.setVolume(0.5f, 0.5f)
        totalTime = mp.duration

        mp1 = MediaPlayer.create(this, R.raw.music1)
        mp1.isLooping = false
        mp1.setVolume(0.5f, 0.5f)
        totalTime1 = mp1.duration

        var btnAdd1 = findViewById<Button>(R.id.addSong1)
        btnAdd1.setOnClickListener(){
            intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setType("*/*")
            startActivityForResult(intent, 111)

        }

        var btnAdd2 = findViewById<Button>(R.id.addSong2)
        btnAdd2.setOnClickListener(){
            intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setType("*/*")
            startActivityForResult(intent, 112)
        }


        positionBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser) {
                        mp.seekTo(progress)
                    }
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {
                }

                override fun onStopTrackingTouch(p0: SeekBar?) {
                }
            }
        )

        positionBar1.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser) {
                        mp1.seekTo(progress)
                    }
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {
                }

                override fun onStopTrackingTouch(p0: SeekBar?) {
                }
            }
        )

        // Thread
        Thread(Runnable {
            while (mp != null) {
                try {
                    var msg = Message()
                    msg.what = mp.currentPosition
                    handler.sendMessage(msg)
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                }
            }
        }).start()

        Thread(Runnable {
            while (mp1 != null) {
                try {
                    var msg = Message()
                    msg.what = mp1.currentPosition
                    handler1.sendMessage(msg)
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                }
            }
        }).start()
    }

    @SuppressLint("HandlerLeak")
    var handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            var currentPosition = msg.what

            // Update positionBar
            positionBar.progress = currentPosition

            // Update Labels
            var elapsedTime = createTimeLabel(currentPosition)
            elapsedTimeLabel.text = elapsedTime

            var remainingTime = createTimeLabel(totalTime - currentPosition)
            remainingTimeLabel.text = "-$remainingTime"

            if (totalTime - currentPosition<=1000*cross)
                mp1.start()
        }
    }

    @SuppressLint("HandlerLeak")
    var handler1 = object : Handler() {
        override fun handleMessage(msg: Message) {
            var currentPosition = msg.what

            // Update positionBar
            positionBar1.progress = currentPosition

            // Update Labels
            var elapsedTime = createTimeLabel1(currentPosition)
            elapsedTimeLabel1.text = elapsedTime

            var remainingTime = createTimeLabel1(totalTime1 - currentPosition)
            remainingTimeLabel1.text = "-$remainingTime"

            if (totalTime1 - currentPosition<=1000*cross)
                mp.start()
        }
    }

    fun createTimeLabel(time: Int): String {
        var timeLabel = ""
        var min = time / 1000 / 60
        var sec = time / 1000 % 60

        timeLabel = "$min:"
        if (sec < 10) timeLabel += "0"
        timeLabel += sec

        return timeLabel
    }

    fun createTimeLabel1(time: Int): String {
        var timeLabel = ""
        var min = time / 1000 / 60
        var sec = time / 1000 % 60

        timeLabel = "$min:"
        if (sec < 10) timeLabel += "0"
        timeLabel += sec

        return timeLabel
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try{
            when (requestCode){
                111 ->{
                    if (resultCode== Activity.RESULT_OK){
                        mp.reset()
                        mp = MediaPlayer.create(this, data!!.data)
                        mp.isLooping = false
                        mp.setVolume(0.5f, 0.5f)
                        totalTime = mp.duration
                        positionBar.max = totalTime
                        mp.start()

                    }
                }
                112 ->{
                    if (resultCode== Activity.RESULT_OK){
                        mp1.reset()
                        mp1 = MediaPlayer.create(this, data!!.data)
                        mp1.isLooping = false
                        mp1.setVolume(0.5f, 0.5f)
                        totalTime1 = mp1.duration
                        positionBar1.max = totalTime1
                    }
                }
            }
        }catch (e: InterruptedException) {
        }

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            mp.stop()
            mp1.stop()
            finish()
            return true
        }
        return false
    }

}