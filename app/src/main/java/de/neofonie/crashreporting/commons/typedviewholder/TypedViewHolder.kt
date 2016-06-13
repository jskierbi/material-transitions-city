/*
 * (c) Neofonie Mobile GmbH (2015)
 *
 * This computer program is the sole property of Neofonie Mobile GmbH (http://mobile.neofonie.de)
 * and is protected under the German Copyright Act (paragraph 69a UrhG).
 *
 * All rights are reserved. Making copies, duplicating, modifying, using or distributing
 * this computer program in any form, without prior written consent of Neofonie Mobile GmbH, is prohibited.
 * Violation of copyright is punishable under the German Copyright Act (paragraph 106 UrhG).
 *
 * Removing this copyright statement is also a violation.
 */
package and.universal.club.toggolino.de.toggolino.utils.typedviewholder

import and.universal.club.toggolino.de.toggolino.commons.extensions.inflate
import android.content.Context
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup

/**
 * Base class for all displayable view holders
 *
 *
 * Created by jakub on 17/07/15.
 */
abstract class TypedViewHolder<T> : RecyclerView.ViewHolder {

  protected var context: Context
    private set

  constructor(@LayoutRes layoutRes: Int, parent: ViewGroup) : super(parent.inflate(layoutRes, parent, false)) {
    context = parent.context
  }

  constructor(view: View) : super(view) {
    context = view.context
  }

  abstract fun bind(dataItem: T)
}
