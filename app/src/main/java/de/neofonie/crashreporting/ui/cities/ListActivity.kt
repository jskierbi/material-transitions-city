package de.neofonie.crashreporting.ui.cities

import and.universal.club.toggolino.de.toggolino.commons.extensions.ioMain
import and.universal.club.toggolino.de.toggolino.commons.extensions.startActivityWithTransitions
import and.universal.club.toggolino.de.toggolino.utils.bindView
import and.universal.club.toggolino.de.toggolino.utils.typedviewholder.TypedViewHolder
import android.app.AlertDialog
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import de.neofonie.crashreporting.R
import de.neofonie.crashreporting.app
import de.neofonie.crashreporting.commons.BaseActivity
import de.neofonie.crashreporting.commons.LoadingLayout
import de.neofonie.crashreporting.commons.typedviewholder.TypedViewHolderAdapter
import de.neofonie.crashreporting.commons.typedviewholder.adapterOf
import de.neofonie.crashreporting.commons.typedviewholder.viewHolder
import de.neofonie.crashreporting.modules.cities.api.CitiesApi
import rx.Observable
import rx.subscriptions.Subscriptions

/**
 * Created by jakub on 13/06/16.
 */
class ListActivity : BaseActivity(R.layout.network_list_activity) {

  private val recyclerView: RecyclerView by bindView(R.id.recycler_view)
  private val loadingLayout: LoadingLayout by bindView(R.id.loading_layout)

  private lateinit var adapter: TypedViewHolderAdapter<Any>

  private var networkSubscription = Subscriptions.empty()
  private var dialogSubscription = Subscriptions.empty()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    adapter = adapterOf {
      viewHolder {
        CityViewHolder(it) { data, holder ->
          startActivityWithTransitions<DetailsActivity>(holder.thumb to getString(R.string.transition_image)) {
            putExtra(DetailsActivity.EXTRA_CITY_ID, data.details_id)
          }
        }
      }
    }
    adapter.data = mutableListOf()
    recyclerView.adapter = adapter
    recyclerView.layoutManager = LinearLayoutManager(this)
    subscribeLoadNetwork()
  }

  fun subscribeLoadNetwork() {
    loadingLayout.isLoadingVisible = true
    networkSubscription.unsubscribe()
    networkSubscription = app.citiesApi.cityList().flatMap {
      Observable.from(it).doOnNext { Thread.sleep(100) }
    }.ioMain().subscribe({ next ->
      loadingLayout.isLoadingVisible = false
      adapter.data.add(next)
      adapter.notifyItemInserted(adapter.data.size - 1)
    }, { error ->
      loadingLayout.isLoadingVisible = false
      Log.e("TAG", "API error", error)
      val dialog = AlertDialog.Builder(this).apply {
        setTitle("Network failed")
        setMessage("Retry?")
        setPositiveButton("Retry") { d, w -> subscribeLoadNetwork() }
        setNegativeButton("Back") { d, v -> onBackPressed() }
        setOnCancelListener { onBackPressed() }
      }.show()
      dialogSubscription.unsubscribe()
      dialogSubscription = Subscriptions.create { dialog.dismiss() }
    })
  }

  override fun onDestroy() {
    networkSubscription.unsubscribe()
    dialogSubscription.unsubscribe()
    super.onDestroy()
  }

  class CityViewHolder(parent: ViewGroup,
                       val onClick: (CitiesApi.CityShort, CityViewHolder) -> Unit = { d, v -> Unit }) :
      TypedViewHolder<CitiesApi.CityShort>(R.layout.li_city, parent) {

    val thumb: ImageView by bindView(R.id.thumb)
    val label: TextView by bindView(R.id.label)
    val separator: View by bindView(R.id.separator)

    override fun bind(dataItem: CitiesApi.CityShort) {
      separator.visibility = if (adapterPosition == 0) View.GONE else View.VISIBLE
      itemView.setOnClickListener { onClick(dataItem, this@CityViewHolder) }
      label.text = dataItem.name
      Picasso.with(context).load(dataItem.thumb).fit().centerCrop().into(thumb)
    }
  }
}