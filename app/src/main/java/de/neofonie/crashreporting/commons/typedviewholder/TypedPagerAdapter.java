/*
 * (c) Neofonie Mobile GmbH (2016)
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
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import de.neofonie.crashreporting.R;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by marcinbak on 21/01/16.
 */
public class TypedPagerAdapter<T> extends PagerAdapter {

  List<T>                                  mDataList;
  TypedViewHolderFactory<T>                mFactory;
  LinkedBlockingQueue<SoftReference<View>> mViewsCache;

  public TypedPagerAdapter(TypedViewHolderFactory<T> factory) {
    mFactory = factory;
    mViewsCache = new LinkedBlockingQueue<>(3);
  }

  public void setDataList(List<T> dataList) {
    if (mDataList == null) {
      mDataList = new ArrayList<>(dataList.size());
    }
    mDataList.clear();
    mDataList.addAll(dataList);
    notifyDataSetChanged();
  }

  @Override
  public int getCount() {
    return mDataList == null ? 0 : mDataList.size();
  }

  @Override
  public boolean isViewFromObject(View view, Object object) {
    return view == object;
  }

  @Override
  public Object instantiateItem(ViewGroup container, int position) {
    SoftReference<View> softRef = mViewsCache.poll();
    View convertView = softRef == null ? null : softRef.get();

    TypedViewHolder<T> holder;
    if (convertView != null) {
      holder = (TypedViewHolder<T>) convertView.getTag(R.id.recycler_pager_viewholder_tag);
    } else {
      holder = mFactory.build(container);
      convertView = holder.itemView;
      convertView.setTag(R.id.recycler_pager_viewholder_tag, holder);
    }

    holder.bind(mDataList.get(position));

    container.addView(convertView);
    return convertView;
  }

  @Override
  public void destroyItem(ViewGroup container, int position, Object object) {
    container.removeView((View) object);
    if (mViewsCache.remainingCapacity() > 0) {
      Iterator<SoftReference<View>> iterator = mViewsCache.iterator();

      while (iterator.hasNext()) {
        SoftReference<View> view = iterator.next();
        if (view.get() == null) {
          iterator.remove();
        }
      }

      mViewsCache.add(new SoftReference<>((View) object));
    }
  }
}