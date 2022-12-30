package com.tanvi.myapplication

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import com.tanvi.myapplication.Constants.SCORE_SHARED_PREF
import com.tanvi.myapplication.Constants.SHARED_PREF_NAME
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.hypot

class HomeActivity : AppCompatActivity() {
    var ivMaxScoreTv: TextView? = null
    lateinit var letsPlayButton: ImageView
    private lateinit var ivRestart: ImageView
    lateinit var tvTimer: TextView
    lateinit var ivScore: TextView
    lateinit var constraintLayout: ConstraintLayout
    lateinit var balloonLayout: ConstraintLayout
    var displayMetrics: DisplayMetrics? = null
    private val defaultAnimationDuration = 4000L
    var screenHeight = 1
    private var countdown_timer: CountDownTimer? = null
    private var counter: Long = 0L
    private var currentSpeed = 1f
    private var currentSpeed2 = 4.0f
    var handler: Handler? = null
    var hasReachedMaxScore: Boolean = false
   // private var balloonArray = arrayOf(R.drawable.red_balloon, R.drawable.yellow_balloon, R.drawable.purple_balloon,
    //    R.drawable.blue_ballon)
    private var balloonArrayColors = mutableListOf<BallonPair>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home)
        balloonArrayColors.add(BallonPair(R.color.balloon_red,R.drawable.red_balloon))
        balloonArrayColors.add(BallonPair(R.color.balloon_yellow,R.drawable.yellow_balloon))
        balloonArrayColors.add(BallonPair(R.color.balloon_blue,R.drawable.blue_ballon))
        balloonArrayColors.add(BallonPair(R.color.balloon_green,R.drawable.green_balloon))
        initViews()
        setListeners()
        setInitialScore()
        displayMetrics = DisplayMetrics()
        handler = Handler(Looper.getMainLooper())
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenHeight = displayMetrics?.heightPixels ?: 0
    }
    private fun setInitialScore(){
        val sharedPref = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE)
        val ivMaxScore = sharedPref.getLong(SCORE_SHARED_PREF, 0L)
        ivMaxScoreTv?.text = ivMaxScore.toString()
        Log.v("mmm","max $ivMaxScore")
    }
    private fun setMaxScore(currentScore: Long) {
        val sharedPref = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE)
        val ivMaxScore = sharedPref.getLong(SCORE_SHARED_PREF, 0L)
        Log.v("mmm","max $ivMaxScore curr $currentScore")
        if (currentScore > ivMaxScore) {

            ivMaxScoreTv?.text = "$currentScore"
            if(!hasReachedMaxScore){ //done
                if(ivMaxScore>0){
                    Toast.makeText(this@HomeActivity, getString(R.string.scored_max), Toast.LENGTH_LONG)
                        .show()
                }
                hasReachedMaxScore = true
            }
            val editor = sharedPref.edit()
            editor.putLong(SCORE_SHARED_PREF, currentScore)
            editor.apply()
        }
    }
    private fun initViews() {
        ivMaxScoreTv = findViewById(R.id.ivMaxScore)
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
        }, 1500)
    }
    private fun setListeners() {
        letsPlayButton.setOnClickListener {
            counter = 0
            ivScore.text ="$counter"
            startTimeCounter()
            handler?.removeCallbacksAndMessages(null)
            createViews()
        }
        ivRestart.setOnClickListener {
            counter = 0
            handler?.removeCallbacksAndMessages(null)
            balloonLayout.removeAllViews()
            ivScore.text = "$counter"
            tvTimer.text = "00:00:00"
            letsPlayButton.isEnabled = true
            letsPlayButton.visibility = View.VISIBLE
            countdown_timer?.cancel()
            hasReachedMaxScore = false
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
                Toast.makeText(this@HomeActivity, "Game is over", Toast.LENGTH_LONG).show()
                setMaxScore(counter)
                hasReachedMaxScore = false
            }
        }
        countdown_timer?.start()
    }
    private fun createBalloonWithParams(params: ConstraintLayout.LayoutParams) {
        val mView = ImageView(this)
        mView.layoutParams = params
        val balloonIcon = balloonArrayColors[Random().nextInt(balloonArrayColors.size)]
       // mView.setTint(balloonIcon.color)
        mView.setImageResource(balloonIcon.icon)
        mView.tag = balloonIcon
        params.bottomMargin = getPixel(generateMargin())
        balloonLayout.addView(mView)
        mView.setOnClickListener {
            mView.isClickable=true
            val mediaPlayer: MediaPlayer = MediaPlayer.create(this, R.raw.music8)
            mediaPlayer.start()
            val img = mView.tag as? BallonPair
            //img?.icon?.let { it1 -> mView.setImageResource(it1) }
            //OR KUCH ni...
            mView.setImageResource(R.drawable.fireworks)
            mView.setTint(img?.color)
            mView.isClickable=false
            // Glide.with(this).load(R.drawable.balloon_animation).into(mView)
            toggle(it)
            counter += when (params.height) {
                in 144..184 -> {
                    150
                }
                in 185..225 -> {
                    125
                }
                else -> {
                    100
                }
            }
            ivScore.text = "$counter"
            setMaxScore(counter)
        }

        animateView(mView)
    }
    private fun balloonCenterParams():ConstraintLayout.LayoutParams{
        val balloonSize = generateBalloonSize()
        val params = ConstraintLayout.LayoutParams(
            getPixel(balloonSize),
            getPixel(balloonSize))
        params.startToStart = balloonLayout.id
        params.endToEnd = balloonLayout.id
        params.bottomToBottom = balloonLayout.id
        return params
    }
    private fun balloonStartParams():ConstraintLayout.LayoutParams {
        val balloonSize = generateBalloonSize()
        val params = ConstraintLayout.LayoutParams(
            getPixel(balloonSize),
            getPixel(balloonSize))
        params.startToStart = balloonLayout.id
        params.bottomToBottom = balloonLayout.id
        params.editorAbsoluteY
        params.horizontalBias = 0.25f
        return params
    }
    private fun balloonEndParams():ConstraintLayout.LayoutParams {
        val balloonSize = generateBalloonSize()
        val params = ConstraintLayout.LayoutParams(
            getPixel(balloonSize),
            getPixel(balloonSize))
        params.endToEnd = balloonLayout.id
        params.bottomToBottom = balloonLayout.id
        params.editorAbsoluteY
        params.horizontalBias = 0.25f
        return params
    }
    private fun balloonSecondParams():ConstraintLayout.LayoutParams{
        val balloonSize = generateBalloonSize()
        val params = ConstraintLayout.LayoutParams(
            getPixel(balloonSize),
            getPixel(balloonSize))
        params.startToStart = balloonLayout.id
        params.endToEnd = balloonLayout.id
        params.bottomToBottom = balloonLayout.id
        params.editorAbsoluteY
        params.horizontalBias = 0.25f
        return params
    }
    private fun balloonFourthParams():ConstraintLayout.LayoutParams {
        val balloonSize = generateBalloonSize()
        val params = ConstraintLayout.LayoutParams(
            getPixel(balloonSize),
            getPixel(balloonSize)
        )
        params.startToStart = balloonLayout.id
        params.endToEnd = balloonLayout.id
        params.bottomToBottom = balloonLayout.id
        params.editorAbsoluteY
        params.horizontalBias = 1.05f
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
    private fun toggle(anyView: View) {
        val cx = anyView.width / 2
        val cy = anyView.height / 2
        val initialRadius = hypot(cx.toDouble(), cy.toDouble()).toFloat()
        val anim =
            ViewAnimationUtils.createCircularReveal(anyView, cx, cy, initialRadius, 0.1f)
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                anyView.visibility = View.INVISIBLE
            }
        })
        anim.start()
    }

    companion object {
    }
}
fun ImageView.setTint(@ColorRes color: Int?) {
    if (color == null) {
        ImageViewCompat.setImageTintList(this, null)
    } else {
        ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(ContextCompat.getColor(context, color)))
    }}
data class BallonPair(val color:Int,val icon:Int)































