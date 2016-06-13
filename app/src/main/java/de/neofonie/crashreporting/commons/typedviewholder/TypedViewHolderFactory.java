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
package de.neofonie.crashreporting.commons.typedviewholder;

import android.view.ViewGroup;

/**
 * Provide instances of TypedViewHolder
 * <p/>
 * Created by jakub on 17/07/15.
 */
public abstract class TypedViewHolderFactory<T> {

  private Class<T> mDataType;

  public TypedViewHolderFactory(Class<T> dataType) {
    this.mDataType = dataType;
  }

  public Class<T> getViewHolderType() {
    return mDataType;
  }

  public abstract TypedViewHolder<T> build(ViewGroup parent);
}
