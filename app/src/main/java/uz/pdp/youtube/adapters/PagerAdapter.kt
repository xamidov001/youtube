package uz.pdp.youtube.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.ads.AdRequest
import uz.pdp.youtube.R
import uz.pdp.youtube.classes.Item
import uz.pdp.youtube.databinding.ItemAdmobBinding
import uz.pdp.youtube.databinding.ItemHomeBinding
import uz.pdp.youtube.databinding.ItemPageBinding

class PagerAdapter(var context: Context, var list: ArrayList<Item>, var listener: OnMyClickListener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var isLoading = false
    private var LOADING = 0
    private var DATA = 1
    private var ADMOB = 2

    interface OnMyClickListener {
        fun onClick(position: Item)
    }

    fun adAll(otherList: List<Item>) {
        val size = list.size
        list.addAll(otherList)
        notifyItemRangeChanged(size, list.size)
    }

    fun addLoading() {
        isLoading = true
    }

    inner class VHProgress(var binding: ItemPageBinding): RecyclerView.ViewHolder(binding.root)

    inner class VH(var bind: ItemHomeBinding): RecyclerView.ViewHolder(bind.root)

    inner class VAD(var bindAd: ItemAdmobBinding): RecyclerView.ViewHolder(bindAd.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == LOADING) {
            return VHProgress(ItemPageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else if (viewType == ADMOB) {
            return VAD(ItemAdmobBinding.inflate(LayoutInflater.from(parent.context),parent, false))
        }
        else {
            return VH(ItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == DATA) {
            val vh = holder as VH
            vh.bind.apply {
                val item = list[position]

                Glide.with(context).load(item.snippet.thumbnails.medium.url).placeholder(ColorDrawable(Color.parseColor("#E3E3E3"))).into(imgVideo)

                titleTxt.text = item.snippet.title
                channelTxt.text = "${item.snippet.channelTitle}•${item.snippet.publishedAt}"
                card.setOnClickListener {
                    item?.let { it1 -> listener.onClick(it1) }
                }
            }
        } else if (getItemViewType(position) == ADMOB) {
            val vad = holder as VAD
            val build = AdRequest.Builder().build()
            vad.bindAd.adView.loadAd(build)

        }
        else {
            val vhProgress = holder as VHProgress
            vhProgress.binding.apply {

            }
        }
    }

    override fun getItemCount(): Int  = list.size

    override fun getItemViewType(position: Int): Int {
        if (position == list.size-1 && isLoading) {
            return LOADING
        } else if (position == 5) {
            return ADMOB
        }
        return DATA
    }

}