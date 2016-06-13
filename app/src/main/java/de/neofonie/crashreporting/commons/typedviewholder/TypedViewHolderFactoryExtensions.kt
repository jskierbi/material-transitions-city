package de.neofonie.crashreporting.commons.typedviewholder

import and.universal.club.toggolino.de.toggolino.utils.typedviewholder.TypedViewHolder
import android.view.ViewGroup

/**
 * Created by jakub on 13/06/16.
 */
inline fun <K : Any, reified T : K> TypedViewHolderAdapter.Builder<K>.viewHolder(crossinline createHolder: (parent: ViewGroup) -> TypedViewHolder<T>)
    = addFactory(
    object : TypedViewHolderFactory<T>(T::class.java) {
      override fun build(parent: ViewGroup) = createHolder(parent)
    })


inline fun <reified T : Any> adapterOf(config: TypedViewHolderAdapter.Builder<T>.() -> Unit)
    = TypedViewHolderAdapter.Builder<T>().apply { config() }.build()
