package de.neofonie.crashreporting.commons.transitions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.TimeInterpolator
import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.transition.TransitionValues
import android.transition.Visibility
import android.util.ArrayMap
import android.util.AttributeSet
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import java.util.ArrayList

/**
 * Created by jakub on 05/03/2015.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class RevealTransition : Visibility {

  constructor() : super()

  constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

  override fun onAppear(sceneRoot: ViewGroup,
                        view: View,
                        startValues: TransitionValues,
                        endValues: TransitionValues): Animator {
    val radius = calculateMaxRadius(view)
    view.visibility = View.INVISIBLE
    return createAnimator(view, 0f, radius).apply {
      addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator) = run { view.visibility = View.VISIBLE }
      })
    }
  }

  override fun onDisappear(sceneRoot: ViewGroup,
                           view: View,
                           startValues: TransitionValues,
                           endValues: TransitionValues): Animator {
    val radius = calculateMaxRadius(view)
    return createAnimator(view, radius, 0f)
  }

  private fun createAnimator(view: View, startRadius: Float, endRadius: Float): Animator {
    val centerX = view.width / 5
    val centerY = view.height * 9 / 10

    val reveal = ViewAnimationUtils.createCircularReveal(view, centerX, centerY, startRadius, endRadius)
    return NoPauseAnimatorWrapper(reveal)
  }

  private class NoPauseAnimatorWrapper(private val mAnimator: Animator) : Animator() {
    private val mListeners = ArrayMap<Animator.AnimatorListener, Animator.AnimatorListener>()

    override fun addListener(listener: Animator.AnimatorListener) {
      val wrapper = AnimatorListenerWrapper(this, listener)
      if (!mListeners.containsKey(listener)) {
        mListeners.put(listener, wrapper)
        mAnimator.addListener(wrapper)
      }
    }

    override fun cancel() = mAnimator.cancel()
    override fun end() = mAnimator.end()
    override fun getDuration() = mAnimator.duration
    override fun getInterpolator() = mAnimator.interpolator
    override fun getListeners() = ArrayList(mListeners.keys)
    override fun getStartDelay() = mAnimator.startDelay
    override fun isPaused() = mAnimator.isPaused
    override fun isRunning() = mAnimator.isRunning
    override fun isStarted() = mAnimator.isStarted

    override fun removeAllListeners() {
      super.removeAllListeners()
      mListeners.clear()
      mAnimator.removeAllListeners()
    }

    override fun removeListener(listener: Animator.AnimatorListener) {
      val wrapper = mListeners[listener]
      if (wrapper != null) {
        mListeners.remove(listener)
        mAnimator.removeListener(wrapper)
      }
    }

    /* We don't want to override pause or resume methods
     * because we don't want them to affect mAnimator.
     * public void pause();
     * public void resume();
     * public void addPauseListener(AnimatorPauseListener listener);
     * public void removePauseListener(AnimatorPauseListener listener);
     */

    override fun setDuration(durationMS: Long) = apply { mAnimator.duration = durationMS }
    override fun setInterpolator(timeInterpolator: TimeInterpolator) = run { mAnimator.interpolator = timeInterpolator }
    override fun setStartDelay(delayMS: Long) = run { mAnimator.startDelay = delayMS }
    override fun setTarget(target: Any) = mAnimator.setTarget(target)
    override fun setupEndValues() = mAnimator.setupEndValues()
    override fun setupStartValues() = mAnimator.setupStartValues()
    override fun start() = mAnimator.start()
  }

  private class AnimatorListenerWrapper(private val mAnimator: Animator,
                                        private val mListener: Animator.AnimatorListener) : Animator.AnimatorListener {
    override fun onAnimationStart(animator: Animator) = mListener.onAnimationStart(mAnimator)
    override fun onAnimationEnd(animator: Animator) = mListener.onAnimationEnd(mAnimator)
    override fun onAnimationCancel(animator: Animator) = mListener.onAnimationCancel(mAnimator)
    override fun onAnimationRepeat(animator: Animator) = mListener.onAnimationRepeat(mAnimator)
  }

  companion object {
    private val TAG = RevealTransition::class.java.simpleName
    internal fun calculateMaxRadius(v: View): Float {
      val widthSquared = (v.width * v.width).toFloat()
      val heightSquared = (v.height * v.height).toFloat()
      val radius = Math.sqrt((widthSquared + heightSquared).toDouble()).toFloat()
      return radius
    }
  }
}