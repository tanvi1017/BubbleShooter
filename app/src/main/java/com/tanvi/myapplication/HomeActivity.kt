package com.tanvi.myapplication

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import java.lang.String
import java.util.*
import java.util.concurrent.TimeUnit


class HomeActivity : AppCompatActivity() {

    lateinit var letsPlayButton: ImageView
    private lateinit var ivRestart: ImageView
    lateinit var tvTimer: TextView
    lateinit var ivScore: TextView
    lateinit var constraintLayout: ConstraintLayout
    lateinit var balloonLayout: ConstraintLayout
    var displayMetrics: DisplayMetrics? = null
    val defaultAnimationDuration = 3000L
    var screenHeight = 1
    private var countdown_timer: CountDownTimer? = null
    private var counter = 0
    private var currentSpeed = 0.7f
    private var currentSpeed2 = 4.0f
    var handler: Handler? = null
    var balloonArray = arrayOf(R.drawable.red_balloon, R.drawable.yellow_balloon, R.drawable.purple_balloon,R.drawable.blue_ballon)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initViews()
        setListeners()
        displayMetrics = DisplayMetrics()
        handler = Handler(Looper.getMainLooper())
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenHeight = displayMetrics?.heightPixels ?: 0
        Log.v("min",getPixel(72f).toString())
        Log.v("min",getPixel(132f).toString())
    }

    private fun initViews() {
        ivScore = findViewById(R.id.ivScore)
        balloonLayout = findViewById(R.id.linearLayout)
        letsPlayButton = findViewById(R.id.btnStart)
        ivRestart = findViewById(R.id.ivRestart)
        tvTimer = findViewById(R.id.tvTimer)
        constraintLayout = findViewById(R.id.constraintLayout)
    }

    private fun createViews() {
        createBalloonWithParams(balloonStartParams())
        createBalloonWithParams(balloonSecondParams())
        createBalloonWithParams(balloonCenterParams())
        createBalloonWithParams(balloonFourthParams())
        createBalloonWithParams(balloonEndParams())
        handler?.postDelayed({
            createViews()
        }, 1000)
    }

    private fun setListeners() {

        letsPlayButton.setOnClickListener {
            startTimeCounter()
            handler?.removeCallbacksAndMessages(null)
            createViews()
        }
        ivRestart.setOnClickListener {
            counter = 0
            handler?.removeCallbacksAndMessages(null)
            //createViews()
            balloonLayout.removeAllViews()
            ivScore.text = "$counter"
            tvTimer.text = "00:00:00"
            letsPlayButton.isEnabled = true
            letsPlayButton.visibility = View.VISIBLE
            countdown_timer?.cancel()
        }
    }


    private fun animateView(cView: View) {
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
            }

            override fun onFinish() {
                handler?.removeCallbacksAndMessages(null)
                letsPlayButton.visibility = View.VISIBLE
                balloonLayout.removeAllViews()
                Toast.makeText(this@HomeActivity, "game is Over", Toast.LENGTH_LONG).show()
            }
        }
        countdown_timer?.start()

    }

    private fun createBalloonWithParams(params: ConstraintLayout.LayoutParams) {
        val mView = ImageView(this)

        mView.layoutParams = params
        val randomStr = balloonArray[Random().nextInt(balloonArray.size)]

        mView.setImageResource(randomStr)
        params.bottomMargin = getPixel(generateMargin())

        balloonLayout.addView(mView)
        mView.setOnClickListener {
            mView.visibility = View.INVISIBLE

            when (params.height) {
                in 144..184 -> {
                    counter+=150
                }
                in 185..225 -> {
                    counter+=125
                }
                else -> {
                    counter+=100
                }
            }
            ivScore.text = "$counter"


        }
        animateView(mView)

    }

    private fun balloonCenterParams():ConstraintLayout.LayoutParams{
        val balloonSize = generateBalloonSize()
        val params = ConstraintLayout.LayoutParams(
            getPixel(balloonSize),
            getPixel(balloonSize)
        )
        params.startToStart = balloonLayout.id
        params.endToEnd = balloonLayout.id
        params.bottomToBottom = balloonLayout.id
        return params
    }
    private fun balloonStartParams():ConstraintLayout.LayoutParams{
        val balloonSize = generateBalloonSize()

        val params = ConstraintLayout.LayoutParams(
            getPixel(balloonSize),
            getPixel(balloonSize)
        )
        params.startToStart = balloonLayout.id
        params.bottomToBottom = balloonLayout.id
        return params
    }
    private fun balloonEndParams():ConstraintLayout.LayoutParams{
        val balloonSize = generateBalloonSize()

        val params = ConstraintLayout.LayoutParams(
            getPixel(balloonSize),
            getPixel(balloonSize)
        )
        params.endToEnd = balloonLayout.id
        params.bottomToBottom = balloonLayout.id
        return params
    }
    private fun balloonSecondParams():ConstraintLayout.LayoutParams{
        val balloonSize = generateBalloonSize()

        val params = ConstraintLayout.LayoutParams(
            getPixel(balloonSize),
            getPixel(balloonSize)
        )
        params.startToStart = balloonLayout.id
        params.endToEnd = balloonLayout.id
        params.bottomToBottom = balloonLayout.id
        params.horizontalBias = 0.25f
        return params
    }
    private fun balloonFourthParams():ConstraintLayout.LayoutParams{
        val balloonSize = generateBalloonSize()

        val params = ConstraintLayout.LayoutParams(
            getPixel(balloonSize),
            getPixel(balloonSize)
        )
        params.startToStart = balloonLayout.id
        params.endToEnd = balloonLayout.id
        params.bottomToBottom = balloonLayout.id
        params.horizontalBias = 0.75f
        return params
    }

    private fun getPixel(f: Float): Int {
        val displayMetrics: DisplayMetrics = resources.displayMetrics
        return (f * displayMetrics.density + 0.5f).toInt()
    }
    private fun generateMargin(): Float {
        return -(Random().nextInt(550) + 80).toFloat()
    }
    private fun generateBalloonSize(): Float {
        return (Random().nextInt(50) + 72).toFloat()
    }

}



























