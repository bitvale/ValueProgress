package com.bitvale.valueprogressdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val DELAY = 500L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val handler = Handler(Looper.getMainLooper())
        var workRunnable: Runnable? = null

        edit_text_value.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val text = s.toString()
                val value = if (text.isNotEmpty()) text.toFloat() else 0f

                workRunnable?.let { handler.removeCallbacks(it) }
                workRunnable = Runnable {
                    progress_percent.percent = value
                    progress_dollar.percent = value / 2
                }
                handler.postDelayed(workRunnable, DELAY)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        edit_text_max_value.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val value = if (edit_text_value.text.toString().isNotEmpty())
                    edit_text_value.text.toString().toFloat()
                else
                    0f
                val text = s.toString()
                val maxValue = if (text.isNotEmpty()) text.toFloat() else 0f

                workRunnable?.let { handler.removeCallbacks(it) }
                workRunnable = Runnable {
                    progress_percent.progressMaxValue = maxValue
                    progress_dollar.progressMaxValue = maxValue
                    progress_percent.percent = value
                    progress_dollar.percent = value / 2
                }
                handler.postDelayed(workRunnable, DELAY)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }
}
