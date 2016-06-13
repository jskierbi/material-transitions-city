package de.neofonie.crashreporting.commons

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity

/**
 * Created by jakub on 13/06/16.
 */
open class BaseActivity(@LayoutRes private val layoutRes: Int) : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layoutRes)
  }
}