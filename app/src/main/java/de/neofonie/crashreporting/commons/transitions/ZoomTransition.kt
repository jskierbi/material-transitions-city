package de.neofonie.crashreporting.commons.transitions

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.transition.TransitionValues
import android.transition.Visibility
import android.util.AttributeSet
import android.view.ViewGroup

/**
 * Created on 6/17/16.
 */
class ZoomTransition : Visibility {
  constructor() : super()

  constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

  override fun onAppear(sceneRoot: ViewGroup?,
                        startValues: TransitionValues?,
                        startVisibility: Int,
                        endValues: TransitionValues?,
                        endVisibility: Int): Animator? {

//    AnimatorSet set = new AnimatorSet();
//    set.playTogether(
//        ObjectAnimator.ofFloat(myView, "rotationX", 0, 360),
//        ObjectAnimator.ofFloat(myView, "rotationY", 0, 180),
//        ObjectAnimator.ofFloat(myView, "rotation", 0, -90),
//        ObjectAnimator.ofFloat(myView, "translationX", 0, 90),
//        ObjectAnimator.ofFloat(myView, "translationY", 0, 90),
//        ObjectAnimator.ofFloat(myView, "scaleX", 1, 1.5f),
//        ObjectAnimator.ofFloat(myView, "scaleY", 1, 0.5f),
//        ObjectAnimator.ofFloat(myView, "alpha", 1, 0.25f, 1)
//    );
//    set.setDuration(5 * 1000).start();
//    return super.onAppear(sceneRoot, startValues, startVisibility, endValues, endVisibility)

    return AnimatorSet().apply {
      playTogether(
          ObjectAnimator.ofFloat(sceneRoot, "scaleX", 1f, 0f),
          ObjectAnimator.ofFloat(sceneRoot, "scaleY", 1f, 0f),
          ObjectAnimator.ofFloat(sceneRoot, "rotation", -90f, 0f)
      )
    }
  }

  override fun onDisappear(sceneRoot: ViewGroup?,
                           startValues: TransitionValues?,
                           startVisibility: Int, endValues:
                           TransitionValues?,
                           endVisibility: Int): Animator? {
    return AnimatorSet().apply {
      playTogether(
          ObjectAnimator.ofFloat(sceneRoot, "scaleX", 0f, 1f),
          ObjectAnimator.ofFloat(sceneRoot, "scaleY", 0f, 1f),
          ObjectAnimator.ofFloat(sceneRoot, "rotation", 0f, 90f)
      )
    }
  }
}