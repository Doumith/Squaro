package com.codem.squaro.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.codem.squaro.data.entities.RepoModel
import com.codem.squaro.databinding.ListItemRepoBinding


/**
 * Created by Doumith.A on 8/3/21.
 */
class RepositoryListAdapter :
    PagedListAdapter<RepoModel, RepositoryListAdapter.ViewHolder>(REPO_COMPARATOR) {

    var onItemCLickListener: OnItemCLickListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            ListItemRepoBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            onItemCLickListener?.onItemClicked(getItem(position))
        }
        getItem(position)?.let { holder.bind(it) }
    }


    inner class ViewHolder(private val binding: ListItemRepoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RepoModel) = with(binding) {
            binding.name.text = item.name
            binding.count.text = item.starsCount.toString()
            binding.bookmark.visibility = if (item.isBookMarked) View.VISIBLE else View.GONE
        }
    }

}

interface OnItemCLickListener {
    fun onItemClicked(repoModel: RepoModel?)
}


private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<RepoModel>() {
    override fun areItemsTheSame(oldItem: RepoModel, newItem: RepoModel): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: RepoModel, newItem: RepoModel): Boolean =
        oldItem == newItem
}