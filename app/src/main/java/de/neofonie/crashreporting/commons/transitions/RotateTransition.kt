package de.neofonie.crashreporting.commons.transitions

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.transition.TransitionValues
import android.transition.Visibility
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

/**
 * Created on 6/17/16.
 */
class RotateTransition : Visibility {
  constructor() : super()

  constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

  override fun onAppear(sceneRoot: ViewGroup,
                        view: View,
                        startValues: TransitionValues,
                        endValues: TransitionValues): Animator {
    return AnimatorSet().apply {
      playTogether(
          ObjectAnimator.ofFloat(view, "rotation", -90f, 0f)
      )
    }
  }

  override fun onDisappear(sceneRoot: ViewGroup,
                           view: View,
                           startValues: TransitionValues,
                           endValues: TransitionValues): Animator {
    return AnimatorSet().apply {
      playTogether(
          ObjectAnimator.ofFloat(view, "rotation", 0f, 90f)
      )
    }
  }
}