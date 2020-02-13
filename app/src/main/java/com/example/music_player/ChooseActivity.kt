package com.example.music_player

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Button
import android.widget.EditText

class ChooseActivity: Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose)

        findViewById<Button>(R.id.backBtn).setOnClickListener {
            finish()
        }

        val nextBtn = findViewById<Button>(R.id.secondActivityBtn)
        nextBtn.setOnClickListener {
            val text = findViewById<EditText>(R.id.edit_text).text.toString()
            if (text.toInt() in 2..10)
            {
                val i = Intent(this, SecondActivity::class.java)
                i.putExtra("text", text)
                startActivity(i)
            }
        }



    }

}