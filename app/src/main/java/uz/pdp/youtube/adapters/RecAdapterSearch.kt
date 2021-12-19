package uz.pdp.youtube.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uz.pdp.youtube.classes.Item
import uz.pdp.youtube.databinding.ItemHomeBinding

class RecAdapterSearch(var context: Context, val listener: OnMyClickListener): ListAdapter<Item, RecAdapterSearch.VH>(MyDiffUtil()) {

    interface OnMyClickListener {
        fun onClick(position: Item)
    }

    class MyDiffUtil : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }

    }

    inner class VH(var binding: ItemHomeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(ItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        holder.binding.apply {

            Glide.with(context).load(item?.snippet?.thumbnails?.medium?.url).placeholder(ColorDrawable(Color.parseColor("#E3E3E3"))).into(imgVideo)

            titleTxt.text = item.snippet.title
            channelTxt.text = "${item.snippet.channelTitle}•${item.snippet.publishedAt}"

            card.setOnClickListener {
                item?.let { it1 -> listener.onClick(it1) }
            }
        }
    }
}