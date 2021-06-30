package com.nxb.githubsearchdemo.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nxb.githubsearchdemo.R
import com.nxb.githubsearchdemo.data.responses.Item
import com.nxb.githubsearchdemo.databinding.RepoItemBinding
import javax.inject.Inject

/**
@author Salman Aziz
created on 6/22/21
 **/

class ReposAdapter @Inject constructor() : RecyclerView.Adapter<ReposAdapter.RepoViewHolder>() {
    class RepoViewHolder(val binding: RepoItemBinding) : RecyclerView.ViewHolder(binding.root)


    var repoClickCallback: ((Item) -> Unit)? = null
    fun setClickListener(repoClickCallback: ((Item) -> Unit)) {
        this.repoClickCallback = repoClickCallback
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.htmlUrl == newItem.htmlUrl
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.htmlUrl == newItem.htmlUrl
        }

    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var repoItems: List<Item>
        get() = differ.currentList
        set(value) {
            differ.submitList(differ.currentList + value)
        }


    override fun getItemCount(): Int {
        return repoItems.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        return RepoViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.repo_item, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        holder.binding.data = repoItems[position]
        holder.itemView.setOnClickListener {
            repoClickCallback?.invoke(repoItems[position])
        }
    }
}