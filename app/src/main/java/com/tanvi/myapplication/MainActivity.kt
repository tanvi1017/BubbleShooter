package com.tanvi.myapplication

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import java.lang.String
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    lateinit var cardView1: ImageView
    lateinit var cardView2: ImageView
    lateinit var cardView3: ImageView
    lateinit var cardView4: ImageView
    lateinit var cardView5: ImageView
    lateinit var cardView6: ImageView
    lateinit var cardView7: ImageView
    lateinit var cardView8: ImageView
    lateinit var cardView9: ImageView
    lateinit var cardView10: ImageView
    lateinit var letsPlayButton: ImageView
    private lateinit var ivRestart: ImageView
    lateinit var tvTimer: TextView
    lateinit var ivScore: TextView
    lateinit var constraintLayout: ConstraintLayout
    var displayMetrics: DisplayMetrics? = null
    val defaultAnimationDuration = 3000L
    var screenHeight = 1
    var touchCount = 1
    private var countdown_timer: CountDownTimer? = null
    private var time_in_milliseconds = 60000L
    private var counter = 0
    private var currentSpeed = 0.7f
    private var currentSpeed2 = 4.0f
    var handler1:Handler?=null
    var handler2:Handler?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        setListeners()
        displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenHeight = displayMetrics?.heightPixels ?: 0

        when (touchCount++) {
            3 -> startAnimation(cardView1)
            4 -> startAnimation(cardView2)
            1 -> startAnimation(cardView3)
            2 -> startAnimation(cardView4)
            5 -> startAnimation(cardView5)
            else -> touchCount = 0
        }
    }

    private fun initViews() {
        ivScore = findViewById(R.id.ivScore)
        cardView1 = findViewById(R.id.cardView1)
        cardView2 = findViewById(R.id.cardView2)
        cardView3 = findViewById(R.id.cardView3)
        cardView4 = findViewById(R.id.cardView4)
        cardView5 = findViewById(R.id.cardView5)

        cardView6 = findViewById(R.id.cardView6)
        cardView7 = findViewById(R.id.cardView7)
        cardView8 = findViewById(R.id.cardView8)
        cardView9 = findViewById(R.id.cardView9)
        cardView10 = findViewById(R.id.cardView10)
        letsPlayButton = findViewById(R.id.btnStart)
        ivRestart = findViewById(R.id.ivRestart)
        tvTimer = findViewById(R.id.tvTimer)
        constraintLayout = findViewById(R.id.constraintLayout)
    }

    private fun setListeners() {
        cardView1.setOnClickListener {
            cardView1.visibility = View.INVISIBLE
            counter += 100;
            ivScore.text = "$counter"
            Handler(Looper.getMainLooper()).postDelayed({
                cardView1.visibility = View.VISIBLE
                runAnimation()
            }, 400)
        }
        cardView2.setOnClickListener {
            cardView2.visibility = View.INVISIBLE
            counter += 150;
            //  currentSpeed += 0.001f
            currentSpeed += 0.1f
            // currentSpeed2-= 0.05f
            ivScore.text = "$counter"
            Handler(Looper.getMainLooper()).postDelayed({
                cardView2.visibility = View.VISIBLE
                runAnimation()
            }, 1000)
        }
        cardView5.setOnClickListener {
            cardView5.visibility = View.INVISIBLE
            counter += 100
            currentSpeed += 0.1f
            // currentSpeed2-= 0.05f
            ivScore.text = "$counter"
            Handler(Looper.getMainLooper()).postDelayed({
                cardView5.visibility = View.VISIBLE
                runAnimation()
            }, 1000)
        }
        cardView3.setOnClickListener {
            cardView3.visibility = View.INVISIBLE
            counter += 150;
            currentSpeed += 0.1f
            //  currentSpeed2-= 0.05f
            ivScore.text = "$counter"
            Handler(Looper.getMainLooper()).postDelayed({
                cardView3.visibility = View.VISIBLE
                runAnimation()
            }, 1000)
        }
        cardView4.setOnClickListener {
            cardView4.visibility = View.INVISIBLE
            counter += 100;
            currentSpeed += 0.05f
            ivScore.text = "$counter"
//            Handler(Looper.getMainLooper()).postDelayed({
//                cardView4.visibility = View.VISIBLE
//                runAnimation()
//            }, 1000)
        }

        cardView6.setOnClickListener {
            cardView6.visibility = View.INVISIBLE
            counter += 100;
            ivScore.text = "$counter"
            Handler(Looper.getMainLooper()).postDelayed({
                cardView1.visibility = View.VISIBLE
                runAnimation()
            }, 400)
        }
        cardView7.setOnClickListener {
            cardView7.visibility = View.INVISIBLE
            counter += 150;
            //  currentSpeed += 0.001f
            currentSpeed += 0.1f
            // currentSpeed2-= 0.05f
            ivScore.text = "$counter"
            Handler(Looper.getMainLooper()).postDelayed({
                cardView2.visibility = View.VISIBLE
                runAnimation()
            }, 1000)
        }
        cardView8.setOnClickListener {
            cardView8.visibility = View.INVISIBLE
            counter += 100
            currentSpeed += 0.1f
            // currentSpeed2-= 0.05f
            ivScore.text = "$counter"
            Handler(Looper.getMainLooper()).postDelayed({
                cardView5.visibility = View.VISIBLE
                runAnimation()
            }, 1000)
        }
        cardView9.setOnClickListener {
            cardView9.visibility = View.INVISIBLE
            counter += 150;
            currentSpeed += 0.1f
            //  currentSpeed2-= 0.05f
            ivScore.text = "$counter"
            Handler(Looper.getMainLooper()).postDelayed({
                cardView3.visibility = View.VISIBLE
                runAnimation()
            }, 1000)
        }
        cardView10.setOnClickListener {
            cardView10.visibility = View.INVISIBLE
            counter += 100;
            currentSpeed += 0.05f
            ivScore.text = "$counter"
            Handler(Looper.getMainLooper()).postDelayed({
                cardView4.visibility = View.VISIBLE
                runAnimation()
            }, 1000)
        }
        letsPlayButton.setOnClickListener {
            letsPlayButton.isEnabled = false
            startTimeCounter()
            fireBubbles()
            handler1?.removeCallbacksAndMessages(null)
            handler2?.removeCallbacksAndMessages(null)
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    fireBubbles1()
                },
                1000
            )
            startAnimation1(cardView2)
            startAnimation1(cardView3)
            startAnimation1(cardView4)
            startAnimation1(cardView5)
            startAnimation1(cardView1)
            showAllBubbles()
        }
        ivRestart.setOnClickListener {
            counter = 0
            handler1?.removeCallbacksAndMessages(null)
            handler2?.removeCallbacksAndMessages(null)
            fireBubbles()
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    fireBubbles1()
                },
                1000
            )
            ivScore.text = "$counter"
            tvTimer.text = "00:00:00"
            letsPlayButton.isEnabled = true
            letsPlayButton.visibility = View.GONE
            countdown_timer?.cancel()

            hideAllBubbles()


        }
    }

    fun startAnimation(cView: View) {
        val valueAnimator = ValueAnimator.ofFloat(0f, -((100 + 120).toFloat()))
        valueAnimator.addUpdateListener {
            val value = it.animatedValue as Float
            cView.translationY = value
        }
        valueAnimator.interpolator = AccelerateInterpolator(currentSpeed)
        valueAnimator.duration = defaultAnimationDuration
        valueAnimator.start()
    }

    fun startAnimation1(cView: View) {
        val valueAnimator = ValueAnimator.ofFloat(0f, -(screenHeight + 1500).toFloat())

        valueAnimator.addUpdateListener {
            val value = it.animatedValue as Float
            cView.translationY = value
        }
        valueAnimator.interpolator = AccelerateInterpolator(currentSpeed)
        if (currentSpeed > 4.0f) {
            valueAnimator.interpolator = DecelerateInterpolator(currentSpeed2)
        }
        valueAnimator.duration = defaultAnimationDuration
        valueAnimator.start()
    }

    @SuppressLint("DefaultLocale")
    fun startTimeCounter() {
        countdown_timer = null
        countdown_timer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val hms = String.format(
                    "%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished)
                    ),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                    )
                )
                tvTimer.text = hms
                letsPlayButton.visibility = View.INVISIBLE
               // runAnimation()
            }

            override fun onFinish() {
                hideAllBubbles()
                handler1?.removeCallbacksAndMessages(null)
                handler2?.removeCallbacksAndMessages(null)
                letsPlayButton.visibility = View.VISIBLE
                Toast.makeText(this@MainActivity, "game is Over", Toast.LENGTH_LONG).show()
            }
        }
        countdown_timer?.start()

    }

    fun runAnimation() {
        when (touchCount) {
            3 -> startAnimation1(cardView1)
            4 -> startAnimation1(cardView2)
            1 -> startAnimation1(cardView3)
            2 -> startAnimation1(cardView4)
            5 -> startAnimation1(cardView5)
            else -> touchCount = 0
        }
        touchCount++;
    }

    private fun fireBubbles() {
        showAllBubbles()
        startAnimation1(cardView1)
        startAnimation1(cardView2)
        startAnimation1(cardView3)
        startAnimation1(cardView4)
        startAnimation1(cardView5)
        handler1 = Handler(Looper.getMainLooper())
        handler1?.postDelayed({
            fireBubbles()
        }, defaultAnimationDuration)
    }

    private fun fireBubbles1() {
        showAllBubbles()
        startAnimation1(cardView6)
        startAnimation1(cardView7)
        startAnimation1(cardView8)
        startAnimation1(cardView9)
        startAnimation1(cardView10)
        handler2 = Handler(Looper.getMainLooper())
        handler2?.postDelayed({
            fireBubbles1()
        }, defaultAnimationDuration)
    }

    fun hideAllBubbles() {
        cardView1.visibility = View.GONE
        cardView2.visibility = View.GONE
        cardView3.visibility = View.GONE
        cardView4.visibility = View.GONE
        cardView5.visibility = View.GONE

        cardView6.visibility = View.GONE
        cardView7.visibility = View.GONE
        cardView8.visibility = View.GONE
        cardView9.visibility = View.GONE
        cardView10.visibility = View.GONE
    }

    private fun showAllBubbles() {
        cardView1.visibility = View.VISIBLE
        cardView2.visibility = View.VISIBLE
        cardView3.visibility = View.VISIBLE
        cardView4.visibility = View.VISIBLE
        cardView5.visibility = View.VISIBLE

        cardView6.visibility = View.VISIBLE
        cardView7.visibility = View.VISIBLE
        cardView8.visibility = View.VISIBLE
        cardView9.visibility = View.VISIBLE
        cardView10.visibility = View.VISIBLE
    }
}




























