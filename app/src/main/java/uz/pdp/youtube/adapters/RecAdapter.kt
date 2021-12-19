package uz.pdp.youtube.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uz.pdp.youtube.classes.comment.Item
import uz.pdp.youtube.databinding.ItemCommentBinding

class RecAdapter(var context: Context): ListAdapter<Item, RecAdapter.VH>(MyDiffUtil()) {

    class MyDiffUtil : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }

    }

    inner class VH(var binding: ItemCommentBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        holder.binding.apply {

            Glide.with(context).load(item.snippet.topLevelComment.snippet.authorProfileImageUrl).into(accountImage)

            accountNameTv.text = "${item.snippet.topLevelComment.snippet.authorDisplayName}•${item.snippet.topLevelComment.snippet.publishedAt.substring(0,16)}"
            likeCountTv.text = item.snippet.topLevelComment.snippet.likeCount.toString()
            messageTv.text = item.snippet.topLevelComment.snippet.textOriginal


        }
    }
}