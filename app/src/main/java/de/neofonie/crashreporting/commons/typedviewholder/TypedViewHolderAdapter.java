/*
 * (c) Neofonie Mobile GmbH (2014)
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

import and.universal.club.toggolino.de.toggolino.utils.typedviewholder.TypedViewHolder;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import de.neofonie.crashreporting.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generic adapter for RecyclerView
 * Uses factory pattern and registration mechanism to register typed ViewHolder for each available item type
 *
 * @param <T> base data type for each item
 */
public class TypedViewHolderAdapter<T> extends RecyclerView.Adapter<TypedViewHolder<? extends T>> {

  private List<T> mList;
  private Map<Class, TypedViewHolderFactory<? extends T>> mViewModelToHolderFactoryMap = new HashMap<>();
  private List<Class> mViewModelTypeList;

  private TypedViewHolderAdapter(Map<Class, TypedViewHolderFactory<? extends T>> viewHolderFactoryMap) {
    this.mViewModelToHolderFactoryMap = viewHolderFactoryMap;
    this.mViewModelTypeList = new ArrayList<>(viewHolderFactoryMap.keySet());
  }

  public static class Builder<T> {

    private Map<Class, TypedViewHolderFactory<? extends T>> mViewHolderFactoryMap = new HashMap<>();

    public Builder<T> addFactory(TypedViewHolderFactory<? extends T> typedViewHolderFactory) {
      final Class viewHolderDataType = typedViewHolderFactory.getViewHolderType();
      if (mViewHolderFactoryMap.containsKey(viewHolderDataType)) {
        throw new IllegalStateException("Factory already registered for type: " + viewHolderDataType);
      }
      mViewHolderFactoryMap.put(typedViewHolderFactory.getViewHolderType(), typedViewHolderFactory);
      return this;
    }

    public TypedViewHolderAdapter<T> build() {
      return new TypedViewHolderAdapter<>(mViewHolderFactoryMap);
    }
  }

  public void setData(List<T> list) {
    mList = list;
    notifyDataSetChanged();
  }

  public List<T> getData() {
    return mList;
  }

  @Override
  public TypedViewHolder<? extends T> onCreateViewHolder(ViewGroup parent, int viewType) {
    Class dataType = mViewModelTypeList.get(viewType);
    TypedViewHolderFactory<? extends T> factory = mViewModelToHolderFactoryMap.get(dataType);
    if (factory == null) {
      throw new IllegalStateException("No factory regisetered for type: " + dataType);
    }
    return factory.build(parent);
  }

  @Override
  public void onBindViewHolder(TypedViewHolder<? extends T> holder, int position) {
    TypedViewHolder holderErasedGeneric = (TypedViewHolder) holder;
    //noinspection unchecked
    holderErasedGeneric.bind(mList.get(position));

    setAnimation(holder.itemView, position);
  }

  @Override
  public int getItemCount() {
    return mList == null ? 0 : mList.size();
  }

  @Override
  public int getItemViewType(int position) {
    Class dataType = mList.get(position).getClass();
    for (int i = 0; i < mViewModelTypeList.size(); ++i) {
      if (dataType.equals(mViewModelTypeList.get(i))) {
        return i;
      }
    }
    throw new IllegalStateException("Factory for data type not registered: " + dataType);
  }

  private int lastPosition = -1;

  /**
   * Here is the key method to apply the animation
   */
  private void setAnimation(View viewToAnimate, int position) {
    // If the bound view wasn't previously displayed on screen, it's animated
    if (position > lastPosition) {
      Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), R.anim.push_left_in);
      viewToAnimate.startAnimation(animation);
      lastPosition = position;
    }
  }
}
